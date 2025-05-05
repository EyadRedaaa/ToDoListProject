import javax.swing.*;
import ui.LoginWindow;
// import ui.theme.AppTheme; // Not strictly needed here, but good practice if setting specific L&F defaults

/**
 * Main application class to launch the Student Login application.
 */
public class App {

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
    		setLookAndFeel();
            createAndShowGUI();
        
    }

    private static void setLookAndFeel() {
        try {
            // Use the system look and feel for better platform integration
            String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(systemLookAndFeel);

            // Optional: You could force Nimbus for a consistent modern look across platforms:
     
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                 if ("Nimbus".equals(info.getName())) {
                     UIManager.setLookAndFeel(info.getClassName());
                     break;
                 }
             }

        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println("Failed to set the desired look and feel, using default.");
            // The default Metal L&F will be used if setting fails.
        }
    }

    private static void createAndShowGUI() {
        new LoginWindow();
    }
}