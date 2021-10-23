package Model;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import GameTypes.GameType;
import GameTypes.ScrabbleSquares;
import GameTypes.StandardWordSquares;
import Handlers.BoardHandler;
import Model.Errors.NumberOfBotsWrongException;
import Model.Errors.NumberOfPlayerWrongException;
import Model.Errors.PortNumberWrongException;
import Players.Bot;
import Players.OnlinePlayer;
import Players.Player;
import Players.Errors.ClientConnectionFailedException;
import Players.Errors.PlayerDisconnectedException;
import Players.Errors.ServerConnectionFailedException;
import View.GameView;
import Boards.Errors.WrongBoardSizeException;
import Controller.GameController;

public class Game {
    private GameType gameType;
    private BoardHandler boardHandler;
    private GameController gameController;
    private GameView gameView;
    private Player[] players;
    private Player thisPlayer;
    private String path;

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        new Game().startGame();
    }

    public Game() {
        
    }

    public void startGame() {
        this.loadLanguage();
        this.mainMenu();
    }

    /**
     * Init the game with new players and new boards for each player
     * @throws NumberOfPlayersOrBotNotCorrectException if number of players of bot is too low
     */
    private void initGame() {
        this.boardHandler = new BoardHandler(this.path, this.path);

        this.players = new Player[Settings.getNumOfPlayers() + Settings.getNumOfBots()];
        if (Settings.getNumOfPlayers() > 0) {
            try {
                this.players[0] = new Player(this.gameType.initBoard(
                    this.path,
                    Settings.getRowSize(), Settings.getColSize()), "Player 1");
            } catch (WrongBoardSizeException e) {
                handelError(e.getMessage());
                return;
            }
            this.thisPlayer = this.players[0];
        }

        for (int i = 1; i < Settings.getNumOfPlayers(); i++) {
            try {
                this.players[i] = new OnlinePlayer(this.gameType.initBoard(
                    this.path,
                    Settings.getRowSize(), Settings.getColSize()), "Online player " + i);                
            } catch (WrongBoardSizeException e) {
                handelError(e.getMessage());
                return;
            }
            try {
                this.gameView.print("Listening on port: " + (Settings.getPortNumber()));
                ((OnlinePlayer) this.players[i]).connectToClient(Settings.getPortNumber());
                ((OnlinePlayer) this.players[i]).sendBoard();
                ((OnlinePlayer) this.players[i]).sendName();
                ((OnlinePlayer) this.players[i]).sendMessage(this.gameType);
                this.gameView.print("Connected on port: " + Settings.getPortNumber() + " with " + this.players[i].getName());
            } catch (PlayerDisconnectedException | ClientConnectionFailedException e) {
                handelError(e.getMessage());
                return;
            }
        }
        for (int j = Settings.getNumOfPlayers(); j < this.players.length; j++) {
            try {
                this.players[j] = new Bot(this.gameType.initBoard(
                    this.path,
                    Settings.getRowSize(), Settings.getColSize()), "Bot " + j);
            } catch (WrongBoardSizeException e) {
                handelError(e.getMessage());
                return;
            }
        }
        this.run();
    }

    private void run() {
        int turn = 0;
        int index = this.gameType.getRandomStartPlayer(this.players.length);
        while (turn < (Settings.getRowSize() * Settings.getColSize())) {
            index = index % this.players.length;
            if (this.thisPlayer != null) {
                this.gameView.print(this.thisPlayer.getBoard().toString(
                    this.gameType.showPoints(),
                    this.gameType.showMultiplyPoints()));
            }
            char letter = '\0';
            if (this.players[index] instanceof OnlinePlayer) {
                try {
                    letter = ((OnlinePlayer) this.players[index]).pickLetter();
                } catch (PlayerDisconnectedException e) {
                    handelError(e.getMessage());
                    return;
                }
            } else if (this.players[index] instanceof Bot) {
                letter = ((Bot) this.players[index]).pickLetter();
            } else if (this.players[index] instanceof Player) {
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

    private void runOnline() throws PlayerDisconnectedException {
        while (true) {
            String message;
            message = (String) this.thisPlayer.getNextMessage();
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

    private void placeLetterForAllPlayers(char letter) {
        ExecutorService threadpool = Executors.newFixedThreadPool(this.players.length);  
        for (Player player : this.players) {
            if (player instanceof Player && player == this.thisPlayer) {
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        player.placeLetter(letter, gameController);
                    }
                };
                threadpool.execute(task);
            } else if (player instanceof Bot) {
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        ((Bot) player).placeLetter(letter, gameController);
                    }
                };
                threadpool.execute(task);
            } else if (player instanceof OnlinePlayer) {
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ((OnlinePlayer) player).placeLetter(letter);
                        } catch (PlayerDisconnectedException e) {
                            threadpool.shutdownNow();
                            return;
                        }
                    }
                };
                threadpool.execute(task);
            }
        }
        
        threadpool.shutdown();
        //wait for all the answers to come in
        while(!threadpool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }

    private void declareWinner() {
        int maxScore = 0;
        String winningMessage = "";
        for (Player player : this.players) {
            int playerScore = gameType.getPoints(this.boardHandler.findAllWords(
                player.getBoard().getGameBoard()));
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
                } catch (PlayerDisconnectedException e) {
                    handelError(e.getMessage());
                    return;
                }
            }
        }
    }

    private void closeConnections() {
        for (Player player : this.players) {
            if (player instanceof OnlinePlayer) {
                player.closeConnection();
            }
        }
    }

    private void loadLanguage() {
        System.out.println(System.getProperty("user.dir"));
        this.path = System.getProperty("user.dir") + "\\data\\" + Settings.getLanguage() + "\\";
        this.gameController = new GameController(
            this.path + "menu\\",
            this.path + "menu\\");
        this.gameView = GameView.getInstance();
    }

    private void mainMenu() {
        this.gameView.printMainText(Settings.getRowSize(), Settings.getColSize(), Settings.getLanguage(), Settings.getNumOfPlayers(), Settings.getNumOfBots(), Settings.getPortNumber(), Settings.getIpAddress());
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
                try {
                    Settings.setRowSize(5);
                    Settings.setColSize(5);
                } catch (WrongBoardSizeException e) {
                    // Nothing wrong with these values
                }
                this.gameType = new ScrabbleSquares(GameType.boardPreDefined);
                this.initGame();
                break;
            case "4":
                // play random Scrabble 5x5
                try {
                    Settings.setRowSize(5);
                    Settings.setColSize(5);
                } catch (WrongBoardSizeException e) {
                    // Nothing wrong with these values
                }
                this.gameType = new ScrabbleSquares(GameType.boardRandom);
                this.initGame();          
                break;
            case "5":
                this.connectToServer();
            try {
                this.runOnline();
            } catch (PlayerDisconnectedException e) {
                this.handelError(e.getMessage());
                return;
            }
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

    private void settingsMenu() {
        this.gameView.printSettingsText(Settings.getRowSize(), Settings.getColSize(), Settings.getLanguage(), Settings.getNumOfPlayers(), Settings.getNumOfBots(), Settings.getPortNumber(), Settings.getIpAddress());
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
                    try {
                        Settings.setRowSize(row);
                        Settings.setColSize(col);
                    } catch (WrongBoardSizeException e) {
                        this.gameView.printBoardSizeErrorText();
                    }
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
                    Settings.setNumOfPlayers(numOfPlayers);
                    this.mainMenu();
                } catch (NumberFormatException e) {
                    this.settingsMenu();
                } catch (NumberOfPlayerWrongException e) {
                    this.gameView.printErr(e.getMessage());
                    this.mainMenu();
                }
                break;
            case "4":
                this.gameView.printSettingsOption(4);
                inputOption = this.gameController.getInput();
                try {
                    int numOfBots = Integer.parseInt(inputOption);
                    Settings.setNumOfBots(numOfBots);
                    this.mainMenu();
                } catch (NumberFormatException e) {
                    this.settingsMenu();
                } catch (NumberOfBotsWrongException e) {
                    this.gameView.printErr(e.getMessage());
                    this.mainMenu();
                }
                break;
            case "5":
                this.gameView.printSettingsOption(5);
                int port = this.gameController.getInputInt();
                try {
                    Settings.setPortNumber(port);
                } catch (PortNumberWrongException e) {
                    this.gameView.printErr(e.getMessage());
                    this.settingsMenu();
                }
                this.mainMenu();
                break;
            case "6":
                this.gameView.printSettingsOption(6);
                Settings.setIpAddress(this.gameController.getInput());
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
                    Settings.setLanguage(foldersList.get(j));
                    this.loadLanguage();
                    return;
                }
            }
        }
    }

    private void connectToServer() {
        this.players = new Player[] { new Player(null, "") };
        this.thisPlayer = this.players[0];
        this.gameView.print("Connecting to server with port: " + Settings.getPortNumber());
        try {
            this.thisPlayer.connectToServer(Settings.getIpAddress(), Settings.getPortNumber());
            this.thisPlayer.getBoardMessage();
            this.thisPlayer.getNameMessage();
            this.gameType = (GameType) this.thisPlayer.getNextMessage();
        } catch (PlayerDisconnectedException | ServerConnectionFailedException e) {
            handelError(e.getMessage());
            return;
        }
        this.gameView.print("Connected as " + this.thisPlayer.getName());
        this.gameView.print(this.thisPlayer.getBoard().toString(this.gameType.showPoints(), this.gameType.showMultiplyPoints()));
    }

    private void handelError(String e) {
        this.gameView.printErr(e);
        this.mainMenu();
    }

}
