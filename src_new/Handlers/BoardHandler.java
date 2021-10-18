package Handlers;
import Boards.Board;
import Boards.Square;
import Boards.Errors.LetterIncorrectException;
import Boards.Errors.PlaceAlreadyTakenException;
import Boards.Errors.PlaceStringIncorrectException;
import Boards.Errors.WrongBoardSizeException;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Arrays;

public class BoardHandler {
    private WordHandler wordHandler;
    private LetterValueHandler letterHandler;
    private Board board;

    public BoardHandler(WordHandler wordHandler, int width, int height) throws WrongBoardSizeException {
        this.wordHandler = wordHandler;
        this.board = new Board(width, height);
    }

    public Board getBoard() {
        return this.board;
    }
    
    public BoardHandler(String wordFileName, String letterFileName, int boardWidth, int boardHeight) throws WrongBoardSizeException {
        this.wordHandler = WordHandler.getInstance();
        this.wordHandler.readFromFile(System.getProperty("user.dir") + "\\data\\" + wordFileName);
        this.letterHandler = LetterValueHandler.getInstance();
        this.letterHandler.readFromFile(System.getProperty("user.dir") + "\\data\\" + letterFileName);
        this.board = new Board(boardWidth, boardHeight);
    }

    public ArrayList<Square[]> findAllWords() {
        Square[][] boardSquare = this.board.getBoard();

        if (boardSquare.length < 1 || boardSquare[0].length < 1) {
            throw new IndexOutOfBoundsException(); // needs to be changed later!
        }

        ArrayList<Square[]> allWords = new ArrayList<Square[]>();
        allWords.addAll(this.getAllRowWords(boardSquare));

        allWords.addAll(this.getAllColWords(boardSquare));
        
        allWords.addAll(this.getAllDiagWords(boardSquare));

        ArrayList<String> wordsText = new ArrayList<>();
        for (int i = 0; i < allWords.size(); i++) {
            String word = "";
            for (int j = 0; j < allWords.get(i).length; j++) {
                word += allWords.get(i)[j].getLetter();
            }
            wordsText.add(word);
        }
        System.out.println(wordsText.toString() + " words");
        System.out.println(allWords.size() + " words found");

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
        // ArrayList<String> wordsText = new ArrayList<>();

        // for (int i = 0; i < board.length; i++) {
        //     String word = "";
        //     for (int j = 0; j < board[i].length; j++) {
        //         word += board[i][j].getLetter();
        //     }
        //     wordsText.add(word);
        // }
        // System.out.println(wordsText.toString() + " words");

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
            while ((tempRow < board.length) && (tempCol < board[col].length) ) {
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
                Square[] subStringSquare = new Square[j-i];
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

    public static void main(String[] args) throws WrongBoardSizeException {
        WordHandler word = WordHandler.getInstance();
        word.readFromFile(System.getProperty("user.dir") + "\\src\\CollinsScrabbleWords2019.txt");
        BoardHandler hand = new BoardHandler(word, 3, 3);
        Board board = hand.getBoard();
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
        hand.findAllWords();
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