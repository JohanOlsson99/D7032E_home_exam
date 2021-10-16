package GameTypes;
import Boards.Square;
import Handlers.LetterValueHandler;

import java.util.ArrayList;
import java.util.Random;

public class GameType {
    public static void main(String[] args) {
        GameType gameType = new GameType();
        
    }
    
    private boolean showPoints;
    private Random random = new Random();

    public GameType() {
        this.showPoints = false;
    }

    public int getRandomStartPlayer(int length) {
        return this.random.nextInt(length);
    }

    public int getPoints(ArrayList<Square[]> words) {
        return this.calculatePoints(words);
    }

    protected int calculatePoints(ArrayList<Square[]> words) {
        LetterValueHandler letterValueHandler = LetterValueHandler.getInstance();
        int totalPoints = 0;
        for (int i = 0; i < words.size(); i++) {
            Square[] word = words.get(i);
            int wordPoints = 0;
            int wordMultiplier = 1;
            for (int j = 0; j < word.length; j++) {
                int letterPoints = letterValueHandler.getValue(word[j].getLetter());
                letterPoints *= this.multiplyLetterPoints(word[j]);
                switch (word[j].getSquareType()) {
                    case Square.DW:
                        wordMultiplier *= 2;
                        break;
                    case Square.TW:
                        wordMultiplier *= 3;
                        break;
                }
                wordPoints += letterPoints;
            }
            wordPoints = this.wordPointsToGive(wordPoints, word.length);
            totalPoints += this.multiplyWordPoints(wordPoints, wordMultiplier);
            // totalPoints += wordPoints * wordMultiplier;
        }
        return totalPoints;
    }

    protected int wordPointsToGive(int currentWordPoints, int wordLength) {
        return currentWordPoints;
    }

    protected int multiplyWordPoints(int points, int multiply) {
        return points * multiply;
    }

    protected int multiplyLetterPoints(Square letter) {
        int multiply = 1;
        switch (letter.getSquareType()) {
            case Square.DL:
                multiply *= 2;
                break;
            case Square.TL:
                multiply *= 3;
                break;
            default:
                multiply = 1;
                break;
        }
        return multiply;
    }

    public boolean showPoints() {
        return this.showPoints;
    }
}
