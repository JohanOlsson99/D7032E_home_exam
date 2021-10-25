import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import Boards.Board;
import Handlers.LetterValueHandler;
import Boards.Square;
import Boards.Exceptions.LetterIncorrectException;
import Boards.Exceptions.PlaceAlreadyTakenException;
import Boards.Exceptions.PlaceStringIncorrectException;
import Boards.Exceptions.WrongBoardSizeException;


public class TestBoard {
    LetterValueHandler letterHandler;

    @Before
    public void init() throws FileNotFoundException {
        this.letterHandler = LetterValueHandler.getInstance();
        this.letterHandler.readFromFile(TestBoardHandler.path);
    }

    private void fillBoard3x3(Board board) throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException {
        board.updateGameBoard('a', "a0");
        board.updateGameBoard('b', "b0");
        board.updateGameBoard('c', "c0");
        board.updateGameBoard('d', "a1");
        board.updateGameBoard('e', "b1");
        board.updateGameBoard('f', "c1");
        board.updateGameBoard('g', "a2");
        board.updateGameBoard('h', "b2");
        board.updateGameBoard('i', "c2");
    }

    public void checkLetters(Board board) {
        Square[][] square = board.getGameBoard();
        assertEquals('A', square[0][0].getLetter());
        assertEquals('B', square[1][0].getLetter());
        assertEquals('C', square[2][0].getLetter());
        assertEquals('D', square[0][1].getLetter());
        assertEquals('E', square[1][1].getLetter());
        assertEquals('F', square[2][1].getLetter());
        assertEquals('G', square[0][2].getLetter());
        assertEquals('H', square[1][2].getLetter());
        assertEquals('I', square[2][2].getLetter());
    }

    @Test
    public void test_place_letter() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        this.checkLetters(board);
    }
    
    @Test(expected = PlaceAlreadyTakenException.class)
    public void test_place_on_already_filled() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateGameBoard('G', "b1");
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void test_place_outside_number() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateGameBoard('G', "b3");
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void test_place_outside_char() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateGameBoard('G', "d3");
    }

    @Test(expected = PlaceStringIncorrectException.class)
    public void test_place_string_wrong() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateGameBoard('G', "@3");
    }
    
    @Test(expected = PlaceStringIncorrectException.class)
    public void test_place_string_wrong_2() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateGameBoard('A', "a3g");
    }
    
    @Test(expected = PlaceStringIncorrectException.class)
    public void test_place_string_wrong_3() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateGameBoard('G', "a");
    }
    
    @Test(expected = LetterIncorrectException.class)
    public void test_place_wrong_letter() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateGameBoard('-', "a1");
    }
    
    @Test(expected = LetterIncorrectException.class)
    public void test_place_wrong_letter_2() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        Board board = new Board(3, 3);
        this.fillBoard3x3(board);
        board.updateGameBoard('[', "a1");
    }
    
    @Test
    public void test_place_correct_letter() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        Board board = new Board(3, 3);
        board.updateGameBoard('a', "a1");
    }
    
    @Test
    public void test_place_correct_letter_2() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        Board board = new Board(3, 3);
        board.updateGameBoard('z', "a1");
    }
    
    @Test(expected = WrongBoardSizeException.class)
    public void test_wrong_board_size() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        new Board(1, 0);
    }
    
    @Test(expected = WrongBoardSizeException.class)
    public void test_wrong_board_size_2() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        new Board(0, 1);
    }
    
    @Test(expected = WrongBoardSizeException.class)
    public void test_wrong_board_size_3() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        new Board(0, 0);
    }
    
    @Test
    public void test_correct_board_size() throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException,
            WrongBoardSizeException {
        new Board(1, 1);
    }
}
