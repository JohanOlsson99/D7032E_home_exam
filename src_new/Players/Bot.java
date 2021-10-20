package Players;

import Boards.Board;
import Boards.Errors.LetterIncorrectException;
import Boards.Errors.PlaceAlreadyTakenException;
import Boards.Errors.PlaceStringIncorrectException;
import Boards.Errors.WrongBoardSizeException;
import Controller.GameController;
import java.util.Random;

public class Bot extends Player {

    private Random random;

    public Bot(Board board, String name) throws WrongBoardSizeException {
        super(board, name);
        random = new Random();
    }

    @Override
    public char pickLetter(GameController gameController) {
        return (char) ('a' + this.random.nextInt(26));
    }

    @Override
    protected String pickPlace(char letter, GameController gameController) {
        int row = this.random.nextInt(this.board.getRowSize());
        int col = this.random.nextInt(this.board.getColSize());
        return Character.toString((char) ('a' + row)) + col;
    }

    /*@Override
    public char pickAndPlace(GameController gameController) {
        char letter = this.pickLetter(null);
        while (true) {
            String place = this.pickPlace('\0', null);
            try {
                this.placeLetterOnBoard(letter, place);
                break;
            } catch (IndexOutOfBoundsException | PlaceStringIncorrectException | PlaceAlreadyTakenException
                    | LetterIncorrectException e) {
                continue;
            }
        }
        return letter;
    }*/

    @Override
    public String placeLetter(char letter, GameController gameController) {
        String place = "";
        while (true) {
            place = this.pickPlace('\0', null);
            try {
                this.placeLetterOnBoard(letter, place);
                break;
            } catch (IndexOutOfBoundsException | PlaceStringIncorrectException | PlaceAlreadyTakenException
                    | LetterIncorrectException e) {
                continue;
            }
        }
        return place;
    }

}
