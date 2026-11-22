// Main.java - Entry point of the Smart Travel Management System
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (EDT) - best practice for Swing
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
