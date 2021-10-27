package Players;

import java.io.IOException;
import java.net.ServerSocket;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import Boards.Board;
import Boards.Exceptions.LetterIncorrectException;
import Boards.Exceptions.PlaceAlreadyTakenException;
import Boards.Exceptions.PlaceStringIncorrectException;
import Players.Exceptions.ClientConnectionFailedException;
import Players.Exceptions.PlayerDisconnectedException;

public class OnlinePlayer extends Player {

    /**
     * sets the disconnect message
     * 
     * @param board the board that this player should have
     * @param name  the name this player should have
     */
    public OnlinePlayer(Board board, String name) {
        super(board, name);
        this.disconnectMessage = "The " + this.getName() + " was disconnected, the game will end now";
    }

    /**
     * 
     * @param letter the letter to be placed by the online player
     * @throws PlayerDisconnectedException if something happens with the connection
     */
    public void placeLetter(char letter) throws PlayerDisconnectedException {
        this.sendMessage(Player.PLACE_LETTER_ONLINE + Character.toString(letter));
        String place = (String) this.getNextMessage();
        try {
            super.placeLetterOnBoard(letter, place);
        } catch (IndexOutOfBoundsException | PlaceStringIncorrectException | PlaceAlreadyTakenException
                | LetterIncorrectException e) {
            // if this happens, something is broken, exits
            this.closeConnection();
            throw new PlayerDisconnectedException(this.disconnectMessage);
        }
    }

    /**
     * 
     * @param port which port to listen for clients
     * @throws ClientConnectionFailedException if something happens with the
     *                                         connection
     * @throws PlayerDisconnectedException     if something happens with the input
     *                                         output connection
     */
    public void connectToClient(int port) throws ClientConnectionFailedException, PlayerDisconnectedException {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            this.connection = serverSocket.accept();
            serverSocket.close();
        } catch (IOException e) {
            throw new ClientConnectionFailedException("Client connection failed, try again");
        }
        this.setOutputInputStream();
    }

    /**
     * sends the board to the client
     * 
     * @throws PlayerDisconnectedException if something happens with the connection
     */
    public void sendBoard() throws PlayerDisconnectedException {
        this.sendMessage(this.board);
    }

    /**
     * send the player name
     * 
     * @throws PlayerDisconnectedException if something happens with the connection
     */
    public void sendName() throws PlayerDisconnectedException {
        this.sendMessage(this.getName());
    }

    /**
     * sends the winner message to the client
     * 
     * @param message message to send
     * @throws PlayerDisconnectedException if something happens with the connection
     */
    public void sendWinnerMessage(String message) throws PlayerDisconnectedException {
        this.sendMessage(Player.WINNER_MESSAGE_ONLINE + message);
    }

    /**
     * tells the client to pick a letter
     * 
     * @return the letter the client picked
     * @throws PlayerDisconnectedException if something happens with the connection
     */
    public char pickLetter() throws PlayerDisconnectedException {
        this.sendMessage(Player.PICK_LETTER_ONLINE);
        return (char) this.getNextMessage();
    }

    private void setOutputInputStream() throws PlayerDisconnectedException {
        try {
            this.outputStream = new ObjectOutputStream(this.connection.getOutputStream());
            this.inputStream = new ObjectInputStream(this.connection.getInputStream());
        } catch (IOException e) {
            this.closeConnection();
            throw new PlayerDisconnectedException(this.disconnectMessage);
        }
    }
}
