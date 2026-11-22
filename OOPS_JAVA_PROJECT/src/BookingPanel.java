// BookingPanel.java - Booking form for travel packages (saves to file after confirm)
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BookingPanel extends JPanel {

    private BookingManager bookingManager;
    private TravelPackage  selectedPackage;
    private MainFrame      mainFrame;

    private JTextField nameField, contactField, dateField;
    private JSpinner   travellersSpinner;
    private JLabel     totalPriceLabel;

    public BookingPanel(BookingManager bookingManager, TravelPackage pkg, MainFrame mainFrame) {
        this.bookingManager  = bookingManager;
        this.selectedPackage = pkg;
        this.mainFrame       = mainFrame;

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

        JLabel title = new JLabel("📋  Complete Your Trip Booking");
        title.setFont(UIConstants.FONT_TITLE);
        title.setForeground(Color.WHITE);
        h.add(title, BorderLayout.WEST);

        JLabel info = new JLabel(selectedPackage.getPackageName() + "  |  "
                + selectedPackage.getDestination() + "  |  ₹"
                + String.format("%.0f", selectedPackage.getPricePerPerson()) + " / person");
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

        // Package Info
        g.gridy = 0; g.gridx = 0; g.gridwidth = 2; form.add(sectionLabel("📦  Package Details"), g); g.gridwidth = 1;

        addReadOnlyRow(form, g, 1, "Package:",    selectedPackage.getPackageName());
        addReadOnlyRow(form, g, 2, "From:",       selectedPackage.getSourceCity() + " → " + selectedPackage.getDestination());
        addReadOnlyRow(form, g, 3, "Duration:",   selectedPackage.getDurationDays() + "N / " + (selectedPackage.getDurationDays() + 1) + "D");
        addReadOnlyRow(form, g, 4, "Includes:",   selectedPackage.getIncludes());
        addReadOnlyRow(form, g, 5, "Price/Person","₹ " + String.format("%.2f", selectedPackage.getPricePerPerson()));

        g.gridy = 6; g.gridx = 0; g.gridwidth = 2; form.add(new JSeparator(), g); g.gridwidth = 1;

        // Traveller Details
        g.gridy = 7; g.gridx = 0; g.gridwidth = 2; form.add(sectionLabel("👤  Traveller Details"), g); g.gridwidth = 1;

        g.gridy = 8;  g.gridx = 0; form.add(fLabel("Full Name:"), g);
        g.gridx = 1; nameField = UIConstants.styledField(22); form.add(nameField, g);

        g.gridy = 9;  g.gridx = 0; form.add(fLabel("Mobile Number:"), g);
        g.gridx = 1; contactField = UIConstants.styledField(22); contactField.setToolTipText("10-digit mobile"); form.add(contactField, g);

        g.gridy = 10; g.gridx = 0; form.add(fLabel("Travel Date:"), g);
        g.gridx = 1; dateField = UIConstants.styledField(22); dateField.setToolTipText("e.g. 25-Dec-2024"); form.add(dateField, g);

        g.gridy = 11; g.gridx = 0; form.add(fLabel("No. of Travellers:"), g);
        g.gridx = 1;
        travellersSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        travellersSpinner.setFont(UIConstants.FONT_BODY);
        travellersSpinner.setPreferredSize(new Dimension(100, 34));
        travellersSpinner.addChangeListener(e -> updateTotal());
        form.add(travellersSpinner, g);

        // Total
        g.gridy = 12; g.gridx = 0; g.gridwidth = 2; form.add(new JSeparator(), g); g.gridwidth = 1;
        g.gridy = 13; g.gridx = 0; form.add(fLabel("Total Amount:"), g);
        g.gridx = 1;
        totalPriceLabel = new JLabel("₹ " + String.format("%.2f", selectedPackage.getPricePerPerson()));
        totalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalPriceLabel.setForeground(UIConstants.SUCCESS);
        form.add(totalPriceLabel, g);

        // Buttons
        g.gridy = 14; g.gridx = 0; g.gridwidth = 2; g.anchor = GridBagConstraints.CENTER;
        g.fill = GridBagConstraints.NONE; g.insets = new Insets(20, 10, 10, 10);
        JButton confirmBtn = UIConstants.accentButton("  ✔  Confirm Booking  ");
        confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        confirmBtn.setPreferredSize(new Dimension(220, 44));
        confirmBtn.addActionListener(e -> handleConfirm());
        form.add(confirmBtn, g);

        g.gridy = 15;
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
        int t = (Integer) travellersSpinner.getValue();
        totalPriceLabel.setText("₹ " + String.format("%.2f", selectedPackage.getTotalPrice(t)));
    }

    private void handleConfirm() {
        try {
            String name       = nameField.getText();
            String contact    = contactField.getText();
            int    travellers = (Integer) travellersSpinner.getValue();
            String date       = dateField.getText();

            Booking booking = bookingManager.createBooking(name, contact, selectedPackage, travellers, date);

            // ---- File Handling: save to disk immediately ----
            DataManager.saveTripBookings(bookingManager.getAllBookings());

            String msg = "✅  Booking Confirmed!\n\n"
                    + "Booking ID     :  " + booking.getBookingId() + "\n"
                    + "Name           :  " + booking.getCustomerName() + "\n"
                    + "Package        :  " + booking.getTravelPackage().getPackageName() + "\n"
                    + "Destination    :  " + booking.getTravelPackage().getDestination() + "\n"
                    + "Travel Date    :  " + booking.getTravelDate() + "\n"
                    + "Travellers     :  " + booking.getNumTravellers() + "\n"
                    + "Total Amount   :  ₹" + String.format("%.2f", booking.getTotalPrice()) + "\n\n"
                    + "Thank you for booking with TripVista! 🎉\n"
                    + "(Your booking has been saved to file.)";
            JOptionPane.showMessageDialog(this, msg, "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
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
}
