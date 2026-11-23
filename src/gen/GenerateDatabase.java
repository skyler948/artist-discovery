package gen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class GenerateDatabase {

    private static final String NAME_PATH = "text/names.txt";
    private static final String OUTPUT_PATH = "text/database.txt";
    private static final char SEPARATOR = ',';
    private static final int SEED = 1403953;
    private static final int MONTHLY_LISTENER_MAX = 5000;
    private static final float ACTIVE_PERCENTAGE = 0.90f;
    private static final Random RANDOM = new Random(SEED);
    private static final int[] QUESTION_MAXIMUMS = new int[]{
            6,              // q1
            15,             // q2
            1,              // q3
            15,             // q4
            1,              // q5
            15,             // q6
            1,              // q7
            7,              // q8
            31,             // q9
            7,              // q10
            7,             // q11
    };
    private static final String[] GENRES = new String[]{
            "Country", "Rock", "Metal", "Blues", "Alternative",
            "Classical", "Comedy", "Dance", "Electronic",
            "Rap", "Hip-Hop", "Industrial", "Inspirational",
            "Jazz", "Pop", "Opera", "Reggae"
    };

    /*
    Small subprogram to create the database used for the main program
     */

    public static void main(String[] args) {
        LinkedList<String> names = new LinkedList<>();
        File nameFile = new File(NAME_PATH);

        try {
            Scanner scanner = new Scanner(nameFile);

            while (scanner.hasNextLine()) {
                names.add(scanner.nextLine());
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        File databaseFile = new File(OUTPUT_PATH);

        try {
            if (databaseFile.createNewFile()) {
                System.out.println("File created.");
            }

            FileWriter writer = new FileWriter(databaseFile, false);

            for (String name : names) {
                StringBuilder builder = new StringBuilder(name);
                builder.append(SEPARATOR);
                builder.append(GENRES[RANDOM.nextInt(0, GENRES.length)]);
                builder.append(SEPARATOR);
                builder.append(RANDOM.nextInt(0, MONTHLY_LISTENER_MAX));
                builder.append(SEPARATOR);
                builder.append(isStillActive());

                for (int i = 0; i < QUESTION_MAXIMUMS.length; i++) {
                    builder.append(SEPARATOR);
                    builder.append(getValue(i));
                }
                builder.append('\n');

                writer.append(builder.toString());
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getValue(int index) {
        return RANDOM.nextInt(0, QUESTION_MAXIMUMS[index] + 1);
    }

    private static boolean isStillActive() {
        float proportion = RANDOM.nextFloat(0, 1);
        return proportion <= ACTIVE_PERCENTAGE;
    }

    public static int[] getQuestionMaximums() {
        return QUESTION_MAXIMUMS;
    }

}
