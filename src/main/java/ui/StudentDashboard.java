package ui;

import data.Task; // Import Task
import data.TodoManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder; // Import Renderer
import ui.theme.AppTheme;
import ui.util.ComponentFactory;
import ui.util.GradientPanel;
import ui.util.TaskCellRenderer;

public class StudentDashboard extends JFrame {
    // Change model and list to use Task objects
    private DefaultListModel<Task> todoListModel;
    private JList<Task> todoList;
    private JTextField todoInput;
    private JButton addButton;
    private JButton deleteButton;
    private JButton toggleCompleteButton; // Button to toggle completion

    private final LoginWindow loginWindow; // Keep reference for logout

    public StudentDashboard(LoginWindow loginRef) {
        this.loginWindow = loginRef; // Store the reference
        setupFrame();
        initComponents();
        layoutComponents();
        attachListeners(); // Will be updated
        loadTasks();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle(AppTheme.DASHBOARD_TITLE);
        setSize(AppTheme.DASHBOARD_INITIAL_SIZE);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Handle closing manually
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void initComponents() {
        // Model uses Task
        todoListModel = new DefaultListModel<>();
        // JList uses Task
        todoList = new JList<>(todoListModel);
        // Set the custom renderer
        todoList.setCellRenderer(new TaskCellRenderer());
        todoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Background needs to be set here AFTER renderer, or renderer overrides it
        todoList.setBackground(new Color(255, 255, 255, 220));
        todoList.setBorder(AppTheme.PADDING_INPUT_FIELD);


        todoInput = ComponentFactory.createStyledTextField(30, AppTheme.TASK_PLACEHOLDER);

        // Initialize buttons (will be added in layout)
        addButton = ComponentFactory.createPrimaryButton("Add Task");
        addButton.setToolTipText(AppTheme.TOOLTIP_ADD_TASK);

        deleteButton = ComponentFactory.createDangerButton("Delete Task");
        deleteButton.setToolTipText(AppTheme.TOOLTIP_DELETE_TASK);

        // New button for toggling completion
        toggleCompleteButton = ComponentFactory.createSecondaryButton("Toggle Complete");
        toggleCompleteButton.setToolTipText(AppTheme.TOOLTIP_TOGGLE_TASK);
    }

    private void layoutComponents() {
        JPanel mainPanel = new GradientPanel(AppTheme.BACKGROUND_GRADIENT_START_DASHBOARD, AppTheme.BACKGROUND_GRADIENT_END_DASHBOARD);
        mainPanel.setLayout(new BorderLayout());

        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppTheme.HEADER_BACKGROUND);
        headerPanel.setBorder(AppTheme.PADDING_HEADER);
        JLabel headerLabel = ComponentFactory.createStyledLabel(AppTheme.DASHBOARD_TITLE + " - To-Do List", AppTheme.FONT_HEADER, AppTheme.TEXT_LIGHT);
        headerPanel.add(headerLabel, BorderLayout.WEST);
        JButton logoutButton = ComponentFactory.createSecondaryButton("Logout");
        logoutButton.setToolTipText(AppTheme.TOOLTIP_LOGOUT);
        logoutButton.addActionListener(e -> performLogout());
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // --- To-Do Section Panel (Center) ---
        JPanel todoSectionPanel = new JPanel(new BorderLayout());
        todoSectionPanel.setOpaque(false);
        todoSectionPanel.setBorder(AppTheme.PADDING_PANEL);

        // Scroll Pane for the List
        JScrollPane scrollPane = new JScrollPane(todoList);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.TEXT_FIELD_BORDER));

        // --- Input & Button Panel ---
        JPanel inputControlPanel = new JPanel(new BorderLayout(10, 5));
        inputControlPanel.setOpaque(false);
        inputControlPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Buttons Panel (Add/Delete/Toggle)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        // Add the toggle button
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(toggleCompleteButton); // Add the new button

        inputControlPanel.add(todoInput, BorderLayout.CENTER);
        inputControlPanel.add(buttonPanel, BorderLayout.EAST);

        todoSectionPanel.add(scrollPane, BorderLayout.CENTER);
        todoSectionPanel.add(inputControlPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(todoSectionPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // Updated attachListeners
    private void attachListeners() {
        // Window Closing Listener (Save tasks then exit)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApplication();
            }
        });

        // Button Listeners
        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteTask());
        toggleCompleteButton.addActionListener(e -> toggleTaskCompletion()); // Listener for new button

        // Input field Enter key listener
        todoInput.addActionListener(e -> addTask());

        // List Listeners
        // 1. Double-click to edit
        todoList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) { // Double-left-click
                    int index = todoList.locationToIndex(e.getPoint());
                    if (index >= 0 && todoList.getCellBounds(index, index).contains(e.getPoint())) {
                        editTask(index);
                    }
                }
            }
        });

        // 2. Update button states based on selection
         todoList.addListSelectionListener(e -> {
             if (!e.getValueIsAdjusting()) { // Avoid multiple events during selection change
                 updateButtonStates();
             }
         });

         // Initial button state
         updateButtonStates();
    }

    // --- Action Methods ---

    private void addTask() {
        String taskDescription = ComponentFactory.getTextFieldValue(todoInput, AppTheme.TASK_PLACEHOLDER);
        if (!taskDescription.isEmpty()) {
            Task newTask = new Task(taskDescription); // Create Task object
            todoListModel.addElement(newTask);
            // Reset input field safely considering placeholder logic
            todoInput.setText(""); // Clear text first
            ComponentFactory.addPlaceholder(todoInput, AppTheme.TASK_PLACEHOLDER); // Re-apply placeholder logic

            todoList.setSelectedIndex(todoListModel.getSize() - 1); // Select the newly added task
            todoList.ensureIndexIsVisible(todoListModel.getSize() - 1); // Scroll to it
        } else {
            showWarningDialog("Task description cannot be empty!");
        }
    }

    private void deleteTask() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            // Confirmation dialog
            Task taskToDelete = todoListModel.getElementAt(selectedIndex);
             int choice = JOptionPane.showConfirmDialog(
                 this,
                 "Are you sure you want to delete the task:\n\"" + escapeHtml(taskToDelete.getDescription()) + "\"", // Escape HTML here too
                 "Confirm Deletion",
                 JOptionPane.YES_NO_OPTION,
                 JOptionPane.WARNING_MESSAGE
             );
             if (choice == JOptionPane.YES_OPTION) {
                 todoListModel.remove(selectedIndex);
                 // Optionally clear selection or select previous/next item
                 if (todoListModel.getSize() > 0) {
                     todoList.setSelectedIndex(Math.max(0, selectedIndex - 1));
                 }
             }
        } else {
            showInfoDialog("No task selected to delete!");
        }
         updateButtonStates(); // Ensure buttons reflect potential selection change
    }

    private void toggleTaskCompletion() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task task = todoListModel.getElementAt(selectedIndex);
            task.setCompleted(!task.isCompleted()); // Toggle status
            // Force repaint for the specific item/row by notifying model listeners
            todoListModel.setElementAt(task, selectedIndex); // This usually triggers repaint
        } else {
             showInfoDialog("No task selected to toggle!");
        }
    }

     private void editTask(int index) {
        Task taskToEdit = todoListModel.getElementAt(index);
        String currentDescription = taskToEdit.getDescription();

        // Show an input dialog for editing
        // Using a temporary JTextField allows for better control if needed later
        JTextField editorField = new JTextField(currentDescription);
        int result = JOptionPane.showConfirmDialog(
                this,
                editorField, // Use the text field directly in the dialog
                AppTheme.EDIT_TASK_PROMPT_TITLE,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newDescription = editorField.getText().trim();

            if (!newDescription.isEmpty() && !newDescription.equals(currentDescription)) {
                taskToEdit.setDescription(newDescription);
                todoListModel.setElementAt(taskToEdit, index); // Update model to reflect change
            } else if (newDescription.isEmpty()){
                 showWarningDialog("Task description cannot be empty after editing.");
                 // Task description remains unchanged if edit results in empty string
            }
             // If description didn't change or user cancelled, do nothing more
        }
    }

    // --- Utility Methods ---

    private void loadTasks() {
        TodoManager.loadTasks(todoListModel);
        updateButtonStates(); // Update buttons after loading
    }

    private void saveTasks() {
        TodoManager.saveTasks(todoListModel);
    }

    private void performLogout() {
         saveTasks(); // Save tasks before logging out
         dispose(); // Close the dashboard
         if(loginWindow != null) {
             loginWindow.showWindow(); // Use the method to make it visible again
         } else {
             // Fallback if login window reference was lost (shouldn't happen)
             SwingUtilities.invokeLater(LoginWindow::new);
             System.err.println("Warning: LoginWindow reference lost during logout.");
         }
     }

    private void closeApplication() {
        saveTasks(); // Save tasks before exiting
        dispose(); // Close the dashboard window
        // If LoginWindow was just hidden, dispose it too
        if (loginWindow != null && loginWindow.isDisplayable()) {
            loginWindow.dispose();
        }
        System.exit(0); // Terminate the application
    }

     private void updateButtonStates() {
        boolean itemSelected = todoList.getSelectedIndex() != -1;
        deleteButton.setEnabled(itemSelected);
        toggleCompleteButton.setEnabled(itemSelected);
     }

     // Helper methods for dialogs
      private void showWarningDialog(String message) {
        JOptionPane.showMessageDialog(this, "⚠️ " + message, AppTheme.ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
    }

      private void showInfoDialog(String message) {
        JOptionPane.showMessageDialog(this, "ℹ️ " + message, AppTheme.ERROR_TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

    // Basic HTML escaping needed if showing task descriptions in dialogs/tooltips
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&").replace("<", "<").replace(">", ">");
    }
}