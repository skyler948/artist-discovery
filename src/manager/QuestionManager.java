package manager;

import input.Validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class QuestionManager {

    public static final byte DAY_QUESTION = 0;

    private File questionFile;

    private ArrayList<String> questions;
    private ArrayList<Byte> questionMaximums;
    private ArrayList<Float> questionWeights;

    private byte[] answers;

    public QuestionManager(String questionPath) throws FileNotFoundException {
        questionFile = new File(questionPath);
        questions = new ArrayList<>();
        questionMaximums = new ArrayList<>();
        questionWeights = new ArrayList<>();

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
                String[] questionValues = scanner.nextLine().split(",");
                questionMaximums.add(Byte.parseByte(questionValues[0]));
                questionWeights.add(Float.parseFloat(questionValues[1]));
            }
        }
        scanner.close();

        answers = new byte[questions.size()];
    }

    public void queryQuestions(Scanner scanner) {
        System.out.println("Give an answer to each question.");

        for (byte i = 0; i < answers.length; i++) {
            System.out.println(questions.get(i));
            answers[i] = validationLoop(scanner, i);
        }
    }

    private byte validationLoop(Scanner scanner, byte question) {
        byte answer;

        do {
            String input = scanner.nextLine().toLowerCase(Locale.ROOT).trim();

            answer = Validation.validateInput(input, questionMaximums.get(question), question);

            if (answer == Validation.INVALID) {
                System.out.println("Value is invalid! Try again.");
            }
        } while (answer == Validation.INVALID);
        
        return answer;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public ArrayList<Byte> getQuestionMaximums() {
        return questionMaximums;
    }

    public ArrayList<Float> getQuestionWeights() {
        return questionWeights;
    }

    public byte[] getAnswers() {
        return answers;
    }

    public int getQuestionCount() {
        return questions.size();
    }

}
