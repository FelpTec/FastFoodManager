package senai.felp;

import javax.swing.*;

public class FastFoodManager {
    public static void main(String[] ignoredArgs) {
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}
