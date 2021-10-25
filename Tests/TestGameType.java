import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Boards.Board;
import Boards.Square;
import Boards.Exceptions.WrongBoardSizeException;
import GameTypes.GameType;
import GameTypes.ScrabbleSquares;
import GameTypes.StandardWordSquares;
import Handlers.LetterValueHandler;


public class TestGameType {

    @Test
    public void testStandardWordSquares() {
        StandardWordSquares standardSquare = new StandardWordSquares(GameType.BOARD_STANDARD);
        ArrayList<Square[]> words = new ArrayList<Square[]>();
        // test a three length word = 1 point
        words.add(new Square[] { new Square('A', 1), new Square('A', 1), new Square('A', 1)});
        assertEquals(1, standardSquare.getPoints(words));
        words = new ArrayList<Square[]>();
        // test a two length word = 0 points
        words.add(new Square[] { new Square('A', 1), new Square('A', 1)});
        assertEquals(0, standardSquare.getPoints(words));
        words = new ArrayList<Square[]>();
        // test a one length word = 0 points
        words.add(new Square[] { new Square('A', 1)});
        assertEquals(0, standardSquare.getPoints(words));
        words = new ArrayList<Square[]>();
        // test a 4 length word, (4 - 3) * 2 = 2 points
        words.add(new Square[] { new Square('A', 2), new Square('A', 2), new Square('A', 2), new Square('A', 2)});
        assertEquals(2, standardSquare.getPoints(words));
        // test the same word, same points
        words.add(new Square[] { new Square('A', 2), new Square('A', 2), new Square('A', 2), new Square('A', 2)});
        assertEquals(2, standardSquare.getPoints(words));
    }
    
    @Test
    public void testScrabbleWordSquares() throws FileNotFoundException {
        LetterValueHandler.getInstance().readFromFile(new TestBoardHandler().path);

        ScrabbleSquares scrabbleSquare = new ScrabbleSquares(GameType.BOARD_STANDARD);
        ArrayList<Square[]> words = new ArrayList<Square[]>();
        // 8 points
        words.add(new Square[] { new Square('A', Square.RL), new Square('A', Square.RL), new Square('C', Square.RL) });
        assertEquals(8, scrabbleSquare.getPoints(words));
        words = new ArrayList<Square[]>();
        // 5 points
        words.add(new Square[] { new Square('A', Square.RL), new Square('B', Square.RL) });
        assertEquals(5, scrabbleSquare.getPoints(words));
        words = new ArrayList<Square[]>();
        // 2 points
        words.add(new Square[] { new Square('A', Square.RL) });
        assertEquals(2, scrabbleSquare.getPoints(words));
        words = new ArrayList<Square[]>();
        // 4 * 3 points (triple word)
        words.add(new Square[] { new Square('A', Square.TW), new Square('A', Square.RL) });
        assertEquals(12, scrabbleSquare.getPoints(words));
        words = new ArrayList<Square[]>();
        // 4 * 2 points (double word)
        words.add(new Square[] { new Square('A', Square.DW), new Square('A', Square.RL) });
        assertEquals(8, scrabbleSquare.getPoints(words));
        words = new ArrayList<Square[]>();
        // 2 * 3 + 2 points (triple letter)
        words.add(new Square[] { new Square('A', Square.TL), new Square('A', Square.RL) });
        assertEquals(8, scrabbleSquare.getPoints(words));
        words = new ArrayList<Square[]>();
        // 2 + 2 * 2 points (double letter)
        words.add(new Square[] { new Square('A', Square.RL), new Square('A', Square.DL) });
        assertEquals(6, scrabbleSquare.getPoints(words));
    }

    @Test
    public void testGameBoardPreDefined() throws WrongBoardSizeException, FileNotFoundException {
        LetterValueHandler.getInstance().readFromFile(new TestBoardHandler().path);

        ScrabbleSquares scrabbleSquare = new ScrabbleSquares(GameType.BOARD_PRE_DEFINED);
        Board board = scrabbleSquare.initBoard(new TestBoardHandler().path, 3, 3);
        Square[][] squares = board.getGameBoard();
        assertEquals(Square.TW, squares[0][0].getSquareType());
        assertEquals(Square.DW, squares[0][1].getSquareType());
        assertEquals(Square.TL, squares[0][2].getSquareType());
        assertEquals(Square.DW, squares[1][0].getSquareType());
        assertEquals(Square.TL, squares[1][1].getSquareType());
        assertEquals(Square.DL, squares[1][2].getSquareType());
        assertEquals(Square.TL, squares[2][0].getSquareType());
        assertEquals(Square.DL, squares[2][1].getSquareType());
        assertEquals(Square.RL, squares[2][2].getSquareType());   
    }
   
    @Test
    public void testGameBoard() throws WrongBoardSizeException, FileNotFoundException {
        LetterValueHandler.getInstance().readFromFile(new TestBoardHandler().path);

        ScrabbleSquares scrabbleSquare = new ScrabbleSquares(GameType.BOARD_STANDARD);
        Board board = scrabbleSquare.initBoard(new TestBoardHandler().path, 3, 3);
        Square[][] squares = board.getGameBoard();
        assertEquals(Square.RL, squares[0][0].getSquareType());
        assertEquals(Square.DL, squares[0][1].getSquareType());
        assertEquals(Square.TL, squares[0][2].getSquareType());
        assertEquals(Square.DL, squares[1][0].getSquareType());
        assertEquals(Square.TL, squares[1][1].getSquareType());
        assertEquals(Square.DW, squares[1][2].getSquareType());
        assertEquals(Square.TL, squares[2][0].getSquareType());
        assertEquals(Square.DW, squares[2][1].getSquareType());
        assertEquals(Square.TW, squares[2][2].getSquareType());   
    }

    @Test
    public void testDifferentStartIndex() {
        GameType gameType = new GameType(GameType.BOARD_STANDARD);
        int startPlayer = gameType.getRandomStartPlayer(10);
        boolean otherStartIndex = false;
        for (int i = 0; i < 100000; i++) {
            if (gameType.getRandomStartPlayer(10) != startPlayer) {
                otherStartIndex = true;
                break;
            }
        }
        assertTrue(otherStartIndex);
    }
    
}
