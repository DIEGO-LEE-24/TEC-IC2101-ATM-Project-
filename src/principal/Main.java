package principal;

import javax.swing.SwingUtilities;
import vista.VistaGUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new VistaGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
