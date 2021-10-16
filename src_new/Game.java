import Boards.Board;
import GameTypes.GameType;
import GameTypes.ScrabbleSquares;
import GameTypes.StandardWordSquares;
import Handlers.BoardHandler;
import Boards.Errors.LetterIncorrectException;
import Boards.Errors.PlaceAlreadyTakenException;
import Boards.Errors.PlaceStringIncorrectException;
import Boards.Errors.WrongBoardSizeException;

public class Game {
    private GameType gameType;
    private BoardHandler boardHandler;

    public Game(GameType gameType) throws WrongBoardSizeException {
        this.gameType = gameType;
        this.boardHandler = new BoardHandler("CollinsScrabbleWords2019.txt", "letter.txt", 3, 3);
    }

    public void run() {
        
    }

    public void runTest() {

    }

    public void test() {
        Board board = this.boardHandler.getBoard();
        try {
            board.updateBoard('Z', "a0");
            board.updateBoard('A', "a1");
            board.updateBoard('G', "a2");
            board.updateBoard('Z', "b0");
            board.updateBoard('A', "b1");
            board.updateBoard('G', "b2");
            board.updateBoard('Z', "c0");
            board.updateBoard('A', "c1");
            board.updateBoard('G', "c2");
        } catch (IndexOutOfBoundsException | PlaceStringIncorrectException | PlaceAlreadyTakenException
                | LetterIncorrectException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(board.toString(true, true));
        System.out.println("Total points: " + gameType.getPoints(this.boardHandler.findAllWords()));
    }

    public static void main(String[] args) throws WrongBoardSizeException {
        GameType gameType = new StandardWordSquares();
        GameType gameType2 = new GameType();
        Game game = new Game(gameType);
        game.test();
        game = new Game(gameType2);
        game.test();
    }

}
