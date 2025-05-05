package ui;

import data.TodoManager; // Import if dashboard load needs login context later
import ui.theme.AppTheme;
import ui.util.ComponentFactory;
import ui.util.GradientPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginWindow extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginWindow() {
        setupFrame();
        initComponents();
        layoutComponents();
        attachListeners();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle(AppTheme.LOGIN_TITLE);
        setSize(AppTheme.LOGIN_WINDOW_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout()); // Use BorderLayout for the main frame
    }

    private void initComponents() {
        emailField = ComponentFactory.createStyledTextField(20, AppTheme.EMAIL_PLACEHOLDER);
        passwordField = ComponentFactory.createStyledPasswordField(20, AppTheme.PASSWORD_PLACEHOLDER);
        loginButton = ComponentFactory.createPrimaryButton("Login");
    }

    private void layoutComponents() {
        // Main Panel (Gradient Background)
        JPanel mainPanel = new GradientPanel(AppTheme.BACKGROUND_GRADIENT_START_LOGIN, AppTheme.BACKGROUND_GRADIENT_END_LOGIN);
        mainPanel.setLayout(new GridBagLayout()); // Use GridBagLayout to center the formPanel

        // Form Panel (White Card)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(AppTheme.CARD_BACKGROUND);
        formPanel.setBorder(AppTheme.PADDING_DIALOG);
        formPanel.setPreferredSize(AppTheme.LOGIN_FORM_SIZE); // Set preferred size

        // --- Form Components ---
        JLabel titleLabel = ComponentFactory.createStyledLabel(AppTheme.LOGIN_TITLE, AppTheme.FONT_TITLE, AppTheme.TEXT_DARK);
        JLabel emailLabel = ComponentFactory.createStyledLabel("Email:", AppTheme.FONT_LABEL, AppTheme.TEXT_DARK);
        JLabel passwordLabel = ComponentFactory.createStyledLabel("Password:", AppTheme.FONT_LABEL, AppTheme.TEXT_DARK);

        // --- GridBagConstraints for Form Layout ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = AppTheme.GBC_INSETS;
        gbc.anchor = GridBagConstraints.WEST; // Align labels to the left

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the title
        formPanel.add(titleLabel, gbc);

        // Reset anchor and gridwidth for other components
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make text fields fill horizontally

        // Email Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE; // Labels don't fill
        formPanel.add(emailLabel, gbc);

        // Email Field
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0; // Allow field to expand horizontally
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0; // Reset weight
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(passwordLabel, gbc);

        // Password Field
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span button
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE; // Don't stretch button
        gbc.anchor = GridBagConstraints.CENTER; // Center button
        formPanel.add(loginButton, gbc);

        // Add Form Panel to the center of the Main Panel
        // GridBagConstraints for centering the formPanel within mainPanel
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.weightx = 1.0; // Allows horizontal centering
        mainGbc.weighty = 1.0; // Allows vertical centering
        mainGbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(formPanel, mainGbc);

        // Add Main Panel to Frame
        add(mainPanel, BorderLayout.CENTER);
    }


    private void attachListeners() {
        // Login Button Action
        loginButton.addActionListener(e -> performLogin());

        // Allow pressing Enter in password field to trigger login
        passwordField.addActionListener(e -> performLogin());
    }

    private void performLogin() {
    	String email = ComponentFactory.getTextFieldValue(emailField, "Enter your email ");
    	String password = ComponentFactory.getPasswordFieldValue(passwordField, AppTheme.PASSWORD_PLACEHOLDER);

        // Basic Validation (Improved)
        if (email.isEmpty() && password.isEmpty()) {
            showErrorDialog("Email and password cannot be empty!");
        } else if (email.isEmpty()) {
            showErrorDialog("Email cannot be empty!");
        } else if (!isValidEmail(email)) { // Add basic email format check
             showErrorDialog("Please enter a valid email address.");
        } else if (password.isEmpty()) {
            showErrorDialog("Password cannot be empty!");
        } else {
            // --- Placeholder Login Logic ---
            // In a real app, you would validate credentials against a database/service
            System.out.println("Attempting login for: " + email);
            // For now, any valid-looking email and non-empty password works
            openDashboard();
        }
    }

    private void openDashboard() {
        // Use invokeLater to ensure dashboard creation happens on EDT after login logic
         SwingUtilities.invokeLater(() -> {
             new StudentDashboard(this); // Pass login window reference for logout
             setVisible(false); // Hide login window instead of disposing if logout needs it
             // dispose(); // Use dispose if you don't need to return to login screen
         });
    }

    // Basic email format check (can be more robust)
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }


    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                this,
                "❌ " + message, // Add icon inline
                AppTheme.LOGIN_ERROR_TITLE,
                JOptionPane.ERROR_MESSAGE
        );
    }

    // Make the login window visible again (called by dashboard on logout)
    public void showWindow() {
         // Reset fields for security/convenience
         emailField.setText("");
         passwordField.setText("");
         ComponentFactory.addPlaceholder(emailField, AppTheme.EMAIL_PLACEHOLDER); // Re-apply placeholder if needed
         ComponentFactory.addPlaceholder(passwordField, AppTheme.PASSWORD_PLACEHOLDER);
         setVisible(true);
    }
}