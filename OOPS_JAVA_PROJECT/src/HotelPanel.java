// HotelPanel.java - Browse and filter hotels, book one
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class HotelPanel extends JPanel {

    private BookingManager bookingManager;
    private MainFrame      mainFrame;
    private JPanel         cardsPanel;
    private JComboBox<String> destCombo;
    private JComboBox<String> typeCombo;

    public HotelPanel(BookingManager bookingManager, MainFrame mainFrame) {
        this.bookingManager = bookingManager;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_MAIN);

        add(buildTopBar(), BorderLayout.NORTH);

        cardsPanel = new JPanel(new GridLayout(0, 2, 16, 16));
        cardsPanel.setBackground(UIConstants.BG_MAIN);
        cardsPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(cardsPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        refreshHotels("All Destinations", "All Types");
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UIConstants.NAV_BG);
        bar.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel heading = new JLabel("🏨  Browse Hotels");
        heading.setFont(UIConstants.FONT_TITLE);
        heading.setForeground(Color.WHITE);
        bar.add(heading, BorderLayout.WEST);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filters.setOpaque(false);

        destCombo = new JComboBox<>(bookingManager.getDestinationNames());
        destCombo.setFont(UIConstants.FONT_BODY);
        destCombo.setPreferredSize(new Dimension(170, 34));

        typeCombo = new JComboBox<>(new String[]{"All Types", "Budget", "Standard", "Luxury"});
        typeCombo.setFont(UIConstants.FONT_BODY);
        typeCombo.setPreferredSize(new Dimension(130, 34));

        JButton searchBtn = UIConstants.accentButton("Search");
        searchBtn.addActionListener(e -> refreshHotels(
                (String) destCombo.getSelectedItem(),
                (String) typeCombo.getSelectedItem()));

        JButton clearBtn = UIConstants.dangerButton("Clear");
        clearBtn.addActionListener(e -> {
            destCombo.setSelectedIndex(0);
            typeCombo.setSelectedIndex(0);
            refreshHotels("All Destinations", "All Types");
        });

        filters.add(new JLabel("<html><font color='white'>Destination:</font></html>"));
        filters.add(destCombo);
        filters.add(new JLabel("<html><font color='white'>Type:</font></html>"));
        filters.add(typeCombo);
        filters.add(searchBtn);
        filters.add(clearBtn);
        bar.add(filters, BorderLayout.EAST);
        return bar;
    }

    public void refreshHotels(String destination, String type) {
        String dest = (destination == null || destination.equals("All Destinations")) ? "" : destination;
        String tp   = (type == null || type.equals("All Types")) ? "" : type;

        cardsPanel.removeAll();
        ArrayList<Hotel> list = bookingManager.searchHotels(dest, tp);

        if (list.isEmpty()) {
            JLabel none = new JLabel("No hotels found for the selected filters.", SwingConstants.CENTER);
            none.setFont(UIConstants.FONT_BODY);
            none.setForeground(UIConstants.TEXT_MUTED);
            cardsPanel.add(none);
        } else {
            for (Hotel h : list) cardsPanel.add(buildHotelCard(h));
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel buildHotelCard(Hotel hotel) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(UIConstants.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                new EmptyBorder(16, 18, 16, 18)));

        // --- Header row: name + rating ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel nameLbl = new JLabel("🏨 " + hotel.getName());
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLbl.setForeground(UIConstants.TEXT_PRIMARY);

        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        ratingPanel.setOpaque(false);
        JLabel starsLbl = new JLabel(hotel.getStarString());
        starsLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        starsLbl.setForeground(new Color(230, 160, 0));
        JLabel ratingNum = new JLabel(hotel.getRating() + "");
        ratingNum.setFont(UIConstants.FONT_SMALL);
        ratingNum.setForeground(UIConstants.TEXT_MUTED);
        ratingPanel.add(starsLbl);
        ratingPanel.add(ratingNum);

        header.add(nameLbl, BorderLayout.WEST);
        header.add(ratingPanel, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        // --- Middle: type badge, location, amenities, description ---
        JPanel middle = new JPanel();
        middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));
        middle.setOpaque(false);

        JPanel badges = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        badges.setOpaque(false);
        JLabel typeBadge = new JLabel(hotel.getTypeBadge());
        typeBadge.setFont(UIConstants.FONT_SMALL);
        typeBadge.setForeground(UIConstants.TEXT_MUTED);
        JLabel locLbl = new JLabel("📍 " + hotel.getDestination());
        locLbl.setFont(UIConstants.FONT_SMALL);
        locLbl.setForeground(UIConstants.TEXT_MUTED);
        badges.add(typeBadge);
        badges.add(new JLabel("|"));
        badges.add(locLbl);
        middle.add(badges);

        JLabel amenLbl = new JLabel("✅ " + hotel.getAmenities());
        amenLbl.setFont(UIConstants.FONT_SMALL);
        amenLbl.setForeground(new Color(39, 100, 60));
        middle.add(amenLbl);

        JTextArea desc = new JTextArea(hotel.getDescription());
        desc.setFont(UIConstants.FONT_SMALL);
        desc.setForeground(UIConstants.TEXT_MUTED);
        desc.setBackground(UIConstants.BG_CARD);
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setEditable(false);
        desc.setFocusable(false);
        desc.setRows(2);
        middle.add(Box.createVerticalStrut(4));
        middle.add(desc);
        card.add(middle, BorderLayout.CENTER);

        // --- Footer: price + Book Now ---
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel priceLbl = new JLabel("₹" + String.format("%.0f", hotel.getPricePerNight()) + " / night");
        priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLbl.setForeground(UIConstants.SUCCESS);
        footer.add(priceLbl, BorderLayout.WEST);

        JButton bookBtn = UIConstants.accentButton("Book Hotel →");
        bookBtn.addActionListener(e -> mainFrame.openHotelBookingForm(hotel));
        footer.add(bookBtn, BorderLayout.EAST);
        card.add(footer, BorderLayout.SOUTH);

        return card;
    }
}
