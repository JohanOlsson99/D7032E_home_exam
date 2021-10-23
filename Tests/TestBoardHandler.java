import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Boards.Board;
import Boards.Square;
import Boards.Errors.LetterIncorrectException;
import Boards.Errors.PlaceAlreadyTakenException;
import Boards.Errors.PlaceStringIncorrectException;
import Boards.Errors.WrongBoardSizeException;
import Handlers.BoardHandler;
import Handlers.WordHandler;

public class TestBoardHandler {
    private BoardHandler boardHandler;
    private WordHandler wordHandler;
    public String path = "C:\\Users\\Johan\\Documents\\skolan\\D7032_programvaruteknik\\D7032E_home_exam\\Tests\\data\\";

    @Before
    public void init() {
        this.boardHandler = new BoardHandler(this.path, this.path);
        this.wordHandler = WordHandler.getInstance();
    }

    public Board getBoard() throws WrongBoardSizeException {
        return new Board(3, 3);
    }

    public void setBoard(Board board) throws IndexOutOfBoundsException, PlaceStringIncorrectException, PlaceAlreadyTakenException, LetterIncorrectException {
        board.updateGameBoard('A', "a0");
        board.updateGameBoard('B', "a1");
        board.updateGameBoard('C', "a2");
        board.updateGameBoard('A', "b0");
        board.updateGameBoard('B', "b1");
        board.updateGameBoard('C', "b2");
        board.updateGameBoard('A', "c0");
        board.updateGameBoard('B', "c1");
        board.updateGameBoard('C', "c2");
    }
    
    public void setBoard2(Board board) throws IndexOutOfBoundsException, PlaceStringIncorrectException, PlaceAlreadyTakenException, LetterIncorrectException {
        board.updateGameBoard('A', "a0");
        board.updateGameBoard('C', "a1");
        board.updateGameBoard('A', "a2");
        board.updateGameBoard('A', "b0");
        board.updateGameBoard('C', "b1");
        board.updateGameBoard('B', "b2");
        board.updateGameBoard('A', "c0");
        board.updateGameBoard('C', "c1");
        board.updateGameBoard('C', "c2");
    }
    
    public void setBoard3(Board board) throws IndexOutOfBoundsException, PlaceStringIncorrectException, PlaceAlreadyTakenException, LetterIncorrectException {
        board.updateGameBoard('C', "a0");
        board.updateGameBoard('B', "a1");
        board.updateGameBoard('A', "a2");
        board.updateGameBoard('C', "b0");
        board.updateGameBoard('B', "b1");
        board.updateGameBoard('A', "b2");
        board.updateGameBoard('C', "c0");
        board.updateGameBoard('B', "c1");
        board.updateGameBoard('A', "c2");
    }

    @Test
    public void testFindWords() throws IndexOutOfBoundsException, PlaceStringIncorrectException, PlaceAlreadyTakenException, LetterIncorrectException, WrongBoardSizeException {
        /*
        A B C
        A B C
        A B C
        words: ABC: 4, AB: 5, BC: 5
        */
        Board board = this.getBoard();
        this.setBoard(board);
        ArrayList<Square[]> words = this.boardHandler.findAllWords(board.getGameBoard());
        assertEquals(14, words.size());
    }

    @Test
    public void testFindWords2() throws IndexOutOfBoundsException, PlaceStringIncorrectException, PlaceAlreadyTakenException, LetterIncorrectException, WrongBoardSizeException {
        /*
        A C A
        A C B
        A C C
        words: ABC: 1, AB: 1, BC: 1
        */
        Board board = this.getBoard();
        this.setBoard2(board);
        ArrayList<Square[]> words = this.boardHandler.findAllWords(board.getGameBoard());
        assertEquals(3, words.size());
    }
    
    @Test
    public void testFindWords3() throws IndexOutOfBoundsException, PlaceStringIncorrectException, PlaceAlreadyTakenException, LetterIncorrectException, WrongBoardSizeException {
        /*
        C B A
        C B A
        C B A
        words: 0
        */
        Board board = this.getBoard();
        this.setBoard3(board);
        ArrayList<Square[]> words = this.boardHandler.findAllWords(board.getGameBoard());
        assertEquals(0, words.size());
    }
}
