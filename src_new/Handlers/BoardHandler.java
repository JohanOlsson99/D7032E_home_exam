package Handlers;
import Boards.Board;
import Boards.Square;
import Boards.Errors.LetterIncorrectException;
import Boards.Errors.PlaceAlreadyTakenException;
import Boards.Errors.PlaceStringIncorrectException;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Arrays;

public class BoardHandler {
    private WordHandler wordHandler;
    private LetterValueHandler letterHandler;
    private Board board;

    public BoardHandler(WordHandler wordHandler) {
        this.wordHandler = wordHandler;
    }
    
    public BoardHandler(String wordFileName, String letterFileName, int boardWidth, int boardHeight) {
        this.wordHandler = WordHandler.getInstance();
        this.wordHandler.readFromFile(System.getProperty("user.dir") + "\\data\\" + wordFileName);
        this.letterHandler = LetterValueHandler.getInstance();
        this.letterHandler.readFromFile(System.getProperty("user.dir") + "\\data\\" + letterFileName);
        this.board = new Board(boardWidth, boardHeight);
    }

    public ArrayList<Square[]> findAllWords(Square[][] board) {
        ArrayList<Square[]> words = new ArrayList<Square[]>();

        if (board.length < 1 || board[0].length < 1) {
            throw new IndexOutOfBoundsException(); // needs to be changed later!
        }

        ArrayList<String> allWords = new ArrayList<String>();
        allWords.addAll(this.getAllRowWords(board));

        allWords.addAll(this.getAllColWords(board));
        
        allWords.addAll(this.getAllDiagWords(board));

        System.out.println(allWords.toString());
        System.out.println(allWords.size() + " words found");

        return words;
    }

    private ArrayList<String> getAllRowWords(Square[][] board) {
        // takes out all row words
        ArrayList<String> rowWords = new ArrayList<String>();
        for (int row = 0; row < board.length; row++) {
            String rowWord = "";
            for (int col = 0; col < board[row].length; col++) {
                rowWord += board[row][col].getLetter();
            }
            rowWords.addAll(this.getAllWordsFromString(rowWord));
        }
        return rowWords;
    }

    private ArrayList<String> getAllColWords(Square[][] board) {
        // takes out all column words
        ArrayList<String> colWords = new ArrayList<String>();
        for (int col = 0; col < board.length; col++) {
            String colWord = "";
            for (int row = 0; row < board[col].length; row++) {
                colWord += board[row][col].getLetter();
            }
            colWords.addAll(this.getAllWordsFromString(colWord));
        }
        return colWords;
    }

    private ArrayList<String> getAllDiagWords(Square[][] board) {
        // takes out all diagonal words from middle to left
        ArrayList<String> diagWords = new ArrayList<String>();
        for (int row = 0; row < board.length; row++) {
            int tempRow = row;
            int tempCol = 0;
            String diagWord = "";
            while ((tempRow < board.length) && (tempCol < board[row].length)) {
                diagWord += board[tempRow][tempCol].getLetter();
                tempRow++;
                tempCol++;
            }
            diagWords.addAll(this.getAllWordsFromString(diagWord));
        }
        
        // takes out all diagonal words from middle + 1 to right
        for (int col = 1; col < board.length; col++) {
            int tempCol = col;
            int tempRow = 0;
            String diagWord = "";
            while ((tempCol < board.length) && (tempRow < board[col].length)) {
                diagWord += board[tempRow][tempCol].getLetter();
                tempRow++;
                tempCol++;
            }
            diagWords.addAll(this.getAllWordsFromString(diagWord));
        }

        return diagWords;
    }
    
    private ArrayList<String> getAllWordsFromString(String word) {
        ArrayList<String> listOfWords = new ArrayList<String>();
        for (int i = 0; i < word.length(); i++) {
            for (int j = i + 1; j <= word.length(); j++) {
                String substring = word.substring(i, j);
                if (this.wordHandler.wordExists(substring)) {
                    listOfWords.add(substring);
                }
            }
        }
        return listOfWords;
    }

    public static void main(String[] args) {
        WordHandler word = WordHandler.getInstance();
        word.readFromFile(System.getProperty("user.dir") + "\\src\\CollinsScrabbleWords2019.txt");
        BoardHandler hand = new BoardHandler(word);
        Board board = new Board(3, 3);
        try {
            board.updateBoard('Z', "a0");
            board.updateBoard('I', "a1");
            board.updateBoard('T', "a2");
            board.updateBoard('Z', "b0");
            board.updateBoard('I', "b1");
            board.updateBoard('T', "b2");
            board.updateBoard('Z', "c0");
            board.updateBoard('I', "c1");
            board.updateBoard('T', "c2");
        } catch (IndexOutOfBoundsException | PlaceStringIncorrectException | PlaceAlreadyTakenException
                | LetterIncorrectException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        hand.findAllWords(board.getBoard());
        System.out.println(board.toString(true, true));
    }
}




// // takes out all row words
        // ArrayList<String> rowWords = new ArrayList<String>();
        // for (row = 0; row < board.length; row++) {
        //     String rowWord = "";
        //     for (col = 0; col < board[row].length; col++) {
        //         rowWord += board[row][col].getLetter();
        //     }
        //     System.out.println("Row word: " + rowWord);
        //     rowWords.addAll(this.getAllSubStrings(rowWord));
        // }
        
        // // takes out all column words
        // ArrayList<String> colWords = new ArrayList<String>();
        // for (col = 0; col < board.length; col++) {
        //     String colWord = "";
        //     for (row = 0; row < board[col].length; row++) {
        //         colWord += board[row][col].getLetter();
        //     }
        //     colWords.addAll(this.getAllSubStrings(colWord));
        // }

        // // takes out all diagonal words from middle to left
        // ArrayList<String> diagWords = new ArrayList<String>();
        // for (row = 0; row < board.length; row++) {
        //     int tempRow = row;
        //     int tempCol = 0;
        //     String diagWord = "";
        //     while ((tempRow < board.length) && (tempCol < board[row].length)) {
        //         diagWord += board[tempRow][tempCol].getLetter();
        //         tempRow++;
        //         tempCol++;
        //     }
        //     diagWords.addAll(this.getAllSubStrings(diagWord));
        // }
        
        // // takes out all diagonal words from middle + 1 to right
        // for (col = 1; col < board.length; col++) {
        //     int tempCol = col;
        //     int tempRow = 0;
        //     String diagWord = "";
        //     while ((tempCol < board.length) && (tempRow < board[col].length)) {
        //         diagWord += board[tempRow][tempCol].getLetter();
        //         tempRow++;
        //         tempCol++;
        //     }
        //     diagWords.addAll(this.getAllSubStrings(diagWord));
        // }
        // rowWords.addAll(colWords);
        // rowWords.addAll(diagWords);