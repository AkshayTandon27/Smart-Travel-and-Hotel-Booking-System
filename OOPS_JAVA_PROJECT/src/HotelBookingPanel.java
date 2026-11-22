// HotelBookingPanel.java - Booking form for hotels
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HotelBookingPanel extends JPanel {

    private BookingManager bookingManager;
    private Hotel          selectedHotel;
    private MainFrame      mainFrame;

    private JTextField nameField, contactField, checkInField, checkOutField;
    private JSpinner   nightsSpinner, guestsSpinner;
    private JLabel     totalPriceLabel;

    public HotelBookingPanel(BookingManager bookingManager, Hotel hotel, MainFrame mainFrame) {
        this.bookingManager = bookingManager;
        this.selectedHotel  = hotel;
        this.mainFrame      = mainFrame;

        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_MAIN);
        add(buildHeader(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(buildForm());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(UIConstants.NAV_BG);
        h.setBorder(new EmptyBorder(18, 25, 18, 25));

        JLabel title = new JLabel("🏨  Hotel Booking");
        title.setFont(UIConstants.FONT_TITLE);
        title.setForeground(Color.WHITE);
        h.add(title, BorderLayout.WEST);

        JLabel info = new JLabel(selectedHotel.getName() + "  |  " + selectedHotel.getType()
                + "  |  ₹" + String.format("%.0f", selectedHotel.getPricePerNight()) + " / night");
        info.setFont(UIConstants.FONT_BODY);
        info.setForeground(UIConstants.ACCENT);
        h.add(info, BorderLayout.EAST);
        return h;
    }

    private JPanel buildForm() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(UIConstants.BG_MAIN);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIConstants.BG_CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                new EmptyBorder(30, 40, 30, 40)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.anchor = GridBagConstraints.WEST;
        g.fill   = GridBagConstraints.HORIZONTAL;

        // --- Hotel Info (read-only) ---
        g.gridy = 0; g.gridx = 0; g.gridwidth = 2;
        form.add(sectionLabel("🏨  Selected Hotel"), g); g.gridwidth = 1;

        addRow(form, g, 1, "Hotel Name:",    readOnly(selectedHotel.getName()));
        addRow(form, g, 2, "Destination:",   readOnly(selectedHotel.getDestination()));
        addRow(form, g, 3, "Category:",      readOnly(selectedHotel.getType() + "  " + selectedHotel.getStarString()));
        addRow(form, g, 4, "Price / Night:", readOnly("₹ " + String.format("%.2f", selectedHotel.getPricePerNight())));
        addRow(form, g, 5, "Amenities:",     readOnly(selectedHotel.getAmenities()));

        g.gridy = 6; g.gridx = 0; g.gridwidth = 2; form.add(new JSeparator(), g); g.gridwidth = 1;

        // --- Guest Details ---
        g.gridy = 7; g.gridx = 0; g.gridwidth = 2;
        form.add(sectionLabel("👤  Guest Details"), g); g.gridwidth = 1;

        g.gridy = 8;  g.gridx = 0; form.add(fLabel("Full Name:"), g);
        g.gridx = 1; nameField = UIConstants.styledField(22); form.add(nameField, g);

        g.gridy = 9;  g.gridx = 0; form.add(fLabel("Mobile Number:"), g);
        g.gridx = 1; contactField = UIConstants.styledField(22); contactField.setToolTipText("10-digit mobile"); form.add(contactField, g);

        g.gridy = 10; g.gridx = 0; form.add(fLabel("Check-in Date:"), g);
        g.gridx = 1; checkInField = UIConstants.styledField(22); checkInField.setToolTipText("e.g. 25-Dec-2024"); form.add(checkInField, g);

        g.gridy = 11; g.gridx = 0; form.add(fLabel("Check-out Date:"), g);
        g.gridx = 1; checkOutField = UIConstants.styledField(22); checkOutField.setToolTipText("e.g. 28-Dec-2024"); form.add(checkOutField, g);

        g.gridy = 12; g.gridx = 0; form.add(fLabel("No. of Nights:"), g);
        g.gridx = 1;
        nightsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 60, 1));
        nightsSpinner.setFont(UIConstants.FONT_BODY);
        nightsSpinner.setPreferredSize(new Dimension(100, 34));
        nightsSpinner.addChangeListener(e -> updateTotal());
        form.add(nightsSpinner, g);

        g.gridy = 13; g.gridx = 0; form.add(fLabel("No. of Guests:"), g);
        g.gridx = 1;
        guestsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        guestsSpinner.setFont(UIConstants.FONT_BODY);
        guestsSpinner.setPreferredSize(new Dimension(100, 34));
        form.add(guestsSpinner, g);

        // --- Total ---
        g.gridy = 14; g.gridx = 0; g.gridwidth = 2; form.add(new JSeparator(), g); g.gridwidth = 1;
        g.gridy = 15; g.gridx = 0; form.add(fLabel("Total Amount:"), g);
        g.gridx = 1;
        totalPriceLabel = new JLabel("₹ " + String.format("%.2f", selectedHotel.getPricePerNight()));
        totalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalPriceLabel.setForeground(UIConstants.SUCCESS);
        form.add(totalPriceLabel, g);

        // --- Buttons ---
        g.gridy = 16; g.gridx = 0; g.gridwidth = 2; g.anchor = GridBagConstraints.CENTER;
        g.fill = GridBagConstraints.NONE; g.insets = new Insets(20, 10, 10, 10);
        JButton confirmBtn = UIConstants.accentButton("  ✔  Confirm Hotel Booking  ");
        confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        confirmBtn.setPreferredSize(new Dimension(240, 44));
        confirmBtn.addActionListener(e -> handleConfirm());
        form.add(confirmBtn, g);

        g.gridy = 17;
        JButton cancelBtn = UIConstants.dangerButton("  ✖  Cancel  ");
        cancelBtn.addActionListener(e -> mainFrame.goToMyBookings());
        form.add(cancelBtn, g);

        GridBagConstraints oc = new GridBagConstraints();
        oc.anchor = GridBagConstraints.CENTER;
        oc.insets = new Insets(30, 50, 30, 50);
        outer.add(form, oc);
        return outer;
    }

    private void updateTotal() {
        int nights = (Integer) nightsSpinner.getValue();
        totalPriceLabel.setText("₹ " + String.format("%.2f", selectedHotel.getPricePerNight() * nights));
    }

    private void handleConfirm() {
        try {
            String name     = nameField.getText();
            String contact  = contactField.getText();
            String checkIn  = checkInField.getText();
            String checkOut = checkOutField.getText();
            int    nights   = (Integer) nightsSpinner.getValue();
            int    guests   = (Integer) guestsSpinner.getValue();

            HotelBooking hb = bookingManager.createHotelBooking(name, contact, selectedHotel,
                                                                 checkIn, checkOut, nights, guests);
            // Save to file immediately after booking
            DataManager.saveHotelBookings(bookingManager.getAllHotelBookings());

            String msg = "✅  Hotel Booking Confirmed!\n\n"
                    + "Booking ID     :  " + hb.getBookingId() + "\n"
                    + "Name           :  " + hb.getCustomerName() + "\n"
                    + "Hotel          :  " + hb.getHotel().getName() + "\n"
                    + "Destination    :  " + hb.getHotel().getDestination() + "\n"
                    + "Check-in       :  " + hb.getCheckIn() + "\n"
                    + "Check-out      :  " + hb.getCheckOut() + "\n"
                    + "Nights         :  " + hb.getNights() + "\n"
                    + "Guests         :  " + hb.getNumGuests() + "\n"
                    + "Total Amount   :  ₹" + String.format("%.2f", hb.getTotalPrice()) + "\n\n"
                    + "Enjoy your stay! 🏨🎉";
            JOptionPane.showMessageDialog(this, msg, "Hotel Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.goToMyBookings();

        } catch (InvalidBookingException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Helper methods
    private void addRow(JPanel form, GridBagConstraints g, int row, String label, JTextField field) {
        g.gridy = row; g.gridx = 0; form.add(fLabel(label), g);
        g.gridx = 1; form.add(field, g);
    }

    private JLabel fLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_BODY);
        l.setForeground(UIConstants.TEXT_PRIMARY);
        l.setPreferredSize(new Dimension(170, 30));
        return l;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_HEADING);
        l.setForeground(UIConstants.NAV_BG);
        l.setBorder(new EmptyBorder(6, 0, 2, 0));
        return l;
    }

    private JTextField readOnly(String val) {
        JTextField f = new JTextField(val);
        f.setEditable(false);
        f.setFont(UIConstants.FONT_BODY);
        f.setBackground(new Color(240, 242, 245));
        f.setForeground(UIConstants.TEXT_PRIMARY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return f;
    }
}
