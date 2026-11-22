package input;

import manager.QuestionManager;

public class Validation {

    public static final byte INVALID = -1;

    public static final byte YES = 1;
    public static final byte NO = 0;

    private static final byte
            MONDAY = 0,
            TUESDAY = 1,
            WEDNESDAY = 2,
            THURSDAY = 3,
            FRIDAY = 4,
            SATURDAY = 5,
            SUNDAY = 6;

    public static byte validateInput(String input, byte maxValue) {
        byte value;

        try {
            value = Byte.parseByte(input);
        } catch (NumberFormatException e) {
            return INVALID;
        }

        if (value <= 0 || value > maxValue) return INVALID;

        return value;
    }

    public static byte validateInput(String input, byte maxValue, byte question) {
        byte value;

        try {
            value = Byte.parseByte(input);
        } catch (NumberFormatException e) {
            if (question == QuestionManager.DAY_QUESTION) {
                return convertDays(input);
            }

            if (input.equals("n") && (maxValue == 1)) {
                return NO;
            } else if (input.equals("y") && (maxValue == 1)) {
                return YES;
            }

            return INVALID;
        }

        if (value < 0 || value > maxValue) return INVALID;

        return value;
    }

    private static byte convertDays(String day) {
        return switch (day) {
            case "monday",
                 "mon",
                 "m" -> MONDAY;
            case "tuesday",
                 "tues", "tue",
                 "tu" -> TUESDAY;
            case "wednesday",
                 "wed",
                 "w" -> WEDNESDAY;
            case "thursday",
                 "thurs", "thu",
                 "th" -> THURSDAY;
            case "friday",
                 "fri",
                 "f" -> FRIDAY;
            case "saturday",
                 "sat",
                 "sa" -> SATURDAY;
            case "sunday",
                 "sun",
                 "su" -> SUNDAY;
            default -> Validation.INVALID;
        };
    }

}
