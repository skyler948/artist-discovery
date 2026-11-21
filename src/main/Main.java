package main;

import manager.DatabaseManager;
import manager.QuestionManager;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private Scanner scanner;

    private QuestionManager questionManager;
    private DatabaseManager databaseManager;

    public Main() {
        scanner = new Scanner(System.in);

        try {
            questionManager = new QuestionManager("text/questions.txt");
            databaseManager = new DatabaseManager("text/database.txt", this);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println(databaseManager.getArtists().get(1));

        questionManager.queryQuestions(scanner);
        System.out.println(Arrays.toString(questionManager.getAnswers()));

        scanner.close();
    }

    public QuestionManager getQuestionManager() {
        return questionManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static void main(String[] args) {
        new Main();
    }

}
