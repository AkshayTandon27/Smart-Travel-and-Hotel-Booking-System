// SearchPanel.java - Search for trips by source, destination and date
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class SearchPanel extends JPanel {

    private BookingManager bookingManager;
    private MainFrame mainFrame;

    // Search input fields
    private JTextField sourceField;
    private JTextField destField;
    private JTextField dateField;
    private JSpinner travellersSpinner;

    // Results area
    private JPanel resultsPanel;

    public SearchPanel(BookingManager bookingManager, MainFrame mainFrame) {
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

        // Show all packages initially
        showResults(bookingManager.getAllPackages());
    }

    // Build the search form at the top
    private JPanel buildSearchForm() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(UIConstants.NAV_BG);
        outer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel("🔍  Search Travel Packages");
        heading.setFont(UIConstants.FONT_TITLE);
        heading.setForeground(Color.WHITE);
        heading.setBorder(new EmptyBorder(0, 0, 14, 0));
        outer.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1: From city, To city, Date
        gbc.gridy = 0;
        gbc.gridx = 0; form.add(label("From City"), gbc);
        gbc.gridx = 1;
        sourceField = UIConstants.styledField(15);
        sourceField.setToolTipText("e.g. Delhi, Mumbai");
        form.add(sourceField, gbc);

        gbc.gridx = 2; form.add(label("To Destination"), gbc);
        gbc.gridx = 3;
        destField = UIConstants.styledField(15);
        destField.setToolTipText("e.g. Goa, Manali");
        form.add(destField, gbc);

        gbc.gridx = 4; form.add(label("Travel Date"), gbc);
        gbc.gridx = 5;
        dateField = UIConstants.styledField(12);
        dateField.setToolTipText("e.g. 25-Dec-2024");
        form.add(dateField, gbc);

        // Row 2: Travellers + Search button
        gbc.gridy = 1;
        gbc.gridx = 0; form.add(label("Travellers"), gbc);
        gbc.gridx = 1;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 20, 1);
        travellersSpinner = new JSpinner(spinnerModel);
        travellersSpinner.setFont(UIConstants.FONT_BODY);
        travellersSpinner.setPreferredSize(new Dimension(80, 36));
        form.add(travellersSpinner, gbc);

        gbc.gridx = 2; gbc.gridwidth = 2;
        JButton searchBtn = UIConstants.accentButton("   Search Packages   ");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        searchBtn.setPreferredSize(new Dimension(200, 40));
        searchBtn.addActionListener(e -> performSearch());
        form.add(searchBtn, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 4; gbc.gridwidth = 2;
        JButton clearBtn = UIConstants.dangerButton("Clear");
        clearBtn.addActionListener(e -> {
            sourceField.setText(""); destField.setText(""); dateField.setText("");
            travellersSpinner.setValue(1);
            showResults(bookingManager.getAllPackages());
        });
        form.add(clearBtn, gbc);

        outer.add(form, BorderLayout.CENTER);
        return outer;
    }

    // Helper to create a white label for the dark form
    private JLabel label(String text) {
        JLabel lbl = new JLabel(text + ":");
        lbl.setFont(UIConstants.FONT_BODY);
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    // Perform the search and refresh results
    private void performSearch() {
        String src  = sourceField.getText().trim();
        String dest = destField.getText().trim();
        ArrayList<TravelPackage> results = bookingManager.searchPackages(src, dest);
        showResults(results);
    }

    // Allow external callers (e.g. ExplorePanel) to pre-fill the destination field
    public void prefillDestination(String destination) {
        destField.setText(destination);
        performSearch();
    }

    // Render package cards in the results area
    private void showResults(ArrayList<TravelPackage> packages) {
        resultsPanel.removeAll();

        if (packages.isEmpty()) {
            JLabel none = new JLabel("No packages found. Try different search terms.");
            none.setFont(UIConstants.FONT_BODY);
            none.setForeground(UIConstants.TEXT_MUTED);
            none.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsPanel.add(Box.createVerticalStrut(30));
            resultsPanel.add(none);
        } else {
            JLabel countLbl = new JLabel(packages.size() + " package(s) found:");
            countLbl.setFont(UIConstants.FONT_HEADING);
            countLbl.setForeground(UIConstants.TEXT_PRIMARY);
            countLbl.setBorder(new EmptyBorder(10, 0, 10, 0));
            resultsPanel.add(countLbl);

            for (TravelPackage pkg : packages) {
                resultsPanel.add(buildPackageCard(pkg));
                resultsPanel.add(Box.createVerticalStrut(14));
            }
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    // Build a horizontal card for a single package
    private JPanel buildPackageCard(TravelPackage pkg) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(UIConstants.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                new EmptyBorder(16, 20, 16, 20)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        // Left: package info
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel nameLbl = new JLabel(pkg.getPackageName());
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLbl.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel routeLbl = new JLabel("📍 " + pkg.getSourceCity() + "  →  " + pkg.getDestination()
                + "    ⏱ " + pkg.getDurationDays() + "N/" + (pkg.getDurationDays() + 1) + "D");
        routeLbl.setFont(UIConstants.FONT_BODY);
        routeLbl.setForeground(UIConstants.TEXT_MUTED);

        JLabel descLbl = new JLabel("<html><body style='width:420px'>" + pkg.getDescription() + "</body></html>");
        descLbl.setFont(UIConstants.FONT_SMALL);
        descLbl.setForeground(UIConstants.TEXT_MUTED);

        JLabel includesLbl = new JLabel("✅ " + pkg.getIncludes());
        includesLbl.setFont(UIConstants.FONT_SMALL);
        includesLbl.setForeground(new Color(39, 100, 60));

        info.add(nameLbl);
        info.add(Box.createVerticalStrut(4));
        info.add(routeLbl);
        info.add(Box.createVerticalStrut(4));
        info.add(descLbl);
        info.add(Box.createVerticalStrut(4));
        info.add(includesLbl);
        card.add(info, BorderLayout.CENTER);

        // Right: price and Book Now button
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);
        right.setBorder(new EmptyBorder(0, 10, 0, 0));

        JLabel priceLbl = new JLabel("₹" + String.format("%.0f", pkg.getPricePerPerson()));
        priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        priceLbl.setForeground(UIConstants.SUCCESS);
        priceLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel perPersonLbl = new JLabel("per person");
        perPersonLbl.setFont(UIConstants.FONT_SMALL);
        perPersonLbl.setForeground(UIConstants.TEXT_MUTED);
        perPersonLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton bookBtn = UIConstants.accentButton("Book Now");
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookBtn.setPreferredSize(new Dimension(120, 36));
        bookBtn.addActionListener(e -> mainFrame.openBookingForm(pkg));

        right.add(Box.createVerticalGlue());
        right.add(priceLbl);
        right.add(perPersonLbl);
        right.add(Box.createVerticalStrut(10));
        right.add(bookBtn);
        right.add(Box.createVerticalGlue());
        card.add(right, BorderLayout.EAST);

        return card;
    }
}
