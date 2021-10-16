import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Boards.Square;
import Boards.Errors.WrongBoardSizeException;
import Handlers.BoardHandler;
import Handlers.LetterValueHandler;
import Handlers.WordHandler;

public class TestBoardHandler {
    private BoardHandler boardHandler;
    private WordHandler wordHandler;

    @Before
    public void init() throws WrongBoardSizeException {
        this.boardHandler = new BoardHandler("", "", 3, 3);
        this.wordHandler = WordHandler.getInstance();
        Square[][] board = new Square[3][3];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Square();
            } 
        }
    }
}
