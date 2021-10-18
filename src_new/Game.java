import Boards.Board;
import Boards.Square;
import GameTypes.GameType;
import GameTypes.ScrabbleSquares;
import GameTypes.StandardWordSquares;
import Handlers.BoardHandler;
import Players.Bot;
import Players.Player;
import View.GameView;
import Boards.Errors.LetterIncorrectException;
import Boards.Errors.PlaceAlreadyTakenException;
import Boards.Errors.PlaceStringIncorrectException;
import Boards.Errors.WrongBoardSizeException;
import Controller.GameController;

public class Game {
    private GameType gameType;
    private BoardHandler boardHandler;
    private GameController gameController;
    private GameView gameView;
    private Player[] players;
    private Player thisPlayer;

    private static class Settings {
        public static int rowSize = 3;
        public static int colSize = 3;
        public static String language = "English";
        public static int numOfPlayers = 1;
        public static int numOfBots = 1;
    }

    public Game(GameType gameType) throws WrongBoardSizeException {
        this.gameType = gameType;
        this.boardHandler = new BoardHandler("CollinsScrabbleWords2019.txt", "letter.txt", 5, 5);
    }

    public void run() {
        int turn = 0;
        int index = 0;
        while (turn < (Settings.rowSize * Settings.colSize)) {
            index = turn % this.players.length;
            if ((this.thisPlayer != null) && (this.players[index] == this.thisPlayer)) {
                char letter = this.gameController.pickLetter();
                System.out.println(letter);
            }

            turn++;
        }
    }

    public void runTest() {

    }

    public void test() {
        Board board = this.boardHandler.getBoard();
        try {
            board.updateBoard('Z', "a0");
            board.updateBoard('A', "a1");
            board.updateBoard('G', "a2");
            board.updateBoard('Z', "b0");
            board.updateBoard('A', "b1");
            board.updateBoard('G', "b2");
            board.updateBoard('Z', "c0");
            board.updateBoard('A', "c1");
            board.updateBoard('G', "c2");
        } catch (IndexOutOfBoundsException | PlaceStringIncorrectException | PlaceAlreadyTakenException
                | LetterIncorrectException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(board.toString(true, true));
        System.out.println("Total points: " + gameType.getPoints(this.boardHandler.findAllWords()));
    }

    private void initGame() {
        try {
            this.boardHandler = new BoardHandler("CollinsScrabbleWords2019.txt", "letter.txt", Settings.colSize, Settings.rowSize);
        } catch (WrongBoardSizeException e) {
            // TODO Auto-generated catch block
            return;
        }

        this.players = new Player[Settings.numOfPlayers + Settings.numOfBots];

        for (int i = 0; i < Settings.numOfPlayers; i++) {
            try {
                this.players[i] = new Player(this.gameType.initBoard(
                    System.getProperty("user.dir") + "\\data\\tile.txt",
                    Settings.rowSize, Settings.colSize));
            } catch (WrongBoardSizeException e) {
                e.printStackTrace();
                return;
            }
        }
        for (int j = Settings.numOfPlayers; j < this.players.length; j++) {
            try {
                this.players[j] = new Bot(this.gameType.initBoard(
                    System.getProperty("user.dir") + "\\data\\tile.txt",
                    Settings.rowSize, Settings.colSize));
            } catch (WrongBoardSizeException e) {
                e.printStackTrace();
                return;
            }
        }
        for (int k = 0; k < this.players.length; k++) {
            System.out.println(this.players[k].getBoard().toString(this.gameType.showPoints(), this.gameType.showMultiplyPoints()));
        }
        if (Settings.numOfPlayers > 0) {
            this.thisPlayer = this.players[0];
        }
        this.run();
    }

    public static void main(String[] args) throws WrongBoardSizeException {
        // GameType gameType = new StandardWordSquares();
        // GameType gameType2 = new GameType();
        // Game game = new Game(gameType);
        // gameType.setTile(System.getProperty("user.dir") + "\\data\\tile.txt", game.boardHandler.getBoard());
        // // game.test();
        // game = new Game(gameType2);
        // gameType2.setTile(System.getProperty("user.dir") + "\\data\\tile.txt", game.boardHandler.getBoard());
        // game.test();
        
        // gameView.printMainText(Settings.rowSize, Settings.colSize, Settings.language, Settings.numOfPlayers, Settings.numOfBots);
        // gameView.printSettingsText(Settings.rowSize, Settings.colSize, Settings.language, Settings.numOfPlayers, Settings.numOfBots);
        // gameView.printSettingsOption(1);
        Game game = new Game();
    }

    public Game() throws WrongBoardSizeException {
        this.gameController = new GameController(
            System.getProperty("user.dir") + "\\data\\menu\\main.txt",
            System.getProperty("user.dir") + "\\data\\menu\\settings.txt");
        this.gameView = GameView.getInstance();
        this.mainMenu();
    }

    private void mainMenu() {
        this.gameView.printMainText(Settings.rowSize, Settings.colSize, Settings.language, Settings.numOfPlayers, Settings.numOfBots);
        String input = this.gameController.getInput();
        switch (input) {
            case "1":
                // play standard
                this.gameType = new StandardWordSquares();
                this.initGame();
                break;
            case "2":
                // play scrabble
                this.gameType = new ScrabbleSquares();
                this.initGame();
                break;
            case "3":
                // play pre-defined Scrabble 5x5
                Settings.rowSize = 5;
                Settings.colSize = 5;
                this.gameType = new ScrabbleSquares();
                this.initGame();
                break;
            case "4":
                // play random Scrabble 5x5
                Settings.rowSize = 5;
                Settings.colSize = 5;
                this.gameType = new ScrabbleSquares();
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
                    if (row < 1 || col < 1) {
                        this.gameView.printWrongInput("Board needs to be at least a 1x1");
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
                this.mainMenu();
                // TODO: Language
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

}
