// TransportPanel.java - Search and book transport (Flight / Train / Bus / Cab)
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class TransportPanel extends JPanel {

    private BookingManager bookingManager;
    private MainFrame      mainFrame;
    private JPanel         resultsPanel;
    private JTextField     sourceField, destField, dateField;
    private JComboBox<String> typeCombo;
    private JSpinner       passengersSpinner;

    public TransportPanel(BookingManager bookingManager, MainFrame mainFrame) {
        this.bookingManager = bookingManager;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_MAIN);

        add(buildSearchForm(), BorderLayout.NORTH);

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(UIConstants.BG_MAIN);
        resultsPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(resultsPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        showResults(bookingManager.getAllTransports()); // Show all by default
    }

    private JPanel buildSearchForm() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(UIConstants.NAV_BG);
        outer.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel heading = new JLabel("🚌  Search Transport Options");
        heading.setFont(UIConstants.FONT_TITLE);
        heading.setForeground(Color.WHITE);
        heading.setBorder(new EmptyBorder(0, 0, 12, 0));
        outer.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0;
        gbc.gridx = 0; form.add(wLabel("From:"), gbc);
        gbc.gridx = 1; sourceField = UIConstants.styledField(14); sourceField.setToolTipText("e.g. Delhi, Mumbai"); form.add(sourceField, gbc);
        gbc.gridx = 2; form.add(wLabel("To:"), gbc);
        gbc.gridx = 3; destField   = UIConstants.styledField(14); destField.setToolTipText("e.g. Goa, Manali");   form.add(destField, gbc);
        gbc.gridx = 4; form.add(wLabel("Date:"), gbc);
        gbc.gridx = 5; dateField   = UIConstants.styledField(12); dateField.setToolTipText("e.g. 25-Dec-2024");   form.add(dateField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0; form.add(wLabel("Type:"), gbc);
        gbc.gridx = 1;
        typeCombo = new JComboBox<>(new String[]{"All Types", "Flight", "Train", "Bus", "Cab"});
        typeCombo.setFont(UIConstants.FONT_BODY);
        typeCombo.setPreferredSize(new Dimension(130, 34));
        form.add(typeCombo, gbc);

        gbc.gridx = 2; form.add(wLabel("Passengers:"), gbc);
        gbc.gridx = 3;
        passengersSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        passengersSpinner.setFont(UIConstants.FONT_BODY);
        passengersSpinner.setPreferredSize(new Dimension(80, 34));
        form.add(passengersSpinner, gbc);

        gbc.gridx = 4; gbc.gridwidth = 1;
        JButton searchBtn = UIConstants.accentButton("Search");
        searchBtn.setPreferredSize(new Dimension(100, 36));
        searchBtn.addActionListener(e -> performSearch());
        form.add(searchBtn, gbc);

        gbc.gridx = 5;
        JButton clearBtn = UIConstants.dangerButton("Clear");
        clearBtn.addActionListener(e -> {
            sourceField.setText(""); destField.setText(""); dateField.setText("");
            typeCombo.setSelectedIndex(0); passengersSpinner.setValue(1);
            showResults(bookingManager.getAllTransports());
        });
        form.add(clearBtn, gbc);

        outer.add(form, BorderLayout.CENTER);
        return outer;
    }

    private JLabel wLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_BODY);
        l.setForeground(Color.WHITE);
        return l;
    }

    private void performSearch() {
        String src  = sourceField.getText().trim();
        String dest = destField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();
        ArrayList<Transport> results = bookingManager.searchTransport(src, dest, type);
        showResults(results);
    }

    public void prefill(String source, String destination) {
        sourceField.setText(source);
        destField.setText(destination);
        performSearch();
    }

    private void showResults(ArrayList<Transport> list) {
        resultsPanel.removeAll();

        if (list.isEmpty()) {
            JLabel none = new JLabel("No transport found. Try different filters.");
            none.setFont(UIConstants.FONT_BODY);
            none.setForeground(UIConstants.TEXT_MUTED);
            none.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsPanel.add(Box.createVerticalStrut(20));
            resultsPanel.add(none);
        } else {
            JLabel countLbl = new JLabel(list.size() + " option(s) found:");
            countLbl.setFont(UIConstants.FONT_HEADING);
            countLbl.setForeground(UIConstants.TEXT_PRIMARY);
            countLbl.setBorder(new EmptyBorder(10, 0, 8, 0));
            resultsPanel.add(countLbl);

            for (Transport t : list) {
                resultsPanel.add(buildTransportCard(t));
                resultsPanel.add(Box.createVerticalStrut(10));
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private JPanel buildTransportCard(Transport t) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(UIConstants.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                new EmptyBorder(14, 18, 14, 18)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Left: type icon + provider + route details
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        // Type badge (coloured)
        JLabel typeLbl = new JLabel(t.getTypeEmoji() + "  " + t.getType().toUpperCase());
        typeLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        typeLbl.setForeground(getTypeColor(t.getType()));

        JLabel providerLbl = new JLabel("🏢  " + t.getProvider());
        providerLbl.setFont(UIConstants.FONT_BODY);
        providerLbl.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel routeLbl = new JLabel("📍 " + t.getSource() + "  →  " + t.getDestination()
                + "    🕐 Departs: " + t.getDepartureTime()
                + "    ⏱ " + t.getDuration());
        routeLbl.setFont(UIConstants.FONT_SMALL);
        routeLbl.setForeground(UIConstants.TEXT_MUTED);

        left.add(typeLbl);
        left.add(Box.createVerticalStrut(4));
        left.add(providerLbl);
        left.add(Box.createVerticalStrut(4));
        left.add(routeLbl);
        card.add(left, BorderLayout.CENTER);

        // Right: price + Book Now
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);

        int passengers = (Integer) passengersSpinner.getValue();
        JLabel priceLbl = new JLabel("₹" + String.format("%.0f", t.getPricePerPerson()));
        priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        priceLbl.setForeground(UIConstants.SUCCESS);
        priceLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel perPLbl = new JLabel("per person");
        perPLbl.setFont(UIConstants.FONT_SMALL);
        perPLbl.setForeground(UIConstants.TEXT_MUTED);
        perPLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton bookBtn = UIConstants.accentButton("Book Now");
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookBtn.addActionListener(e -> mainFrame.openTransportBookingForm(t, (Integer) passengersSpinner.getValue(), dateField.getText()));

        right.add(Box.createVerticalGlue());
        right.add(priceLbl);
        right.add(perPLbl);
        right.add(Box.createVerticalStrut(8));
        right.add(bookBtn);
        right.add(Box.createVerticalGlue());
        card.add(right, BorderLayout.EAST);

        return card;
    }

    private Color getTypeColor(String type) {
        switch (type) {
            case "Flight": return new Color(0, 100, 200);
            case "Train":  return new Color(150, 60, 0);
            case "Bus":    return new Color(0, 140, 60);
            case "Cab":    return new Color(180, 100, 0);
            default:       return UIConstants.TEXT_PRIMARY;
        }
    }
}
