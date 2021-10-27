package Handlers;

import Boards.Square;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class BoardHandler {
    private WordHandler wordHandler;
    private LetterValueHandler letterHandler;

    /**
     * Creates a boardHandler with wordHandler and letterHandler
     * 
     * @param wordFilePath   file path to words.txt
     * @param letterFilePath file path to letter.txt
     * @throws java.io.FileNotFoundException if WordHandler or LetterValueHandler
     *                                       didn't found the file they where
     *                                       looking for
     */
    public BoardHandler(String wordFilePath, String letterFilePath) throws FileNotFoundException {
        this.wordHandler = WordHandler.getInstance();
        this.wordHandler.readFromFile(wordFilePath);
        this.letterHandler = LetterValueHandler.getInstance();
        this.letterHandler.readFromFile(letterFilePath);
    }

    /**
     * Gets all possible words from a 2D-array of squares
     * 
     * @param boardSquare a 2D-array of squares
     * @return a 2D-list with all square words that exists in the 2D-array of
     *         squares
     */
    public ArrayList<Square[]> findAllWords(Square[][] boardSquare) {

        ArrayList<Square[]> allWords = new ArrayList<Square[]>();
        allWords.addAll(this.getAllRowWords(boardSquare));

        allWords.addAll(this.getAllColWords(boardSquare));

        allWords.addAll(this.getAllDiagWords(boardSquare));

        return allWords;
    }

    private ArrayList<Square[]> getAllRowWords(Square[][] board) {
        // takes out all row words
        ArrayList<Square[]> rowWordsSquare = new ArrayList<Square[]>();
        for (int row = 0; row < board.length; row++) {
            // square array with the squares of a row
            Square[] rowSquares = new Square[board[row].length];
            for (int col = 0; col < board[row].length; col++) {
                rowSquares[col] = board[row][col];
            }
            rowWordsSquare.addAll(this.getAllWordsFromSquare(rowSquares));
        }
        return rowWordsSquare;
    }

    private ArrayList<Square[]> getAllColWords(Square[][] board) {
        // takes out all column words
        ArrayList<Square[]> colWordsSquare = new ArrayList<Square[]>();
        for (int col = 0; col < board[0].length; col++) {
            Square[] colSquares = new Square[board.length];
            for (int row = 0; row < board.length; row++) {
                colSquares[row] = board[row][col];
            }
            colWordsSquare.addAll(this.getAllWordsFromSquare(colSquares));
        }
        return colWordsSquare;
    }

    private ArrayList<Square[]> getAllDiagWords(Square[][] board) {
        // takes out all diagonal words from middle to left
        ArrayList<Square[]> diagWordsSquare = new ArrayList<Square[]>();
        for (int row = 0; row < board.length; row++) {
            int tempRow = row;
            int tempCol = 0;
            ArrayList<Square> diagWordSquares = new ArrayList<Square>();
            while ((tempRow < board.length) && (tempCol < board[row].length)) {
                diagWordSquares.add(board[tempRow][tempCol]);
                tempRow++;
                tempCol++;
            }
            diagWordsSquare.addAll(this.getAllWordsFromSquare(diagWordSquares.toArray(new Square[0])));
        }

        // takes out all diagonal words from middle + 1 to right
        for (int col = 1; col < board.length; col++) {
            int tempCol = col;
            int tempRow = 0;
            ArrayList<Square> diagWordSquares = new ArrayList<Square>();
            while ((tempRow < board.length) && (tempCol < board[col].length)) {
                diagWordSquares.add(board[tempRow][tempCol]);
                tempRow++;
                tempCol++;
            }
            diagWordsSquare.addAll(this.getAllWordsFromSquare(diagWordSquares.toArray(new Square[0])));
        }

        return diagWordsSquare;
    }

    private ArrayList<Square[]> getAllWordsFromSquare(Square[] word) {
        ArrayList<Square[]> listOfWords = new ArrayList<Square[]>();
        for (int i = 0; i < word.length; i++) {
            for (int j = i + 1; j <= word.length; j++) {
                // substring to check if word exists later
                String subString = "";
                // substring square to return if the word exists
                Square[] subStringSquare = new Square[j - i];
                int index = 0;
                for (int k = i; k < j; k++) {
                    subString += word[k].getLetter();
                    subStringSquare[index++] = word[k];
                }
                if (this.wordHandler.wordExists(subString)) {
                    listOfWords.add(subStringSquare);
                }
            }
        }
        return listOfWords;
    }
}
