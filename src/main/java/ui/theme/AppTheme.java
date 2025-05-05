package ui.theme;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.BorderFactory; // Add this import

public class AppTheme {

    // Colors
    public static final Color PRIMARY_LIGHT = new Color(56, 0, 135); // Light Blue
    public static final Color PRIMARY_DARK = new Color(41, 128, 185);  // Darker Blue
    public static final Color SECONDARY_LIGHT = new Color(70, 130, 180); // SteelBlue
    public static final Color SECONDARY_DARK = new Color(25, 25, 112);   // MidnightBlue
    public static final Color BACKGROUND_GRADIENT_START_LOGIN = PRIMARY_LIGHT;
    public static final Color BACKGROUND_GRADIENT_END_LOGIN = PRIMARY_DARK;
    public static final Color BACKGROUND_GRADIENT_START_DASHBOARD = SECONDARY_LIGHT;
    public static final Color BACKGROUND_GRADIENT_END_DASHBOARD = SECONDARY_DARK;

    public static final Color CARD_BACKGROUND = Color.WHITE;
    public static final Color HEADER_BACKGROUND = new Color(44, 62, 80); // Dark Blue-Gray
    public static final Color TEXT_LIGHT = Color.WHITE;
    public static final Color TEXT_DARK = new Color(44, 62, 80);
    public static final Color TEXT_COMPLETED_TASK = Color.GRAY; // Color for completed tasks text
    public static final Color TEXT_FIELD_BACKGROUND = new Color(240, 240, 240);
    public static final Color TEXT_FIELD_BORDER = Color.GRAY;
    public static final Color PLACEHOLDER_COLOR = Color.GRAY;


    public static final Color BUTTON_PRIMARY_BACKGROUND = new Color(46, 204, 113); // Green
    public static final Color BUTTON_PRIMARY_FOREGROUND = Color.WHITE;
    public static final Color BUTTON_PRIMARY_HOVER = new Color(39, 174, 96); // Darker Green

    public static final Color BUTTON_DANGER_BACKGROUND = new Color(231, 76, 60); // Red
    public static final Color BUTTON_DANGER_FOREGROUND = Color.WHITE;
    public static final Color BUTTON_DANGER_HOVER = new Color(192, 57, 43); // Darker Red

    public static final Color BUTTON_SECONDARY_BACKGROUND = new Color(127, 140, 141); // Gray
    public static final Color BUTTON_SECONDARY_FOREGROUND = Color.WHITE;
    public static final Color BUTTON_SECONDARY_HOVER = new Color(108, 122, 123); // Darker Gray


    // Fonts
    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 24);
    public static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);
    public static final Font FONT_INPUT = new Font("Arial", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);
    public static final Font FONT_LIST_ITEM = new Font("Arial", Font.PLAIN, 16);
    public static final Font FONT_HEADER = new Font("Arial", Font.BOLD, 24);

    // Padding & Borders
    public static final Border PADDING_DIALOG = new EmptyBorder(30, 30, 30, 30);
    public static final Border PADDING_PANEL = new EmptyBorder(20, 20, 20, 20);
    public static final Border PADDING_HEADER = new EmptyBorder(15, 20, 15, 20);
    public static final Border PADDING_BUTTON = new EmptyBorder(10, 25, 10, 25);
    public static final Border PADDING_INPUT_FIELD = new EmptyBorder(5, 10, 5, 10);
    public static final Border BORDER_INPUT_FIELD = BorderFactory.createLineBorder(TEXT_FIELD_BORDER, 1);
    public static final Border BORDER_COMPOUND_INPUT = BorderFactory.createCompoundBorder(
            BORDER_INPUT_FIELD, PADDING_INPUT_FIELD
    );

    // Insets for GridBagLayout
    public static final Insets GBC_INSETS = new Insets(10, 10, 10, 10);

    // Dimensions
    public static final Dimension LOGIN_FORM_SIZE = new Dimension(400, 300);
    public static final Dimension LOGIN_WINDOW_SIZE = new Dimension(800, 500);
    public static final Dimension DASHBOARD_INITIAL_SIZE = new Dimension(1000, 700); // Start large but not maximized

    // String Constants
    public static final String LOGIN_TITLE = "Student Login";
    public static final String DASHBOARD_TITLE = "Student Dashboard";
    public static final String ERROR_TITLE = "Error";
    public static final String LOGIN_ERROR_TITLE = "Login Error";
    public static final String EMAIL_PLACEHOLDER = "Enter your email";
    public static final String PASSWORD_PLACEHOLDER = "Enter your password";
    public static final String TASK_PLACEHOLDER = "Enter a new task";
    public static final String EDIT_TASK_PROMPT_TITLE = "Edit Task";
    public static final String EDIT_TASK_PROMPT_MESSAGE = "Enter the new description for the task:";

    // Add tooltips
    public static final String TOOLTIP_ADD_TASK = "Add the task entered in the text field";
    public static final String TOOLTIP_DELETE_TASK = "Delete the selected task";
    public static final String TOOLTIP_TOGGLE_TASK = "Mark selected task as complete or incomplete"; // Refined tooltip
    public static final String TOOLTIP_LOGOUT = "Save tasks and return to Login screen";
}


