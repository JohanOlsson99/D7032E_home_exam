package Players;
import Boards.Board;
import Boards.Errors.WrongBoardSizeException;

public class Player {

    Board board;

    public Player(Board board) throws WrongBoardSizeException {
        this.board = board;
    }

    public void placeLetter(char letter) {

    }

    public char pickLetter() {
        return 'a';
    }

    public Board getBoard() {
        return this.board;
    }
    
}
