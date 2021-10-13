package GameTypes;
import Boards.Board;

public class StandardWordSquares extends GameType {
    public StandardWordSquares() {
        super();
    }

    @Override
    public int calculatePoints(Board board) {
        return 1;
    }
    
}