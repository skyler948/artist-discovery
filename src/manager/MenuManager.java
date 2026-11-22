package manager;

import artist.Artist;
import input.Validation;
import main.Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MenuManager {

    private static final byte MAX_MAIN_MENU_CHOICE = 3;
    private static final byte RANKING = 25;

    private Main main;

    private boolean running = true;

    public MenuManager(Main main) {
        this.main = main;
    }

    public void mainMenuLoop() {
        displayMainMenu();
        byte choice = Validation.validateInput(main.getScanner().nextLine().trim(), MAX_MAIN_MENU_CHOICE);
        switch (choice) {
            case -1:
                System.out.println("Invalid input! Try again.\n");
                break;
            case 1:
                discoverArtist();
                break;
            case 2:
                rankArtist();
                break;
            case 3:
                exit();
                break;
        }
    }

    private void displayMainMenu() { // TODO: What other options can we have here?
        System.out.println("""
                --- Choose an option ---
                1 - Discover your artist!
                2 - See artist rankings
                3 - Exit""");
    }

    private void displayRankingMenu() {
        System.out.println("\n--- Choose a question to rank ---");
        for (int i = 1; i < main.getQuestionManager().getQuestionCount(); i++) {
            System.out.printf("%d - %s\n",
                    (i + 1), main.getQuestionManager().getQuestions().get(i));
        }
    }

    private void discoverArtist() {
        main.getQuestionManager().queryQuestions(main.getScanner());

        float score = main.getScoreManager().calculateUserScore(main.getQuestionManager().getAnswers());
        Artist artist = main.getScoreManager().getNearestArtistFromScore(score);

        // TODO: Add more information to artist database file to display upon user input
        System.out.printf("\n--- Your artist ---\n%s\nYour score: %.2f\nArtist score: %.2f\n\n",
                artist, score, artist.getScore());
    }

    private byte rankArtistLoop() {
        byte choice;

        do {
            displayRankingMenu();

            choice = Validation.validateInput(main.getScanner().nextLine().trim(), (byte) main.getQuestionManager().getQuestionCount());

            if (choice == Validation.INVALID || choice == 1) System.out.println("Invalid input! Try again.\n");
        } while (choice == Validation.INVALID || choice == 1);

        return choice;
    }

    private void rankArtist() { // TODO: Right now all top <x> results are the same, bias dataset to normal curve maybe?
        byte question = (byte) (rankArtistLoop() - 1);

        ArrayList<Artist> artists = new ArrayList<>(main.getDatabaseManager().getArtists());

        boolean swapped;
        for (int i = 0; i < artists.size(); i++) {
            swapped = false;

            for (int j = 0; j < artists.size() - i - 1; j++) {
                if (artists.get(j).getAnswer(question) <= artists.get(j + 1).getAnswer(question)) {
                    Artist temp = artists.get(j);
                    artists.set(j, artists.get(j + 1));
                    artists.set(j + 1, temp);
                    swapped = true;
                }
            }

            if (!swapped) break;
        }

        Queue<Artist> topArtists = new LinkedList<>(artists.subList(0, RANKING));

        System.out.printf("\n--- Top %s results ---\n", RANKING);
        while (!topArtists.isEmpty()) {
            String name = topArtists.peek().getName();
            byte answer = topArtists.peek().getAnswer(question);

            System.out.printf("%s - (%d/%d)\n",
                    name, answer, main.getQuestionManager().getQuestionMaximums().get(question));
            topArtists.poll();
        }
        System.out.println();
    }

    private void exit() {
        System.out.println("\n(in mario voice) buh-bye!");
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

}
