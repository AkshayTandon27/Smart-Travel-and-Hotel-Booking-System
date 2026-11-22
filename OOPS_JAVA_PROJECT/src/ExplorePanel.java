// ExplorePanel.java - Browse and filter all destinations
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class ExplorePanel extends JPanel {

    private BookingManager bookingManager;
    private MainFrame mainFrame;
    private JPanel cardsGrid;      // The grid that holds destination cards
    private JTextField filterField;

    public ExplorePanel(BookingManager bookingManager, MainFrame mainFrame) {
        // Allow null bookingManager so HomePanel can safely create an ExplorePanel
        this.bookingManager = (bookingManager != null) ? bookingManager : new BookingManager();
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_MAIN);

        add(buildTopBar(), BorderLayout.NORTH);

        cardsGrid = new JPanel(new GridLayout(0, 2, 20, 20));
        cardsGrid.setBackground(UIConstants.BG_MAIN);
        cardsGrid.setBorder(new EmptyBorder(10, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(cardsGrid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        refreshDestinations(""); // Load all by default
    }

    // Top bar with heading and filter input
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UIConstants.BG_MAIN);
        bar.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel heading = new JLabel("🗺  Explore Destinations");
        heading.setFont(UIConstants.FONT_TITLE);
        heading.setForeground(UIConstants.TEXT_PRIMARY);
        bar.add(heading, BorderLayout.WEST);

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filterRow.setOpaque(false);

        filterField = UIConstants.styledField(20);
        filterField.setToolTipText("Filter by name, location or keyword...");

        JButton filterBtn = UIConstants.accentButton("Filter");
        filterBtn.addActionListener(e -> refreshDestinations(filterField.getText()));

        JButton clearBtn = UIConstants.dangerButton("Clear");
        clearBtn.addActionListener(e -> {
            filterField.setText("");
            refreshDestinations("");
        });

        filterRow.add(new JLabel("Filter: "));
        filterRow.add(filterField);
        filterRow.add(filterBtn);
        filterRow.add(clearBtn);
        bar.add(filterRow, BorderLayout.EAST);

        return bar;
    }

    // Rebuild the destination cards based on a keyword filter
    public void refreshDestinations(String keyword) {
        cardsGrid.removeAll();
        ArrayList<Destination> list = keyword.isEmpty()
                ? bookingManager.getAllDestinations()
                : bookingManager.filterDestinations(keyword);

        if (list.isEmpty()) {
            JLabel noResult = new JLabel("No destinations match your search.", SwingConstants.CENTER);
            noResult.setFont(UIConstants.FONT_BODY);
            noResult.setForeground(UIConstants.TEXT_MUTED);
            cardsGrid.add(noResult);
        } else {
            for (Destination dest : list) {
                cardsGrid.add(buildDestinationCard(dest));
            }
        }
        cardsGrid.revalidate();
        cardsGrid.repaint();
    }

    // Build a card for one destination
    private JPanel buildDestinationCard(Destination dest) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(UIConstants.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                new EmptyBorder(18, 18, 18, 18)));

        // Header: emoji + name + location
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        header.setOpaque(false);

        JLabel emojiLbl = new JLabel(dest.getEmoji());
        emojiLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        header.add(emojiLbl);

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);

        JLabel nameLbl = new JLabel(dest.getName());
        nameLbl.setFont(UIConstants.FONT_HEADING);
        nameLbl.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel locLbl = new JLabel("📍 " + dest.getLocation());
        locLbl.setFont(UIConstants.FONT_SMALL);
        locLbl.setForeground(UIConstants.TEXT_MUTED);

        titleBlock.add(nameLbl);
        titleBlock.add(locLbl);
        header.add(titleBlock);
        card.add(header, BorderLayout.NORTH);

        // Description
        JTextArea descArea = new JTextArea(dest.getDescription());
        descArea.setFont(UIConstants.FONT_BODY);
        descArea.setForeground(UIConstants.TEXT_MUTED);
        descArea.setBackground(UIConstants.BG_CARD);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setRows(3);
        descArea.setFocusable(false);
        card.add(descArea, BorderLayout.CENTER);

        // Footer: price + View Packages button
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel priceLbl = new JLabel("From ₹" + String.format("%.0f", dest.getBasePrice()) + " / person");
        priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        priceLbl.setForeground(UIConstants.SUCCESS);
        footer.add(priceLbl, BorderLayout.WEST);

        JButton viewBtn = UIConstants.accentButton("View Packages →");
        viewBtn.addActionListener(e -> {
            // Navigate to Search panel pre-filled with this destination
            SearchPanel sp = new SearchPanel(bookingManager, mainFrame);
            sp.prefillDestination(dest.getName());
            mainFrame.showPanel(sp, null);
        });
        footer.add(viewBtn, BorderLayout.EAST);
        card.add(footer, BorderLayout.SOUTH);

        return card;
    }
}
