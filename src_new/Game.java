import java.io.File;
import java.util.ArrayList;

import GameTypes.GameType;
import GameTypes.ScrabbleSquares;
import GameTypes.StandardWordSquares;
import Handlers.BoardHandler;
import Players.Bot;
import Players.Player;
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

    private static class Settings {
        public static int rowSize = 2;
        public static int colSize = 2;
        public static String language = "English";
        public static int numOfPlayers = 1;
        public static int numOfBots = 24;
    }

    public void run() {
        int turn = 0;
        int index = this.gameType.getRandomStartPlayer(this.players.length);
        while (turn < (Settings.rowSize * Settings.colSize)) {
            index = index % this.players.length;
            if (this.thisPlayer != null) {
                this.gameView.print(this.thisPlayer.getBoard().toString(
                    this.gameType.showPoints(),
                    this.gameType.showMultiplyPoints()));
            }
            char letter = this.players[index].pickAndPlace(gameController);
            this.placeLetterForAllPlayers(letter, this.players[index]);
            turn++;
            index++;
        }
        if (this.thisPlayer != null) {
            this.gameView.print(this.thisPlayer.getBoard().toString(
                this.gameType.showPoints(),
                this.gameType.showMultiplyPoints()));
        }
        this.declareWinner();
    }

    private void declareWinner() {
        int maxScore = 0;
        for (Player player : this.players) {
            int playerScore = gameType.getPoints(this.boardHandler.findAllWords(
                player.getBoard().getBoard()));
            player.setPoints(playerScore);
            this.gameView.print(player.getName() + " : " + playerScore);
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
        this.gameView.printWinners(winners, maxScore);
    }

    private void placeLetterForAllPlayers(char letter, Player skipPlayer) {
        for (Player player : this.players) {
            if (player == skipPlayer) {
                continue;
            }
            if (player instanceof Player && player == this.thisPlayer) {
                player.placeLetter(letter, gameController);
            } else if (player instanceof Bot) {
                ((Bot) player).placeLetter(letter, gameController);
            }
        }
    }

    public void runTest() {

    }

    private void initGame() {
        try {
            this.boardHandler = new BoardHandler(this.path, this.path, Settings.colSize, Settings.rowSize);
        } catch (WrongBoardSizeException e) {
            // TODO Auto-generated catch block
            return;
        }

        this.players = new Player[Settings.numOfPlayers + Settings.numOfBots];

        for (int i = 0; i < Settings.numOfPlayers; i++) {
            try {
                this.players[i] = new Player(this.gameType.initBoard(
                    this.path,
                    Settings.rowSize, Settings.colSize), "Player " + i);
            } catch (WrongBoardSizeException e) {
                e.printStackTrace();
                return;
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
        if (Settings.numOfPlayers > 0) {
            this.thisPlayer = this.players[0];
        }
        this.run();
    }

    public static void main(String[] args) {
        Game game = new Game();
    }

    public Game() {
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

    private void mainMenu() {
        this.gameView.printMainText(Settings.rowSize, Settings.colSize, Settings.language, Settings.numOfPlayers, Settings.numOfBots);
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
        this.gameView.printSettingsText(Settings.rowSize, Settings.colSize, Settings.language, Settings.numOfPlayers, Settings.numOfBots);
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
                this.mainMenu();
                return;
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

}
