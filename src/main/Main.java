package main;

import manager.DatabaseManager;
import manager.MenuManager;
import manager.QuestionManager;
import manager.ScoreManager;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    private Scanner scanner;

    private QuestionManager questionManager;
    private DatabaseManager databaseManager;
    private MenuManager menuManager;
    private ScoreManager scoreManager;

    public Main() {
        scanner = new Scanner(System.in);

        try {
            questionManager = new QuestionManager("text/questions.txt");
            databaseManager = new DatabaseManager("text/database.txt", this);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        menuManager = new MenuManager(this);
        scoreManager = new ScoreManager(this);
        
        while (menuManager.isRunning()) {
            menuManager.mainMenuLoop();
        }

        scanner.close();
    }

    public QuestionManager getQuestionManager() {
        return questionManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public static void main(String[] args) {
        new Main();
    }

}
