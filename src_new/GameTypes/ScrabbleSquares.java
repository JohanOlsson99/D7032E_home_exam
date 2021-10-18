package GameTypes;
import Boards.Square;
import Handlers.LetterValueHandler;

import java.util.ArrayList;

public class ScrabbleSquares extends GameType {
    
    public ScrabbleSquares() {
        super();
        this.showPoints = true;
        this.showMultiplyPoints = true;
    }

    @Override
    protected int wordPointsToGive(int currentWordPoints, int wordLength) {
        return currentWordPoints;
    }

    @Override
    protected int multiplyWordPoints(int points, int multiply) {
        return points * multiply;
    }

    @Override
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
    
}
