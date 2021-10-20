package Players;
import Boards.Board;
import Boards.Errors.LetterIncorrectException;
import Boards.Errors.PlaceAlreadyTakenException;
import Boards.Errors.PlaceStringIncorrectException;
import Boards.Errors.WrongBoardSizeException;
import Controller.GameController;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Player {

    public static final String pickLetterOnline = "Pick letter";
    public static final String placeLetterOnline = "Place letter: ";
    public static final String winnerMessageOnline = "Winner: ";

    protected Board board;
    private String name;
    private int points;

    protected Socket connection;
    protected ObjectInputStream inputStream;
    protected ObjectOutputStream outputStream;

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

    public String placeLetter(char letter, GameController gameController) {
        String place = this.pickPlace(letter, gameController);
        do {
            try {
                this.placeLetterOnBoard(letter, place);
                break;
            } catch (IndexOutOfBoundsException | PlaceStringIncorrectException | PlaceAlreadyTakenException e) {
                gameController.printErr(e.getMessage());
                place = this.pickPlace(letter, gameController);    
            } catch (LetterIncorrectException e) { }
        } while (true);
        return place;
    }

    protected void placeLetterOnBoard(char letter, String place) throws
            IndexOutOfBoundsException,
            PlaceStringIncorrectException,
            PlaceAlreadyTakenException,
            LetterIncorrectException {
        this.board.updateBoard(letter, place);
    }

    public char pickLetter(GameController gameController) {
        char letter;
        do {
            letter = Character.toUpperCase(gameController.pickLetter());
        } while (letter < 'A' || letter > 'Z');
        return letter;
    }

    protected String pickPlace(char letter, GameController gameController) {
        return gameController.pickPlace(letter);
    }

    public Board getBoard() {
        return this.board;
    }

    public void getBoardMessage() throws ClassNotFoundException, IOException {
        this.board = (Board) this.getNextMessage();
    }
    
    public void getNameMessage() throws ClassNotFoundException, IOException {
        this.name = (String) this.getNextMessage();
    }

    public void connectToServer(String ipAdress, int port) throws UnknownHostException, IOException {
        this.connection = new Socket(ipAdress, port);
        this.setInputOutputStream();
    }

    public void closeConnection() {
        try {
            this.connection.close();
        } catch (IOException e) {
            // already closed if you get here
        }
    }

    protected void setInputOutputStream() throws IOException {
        this.inputStream = new ObjectInputStream(this.connection.getInputStream());
        this.outputStream = new ObjectOutputStream(this.connection.getOutputStream());
    }

    public Object getNextMessage() throws ClassNotFoundException, IOException {
        return this.inputStream.readObject();
    }

    public void sendMessage(Object message) throws IOException {
        this.outputStream.writeObject(message);
    }
    
}
