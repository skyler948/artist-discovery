package manager;

import artist.Artist;
import main.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class MenuManager {

    private static final byte INVALID = -1;
    private static final byte MAX_MAIN_MENU_CHOICE = 3;
    private static final byte RANKING = 25;

    private Main main;

    private boolean running = true;

    public MenuManager(Main main) {
        this.main = main;
    }

    public void mainMenuLoop() {
        displayMainMenu();
        byte choice = validateInput(main.getScanner().nextLine().trim(), MAX_MAIN_MENU_CHOICE);
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

    private void discoverArtist() { // TODO: Finish this!
        main.getQuestionManager().queryQuestions(main.getScanner());
    }

    private byte rankArtistLoop() {
        byte choice;

        do {
            displayRankingMenu();

            choice = validateInput(main.getScanner().nextLine().trim(), (byte) main.getQuestionManager().getQuestionCount());

            if (choice == INVALID || choice == 1) System.out.println("Invalid input! Try again.\n");
        } while (choice == INVALID || choice == 1);

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

    private byte validateInput(String input, byte maxValue) { // TODO: Move validation & associated constants to its own class
        byte value;

        try {
            value = Byte.parseByte(input);
        } catch (NumberFormatException e) {
            return INVALID;
        }

        if (value <= 0 || value > maxValue) return INVALID;

        return value;
    }

    private void exit() {
        System.out.println("\n(in mario voice) buh-bye!");
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

}
