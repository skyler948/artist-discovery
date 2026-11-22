package artist;

import input.Validation;
import main.Main;
import manager.ScoreManager;

public class Artist {

    public static final byte QUESTION_DATA_BIT = 5;
    private static final byte QUESTION_DATA_MASK = 0x1F;

    private String name;
    private long questionData; // All data is in one variable to save memory
    private float score;

    private Main main;

    public Artist(String name, long questionData, Main main) {
        this.name = name;
        this.questionData = questionData;
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

    public float getScore() {
        return score;
    }

}
