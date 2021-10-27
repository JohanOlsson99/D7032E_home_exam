import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Boards.Board;
import Boards.Square;
import Boards.Exceptions.LetterIncorrectException;
import Boards.Exceptions.PlaceAlreadyTakenException;
import Boards.Exceptions.PlaceStringIncorrectException;
import Boards.Exceptions.WrongBoardSizeException;
import GameTypes.ScrabbleSquares;
import GameTypes.StandardWordSquares;
import Handlers.BoardHandler;

public class TestBoardHandler {
    private BoardHandler boardHandler;
    public static String path = "C:/Users/Johol/Documents/skolan/D7032E_software_engineering/D7032E_home_exam/test/data/";
    // public static String path = "C:/Users/Johol/Documents/skolan/D7032E_software_engineering/D7032E_home_exam/test/data/";

    /**
     * Add you path here an uncomment the path string, add "test/data/", important with that last slash (/)
     */
    // public static String path = "[PATH TO ROOT FOLDER HERE]/test/data/";

    @Before
    public void init() throws FileNotFoundException {
        this.boardHandler = new BoardHandler(TestBoardHandler.path, TestBoardHandler.path);
    }

    public Board getBoard() throws WrongBoardSizeException {
        return new Board(3, 3);
    }

    public void setBoard(Board board) throws IndexOutOfBoundsException, PlaceStringIncorrectException,
            PlaceAlreadyTakenException, LetterIncorrectException {
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

    public void setBoard2(Board board) throws IndexOutOfBoundsException, PlaceStringIncorrectException,
            PlaceAlreadyTakenException, LetterIncorrectException {
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

    public void setBoard3(Board board) throws IndexOutOfBoundsException, PlaceStringIncorrectException,
            PlaceAlreadyTakenException, LetterIncorrectException {
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
    public void testFindWords() throws IndexOutOfBoundsException, PlaceStringIncorrectException,
            PlaceAlreadyTakenException, LetterIncorrectException, WrongBoardSizeException {
        /*
         * A B C
         * A B C 
         * A B C 
         * words: ABC: 4, AB: 5, BC: 5
         */
        Board board = this.getBoard();
        this.setBoard(board);
        ArrayList<Square[]> words = this.boardHandler.findAllWords(board.getGameBoard());
        assertEquals(14, words.size());
    }

    @Test
    public void testFindWords2() throws IndexOutOfBoundsException, PlaceStringIncorrectException,
            PlaceAlreadyTakenException, LetterIncorrectException, WrongBoardSizeException {
        /*
         * A C A 
         * A C B 
         * A C C 
         * words: ABC: 1, AB: 1, BC: 1
         */
        Board board = this.getBoard();
        this.setBoard2(board);
        ArrayList<Square[]> words = this.boardHandler.findAllWords(board.getGameBoard());
        assertEquals(3, words.size());
    }

    @Test
    public void testFindWords3() throws IndexOutOfBoundsException, PlaceStringIncorrectException,
            PlaceAlreadyTakenException, LetterIncorrectException, WrongBoardSizeException {
        /*
         * C B A 
         * C B A 
         * C B A 
         * words: 0
         */
        Board board = this.getBoard();
        this.setBoard3(board);
        ArrayList<Square[]> words = this.boardHandler.findAllWords(board.getGameBoard());
        assertEquals(0, words.size());
    }

    @Test
    public void testCalculateScore() throws IndexOutOfBoundsException, PlaceStringIncorrectException, PlaceAlreadyTakenException, LetterIncorrectException, WrongBoardSizeException {
        Board board = this.getBoard();
        this.setBoard2(board);
        ArrayList<Square[]> words = this.boardHandler.findAllWords(board.getGameBoard());
        StandardWordSquares type = new StandardWordSquares(0);
        assertEquals(1, type.getPoints(words));
    }
    
    @Test
    public void testCalculateScore2() throws IndexOutOfBoundsException, PlaceStringIncorrectException, PlaceAlreadyTakenException, LetterIncorrectException, WrongBoardSizeException {
        Board board = this.getBoard();
        this.setBoard2(board);
        ArrayList<Square[]> words = this.boardHandler.findAllWords(board.getGameBoard());
        ScrabbleSquares type = new ScrabbleSquares(0);
        assertEquals(21, type.getPoints(words));
    }
}
