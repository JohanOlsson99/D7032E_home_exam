package Players;

import Boards.Board;
import Boards.Exceptions.LetterIncorrectException;
import Boards.Exceptions.PlaceAlreadyTakenException;
import Boards.Exceptions.PlaceStringIncorrectException;
import Controller.GameController;
import java.util.Random;

public class Bot extends Player {

    private Random random;

    /**
     * initialize a random variable
     * 
     * @param board the board that this player should have
     * @param name  the name this player should have
     */
    public Bot(Board board, String name) {
        super(board, name);
        random = new Random();
    }

    /**
     * 
     * @return the letter picked by the bot
     */
    public char pickLetter() {
        return (char) ('a' + this.random.nextInt(26));
    }

    protected String pickPlace(char letter) {
        int row = this.random.nextInt(this.board.getRowSize());
        int col = this.random.nextInt(this.board.getColSize());
        return Character.toString((char) ('a' + row)) + col;
    }

    @Override
    public String placeLetter(char letter, GameController gameController) {
        String place = "";
        while (true) {
            place = this.pickPlace('\0');
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
