import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Boards.Board;
import Boards.Errors.LetterIncorrectException;
import Boards.Errors.PlaceAlreadyTakenException;
import Boards.Errors.PlaceStringIncorrectException;
import Handlers.LetterValueHandler;


public class TestBoard {
    LetterValueHandler letterHandler;

    @Before
    public void init() {
        this.letterHandler = LetterValueHandler.getInstance();
        this.letterHandler.readFromFile(System.getProperty("user.dir") + "\\data\\letter.txt");
    }

    private void fillBoard3x3(Board board) throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException {
        board.updateBoard('a', "a0");
        board.updateBoard('b', "b0");
        board.updateBoard('c', "c0");
        board.updateBoard('d', "a1");
        board.updateBoard('e', "b1");
        board.updateBoard('f', "c1");
        board.updateBoard('g', "a2");
        board.updateBoard('h', "b2");
        board.updateBoard('i', "c2");
    }

    @Test
    public void test_place_letter() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
    }
    
    @Test(expected = PlaceAlreadyTakenException.class)
    public void test_place_on_already_filled() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateBoard('G', "b1");
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void test_place_outside_number() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateBoard('G', "b3");
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void test_place_outside_char() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateBoard('G', "d3");
    }

    @Test(expected = PlaceStringIncorrectException.class)
    public void test_place_string_wrong() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateBoard('G', "@3");
    }
    
    @Test(expected = PlaceStringIncorrectException.class)
    public void test_place_string_wrong_2() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateBoard('G', "a3g");
    }
    
    @Test(expected = PlaceStringIncorrectException.class)
    public void test_place_string_wrong_3() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateBoard('G', "a");
    }
    
    @Test(expected = LetterIncorrectException.class)
    public void test_place_wrong_letter() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateBoard('-', "a1");
    }
}
