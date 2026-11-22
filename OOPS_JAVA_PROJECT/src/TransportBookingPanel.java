// TransportBookingPanel.java - Booking form for transport options
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TransportBookingPanel extends JPanel {

    private BookingManager bookingManager;
    private Transport      selectedTransport;
    private MainFrame      mainFrame;
    private int            prefillPassengers;
    private String         prefillDate;

    private JTextField nameField, contactField, dateField;
    private JSpinner   passengersSpinner;
    private JLabel     totalPriceLabel;

    public TransportBookingPanel(BookingManager bookingManager, Transport transport,
                                 MainFrame mainFrame, int passengers, String date) {
        this.bookingManager    = bookingManager;
        this.selectedTransport = transport;
        this.mainFrame         = mainFrame;
        this.prefillPassengers = passengers;
        this.prefillDate       = date;

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

        JLabel title = new JLabel(selectedTransport.getTypeEmoji() + "  Transport Booking");
        title.setFont(UIConstants.FONT_TITLE);
        title.setForeground(Color.WHITE);
        h.add(title, BorderLayout.WEST);

        JLabel info = new JLabel(selectedTransport.getProvider() + "  |  "
                + selectedTransport.getSource() + " → " + selectedTransport.getDestination()
                + "  |  ₹" + String.format("%.0f", selectedTransport.getPricePerPerson()) + " / person");
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

        // --- Transport Info ---
        g.gridy = 0; g.gridx = 0; g.gridwidth = 2;
        form.add(sectionLabel(selectedTransport.getTypeEmoji() + "  Transport Details"), g); g.gridwidth = 1;

        addReadOnlyRow(form, g, 1, "Type:",           selectedTransport.getType() + "  " + selectedTransport.getTypeEmoji());
        addReadOnlyRow(form, g, 2, "Provider:",       selectedTransport.getProvider());
        addReadOnlyRow(form, g, 3, "Route:",          selectedTransport.getSource() + "  →  " + selectedTransport.getDestination());
        addReadOnlyRow(form, g, 4, "Departure:",      selectedTransport.getDepartureTime());
        addReadOnlyRow(form, g, 5, "Duration:",       selectedTransport.getDuration());
        addReadOnlyRow(form, g, 6, "Price / Person:", "₹ " + String.format("%.2f", selectedTransport.getPricePerPerson()));

        g.gridy = 7; g.gridx = 0; g.gridwidth = 2; form.add(new JSeparator(), g); g.gridwidth = 1;

        // --- Passenger Details ---
        g.gridy = 8; g.gridx = 0; g.gridwidth = 2;
        form.add(sectionLabel("👤  Passenger Details"), g); g.gridwidth = 1;

        g.gridy = 9;  g.gridx = 0; form.add(fLabel("Full Name:"), g);
        g.gridx = 1; nameField = UIConstants.styledField(22); form.add(nameField, g);

        g.gridy = 10; g.gridx = 0; form.add(fLabel("Mobile Number:"), g);
        g.gridx = 1; contactField = UIConstants.styledField(22); contactField.setToolTipText("10-digit mobile"); form.add(contactField, g);

        g.gridy = 11; g.gridx = 0; form.add(fLabel("Travel Date:"), g);
        g.gridx = 1;
        dateField = UIConstants.styledField(22);
        dateField.setText(prefillDate);
        dateField.setToolTipText("e.g. 25-Dec-2024");
        form.add(dateField, g);

        g.gridy = 12; g.gridx = 0; form.add(fLabel("No. of Passengers:"), g);
        g.gridx = 1;
        passengersSpinner = new JSpinner(new SpinnerNumberModel(Math.max(1, prefillPassengers), 1, 20, 1));
        passengersSpinner.setFont(UIConstants.FONT_BODY);
        passengersSpinner.setPreferredSize(new Dimension(100, 34));
        passengersSpinner.addChangeListener(e -> updateTotal());
        form.add(passengersSpinner, g);

        // --- Total ---
        g.gridy = 13; g.gridx = 0; g.gridwidth = 2; form.add(new JSeparator(), g); g.gridwidth = 1;
        g.gridy = 14; g.gridx = 0; form.add(fLabel("Total Fare:"), g);
        g.gridx = 1;
        double initTotal = selectedTransport.getTotalPrice(Math.max(1, prefillPassengers));
        totalPriceLabel = new JLabel("₹ " + String.format("%.2f", initTotal));
        totalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalPriceLabel.setForeground(UIConstants.SUCCESS);
        form.add(totalPriceLabel, g);

        // --- Buttons ---
        g.gridy = 15; g.gridx = 0; g.gridwidth = 2; g.anchor = GridBagConstraints.CENTER;
        g.fill = GridBagConstraints.NONE; g.insets = new Insets(20, 10, 10, 10);
        JButton confirmBtn = UIConstants.accentButton("  ✔  Confirm Transport Booking  ");
        confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        confirmBtn.setPreferredSize(new Dimension(260, 44));
        confirmBtn.addActionListener(e -> handleConfirm());
        form.add(confirmBtn, g);

        g.gridy = 16;
        JButton backBtn = UIConstants.dangerButton("  ✖  Cancel  ");
        backBtn.addActionListener(e -> mainFrame.goToMyBookings());
        form.add(backBtn, g);

        GridBagConstraints oc = new GridBagConstraints();
        oc.anchor = GridBagConstraints.CENTER;
        oc.insets = new Insets(30, 50, 30, 50);
        outer.add(form, oc);
        return outer;
    }

    private void updateTotal() {
        int p = (Integer) passengersSpinner.getValue();
        totalPriceLabel.setText("₹ " + String.format("%.2f", selectedTransport.getTotalPrice(p)));
    }

    private void handleConfirm() {
        try {
            String name       = nameField.getText();
            String contact    = contactField.getText();
            String date       = dateField.getText();
            int    passengers = (Integer) passengersSpinner.getValue();

            TransportBooking tb = bookingManager.createTransportBooking(
                    name, contact, selectedTransport, date, passengers);

            // Save to file immediately
            DataManager.saveTransportBookings(bookingManager.getAllTransportBookings());

            String msg = "✅  Transport Booking Confirmed!\n\n"
                    + "Booking ID     :  " + tb.getBookingId() + "\n"
                    + "Name           :  " + tb.getCustomerName() + "\n"
                    + "Transport      :  " + tb.getTransport().getProvider() + "\n"
                    + "Route          :  " + tb.getTransport().getSource() + " → " + tb.getTransport().getDestination() + "\n"
                    + "Departure      :  " + tb.getTransport().getDepartureTime() + "\n"
                    + "Travel Date    :  " + tb.getTravelDate() + "\n"
                    + "Passengers     :  " + tb.getNumPassengers() + "\n"
                    + "Total Fare     :  ₹" + String.format("%.2f", tb.getTotalPrice()) + "\n\n"
                    + "Have a safe journey! " + tb.getTransport().getTypeEmoji() + " 🎉";
            JOptionPane.showMessageDialog(this, msg, "Transport Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.goToMyBookings();

        } catch (InvalidBookingException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addReadOnlyRow(JPanel form, GridBagConstraints g, int row, String label, String value) {
        g.gridy = row; g.gridx = 0; form.add(fLabel(label), g);
        g.gridx = 1;
        JTextField f = new JTextField(value);
        f.setEditable(false);
        f.setFont(UIConstants.FONT_BODY);
        f.setBackground(new Color(240, 242, 245));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        form.add(f, g);
    }

    private JLabel fLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_BODY);
        l.setForeground(UIConstants.TEXT_PRIMARY);
        l.setPreferredSize(new Dimension(180, 30));
        return l;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_HEADING);
        l.setForeground(UIConstants.NAV_BG);
        l.setBorder(new EmptyBorder(6, 0, 2, 0));
        return l;
    }
}
