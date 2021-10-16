package GameTypes;
import Boards.Square;
import java.util.ArrayList;

public class StandardWordSquares extends GameType {
    public StandardWordSquares() {
        super();
    }

    @Override
    public int getPoints(ArrayList<Square[]> words) {
        ArrayList<String> wordsString = new ArrayList<String>();
        ArrayList<Square[]> wordsToCalculate = new ArrayList<Square[]>();
        for (int i = 0; i < words.size(); i++) {
            String word = "";
            for (int j = 0; j < words.get(i).length; j++) {
                word += words.get(i)[j].getLetter();
            }
            if (!wordsString.contains(word)) {
                wordsString.add(word);
                wordsToCalculate.add(words.get(i));
            }
        }
        System.out.println(wordsString.toString());
        return this.calculatePoints(wordsToCalculate);
    }

    @Override
    protected int wordPointsToGive(int currentWordPoints, int wordLength) {
        if (wordLength < 3) {
            return 0;
        } else if (wordLength == 3) {
            return 1;
        }
        return (wordLength - 3) * 2;
    }

    @Override
    protected int multiplyWordPoints(int points, int multiply) {
        return points;
    }

    @Override
    protected int multiplyLetterPoints(Square letter) {
        return 1;
    }
    
    
}