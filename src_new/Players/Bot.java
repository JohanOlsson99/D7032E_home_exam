package Players;

import Boards.Board;
import Boards.Errors.WrongBoardSizeException;

public class Bot extends Player {

    public Bot(Board board) throws WrongBoardSizeException {
        super(board);
    }
    
    @Override
    public void placeLetter(char letter) {

    }

    @Override
    public char pickLetter() {
        return 'a';
    }
}
