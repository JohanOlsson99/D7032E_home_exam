package GameTypes;
import Boards.Board;

public class ScrabbleSquares extends GameType {
    
    public ScrabbleSquares() {
        super();
    }

    @Override
    public int calculatePoints(Board board) {
        return 1;
    }
    
}
