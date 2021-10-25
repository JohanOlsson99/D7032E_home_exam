package Players;

import Boards.Board;
import Boards.Exceptions.LetterIncorrectException;
import Boards.Exceptions.PlaceAlreadyTakenException;
import Boards.Exceptions.PlaceStringIncorrectException;
import Controller.GameController;
import Players.Exceptions.PlayerDisconnectedException;
import Players.Exceptions.ServerConnectionFailedException;

import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Player {

    public static final String PICK_LETTER_ONLINE = "Pick letter";
    public static final String PLACE_LETTER_ONLINE = "Place letter: ";
    public static final String WINNER_MESSAGE_ONLINE = "Winner: ";

    protected Board board;
    private String name;
    private int points;

    protected String disconnectMessage;
    protected Socket connection;
    protected ObjectInputStream inputStream;
    protected ObjectOutputStream outputStream;

    /**
     * sets the disconnect message
     * 
     * @param board the board that this player should have
     * @param name  the name this player should have
     */
    public Player(Board board, String name) {
        this.board = board;
        this.name = name;
        this.disconnectMessage = "You have been disconnected from the server, the game will end now";
    }

    /**
     * sets this player's points
     * 
     * @param points this players points
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * get this player's points
     * 
     * @return this players points
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * get this player's name
     * 
     * @return this player's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * place a letter for this player
     * 
     * @param letter         the letter to be placed
     * @param gameController a gameController
     * @return the place this player choose
     */
    public String placeLetter(char letter, GameController gameController) {
        String place;
        do {
            place = this.pickPlace(letter, gameController);
            try {
                this.placeLetterOnBoard(letter, place);
                break;
            } catch (IndexOutOfBoundsException | PlaceStringIncorrectException | PlaceAlreadyTakenException e) {
                gameController.printErr(e.getMessage());
            } catch (LetterIncorrectException e) {
                // should not get here, another player have already checked the letter
            }
        } while (true);
        return place;
    }

    protected void placeLetterOnBoard(char letter, String place) throws IndexOutOfBoundsException,
            PlaceStringIncorrectException, PlaceAlreadyTakenException, LetterIncorrectException {
        this.board.updateGameBoard(letter, place);
    }

    /**
     * get the player to pick a letter
     * 
     * @param gameController a gameController
     * @return the letter the player choose
     */
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

    /**
     * 
     * @return this players board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * listens for the board from the server
     * 
     * @throws PlayerDisconnectedException if something happens with the connection
     */
    public void getBoardMessage() throws PlayerDisconnectedException {
        try {
            this.board = (Board) this.getNextMessage();
        } catch (ClassCastException e) {
            this.closeConnection();
            throw new PlayerDisconnectedException(this.disconnectMessage);
        }
    }

    /**
     * listens for the name from the server
     * 
     * @throws PlayerDisconnectedException if something happens with the connection
     */
    public void getNameMessage() throws PlayerDisconnectedException {
        try {
            this.name = (String) this.getNextMessage();
        } catch (ClassCastException e) {
            this.closeConnection();
            throw new PlayerDisconnectedException(this.disconnectMessage);
        }
    }

    /**
     * tries to connect to the server
     * 
     * @param ipAdress the ip address which the server is at
     * @param port     the port the server listens at
     * @throws ServerConnectionFailedException if connection to the server was not
     *                                         possible
     * @throws PlayerDisconnectedException     if something happens with the
     *                                         connection
     */
    public void connectToServer(String ipAdress, int port)
            throws ServerConnectionFailedException, PlayerDisconnectedException {
        try {
            this.connection = new Socket(ipAdress, port);
            this.setInputOutputStream();
        } catch (IOException e) {
            throw new ServerConnectionFailedException("Server connection failed, try again");
        }
    }

    /**
     * closes the connection
     */
    public void closeConnection() {
        try {
            this.connection.close();
        } catch (IOException e) {
            // already closed if you get here
        }
    }

    private void setInputOutputStream() throws PlayerDisconnectedException {
        try {
            this.inputStream = new ObjectInputStream(this.connection.getInputStream());
            this.outputStream = new ObjectOutputStream(this.connection.getOutputStream());
        } catch (IOException e) {
            this.closeConnection();
            throw new PlayerDisconnectedException(this.disconnectMessage);
        }
    }

    /**
     * listens for a message and then returns it
     * 
     * @return the object which was sent to this player
     * @throws PlayerDisconnectedException if something happens with the connection
     */
    public Object getNextMessage() throws PlayerDisconnectedException {
        try {
            return this.inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            this.closeConnection();
            throw new PlayerDisconnectedException(this.disconnectMessage);
        }
    }

    /**
     * sends a message
     * 
     * @param message the message to send
     * @throws PlayerDisconnectedException if something happens with the connection
     */
    public void sendMessage(Object message) throws PlayerDisconnectedException {
        try {
            this.outputStream.writeObject(message);
        } catch (IOException e) {
            this.closeConnection();
            throw new PlayerDisconnectedException(this.disconnectMessage);
        }
    }

}
