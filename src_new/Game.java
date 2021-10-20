import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import GameTypes.GameType;
import GameTypes.ScrabbleSquares;
import GameTypes.StandardWordSquares;
import Handlers.BoardHandler;
import Players.Bot;
import Players.OnlinePlayer;
import Players.Player;
import View.GameView;
import Boards.Errors.LetterIncorrectException;
import Boards.Errors.PlaceAlreadyTakenException;
import Boards.Errors.PlaceStringIncorrectException;
import Boards.Errors.WrongBoardSizeException;
import Controller.GameController;
import Boards.Board;

public class Game {
    private GameType gameType;
    private BoardHandler boardHandler;
    private GameController gameController;
    private GameView gameView;
    private Player[] players;
    private Player thisPlayer;
    private String path;

    private static class Settings {
        public static String language = "English";
        public static String ipAddress = "127.0.0.1";
        public static int rowSize = 1;
        public static int colSize = 1;
        public static int numOfPlayers = 2;
        public static int numOfBots = 0;
        public static int portNumber = 2048;
    }

    public void run() throws ClassNotFoundException, IOException {
        int turn = 0;
        int index = this.gameType.getRandomStartPlayer(this.players.length);
        while (turn < (Settings.rowSize * Settings.colSize)) {
            index = index % this.players.length;
            if (this.thisPlayer != null) {
                this.gameView.print(this.thisPlayer.getBoard().toString(
                    this.gameType.showPoints(),
                    this.gameType.showMultiplyPoints()));
            }
            char letter;
            if (this.players[index] instanceof OnlinePlayer) {
                letter = ((OnlinePlayer) this.players[index]).pickLetter();
            } else {
                letter = this.players[index].pickLetter(gameController);
            }
            this.placeLetterForAllPlayers(letter);
            turn++;
            index++;
        }
        if (this.thisPlayer != null) {
            this.gameView.print(this.thisPlayer.getBoard().toString(
                this.gameType.showPoints(),
                this.gameType.showMultiplyPoints()));
        }
        this.declareWinner();
        this.closeConnections();
        this.mainMenu();
    }

    private void closeConnections() {
        for (Player player : this.players) {
            if (player instanceof OnlinePlayer) {
                player.closeConnection();
            }
        }
    }

    private void declareWinner() {
        int maxScore = 0;
        String winningMessage = "";
        for (Player player : this.players) {
            int playerScore = gameType.getPoints(this.boardHandler.findAllWords(
                player.getBoard().getBoard()));
            player.setPoints(playerScore);
            winningMessage += player.getName() + " : " + playerScore + "\n";
            if (maxScore < playerScore) {
                maxScore = playerScore;
            }
        }
        ArrayList<Player> winners = new ArrayList<Player>();
        for (Player player : this.players) {
            if (player.getPoints() == maxScore) {
                winners.add(player);
            }
        }
        winningMessage += this.gameView.getWinnerText(winners, maxScore);
        for (Player player : this.players) {
            if (player instanceof Player && player == this.thisPlayer) {
                this.gameView.print(winningMessage);
            } else if (player instanceof OnlinePlayer) {
                try {
                    ((OnlinePlayer) player).sendWinnerMessage(winningMessage);
                } catch (IOException | ClassNotFoundException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void placeLetterForAllPlayers(char letter) throws IOException {
        for (Player player : this.players) {
            if (player instanceof Player && player == this.thisPlayer) {
                player.placeLetter(letter, gameController);
            } else if (player instanceof Bot) {
                ((Bot) player).placeLetter(letter, gameController);
            } else if (player instanceof OnlinePlayer) {
                try {
                    ((OnlinePlayer) player).placeLetter(letter);
                } catch (ClassNotFoundException | IndexOutOfBoundsException | PlaceStringIncorrectException
                        | PlaceAlreadyTakenException | LetterIncorrectException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void runTest() {

    }

    private void initGame() throws ClassNotFoundException, IOException {
        try {
            this.boardHandler = new BoardHandler(this.path, this.path, Settings.colSize, Settings.rowSize);
        } catch (WrongBoardSizeException e) {
            // TODO Auto-generated catch block
            return;
        }

        this.players = new Player[Settings.numOfPlayers + Settings.numOfBots];
        if (Settings.numOfPlayers > 0) {
            try {
                this.players[0] = new Player(this.gameType.initBoard(
                    this.path,
                    Settings.rowSize, Settings.colSize), "Player 1");
            } catch (WrongBoardSizeException e) {
                e.printStackTrace();
                return;
            }
            this.thisPlayer = this.players[0];
        }

        for (int i = 1; i < Settings.numOfPlayers; i++) {
            try {
                this.players[i] = new OnlinePlayer(this.gameType.initBoard(
                    this.path,
                    Settings.rowSize, Settings.colSize), "Online player " + i);                
            } catch (WrongBoardSizeException e) {
                e.printStackTrace();
                return;
            }
            try {
                this.gameView.print("Listening on port: " + (Settings.portNumber));
                ((OnlinePlayer) this.players[i]).connectToClient(Settings.portNumber);
                ((OnlinePlayer) this.players[i]).sendBoard();
                ((OnlinePlayer) this.players[i]).sendName();
                ((OnlinePlayer) this.players[i]).sendMessage(this.gameType);
                this.gameView.print("Connected on port: " + Settings.portNumber + " with " + this.players[i].getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int j = Settings.numOfPlayers; j < this.players.length; j++) {
            try {
                this.players[j] = new Bot(this.gameType.initBoard(
                    this.path,
                    Settings.rowSize, Settings.colSize), "Bot " + j);
            } catch (WrongBoardSizeException e) {
                e.printStackTrace();
                return;
            }
        }
        this.run();
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        new Game();
    }

    public Game() throws ClassNotFoundException, IOException {
        this.loadLanguage();
        this.mainMenu();
    }

    private void loadLanguage() {
        this.path = System.getProperty("user.dir") + "\\data\\" + Settings.language + "\\";
        this.gameController = new GameController(
            this.path + "menu\\",
            this.path + "menu\\");
        this.gameView = GameView.getInstance();
    }

    private void mainMenu() throws ClassNotFoundException, IOException {
        this.gameView.printMainText(Settings.rowSize, Settings.colSize, Settings.language, Settings.numOfPlayers, Settings.numOfBots, Settings.portNumber, Settings.ipAddress);
        String input = this.gameController.getInput();
        switch (input) {
            case "1":
                // play standard
                this.gameType = new StandardWordSquares(GameType.boardStandard);
                this.initGame();
                break;
            case "2":
                // play scrabble
                this.gameType = new ScrabbleSquares(GameType.boardStandard);
                this.initGame();
                break;
            case "3":
                // play pre-defined Scrabble 5x5
                Settings.rowSize = 5;
                Settings.colSize = 5;
                this.gameType = new ScrabbleSquares(GameType.boardPreDefined);
                this.initGame();
                break;
            case "4":
                // play random Scrabble 5x5
                Settings.rowSize = 5;
                Settings.colSize = 5;
                this.gameType = new ScrabbleSquares(GameType.boardRandom);
                this.initGame();            
                break;
            case "5":
                // TODO: Connect to server
                this.connectToServer();
                break;
            case "6":
                this.settingsMenu();
                break;
            case "!":
                break;
            default:
                this.mainMenu();
                break;
        }

    }

    private void settingsMenu() throws ClassNotFoundException, IOException {
        this.gameView.printSettingsText(Settings.rowSize, Settings.colSize, Settings.language, Settings.numOfPlayers, Settings.numOfBots, Settings.portNumber, Settings.ipAddress);
        String input = this.gameController.getInput();
        switch (input) {
            case "1":
                this.gameView.printSettingsOption(1);
                String inputOption = this.gameController.getInput();
                String[] options = inputOption.split("/");
                int row, col;
                try {
                    row = Integer.parseInt(options[0]);
                    col = Integer.parseInt(options[1]);
                    if (row < 1 || col < 1 || row > 9 || col > 9) {
                        this.gameView.printBoardSizeErrorText();
                    }
                    Settings.rowSize = row;
                    Settings.colSize = col;
                    this.mainMenu();
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    this.settingsMenu();
                }
                break;
            case "2":
                this.gameView.printSettingsOption(2);
                this.setLanguage();
                this.mainMenu();
                break;
            case "3":
                this.gameView.printSettingsOption(3);
                inputOption = this.gameController.getInput();
                try {
                    int numOfPlayers = Integer.parseInt(inputOption);
                    Settings.numOfPlayers = numOfPlayers;
                    this.mainMenu();
                } catch(NumberFormatException e) {
                    this.settingsMenu();
                }
                break;
            case "4":
                this.gameView.printSettingsOption(4);
                inputOption = this.gameController.getInput();
                try {
                    int numOfBots = Integer.parseInt(inputOption);
                    Settings.numOfBots = numOfBots;
                    this.mainMenu();
                } catch(NumberFormatException e) {
                    this.settingsMenu();
                }
                break;
            case "5":
                this.gameView.printSettingsOption(5);
                int port = this.gameController.getInputInt();
                if (port < 0 || port > 65536) {
                    this.gameView.print("Port needs to be between 0 and 65536");
                } else {
                    Settings.portNumber = port;
                }
                this.mainMenu();
                break;
            case "6":
                this.gameView.printSettingsOption(6);
                Settings.ipAddress = this.gameController.getInput();
                this.mainMenu();
                break;
            case "7":
                this.mainMenu();
                break;
            default:
                this.settingsMenu();
                break;
        }

    }

    private void setLanguage() {
        File folder = new File(System.getProperty("user.dir") + "\\data\\");
        File[] listOfFiles = folder.listFiles();

        ArrayList<String> foldersList = new ArrayList<String>();
        String folders = "";
        boolean first = true;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isDirectory()) {
                foldersList.add(listOfFiles[i].getName());
                if (first) {
                    folders += listOfFiles[i].getName();
                    first = false;
                } else {
                    folders += ", " + listOfFiles[i].getName();
                }
            }
        }
        this.gameView.print(folders + "\n exit with !");
        while (true) {
            String input = this.gameController.getInput();
            if (input.equals("!")) {
                return;
            }
            for (int j = 0; j < foldersList.size(); j++) {
                if (input.equals(foldersList.get(j))) {
                    Settings.language = foldersList.get(j);
                    this.loadLanguage();
                    return;
                }
            }
        }
    }

    private void connectToServer() throws ClassNotFoundException, IOException {
        this.players = new Player[1];
        try {
            this.players[0] = new Player(null, "");
        } catch (WrongBoardSizeException e) {
            e.printStackTrace();
        }
        this.thisPlayer = this.players[0];
        this.gameView.print("Connecting to server with port: " + Settings.portNumber);
        try {
            this.thisPlayer.connectToServer(Settings.ipAddress, Settings.portNumber);
        } catch (IOException e) {
            this.gameView.printErr("Could not connect to server, check port and ip address");
            this.settingsMenu();
            return;
        }
        
        this.thisPlayer.getBoardMessage();
        this.thisPlayer.getNameMessage();
        this.gameView.print("Connected as " + this.thisPlayer.getName());
        this.gameType = (GameType) this.thisPlayer.getNextMessage();
        this.gameView.print(this.thisPlayer.getBoard().toString(this.gameType.showPoints(), this.gameType.showMultiplyPoints()));
        while (true) {
            String message = (String) this.thisPlayer.getNextMessage();
            if (message.contains(Player.pickLetterOnline)) {
                // pick letter
                char letter = this.thisPlayer.pickLetter(this.gameController);
                this.thisPlayer.sendMessage(letter);
            } else if (message.contains(Player.placeLetterOnline)) {
                char letter = message.split(":")[1].replaceAll(" ", "").charAt(0);
                String place = this.thisPlayer.placeLetter(letter, this.gameController);
                this.thisPlayer.sendMessage(place);
                this.gameView.print(this.thisPlayer.getBoard().toString(this.gameType.showPoints(), this.gameType.showMultiplyPoints()));
            } else if (message.contains(Player.winnerMessageOnline)) {
                String winMsg = message.replace(Player.winnerMessageOnline, "");
                winMsg = winMsg.replace(this.thisPlayer.getName(), "You");
                winMsg = winMsg.replaceAll("Player", "Online player");
                this.gameView.print(winMsg);
                break;
            }
        }
        this.thisPlayer.closeConnection();
        this.mainMenu();
    }

}
