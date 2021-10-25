package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Boards.Exceptions.WrongBoardSizeException;
import GameTypes.GameType;
import GameTypes.ScrabbleSquares;
import GameTypes.StandardWordSquares;
import Handlers.BoardHandler;
import Model.Exceptions.NumberOfBotsWrongException;
import Model.Exceptions.NumberOfPlayerWrongException;
import Model.Exceptions.PortNumberWrongException;
import Players.Bot;
import Players.OnlinePlayer;
import Players.Player;
import Players.Exceptions.ClientConnectionFailedException;
import Players.Exceptions.PlayerDisconnectedException;
import Players.Exceptions.ServerConnectionFailedException;
import View.GameView;
import Controller.GameController;

public class Game {
    private GameType gameType;
    private BoardHandler boardHandler;
    private GameController gameController;
    private GameView gameView;
    private String path;
    private GameRunner gameRunner;

    /**
     * Loads the pre defined language and start up the game with main menu
     */
    public void startGame() {
        this.loadLanguage();
        this.mainMenu();
    }

    /**
     * Init the game with new players and new boards for each player
     * 
     * @throws NumberOfPlayersOrBotNotCorrectException if number of players of bot
     *                                                 is too low
     */
    private void initGame() {
        try {
            this.boardHandler = new BoardHandler(this.path, this.path);
        } catch (FileNotFoundException e) {
            this.gameView.printErr(e.getMessage());
            this.mainMenu();
            return;
        }

        Player[] players = new Player[Settings.getNumOfPlayers() + Settings.getNumOfBots()];
        Player thisPlayer = null;
        if (Settings.getNumOfPlayers() > 0) {
            try {
                players[0] = new Player(
                        this.gameType.initBoard(this.path, Settings.getRowSize(), Settings.getColSize()), "Player 1");
            } catch (WrongBoardSizeException | FileNotFoundException e) {
                handelError(e.getMessage());
                return;
            }
            thisPlayer = players[0];
        }

        for (int i = 1; i < Settings.getNumOfPlayers(); i++) {
            try {
                players[i] = new OnlinePlayer(
                        this.gameType.initBoard(this.path, Settings.getRowSize(), Settings.getColSize()),
                        "Online player " + i);
            } catch (WrongBoardSizeException | FileNotFoundException e) {
                handelError(e.getMessage());
                return;
            }
            try {
                this.gameView.print("Listening on port: " + (Settings.getPortNumber()));
                ((OnlinePlayer) players[i]).connectToClient(Settings.getPortNumber());
                ((OnlinePlayer) players[i]).sendBoard();
                ((OnlinePlayer) players[i]).sendName();
                ((OnlinePlayer) players[i]).sendMessage(this.gameType);
                ((OnlinePlayer) players[i]).sendMessage(Settings.getLanguage());
                this.gameView.print("Connected on port: " + Settings.getPortNumber() + " with " + players[i].getName());
            } catch (PlayerDisconnectedException | ClientConnectionFailedException e) {
                handelError(e.getMessage());
                return;
            }
        }
        for (int j = Settings.getNumOfPlayers(); j < players.length; j++) {
            try {
                players[j] = new Bot(this.gameType.initBoard(this.path, Settings.getRowSize(), Settings.getColSize()),
                        "Bot " + j);
            } catch (WrongBoardSizeException | FileNotFoundException e) {
                handelError(e.getMessage());
                return;
            }
        }
        this.gameRunner = new GameRunner(this.gameType, this.gameController);
        try {
            this.gameRunner.run(players, thisPlayer);
        } catch (PlayerDisconnectedException e) {
            this.gameView.printErr(e.getMessage());
            this.closeConnections(players);
            this.mainMenu();
            return;
        }
        this.calculateAndSendWinningMessage(players, thisPlayer);
        this.closeConnections(players);
        this.mainMenu();
    }

    /**
     * calls other methods in correct order to calculate the score for each player
     * and sends the winner message to all players
     * 
     * @param players    the players which plays the game
     * @param thisPlayer the player at this computer
     */
    public void calculateAndSendWinningMessage(Player[] players, Player thisPlayer) {
        int maxScore = this.calculatePointsForPlayers(players);
        ArrayList<Player> winners = this.getWinners(players, maxScore);
        String playerPoints = this.getPlayersPoints(players);
        this.sendWinnersString(players, playerPoints, winners, maxScore, thisPlayer);
    }

    /**
     * Calculate the points for each player and stores it in the player class, form
     * this it takes out the highest score the players got
     * 
     * @param players the players which plays the game
     * @return the maximal score for all players
     */
    public int calculatePointsForPlayers(Player[] players) {
        int maxScore = 0;
        for (Player player : players) {
            int playerScore = gameType.getPoints(this.boardHandler.findAllWords(player.getBoard().getGameBoard()));
            player.setPoints(playerScore);
            if (maxScore < playerScore) {
                maxScore = playerScore;
            }
        }
        return maxScore;
    }

    /**
     * gets a list of winners from a list of players with a max score, all players
     * that have this max score is returned as a list
     * 
     * @param players  list of players
     * @param maxScore the maximum score the winner/winners have
     * @return an array list of winners
     */
    public ArrayList<Player> getWinners(Player[] players, int maxScore) {
        ArrayList<Player> winners = new ArrayList<Player>();
        for (Player player : players) {
            if (player.getPoints() == maxScore) {
                winners.add(player);
            }
        }
        return winners;
    }

    /**
     * get all player points as a string, example of this is Bot 1: 5 Player 1 : 14
     * 
     * @param players the list of players
     * @return the string to print
     */
    public String getPlayersPoints(Player[] players) {
        String playerScores = "";
        for (Player player : players) {
            int playerScore = player.getPoints();
            playerScores += player.getName() + " : " + playerScore + "\n";
        }
        return playerScores;
    }

    /**
     * Sends the string to all players or print if the player is thisPlayer
     * 
     * @param players      the players
     * @param playerPoints (optional) the points that each player got
     * @param winners      list of winners
     * @param maxScore     the maximum score that the players got
     * @param thisPlayer   the player at this computer
     */
    public void sendWinnersString(Player[] players, String playerPoints, ArrayList<Player> winners, int maxScore,
            Player thisPlayer) {
        String winningMessage = playerPoints;
        winningMessage += this.gameView.getWinnerText(winners, maxScore);
        for (Player player : players) {
            if (player instanceof Player && player == thisPlayer) {
                this.gameView.print(winningMessage);
            } else if (player instanceof OnlinePlayer) {
                try {
                    ((OnlinePlayer) player).sendWinnerMessage(winningMessage);
                } catch (PlayerDisconnectedException e) {
                    // even if one player disconnected we want to send the
                    // winning message to the other players
                    this.gameView.printErr(e.getMessage());
                    continue;
                }
            }
        }
    }

    private void closeConnections(Player[] players) {
        for (Player player : players) {
            if (player instanceof OnlinePlayer) {
                player.closeConnection();
            }
        }
    }

    private void loadLanguage() {
        this.path = System.getProperty("user.dir") + "\\data\\" + Settings.getLanguage() + "\\";
        this.gameController = new GameController(this.path + "menu\\", this.path + "menu\\");
        this.gameView = GameView.getInstance();
    }

    private void mainMenu() {
        this.gameView.printMainText(Settings.getRowSize(), Settings.getColSize(), Settings.getLanguage(),
                Settings.getNumOfPlayers(), Settings.getNumOfBots(), Settings.getPortNumber(), Settings.getIpAddress());
        String input = this.gameController.getInput();
        switch (input) {
        case "1":
            // play standard
            this.gameType = new StandardWordSquares(GameType.BOARD_STANDARD);
            this.initGame();
            break;
        case "2":
            // play scrabble
            this.gameType = new ScrabbleSquares(GameType.BOARD_STANDARD);
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
            this.gameType = new ScrabbleSquares(GameType.BOARD_PRE_DEFINED);
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
            this.gameType = new ScrabbleSquares(GameType.BOARD_RANDOM);
            this.initGame();
            break;
        case "5":
            Player player;
            try {
                player = this.connectToServer();
            } catch (ServerConnectionFailedException e) {
                this.gameView.printErr(e.getMessage());
                this.mainMenu();
                return;
            }
            try {
                this.gameRunner = new GameRunner(this.gameType, this.gameController);
                this.gameRunner.runOnline(player);
                this.mainMenu();
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
        this.gameView.printSettingsText(Settings.getRowSize(), Settings.getColSize(), Settings.getLanguage(),
                Settings.getNumOfPlayers(), Settings.getNumOfBots(), Settings.getPortNumber(), Settings.getIpAddress());
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
                this.settingsMenu();
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                this.settingsMenu();
            }
            break;
        case "2":
            this.gameView.printSettingsOption(2);
            this.setLanguage();
            this.settingsMenu();
            break;
        case "3":
            this.gameView.printSettingsOption(3);
            inputOption = this.gameController.getInput();
            try {
                int numOfPlayers = Integer.parseInt(inputOption);
                Settings.setNumOfPlayers(numOfPlayers);
                this.settingsMenu();
            } catch (NumberFormatException e) {
                this.settingsMenu();
            } catch (NumberOfPlayerWrongException e) {
                this.gameView.printErr(e.getMessage());
                this.settingsMenu();
            }
            break;
        case "4":
            this.gameView.printSettingsOption(4);
            inputOption = this.gameController.getInput();
            try {
                int numOfBots = Integer.parseInt(inputOption);
                Settings.setNumOfBots(numOfBots);
                this.settingsMenu();
            } catch (NumberFormatException e) {
                this.settingsMenu();
            } catch (NumberOfBotsWrongException e) {
                this.gameView.printErr(e.getMessage());
                this.settingsMenu();
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
            this.settingsMenu();
            break;
        case "6":
            this.gameView.printSettingsOption(6);
            Settings.setIpAddress(this.gameController.getInput());
            this.settingsMenu();
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

    private Player connectToServer() throws ServerConnectionFailedException {
        Player[] players = new Player[] { new Player(null, "") };
        Player thisPlayer = players[0];
        this.gameView.print("Connecting to server with port: " + Settings.getPortNumber());
        try {
            thisPlayer.connectToServer(Settings.getIpAddress(), Settings.getPortNumber());
            thisPlayer.getBoardMessage();
            thisPlayer.getNameMessage();
            this.gameType = (GameType) thisPlayer.getNextMessage();
            Settings.setLanguage((String) thisPlayer.getNextMessage());
            this.loadLanguage();
        } catch (PlayerDisconnectedException | ServerConnectionFailedException e) {
            throw new ServerConnectionFailedException(e.getMessage());
        }
        this.gameView.print("Connected as " + thisPlayer.getName());
        this.gameView
                .print(thisPlayer.getBoard().toString(this.gameType.showPoints(), this.gameType.showMultiplyPoints()));
        return thisPlayer;
    }

    private void handelError(String error) {
        this.gameView.printErr(error);
        this.mainMenu();
    }

}
