package GameTypes;

import Boards.Square;
import Boards.Exceptions.WrongBoardSizeException;
import Boards.Board;
import Handlers.LetterValueHandler;

import java.io.Serializable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameType implements Serializable {

    private int boardType;

    public static final int BOARD_STANDARD = 1;
    public static final int BOARD_PRE_DEFINED = 2;
    public static final int BOARD_RANDOM = 3;

    protected boolean showPoints = true;
    protected boolean showMultiplyPoints = true;
    private Random random;

    /**
     * 
     * @param boardType a value to define how the board should be initialized,
     *                  standard, predefined or random
     */
    public GameType(int boardType) {
        this.boardType = boardType;
        this.random = new Random();
    }

    public Board initBoard(String filePath, int width, int height) throws WrongBoardSizeException, FileNotFoundException {
        Board board = new Board(width, height);
        if (this.boardType == GameType.BOARD_RANDOM) {
            this.setTileRandom(board);
        } else if (this.boardType == GameType.BOARD_PRE_DEFINED) {
            this.setTile(filePath + "tilePreDefined.txt", board);
        } else {
            this.setTile(filePath + "tile.txt", board);
        }
        return board;
    }

    protected void setTileRandom(Board board) {
        for (int row = 0; row < board.getRowSize(); row++) {
            for (int col = 0; col < board.getColSize(); col++) {
                board.setGameBoardType(row, col, this.random.nextInt(Square.TW) + Square.RL);
            }
        }
    }

    protected void setTile(String filePath, Board board) throws FileNotFoundException {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            int row = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String gameTypes = line.replace(" ", "");
                for (int col = 0; col < gameTypes.length(); col++) {
                    if (row >= board.getRowSize() || col >= board.getColSize()) {
                        break;
                    }
                    int gameType = Character.getNumericValue(gameTypes.charAt(col));
                    board.setGameBoardType(row, col, gameType);
                }
                row++;
            }
            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            throw new FileNotFoundException("The file to get the board could not be found");
        }
    }

    /**
     * Gets a random start player as an index
     * 
     * @param length how many players there are in this game
     * @return the start player as the index from 0
     */
    public int getRandomStartPlayer(int length) {
        return this.random.nextInt(length);
    }

    /**
     * gets the total points for words
     * 
     * @param words the words which you want to get how much points these words
     *              together make up
     * @return an number of how much points these words got together
     */
    public int getPoints(ArrayList<Square[]> words) {
        return this.calculatePoints(words);
    }

    protected int calculatePoints(ArrayList<Square[]> words) {
        LetterValueHandler letterValueHandler = LetterValueHandler.getInstance();
        int totalPoints = 0;
        for (int i = 0; i < words.size(); i++) {
            Square[] word = words.get(i);
            int wordPoints = 0;
            int wordMultiplier = 1;
            for (int j = 0; j < word.length; j++) {
                int letterPoints = letterValueHandler.getValue(word[j].getLetter());
                letterPoints *= this.multiplyLetterPoints(word[j]);
                switch (word[j].getSquareType()) {
                case Square.DW:
                    wordMultiplier *= 2;
                    break;
                case Square.TW:
                    wordMultiplier *= 3;
                    break;
                default:
                    wordMultiplier *= 1;
                    break;
                }
                wordPoints += letterPoints;
            }
            wordPoints = this.wordPointsToGive(wordPoints, word.length);
            totalPoints += this.multiplyWordPoints(wordPoints, wordMultiplier);
            // totalPoints += wordPoints * wordMultiplier;
        }
        return totalPoints;
    }

    /**
     * 
     * @param currentWordPoints calculated word points
     * @param wordLength        length of the word
     * @return returns the points for a word for different game modes, ex points for
     *         wordLength and points for letter value.
     */
    protected int wordPointsToGive(int currentWordPoints, int wordLength) {
        // returns the calculated points
        return currentWordPoints;
    }

    /**
     * 
     * @param currentWordPoints current word points
     * @param multiply          multiply value
     * @return returns the points times the multiply value, can be changed for
     *         different game modes
     */
    protected int multiplyWordPoints(int currentWordPoints, int multiply) {
        return currentWordPoints * multiply;
    }

    /**
     * 
     * @param letter the square letter
     * @return the multiply letter value, ex Double letter och Tripple letter
     */
    protected int multiplyLetterPoints(Square letter) {
        int multiply = 1;
        switch (letter.getSquareType()) {
        case Square.DL:
            multiply *= 2;
            break;
        case Square.TL:
            multiply *= 3;
            break;
        default:
            multiply = 1;
            break;
        }
        return multiply;
    }

    /**
     * 
     * @return the variable for if this game mode should print points for each
     *         letter
     */
    public boolean showPoints() {
        return this.showPoints;
    }

    /**
     * 
     * @return the variable for if this game mode should print multiply points
     *         explanation
     */
    public boolean showMultiplyPoints() {
        return this.showMultiplyPoints;
    }
}
