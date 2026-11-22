// HomePanel.java - Dashboard (updated: accepts shared BookingManager, 3 CTA buttons)
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomePanel extends JPanel {

    private BookingManager bookingManager;
    private MainFrame      mainFrame;

    public HomePanel(BookingManager bookingManager, MainFrame mainFrame) {
        this.bookingManager = bookingManager;
        this.mainFrame      = mainFrame;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_MAIN);

        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(UIConstants.BG_MAIN);

        contentWrapper.add(buildHeroBanner());
        contentWrapper.add(Box.createVerticalStrut(20));
        contentWrapper.add(buildStatsBar());
        contentWrapper.add(Box.createVerticalStrut(20));
        contentWrapper.add(buildQuickSearch());
        contentWrapper.add(Box.createVerticalStrut(20));
        contentWrapper.add(buildPopularDestinations());
        contentWrapper.add(Box.createVerticalStrut(30));

        JScrollPane scroll = new JScrollPane(contentWrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildHeroBanner() {
        JPanel hero = new JPanel(new GridBagLayout());
        hero.setBackground(UIConstants.NAV_BG);
        hero.setBorder(new EmptyBorder(38, 50, 38, 50));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(6, 0, 6, 0);

        JLabel tagline = new JLabel("Your Complete Travel Companion  ✈ 🏨 🚌");
        tagline.setFont(new Font("Segoe UI", Font.BOLD, 26));
        tagline.setForeground(Color.WHITE);
        gbc.gridy = 0; hero.add(tagline, gbc);

        JLabel sub = new JLabel("Book flights, hotels, trains, buses and cabs — all in one place.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        sub.setForeground(new Color(180, 190, 210));
        gbc.gridy = 1; hero.add(sub, gbc);

        // Three CTA buttons in a row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setOpaque(false);

        JButton tripsBtn     = ctaButton("✈  Explore Trips",     UIConstants.ACCENT);
        JButton hotelsBtn    = ctaButton("🏨  Book Hotels",       new Color(0, 150, 100));
        JButton transportBtn = ctaButton("🚌  Book Transport",    new Color(60, 100, 180));

        tripsBtn.addActionListener(e -> mainFrame.showPanel(
                new SearchPanel(bookingManager, mainFrame), null));
        hotelsBtn.addActionListener(e -> mainFrame.showPanel(
                new HotelPanel(bookingManager, mainFrame), null));
        transportBtn.addActionListener(e -> mainFrame.showPanel(
                new TransportPanel(bookingManager, mainFrame), null));

        btnRow.add(tripsBtn);
        btnRow.add(hotelsBtn);
        btnRow.add(transportBtn);
        gbc.gridy = 2; gbc.insets = new Insets(16, 0, 0, 0);
        hero.add(btnRow, gbc);

        return hero;
    }

    private JButton ctaButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(190, 42));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    // Stats bar showing number of destinations, hotels, transport options
    private JPanel buildStatsBar() {
        JPanel bar = new JPanel(new GridLayout(1, 4, 0, 0));
        bar.setBackground(new Color(230, 240, 250));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        bar.add(statItem("8", "Destinations"));
        bar.add(statItem("21", "Hotels Listed"));
        bar.add(statItem("27", "Transport Options"));
        bar.add(statItem("8", "Travel Packages"));
        return bar;
    }

    private JPanel statItem(String number, String label) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(230, 240, 250));
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIConstants.BORDER));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0;
        JLabel num = new JLabel(number, SwingConstants.CENTER);
        num.setFont(new Font("Segoe UI", Font.BOLD, 24));
        num.setForeground(UIConstants.ACCENT);
        p.add(num, g);
        g.gridy = 1;
        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(UIConstants.FONT_SMALL);
        lbl.setForeground(UIConstants.TEXT_MUTED);
        p.add(lbl, g);
        return p;
    }

    private JPanel buildQuickSearch() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                "  Quick Destination Search  ", 0, 0, UIConstants.FONT_HEADING, UIConstants.TEXT_PRIMARY));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JTextField searchField = UIConstants.styledField(28);
        searchField.setToolTipText("Type a destination e.g. Goa, Manali...");

        JButton searchBtn = UIConstants.accentButton("  Search  ");
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            ExplorePanel ep = new ExplorePanel(bookingManager, mainFrame);
            ep.refreshDestinations(keyword);
            mainFrame.showPanel(ep, null);
        });

        panel.add(new JLabel("🔍 "));
        panel.add(searchField);
        panel.add(searchBtn);
        return panel;
    }

    private JPanel buildPopularDestinations() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UIConstants.BG_MAIN);
        wrapper.setBorder(new EmptyBorder(0, 30, 0, 30));

        JLabel heading = new JLabel("🌟  Popular Destinations");
        heading.setFont(UIConstants.FONT_TITLE);
        heading.setForeground(UIConstants.TEXT_PRIMARY);
        heading.setBorder(new EmptyBorder(0, 0, 14, 0));
        wrapper.add(heading, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 4, 16, 16));
        grid.setBackground(UIConstants.BG_MAIN);

        String[][] spots = {
            {"🏖", "Goa",             "Packages from ₹8,500"},
            {"🏔", "Manali",          "Packages from ₹9,200"},
            {"🏰", "Jaipur",          "Packages from ₹6,500"},
            {"🌴", "Kerala",          "Packages from ₹11,000"},
            {"🕌", "Varanasi",        "Packages from ₹5,500"},
            {"🐠", "Andaman Islands", "Packages from ₹18,000"},
            {"🏕", "Leh Ladakh",      "Packages from ₹15,000"},
            {"🌿", "Ooty",            "Packages from ₹7,000"}
        };

        for (String[] spot : spots) grid.add(buildMiniCard(spot[0], spot[1], spot[2]));
        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildMiniCard(String emoji, String name, String price) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIConstants.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                new EmptyBorder(14, 10, 14, 10)));

        JLabel emojiLbl = new JLabel(emoji, SwingConstants.CENTER);
        emojiLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        emojiLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLbl = new JLabel(name, SwingConstants.CENTER);
        nameLbl.setFont(UIConstants.FONT_HEADING);
        nameLbl.setForeground(UIConstants.TEXT_PRIMARY);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLbl = new JLabel(price, SwingConstants.CENTER);
        priceLbl.setFont(UIConstants.FONT_SMALL);
        priceLbl.setForeground(UIConstants.ACCENT);
        priceLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Click to view hotels for that destination
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                HotelPanel hp = new HotelPanel(bookingManager, mainFrame);
                hp.refreshHotels(name, "All Types");
                mainFrame.showPanel(hp, null);
            }
        });

        card.add(emojiLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(nameLbl);
        card.add(Box.createVerticalStrut(4));
        card.add(priceLbl);
        return card;
    }
}
