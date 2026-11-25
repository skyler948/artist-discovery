package manager;

import artist.Artist;
import input.Validation;
import main.Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MenuManager {

    private static final byte MAX_MAIN_MENU_CHOICE = 3;
    private static final byte RANKING = 6;

    private static final byte
            HIGH = 1,
            AVERAGE = 2,
            LOW = 3;

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
                --- Choose an Option ---
                1 - Discover your artist!
                2 - See artist rankings by score
                3 - Exit""");
    }

    private void displayRankingMenu() {
        System.out.println("""
                --- Choose a method to rank ---
                1 - Highest score
                2 - Average score
                3 - Lowest score""");
    }

    private void discoverArtist() {
        System.out.println();
        main.getQuestionManager().queryQuestions(main.getScanner());

        float score = main.getScoreManager().calculateUserScore(main.getQuestionManager().getAnswers());
        Artist artist = main.getScoreManager().getNearestArtistFromScore(score);

        System.out.println("\n--- Your Artist ---");
        artist.displayInformation();
        System.out.printf("\nYour score: %.2f\nDifference: %.2f\n\n",
                score, (artist.getScore() - score));
    }

    private byte rankArtistLoop() {
        byte choice;

        do {
            displayRankingMenu();

            choice = Validation.validateInput(main.getScanner().nextLine().trim(), (byte) 3);

            if (choice == Validation.INVALID) System.out.println("Invalid input! Try again.\n");
        } while (choice == Validation.INVALID);

        return choice;
    }

    private void rankArtist() {
        System.out.println();
        byte question = rankArtistLoop();

        ArrayList<Artist> artists = new ArrayList<>(main.getDatabaseManager().getArtists());

        boolean swapped;
        for (int i = 0; i < artists.size(); i++) {
            swapped = false;

            for (int j = 0; j < artists.size() - i - 1; j++) {
                if (artists.get(j).getScore() <= artists.get(j + 1).getScore()) {
                    Artist temp = artists.get(j);
                    artists.set(j, artists.get(j + 1));
                    artists.set(j + 1, temp);
                    swapped = true;
                }
            }

            if (!swapped) break;
        }

        Queue<Artist> artistsRanked = getQueueOfArtists(question, artists);
        if (artistsRanked == null) return;

        System.out.printf("\n--- Top %s %s Results ---\n", RANKING, getRankingMode(question));
        while (!artistsRanked.isEmpty()) {
            Artist artist = artistsRanked.poll();

            System.out.printf("%s - %.2f\n",
                    artist.getName(), artist.getScore());
        }
        System.out.println();
    }

    private Queue<Artist> getQueueOfArtists(byte mode, ArrayList<Artist> source) {
        int middle = source.size() / 2;
        int deviation = RANKING / 2;

        return switch (mode) {
            case HIGH -> new LinkedList<>(source.subList(0, RANKING));
            case AVERAGE -> new LinkedList<>(source.subList(middle - deviation, middle + deviation));
            case LOW -> new LinkedList<>(source.subList(source.size() - RANKING, source.size()));

            default -> null;
        };
    }

    private String getRankingMode(byte mode) {
        return switch (mode) {
            case HIGH -> "Highest";
            case AVERAGE -> "Average";
            case LOW -> "Lowest";

            default -> null;
        };
    }

    private void exit() {
        System.out.println("\n(in mario voice) buh-bye!");
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

}
