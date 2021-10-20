package Players;

import java.io.IOException;
import java.net.ServerSocket;

import java.net.InetAddress;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import Boards.Board;
import Boards.Errors.LetterIncorrectException;
import Boards.Errors.PlaceAlreadyTakenException;
import Boards.Errors.PlaceStringIncorrectException;
import Boards.Errors.WrongBoardSizeException;

public class OnlinePlayer extends Player {

    public OnlinePlayer(Board board, String name) throws WrongBoardSizeException {
        super(board, name);
    }

    public void placeLetter(char letter) throws IOException, ClassNotFoundException, IndexOutOfBoundsException, PlaceStringIncorrectException, PlaceAlreadyTakenException, LetterIncorrectException {
        this.sendMessage(Player.placeLetterOnline + Character.toString(letter));
        String place = (String) this.getNextMessage();
        super.placeLetterOnBoard(letter, place);
    }

    public void connectToClient(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        this.connection = serverSocket.accept();
        this.setInputOutputStream();
        
        serverSocket.close();
    }

    public void sendBoard() throws IOException {
        this.sendMessage(this.board);
    }
    
    public void sendName() throws IOException {
        this.sendMessage(this.getName());
    }

    public void sendWinnerMessage(String message) throws IOException, ClassNotFoundException {
        this.sendMessage(Player.winnerMessageOnline + message);
    }

    @Override
    protected void setInputOutputStream() throws IOException {
        this.outputStream = new ObjectOutputStream(this.connection.getOutputStream());
        this.inputStream = new ObjectInputStream(this.connection.getInputStream());
    }

    public char pickLetter() throws ClassNotFoundException, IOException {
        this.sendMessage(Player.pickLetterOnline);
        return (char) this.getNextMessage();
    }
    
}
