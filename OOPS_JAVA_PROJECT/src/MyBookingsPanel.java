// MyBookingsPanel.java - Tabbed view for all 3 booking types + cancel + file save
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class MyBookingsPanel extends JPanel {

    private BookingManager bookingManager;
    private JTabbedPane    tabbedPane;

    // Trip bookings table
    private JTable         tripTable;
    private DefaultTableModel tripModel;
    private static final String[] TRIP_COLS = {"Booking ID","Name","Package","Destination","Date","Travellers","Total Price","Status"};

    // Hotel bookings table
    private JTable         hotelTable;
    private DefaultTableModel hotelModel;
    private static final String[] HOTEL_COLS = {"Booking ID","Name","Hotel","Destination","Check-in","Check-out","Nights","Total Price","Status"};

    // Transport bookings table
    private JTable         transportTable;
    private DefaultTableModel transportModel;
    private static final String[] TRANSPORT_COLS = {"Booking ID","Name","Type","Provider","Route","Date","Passengers","Total Fare","Status"};

    public MyBookingsPanel(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_MAIN);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabbedPane(), BorderLayout.CENTER);
        refreshAllTabs();
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(UIConstants.NAV_BG);
        h.setBorder(new EmptyBorder(16, 25, 16, 25));

        JLabel title = new JLabel("📋  My Bookings");
        title.setFont(UIConstants.FONT_TITLE);
        title.setForeground(Color.WHITE);
        h.add(title, BorderLayout.WEST);

        JLabel sub = new JLabel("All your trips, hotels and transport in one place");
        sub.setFont(UIConstants.FONT_BODY);
        sub.setForeground(UIConstants.TEXT_MUTED);
        h.add(sub, BorderLayout.EAST);
        return h;
    }

    private JTabbedPane buildTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIConstants.FONT_HEADING);
        tabbedPane.setBackground(UIConstants.BG_MAIN);

        tabbedPane.addTab("✈  Trip Bookings",      buildTripTab());
        tabbedPane.addTab("🏨  Hotel Bookings",     buildHotelTab());
        tabbedPane.addTab("🚌  Transport Bookings", buildTransportTab());

        return tabbedPane;
    }

    // ==================== TRIP TAB ====================
    private JPanel buildTripTab() {
        tripModel = new DefaultTableModel(TRIP_COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tripTable = styledTable(tripModel);
        applyStatusRenderer(tripTable, 7);
        int[] w = {85, 130, 155, 110, 95, 75, 100, 90};
        setColumnWidths(tripTable, w);

        return buildTabPanel(tripTable, "✖ Cancel Selected Trip Booking", e -> cancelSelected("trip"));
    }

    // ==================== HOTEL TAB ====================
    private JPanel buildHotelTab() {
        hotelModel = new DefaultTableModel(HOTEL_COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        hotelTable = styledTable(hotelModel);
        applyStatusRenderer(hotelTable, 8);
        int[] w = {85, 120, 155, 100, 90, 90, 60, 100, 90};
        setColumnWidths(hotelTable, w);

        return buildTabPanel(hotelTable, "✖ Cancel Selected Hotel Booking", e -> cancelSelected("hotel"));
    }

    // ==================== TRANSPORT TAB ====================
    private JPanel buildTransportTab() {
        transportModel = new DefaultTableModel(TRANSPORT_COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        transportTable = styledTable(transportModel);
        applyStatusRenderer(transportTable, 8);
        int[] w = {85, 110, 70, 150, 130, 90, 80, 100, 90};
        setColumnWidths(transportTable, w);

        return buildTabPanel(transportTable, "✖ Cancel Selected Transport Booking", e -> cancelSelected("transport"));
    }

    // Build a tab panel with its table and bottom buttons
    private JPanel buildTabPanel(JTable table, String cancelLabel, java.awt.event.ActionListener cancelAction) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BG_MAIN);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottom.setBackground(UIConstants.BG_MAIN);
        bottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER));

        JButton refreshBtn = UIConstants.accentButton("🔄 Refresh");
        refreshBtn.addActionListener(e -> refreshAllTabs());

        JButton cancelBtn = UIConstants.dangerButton(cancelLabel);
        cancelBtn.addActionListener(cancelAction);

        bottom.add(refreshBtn);
        bottom.add(cancelBtn);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    // ==================== REFRESH ====================
    public void refreshAllTabs() {
        refreshTripTable();
        refreshHotelTable();
        refreshTransportTable();
    }

    private void refreshTripTable() {
        tripModel.setRowCount(0);
        ArrayList<Booking> list = bookingManager.getAllBookings();
        if (list.isEmpty()) {
            tripModel.addRow(new Object[]{"—", "No trip bookings yet", "—", "—", "—", "—", "—", "—"});
        } else {
            for (Booking b : list) {
                tripModel.addRow(new Object[]{
                    b.getBookingId(), b.getCustomerName(),
                    b.getTravelPackage().getPackageName(), b.getTravelPackage().getDestination(),
                    b.getTravelDate(), b.getNumTravellers(),
                    "₹ " + String.format("%.2f", b.getTotalPrice()), b.getStatus().toString()
                });
            }
        }
    }

    private void refreshHotelTable() {
        hotelModel.setRowCount(0);
        ArrayList<HotelBooking> list = bookingManager.getAllHotelBookings();
        if (list.isEmpty()) {
            hotelModel.addRow(new Object[]{"—", "No hotel bookings yet", "—", "—", "—", "—", "—", "—", "—"});
        } else {
            for (HotelBooking hb : list) {
                hotelModel.addRow(new Object[]{
                    hb.getBookingId(), hb.getCustomerName(),
                    hb.getHotel().getName(), hb.getHotel().getDestination(),
                    hb.getCheckIn(), hb.getCheckOut(), hb.getNights(),
                    "₹ " + String.format("%.2f", hb.getTotalPrice()), hb.getStatus().toString()
                });
            }
        }
    }

    private void refreshTransportTable() {
        transportModel.setRowCount(0);
        ArrayList<TransportBooking> list = bookingManager.getAllTransportBookings();
        if (list.isEmpty()) {
            transportModel.addRow(new Object[]{"—", "No transport bookings yet", "—", "—", "—", "—", "—", "—", "—"});
        } else {
            for (TransportBooking tb : list) {
                Transport t = tb.getTransport();
                transportModel.addRow(new Object[]{
                    tb.getBookingId(), tb.getCustomerName(),
                    t.getTypeEmoji() + " " + t.getType(), t.getProvider(),
                    t.getSource() + " → " + t.getDestination(),
                    tb.getTravelDate(), tb.getNumPassengers(),
                    "₹ " + String.format("%.2f", tb.getTotalPrice()), tb.getStatus().toString()
                });
            }
        }
    }

    // ==================== CANCEL ====================
    private void cancelSelected(String type) {
        JTable table;
        DefaultTableModel model;
        int statusCol;

        switch (type) {
            case "hotel":     table = hotelTable;     model = hotelModel;     statusCol = 8; break;
            case "transport": table = transportTable; model = transportModel; statusCol = 8; break;
            default:          table = tripTable;      model = tripModel;      statusCol = 7; break;
        }

        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to cancel.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookingId = (String) model.getValueAt(row, 0);
        String status    = (String) model.getValueAt(row, statusCol);

        if ("CANCELLED".equals(status)) {
            JOptionPane.showMessageDialog(this, "This booking is already cancelled.", "Already Cancelled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Cancel booking " + bookingId + "?", "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) return;

        boolean ok = false;
        switch (type) {
            case "hotel":     ok = bookingManager.cancelHotelBooking(bookingId);
                              DataManager.saveHotelBookings(bookingManager.getAllHotelBookings()); break;
            case "transport": ok = bookingManager.cancelTransportBooking(bookingId);
                              DataManager.saveTransportBookings(bookingManager.getAllTransportBookings()); break;
            default:          ok = bookingManager.cancelBooking(bookingId);
                              DataManager.saveTripBookings(bookingManager.getAllBookings()); break;
        }

        if (ok) {
            JOptionPane.showMessageDialog(this, "Booking " + bookingId + " cancelled successfully.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            refreshAllTabs();
        } else {
            JOptionPane.showMessageDialog(this, "Could not cancel. Booking not found or already cancelled.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== HELPERS ====================
    private JTable styledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(UIConstants.FONT_BODY);
        table.setRowHeight(34);
        table.setGridColor(UIConstants.BORDER);
        table.setSelectionBackground(new Color(0, 184, 169, 50));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.getTableHeader().setFont(UIConstants.FONT_BUTTON);
        table.getTableHeader().setBackground(UIConstants.NAV_BG);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        return table;
    }

    // Colour the status column green/red
    private void applyStatusRenderer(JTable table, int col) {
        table.getColumnModel().getColumn(col).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                l.setForeground("CONFIRMED".equals(v) ? UIConstants.SUCCESS : UIConstants.DANGER);
                l.setFont(new Font("Segoe UI", Font.BOLD, 12));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                return l;
            }
        });
    }

    private void setColumnWidths(JTable table, int[] widths) {
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }
}
