package artist;

import input.Validation;
import main.Main;
import manager.ScoreManager;

public class Artist {

    public static final byte QUESTION_DATA_BIT = 5;
    private static final byte QUESTION_DATA_MASK = 0x1F;

    private String name;
    private String genre;
    private long questionData;
    private short monthlyListeners;
    private float score;
    private boolean active;

    private Main main;

    public Artist(String name, String genre, long questionData, short monthlyListeners, boolean active, Main main) {
        this.name = name;
        this.genre = genre;
        this.questionData = questionData;
        this.monthlyListeners = monthlyListeners;
        this.active = active;
        this.main = main;
        this.score = calculateArtistScore();
    }

    public Artist(Main main, float score) {
        this.name = null;
        this.questionData = 0;
        this.main = main;
        this.score = score;
    }

    private float calculateArtistScore() {
        float score = 0;
        for (int i = 0; i < main.getQuestionManager().getQuestionCount(); i++) {
            score += ScoreManager.calculateAnswerScore(getAnswer(i), main.getQuestionManager().getQuestionWeights().get(i));
        }
        return score;
    }

    public String getName() {
        return name;
    }

    public long getQuestionData() {
        return questionData;
    }

    public byte getAnswer(int question) {
        if (question < 0 || question >= main.getQuestionManager().getQuestionCount()) return Validation.INVALID;

        int size = (question + 1) * QUESTION_DATA_BIT;
        long answer = (questionData >> size) & QUESTION_DATA_MASK;
        return (byte) answer;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name);
        builder.append(" | ");
        for (int i = 0; i < main.getQuestionManager().getQuestionCount(); i++) {
            builder.append(String.format("(q%s): ", i + 1));
            builder.append(getAnswer(i));
            builder.append("/");
            builder.append(main.getQuestionManager().getQuestionMaximums().get(i));
            builder.append((i != main.getQuestionManager().getQuestionCount() - 1) ? ", " : " ");
        }

        return builder.toString();
    }

    public void displayInformation() {
        System.out.printf("Artist name: %s\nPrimary genre: %s\nMonthly listeners: %d\nQuestion score: %.2f\nActive? %s\n",
                name, genre, monthlyListeners, score, (active ? "Yes" : "No"));
    }

    public float getScore() {
        return score;
    }

    public short getMonthlyListeners() {
        return monthlyListeners;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isActive() {
        return active;
    }

}
