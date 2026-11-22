package manager;

import artist.Artist;
import main.Main;
import tree.BinaryTree;

import java.util.ArrayList;

public class ScoreManager {

    private Main main;

    private BinaryTree scoreTree;

    public ScoreManager(Main main) {
        this.main = main;
        this.scoreTree = new BinaryTree();

        ArrayList<Artist> artists = new ArrayList<>(main.getDatabaseManager().getArtists());

        Artist averageArtist = new Artist(main, calculateAverageScore(artists)); // John Artist
        scoreTree.insert(averageArtist);

        insertArtists(artists);
    }

    private float calculateAverageScore(ArrayList<Artist> artists) {
        float total = 0;
        for (Artist artist : artists) {
            total += artist.getScore();
        }
        return total / artists.size();
    }

    private void insertArtists(ArrayList<Artist> artists) {
        for (Artist artist : artists) {
            scoreTree.insert(artist);
        }
    }

    public Artist getNearestArtistFromScore(float score) {
        return scoreTree.nearest(score);
    }

    public static float calculateAnswerScore(int answer, float weight) {
        return ((answer + 1) * 10) * weight;
    }

    public float calculateUserScore(byte[] answers) {
        float score = 0;
        for (int i = 0; i < answers.length; i++) {
            score += calculateAnswerScore(answers[i], main.getQuestionManager().getQuestionWeights().get(i));
        }
        return score;
    }

}
