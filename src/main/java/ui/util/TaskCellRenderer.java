package ui.util;

import data.Task;
import java.awt.*;
import javax.swing.*;
import ui.theme.AppTheme;

public class TaskCellRenderer extends JPanel implements ListCellRenderer<Task> {

    private JCheckBox checkBox;
    private JLabel label;

    public TaskCellRenderer() {
        setLayout(new BorderLayout(5, 5)); // Add gap between checkbox and label
        setOpaque(true); // Ensure background colors are painted

        checkBox = new JCheckBox();
        checkBox.setOpaque(false); // Checkbox background should be transparent to see panel color
        // Prevent checkbox from stealing focus from the list item itself
        checkBox.setFocusable(false);
        // We handle toggling via button/double-click, not direct checkbox interaction
        checkBox.setEnabled(false);


        label = new JLabel();
        label.setOpaque(false); // Label background should be transparent
        label.setFont(AppTheme.FONT_LIST_ITEM); // Use theme font
        // Ensure label respects panel's horizontal space if text is long
        label.setVerticalAlignment(JLabel.CENTER);


        add(checkBox, BorderLayout.WEST);
        add(label, BorderLayout.CENTER);

        setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5)); // Add some padding within the cell
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Task> list, Task task, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        if (task != null) {
            label.setText(task.getDescription()); // Set raw text first
            checkBox.setSelected(task.isCompleted());

            // Apply strikethrough and different color if completed
            if (task.isCompleted()) {
                 // Using HTML for strikethrough
                label.setText(escapeHtml(task.getDescription()));
                label.setForeground(AppTheme.TEXT_COMPLETED_TASK); // Use a specific color for completed tasks
            } else {
                // Reset text without HTML if not completed
                label.setText(escapeHtml(task.getDescription())); // Ensure non-completed tasks don't have strikethrough
                label.setForeground(AppTheme.TEXT_DARK); // Default text color
            }
        } else {
             label.setText(""); // Handle null task case if necessary
             checkBox.setSelected(false);
             label.setForeground(AppTheme.TEXT_DARK); // Reset color
        }

        // Handle selection highlighting
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            // Keep text color readable on selection background
            // If completed, use a slightly darker version of the completed color, otherwise standard selection foreground
             Color foregroundOnSelection = task != null && task.isCompleted()
                                             ? AppTheme.TEXT_COMPLETED_TASK.darker()
                                             : list.getSelectionForeground();
            label.setForeground(foregroundOnSelection);

        } else {
            // Use the list's default background when not selected
            // Check list background explicitly in case it was changed (like ours)
            setBackground(list.getBackground());
             // Reset foreground color based on completion status when not selected
             label.setForeground(task != null && task.isCompleted() ? AppTheme.TEXT_COMPLETED_TASK : AppTheme.TEXT_DARK);
        }

        // The entire cell (panel) should reflect the enabled state
        setEnabled(list.isEnabled());
        // Components inside should also match the enabled state visually
        label.setEnabled(list.isEnabled());
        // Checkbox visual state is handled separately by its 'completed' status usually,
        // but we disabled interactions, so matching list enabled state is fine.
        // checkBox.setEnabled(list.isEnabled()); // Already handled above


        return this;
    }

     // Basic HTML escaping to prevent issues if task description contains < or >
     private String escapeHtml(String text) {
         if (text == null) return "";
         // Basic escaping for characters that interfere with HTML tags
         return text.replace("&", "&")
                    .replace("<", "<")
                    .replace(">", ">")
                    .replace("\"", "\\\"")
                    .replace("'", "'");
     }
}