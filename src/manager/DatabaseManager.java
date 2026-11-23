package manager;

import artist.Artist;
import main.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class DatabaseManager {

    private Main main;

    private File databaseFile;

    private LinkedList<Artist> artists;

    public DatabaseManager(String path, Main main) throws FileNotFoundException {
        this.main = main;
        databaseFile = new File(path);
        artists = new LinkedList<>();

        if (!databaseFile.exists() || !databaseFile.isFile()) {
            throw new FileNotFoundException("Path does not point to a file.");
        }

        Scanner scanner = new Scanner(databaseFile);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");

            String name = line[0];
            String genre = line[1];
            short monthlyListeners = Short.parseShort(line[2]);
            boolean active = Boolean.parseBoolean(line[3]);

            long data = 0;
            for (int i = main.getQuestionManager().getQuestionCount(); i >= 1; i--) {
                byte field = Byte.parseByte(line[i + 3]);
                data |= field;
                data = data << Artist.QUESTION_DATA_BIT;
            }

            artists.add(new Artist(name, genre, data, monthlyListeners, active, main));
        }
        scanner.close();
    }

    public LinkedList<Artist> getArtists() {
        return artists;
    }

}
