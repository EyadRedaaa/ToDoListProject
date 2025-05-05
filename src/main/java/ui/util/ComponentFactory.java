package ui.util;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import ui.theme.AppTheme;
// It's good practice to import Color explicitly if used often
// import java.awt.Color;

public class ComponentFactory {

    public static JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    public static JTextField createStyledTextField(int columns, String placeholder) {
        JTextField textField = new JTextField(columns);
        textField.setFont(AppTheme.FONT_INPUT);
        textField.setBorder(AppTheme.BORDER_COMPOUND_INPUT);
        textField.setBackground(AppTheme.TEXT_FIELD_BACKGROUND);
        addPlaceholder(textField, placeholder);
        return textField;
    }

    public static JPasswordField createStyledPasswordField(int columns, String placeholder) {
        JPasswordField passwordField = new JPasswordField(columns);
        passwordField.setFont(AppTheme.FONT_INPUT);
        passwordField.setBorder(AppTheme.BORDER_COMPOUND_INPUT);
        passwordField.setBackground(AppTheme.TEXT_FIELD_BACKGROUND);
        addPlaceholder(passwordField, placeholder);
        return passwordField;
    }

    public static JButton createStyledButton(String text, Color background, Color foreground, Color hoverBackground) {
        JButton button = new JButton(text);
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setBorder(AppTheme.PADDING_BUTTON);
        button.setFocusPainted(false);
        button.setOpaque(true); // Needed for background color on some L&Fs
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // --- FIX: Declare originalBackground here, before the listeners ---
        final Color originalBackground = background; // Make it final or effectively final

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            // --- FIX: Remove declaration from here ---
            // Color originalBackground = background; // REMOVED

            @Override
            public void mouseEntered(MouseEvent e) {
                 if (button.isEnabled()) { // Only change if enabled
                    button.setBackground(hoverBackground);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                 if (button.isEnabled()) { // Check enabled on exit too
                    button.setBackground(originalBackground); // Use variable from outer scope
                 }
                 // If disabled, the PropertyChangeListener should have already set the color
            }

             // Optional: Handle pressed state visually
             @Override
             public void mousePressed(MouseEvent e) {
                 if (button.isEnabled()) {
                    // Slightly darker version of hover or original
                    button.setBackground(hoverBackground.darker());
                 }
             }

             @Override
             public void mouseReleased(MouseEvent e) {
                  if (button.isEnabled()) {
                       // Revert to hover color if mouse is still over, otherwise original
                        Point p = e.getPoint();
                        // Check bounds relative to the button itself
                        if(button.getBounds().contains(p)) { // More robust check
                            button.setBackground(hoverBackground);
                        } else {
                            button.setBackground(originalBackground); // Use variable from outer scope
                        }
                  }
             }
        });

        // Ensure background resets if button is disabled/enabled
        button.addPropertyChangeListener("enabled", evt -> {
            if (!(Boolean) evt.getNewValue()) { // If disabled
                // Use variable from outer scope
                button.setBackground(originalBackground.darker().darker()); // Show distinct disabled state
            } else { // If enabled
                // Reset to original enabled color, use variable from outer scope
                // Check if mouse is currently over the button on re-enable
                Point mousePos = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(mousePos, button.getParent()); // Convert to parent's coord system
                if (button.getBounds().contains(mousePos)) {
                     button.setBackground(hoverBackground); // Apply hover if mouse is still over
                } else {
                     button.setBackground(originalBackground); // Otherwise, set original
                }
            }
        });


        return button;
    }

    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, AppTheme.BUTTON_PRIMARY_BACKGROUND, AppTheme.BUTTON_PRIMARY_FOREGROUND, AppTheme.BUTTON_PRIMARY_HOVER);
    }

    public static JButton createDangerButton(String text) {
        return createStyledButton(text, AppTheme.BUTTON_DANGER_BACKGROUND, AppTheme.BUTTON_DANGER_FOREGROUND, AppTheme.BUTTON_DANGER_HOVER);
    }
     public static JButton createSecondaryButton(String text) {
        return createStyledButton(text, AppTheme.BUTTON_SECONDARY_BACKGROUND, AppTheme.BUTTON_SECONDARY_FOREGROUND, AppTheme.BUTTON_SECONDARY_HOVER);
    }


    // Helper for placeholder text
    public static void addPlaceholder(JTextField textField, String placeholder) {
        // Initial state only if field is empty
        if (textField.getText().isEmpty()) {
             textField.setForeground(AppTheme.PLACEHOLDER_COLOR);
             textField.setText(placeholder);
        }

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder) && textField.getForeground().equals(AppTheme.PLACEHOLDER_COLOR)) {
                    textField.setText("");
                    textField.setForeground(AppTheme.TEXT_DARK); // Or original text color
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(AppTheme.PLACEHOLDER_COLOR);
                    textField.setText(placeholder);
                }
            }
        });
    }

    // Overload for JPasswordField (slightly different handling for password char)
     public static void addPlaceholder(JPasswordField passwordField, String placeholder) {
         // Initial state only if field is empty
         final char defaultEchoChar = passwordField.getEchoChar(); // Store default echo char

         if (passwordField.getPassword().length == 0) {
             passwordField.setForeground(AppTheme.PLACEHOLDER_COLOR);
             passwordField.setEchoChar((char) 0); // Show placeholder text
             passwordField.setText(placeholder);
         }


        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                 String currentText = new String(passwordField.getPassword());
                 // Check if the placeholder is currently displayed
                if (currentText.equals(placeholder) && passwordField.getEchoChar() == (char) 0) {
                    passwordField.setText("");
                    passwordField.setEchoChar(defaultEchoChar); // Set echo char back
                    passwordField.setForeground(AppTheme.TEXT_DARK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setForeground(AppTheme.PLACEHOLDER_COLOR);
                    passwordField.setEchoChar((char) 0); // Show placeholder text
                    passwordField.setText(placeholder);
                } else {
                    // Ensure echo char is set if user typed something and tabbed away
                    passwordField.setEchoChar(defaultEchoChar);
                }
            }
        });
    }

    // Helper to get text, handling placeholder
    public static String getTextFieldValue(JTextField textField, String placeholder) {
        if (textField.getText().equals(placeholder) && textField.getForeground().equals(AppTheme.PLACEHOLDER_COLOR)) {
            return "";
        }
        return textField.getText().trim();
    }

     public static String getPasswordFieldValue(JPasswordField passwordField, String placeholder) {
         String currentText = new String(passwordField.getPassword());
         // Check if placeholder is showing
         if (passwordField.getEchoChar() == (char) 0 && currentText.equals(placeholder)) {
             return "";
         }
         // Ensure echo char is set correctly before returning, in case focus was lost weirdly
         // But only if it actually contains text (don't set echo char on empty field)
         if (passwordField.getPassword().length > 0) {
             passwordField.setEchoChar('*'); // Or use the stored defaultEchoChar if needed
         }
         return currentText.trim();
     }
}