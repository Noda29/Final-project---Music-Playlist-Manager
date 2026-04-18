package util;

public class InputValidator {

    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static boolean isValidDuration(int duration) {
        return duration > 0;
    }

    public static boolean isValidId(int id) {
        return id > 0;
    }
}
