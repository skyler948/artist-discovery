package main;

import manager.QuestionManager;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private Scanner scanner;

    private QuestionManager questionManager;

    public Main() {
        scanner = new Scanner(System.in);

        try {
            questionManager = new QuestionManager("text/questions.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        questionManager.queryQuestions(scanner);
        System.out.println(Arrays.toString(questionManager.getAnswers()));
    }

    public static void main(String[] args) {
        new Main();
    }

}
