package Players;
import Boards.Board;
import Boards.Errors.LetterIncorrectException;
import Boards.Errors.PlaceAlreadyTakenException;
import Boards.Errors.PlaceStringIncorrectException;
import Boards.Errors.WrongBoardSizeException;
import Controller.GameController;
import View.GameView;

public class Player {

    protected Board board;
    private String name;
    private int points;

    public Player(Board board, String name) throws WrongBoardSizeException {
        this.board = board;
        this.name = name;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return this.points;
    }

    public String getName() {
        return this.name;
    }

    public void placeLetter(char letter, GameController gameController) {
        GameView gameView = GameView.getInstance();
        String place = this.pickPlace(letter, gameController);
        do {
            try {
                this.placeLetterOnBoard(letter, place);
                break;
            } catch (IndexOutOfBoundsException | PlaceStringIncorrectException | PlaceAlreadyTakenException e) {
                gameView.print(e.getMessage());
                place = this.pickPlace(letter, gameController);    
            } catch (LetterIncorrectException e) {
                return; // should not get here!
            }
        } while (true);
    }

    protected void placeLetterOnBoard(char letter, String place) throws
        IndexOutOfBoundsException,
        PlaceStringIncorrectException,
        PlaceAlreadyTakenException,
        LetterIncorrectException {
        this.board.updateBoard(letter, place);
    }

    public char pickAndPlace(GameController gameController) {
        GameView gameView = GameView.getInstance();
        char letter = this.pickLetter(gameController);
        String place = this.pickPlace(letter, gameController);
        do {
            try {
                this.placeLetterOnBoard(letter, place);
                break;
            } catch (IndexOutOfBoundsException | PlaceStringIncorrectException | PlaceAlreadyTakenException e) {
                gameView.print(e.getMessage());
                place = this.pickPlace(letter, gameController);    
            } catch (LetterIncorrectException e) {
                gameView.print(e.getMessage());
                letter = this.pickLetter(gameController);
                place = this.pickPlace(letter, gameController);
            }
        } while (true);
        return letter;
    }

    protected char pickLetter(GameController gameController) {
        return gameController.pickLetter();
    }

    protected String pickPlace(char letter, GameController gameController) {
        return gameController.pickPlace(letter);
    }

    public Board getBoard() {
        return this.board;
    }
    
}
