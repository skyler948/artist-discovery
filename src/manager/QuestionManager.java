package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class QuestionManager {

    private static final byte INVALID = -1;
    private static final byte DAY_QUESTION = 0;
    private static final byte NO = 0;
    private static final byte YES = 1;

    private File questionFile;

    private ArrayList<String> questions;
    private ArrayList<Byte> questionMaximums;

    private byte[] answers;

    public QuestionManager(String questionPath) throws FileNotFoundException {
        questionFile = new File(questionPath);
        questions = new ArrayList<>();
        questionMaximums = new ArrayList<>();

        if (!questionFile.exists() || !questionFile.isFile()) {
            throw new FileNotFoundException("Path does not point to a file.");
        }

        int line = 0;
        Scanner scanner = new Scanner(questionFile);
        while (scanner.hasNextLine()) {
            line++;
            if (line % 2 != 0) {
                questions.add(scanner.nextLine());
            } else {
                questionMaximums.add(Byte.parseByte(scanner.nextLine()));
            }
        }
        scanner.close();

        answers = new byte[questions.size()];
    }

    public void queryQuestions(Scanner scanner) {
        System.out.println("Give an answer to each question.");

        for (int i = 0; i < answers.length; i++) {
            System.out.println(questions.get(i));
            answers[i] = validationLoop(scanner, i);
        }
    }

    private byte validationLoop(Scanner scanner, int question) {
        byte answer;
        boolean invalid;

        do {
            String input = scanner.nextLine().toLowerCase(Locale.ROOT).trim();

            answer = validateQuestion(question, input, questionMaximums.get(question));

            invalid = (answer < 0 || answer > questionMaximums.get(question));

            if (invalid) {
                System.out.println("Value is invalid! Try again.");
            }
        } while (invalid);
        
        return answer;
    }

    private byte validateQuestion(int question, String input, int max) {
        byte answer;

        try {
            answer = Byte.parseByte(input);
        } catch (NumberFormatException e) {
            if (question == DAY_QUESTION) {
                return convertDays(input);
            }

            if (input.equals("n") && (max == 1)) {
                return NO;
            } else if (input.equals("y") && (max == 1)) {
                return YES;
            }

            return INVALID;
        }

        return answer;
    }

    private byte convertDays(String day) {
        return switch (day) {
            case "monday",
                 "mon",
                 "m" -> 0;
            case "tuesday",
                 "tues", "tue",
                 "tu" -> 1;
            case "wednesday",
                 "wed",
                 "w" -> 2;
            case "thursday",
                 "thurs", "thu",
                 "th" -> 3;
            case "friday",
                 "fri",
                 "f" -> 4;
            case "saturday",
                 "sat",
                 "sa" -> 5;
            case "sunday",
                 "sun",
                 "su" -> 6;
            default -> INVALID;
        };
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public byte[] getAnswers() {
        return answers;
    }

}
