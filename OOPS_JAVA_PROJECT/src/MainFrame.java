// MainFrame.java - Main window (updated: Hotels + Transport nav, 6 panels)
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private BookingManager bookingManager;
    private JPanel contentArea;

    // Panels (created once, reused)
    private HomePanel      homePanel;
    private ExplorePanel   explorePanel;
    private SearchPanel    searchPanel;
    private HotelPanel     hotelPanel;
    private TransportPanel transportPanel;
    private MyBookingsPanel myBookingsPanel;

    // Nav buttons
    private JButton btnHome, btnExplore, btnTrips, btnHotels, btnTransport, btnBookings;

    public MainFrame() {
        bookingManager = new BookingManager();

        setTitle("TripVista - Smart Travel & Hotel Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 740);
        setMinimumSize(new Dimension(950, 620));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildNavBar(), BorderLayout.NORTH);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(UIConstants.BG_MAIN);
        add(contentArea, BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        // Initialise all panels once
        homePanel      = new HomePanel(bookingManager, this);
        explorePanel   = new ExplorePanel(bookingManager, this);
        searchPanel    = new SearchPanel(bookingManager, this);
        hotelPanel     = new HotelPanel(bookingManager, this);
        transportPanel = new TransportPanel(bookingManager, this);
        myBookingsPanel = new MyBookingsPanel(bookingManager);

        showPanel(homePanel, btnHome);
    }

    private JPanel buildNavBar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(UIConstants.NAV_BG);
        nav.setPreferredSize(new Dimension(0, 58));

        JLabel logo = new JLabel("  ✈  TripVista");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 19));
        logo.setForeground(UIConstants.ACCENT);
        logo.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        nav.add(logo, BorderLayout.WEST);

        JPanel navBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 10));
        navBtns.setOpaque(false);

        btnHome      = navButton("🏠 Home");
        btnExplore   = navButton("🗺 Explore");
        btnTrips     = navButton("✈ Trips");
        btnHotels    = navButton("🏨 Hotels");
        btnTransport = navButton("🚌 Transport");
        btnBookings  = navButton("📋 My Bookings");

        // Event handling - navigate between panels
        btnHome.addActionListener(e -> showPanel(homePanel, btnHome));
        btnExplore.addActionListener(e -> {
            explorePanel.refreshDestinations("");
            showPanel(explorePanel, btnExplore);
        });
        btnTrips.addActionListener(e -> showPanel(searchPanel, btnTrips));
        btnHotels.addActionListener(e -> {
            hotelPanel.refreshHotels("All Destinations", "All Types");
            showPanel(hotelPanel, btnHotels);
        });
        btnTransport.addActionListener(e -> showPanel(transportPanel, btnTransport));
        btnBookings.addActionListener(e -> {
            myBookingsPanel.refreshAllTabs();
            showPanel(myBookingsPanel, btnBookings);
        });

        for (JButton b : new JButton[]{btnHome, btnExplore, btnTrips, btnHotels, btnTransport, btnBookings}) {
            navBtns.add(b);
        }
        nav.add(navBtns, BorderLayout.EAST);
        return nav;
    }

    private JButton navButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(UIConstants.NAV_TEXT);
        btn.setBackground(UIConstants.NAV_BG);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        return btn;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel();
        footer.setBackground(UIConstants.NAV_BG);
        footer.setPreferredSize(new Dimension(0, 28));
        JLabel lbl = new JLabel("© 2024 TripVista  |  Smart Travel & Hotel Booking System  |  Powered by Java Swing");
        lbl.setFont(UIConstants.FONT_SMALL);
        lbl.setForeground(UIConstants.TEXT_MUTED);
        footer.add(lbl);
        return footer;
    }

    // Swap content and highlight the active nav button
    public void showPanel(JPanel panel, JButton activeBtn) {
        contentArea.removeAll();
        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();

        for (JButton b : new JButton[]{btnHome, btnExplore, btnTrips, btnHotels, btnTransport, btnBookings}) {
            if (b != null) { b.setBackground(UIConstants.NAV_BG); b.setForeground(UIConstants.NAV_TEXT); }
        }
        if (activeBtn != null) activeBtn.setBackground(UIConstants.ACCENT);
    }

    // Open trip booking form
    public void openBookingForm(TravelPackage pkg) {
        showPanel(new BookingPanel(bookingManager, pkg, this), null);
    }

    // Open hotel booking form
    public void openHotelBookingForm(Hotel hotel) {
        showPanel(new HotelBookingPanel(bookingManager, hotel, this), null);
    }

    // Open transport booking form
    public void openTransportBookingForm(Transport transport, int passengers, String date) {
        showPanel(new TransportBookingPanel(bookingManager, transport, this, passengers, date), null);
    }

    // Navigate to My Bookings (called after any booking is confirmed)
    public void goToMyBookings() {
        myBookingsPanel.refreshAllTabs();
        showPanel(myBookingsPanel, btnBookings);
    }

    public BookingManager getBookingManager() { return bookingManager; }
}
