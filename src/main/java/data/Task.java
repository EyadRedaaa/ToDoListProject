package data;

import java.io.Serializable;
import java.util.Objects;

public class Task implements Serializable {
    // Required for serialization compatibility if class structure changes later
    private static final long serialVersionUID = 1L;

    private String description;
    private boolean completed;

    public Task(String description) {
        this.description = description;
        this.completed = false; // Tasks start as not completed
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        // Default toString used by JList if no renderer is set,
        // but we will use a custom renderer. Still good practice to have.
        return description + (completed ? " (Completed)" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        // Consider tasks equal if description and completion status are the same
        return completed == task.completed &&
               Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, completed);
    }
} 
    

