// UIConstants.java - Central place for all UI colours, fonts and styles
import java.awt.*;

public class UIConstants {

    // --- Colour Palette ---
    public static final Color NAV_BG       = new Color(13, 27, 62);    // Dark navy blue
    public static final Color NAV_TEXT     = new Color(255, 255, 255);  // White
    public static final Color ACCENT       = new Color(0, 184, 169);    // Teal
    public static final Color ACCENT_HOVER = new Color(0, 150, 136);    // Darker teal
    public static final Color BG_MAIN      = new Color(245, 247, 250);  // Light grey-white
    public static final Color BG_CARD      = new Color(255, 255, 255);  // White card
    public static final Color TEXT_PRIMARY = new Color(30, 30, 50);     // Near-black
    public static final Color TEXT_MUTED   = new Color(110, 120, 140);  // Grey
    public static final Color SUCCESS      = new Color(39, 174, 96);    // Green
    public static final Color DANGER       = new Color(231, 76, 60);    // Red
    public static final Color BORDER       = new Color(220, 225, 235);  // Light border

    // --- Fonts ---
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_NAV     = new Font("Segoe UI", Font.BOLD, 14);

    // --- Helper: styled accent button ---
    public static javax.swing.JButton accentButton(String text) {
        javax.swing.JButton btn = new javax.swing.JButton(text);
        btn.setBackground(ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    // --- Helper: danger (red) button ---
    public static javax.swing.JButton dangerButton(String text) {
        javax.swing.JButton btn = new javax.swing.JButton(text);
        btn.setBackground(DANGER);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    // --- Helper: Create a styled text field ---
    public static javax.swing.JTextField styledField(int cols) {
        javax.swing.JTextField field = new javax.swing.JTextField(cols);
        field.setFont(FONT_BODY);
        field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(BORDER, 1),
                javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return field;
    }
}
