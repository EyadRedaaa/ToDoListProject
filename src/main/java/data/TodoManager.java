package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class TodoManager {

    // Store file in user's home directory, in a subfolder
    private static final String APP_DATA_FOLDER = ".StudentApp"; // Hidden folder convention
    private static final String TODO_FILE_NAME = "todolist.dat";

    private static File getDataFile() {
        String homeDir = System.getProperty("user.home");
        File appFolder = new File(homeDir, APP_DATA_FOLDER);
        if (!appFolder.exists()) {
            if (!appFolder.mkdirs()) { // Create folder if it doesn't exist
                System.err.println("Warning: Could not create application data folder: " + appFolder.getAbsolutePath());
                // Fallback to current directory if folder creation fails
                return new File(TODO_FILE_NAME);
            }
        }
        return new File(appFolder, TODO_FILE_NAME);
    }


    // Load tasks from file - now loads List<Task>
    @SuppressWarnings("unchecked") // Suppress warning for cast from reading ObjectInputStream
    public static void loadTasks(DefaultListModel<Task> model) {
        model.clear(); // Clear existing items before loading
        File file = getDataFile();
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<Task> tasks = (List<Task>) ois.readObject();
                tasks.forEach(model::addElement);
                System.out.println("Tasks loaded from: " + file.getAbsolutePath());
            } catch (IOException | ClassNotFoundException | ClassCastException e) { // Catch potential cast issues
                System.err.println("Error loading tasks: " + e.getMessage());
                 // Inform user about potential data format issues
                JOptionPane.showMessageDialog(null,
                        "Could not load saved tasks. File might be corrupted or in an old format.\n" + file.getAbsolutePath(),
                        "Load Error", JOptionPane.WARNING_MESSAGE);
                // Optionally, attempt to delete or rename the corrupt file here
            }
        } else {
             System.out.println("Task data file not found, starting fresh: " + file.getAbsolutePath());
        }
    }

    // Save tasks to file - now saves List<Task>
    public static void saveTasks(DefaultListModel<Task> model) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            tasks.add(model.getElementAt(i));
        }

        File file = getDataFile();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(tasks);
            System.out.println("Tasks saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
             JOptionPane.showMessageDialog(null,
                        "Could not save tasks to file:\n" + file.getAbsolutePath() + "\nError: " + e.getMessage(),
                        "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}