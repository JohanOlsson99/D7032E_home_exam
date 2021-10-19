package GameTypes;
import Boards.Square;
import Boards.Errors.WrongBoardSizeException;
import Boards.Board;
import Handlers.LetterValueHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameType {

    private int boardType;

    public static final int boardStandard = 1;
    public static final int boardPreDefined = 2;
    public static final int boardRandom = 3;
    
    public static void main(String[] args) {
        GameType gameType = new GameType(1);   
    }
    
    protected boolean showPoints = true;
    protected boolean showMultiplyPoints = true;
    private Random random;

    public GameType(int boardType) {
        this.boardType = boardType;
        this.random = new Random();
    }

    public Board initBoard(String filePath, int width, int height) throws WrongBoardSizeException {
        Board board = new Board(width, height);
        if (this.boardType == this.boardRandom) {
            this.setTileRandom(board);
        } else if (this.boardType == this.boardPreDefined) {
            this.setTile(filePath + "tilePreDefined.txt", board);
        } else {
            this.setTile(filePath + "tile.txt", board);
        }
        return board;
    }
    
    protected void setTileRandom(Board board) {
        for (int row = 0; row < board.getRowSize(); row++) {
            for (int col = 0; col < board.getColSize(); col++) {
                board.setBoardType(row, col, this.random.nextInt(Square.TW) + Square.RL);
            }
        }
    }

    protected void setTile(String filePath, Board board) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            int row = 0;
            while((line = bufferedReader.readLine()) != null) {
                String gameTypes = line.replace(" ", "");
                for (int col = 0; col < gameTypes.length(); col++) {
                    if (row >= board.getRowSize() || col >= board.getColSize()) {
                        break;
                    }
                    int gameType = Character.getNumericValue(gameTypes.charAt(col));
                    board.setBoardType(row, col, gameType);
                }
                row++;
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getRandomStartPlayer(int length) {
        return this.random.nextInt(length);
    }

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
     * @param wordLength length of the word
     * @return returns the points for a word for different game modes,
     * ex points for wordLength and points for letter value.
     */
    protected int wordPointsToGive(int currentWordPoints, int wordLength) {
        // returns the calculated points
        return currentWordPoints;
    }

    /**
     * 
     * @param currentWordPoints current word points
     * @param multiply multiply value
     * @return returns the points times the multiply value, can be changed for different game modes
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

    public boolean showPoints() {
        return this.showPoints;
    }
    
    public boolean showMultiplyPoints() {
        return this.showMultiplyPoints;
    }
}
