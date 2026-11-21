package artist;

import main.Main;

public class Artist {

    private static final byte INVALID = -1;

    public static final byte QUESTION_DATA_BIT = 5;
    private static final byte QUESTION_DATA_MASK = 0x1F;

    private String name;
    private long questionData; // All data is in one variable to save memory

    private Main main;

    public Artist(String name, long questionData, Main main) {
        this.name = name;
        this.questionData = questionData;
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public long getQuestionData() {
        return questionData;
    }

    public byte getAnswer(int question) {
        if (question < 0 || question >= main.getQuestionManager().getQuestionCount()) return INVALID;

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

}
