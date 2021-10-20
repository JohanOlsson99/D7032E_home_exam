package Boards;

import Boards.Errors.*;
import Handlers.LetterValueHandler;

import java.io.Serializable;

public class Board implements Serializable {
    
    public static final String REGULAR = "\u001B[47m\u001B[30m"; //white background black text
    public static final String DOUBLE_LETTER = "\u001B[46m\u001B[30m"; //cyan background black text
    public static final String TRIPLE_LETTER = "\u001B[42m\u001B[30m"; //green background black text
    public static final String DOUBLE_WORD = "\u001B[43m\u001B[30m"; //yellow background black text
    public static final String TRIPLE_WORD = "\u001B[45m\u001B[30m"; //magenta background black text
    public static final String HEADING = "\u001B[44m\u001B[37m"; //blue background white text
    public static final String RESET = "\u001B[0m"; //reset to default

    private Square[][] board;
    private LetterValueHandler letterValueHandler;

    public Board(int width, int height) throws WrongBoardSizeException {
        if (width < 1 || height < 1) {
            throw new WrongBoardSizeException("Board have to be at least a 1 by 1 grid");
        }
        this.board = new Square[width][height];
        this.newBoard();
        this.letterValueHandler = LetterValueHandler.getInstance();
    }

    private void newBoard() {        
        for (int i = 0; i < this.board.length; i++) {
            // int asciiRowCount = 97;
            // int type = 1;
            for (int j = 0; j < this.board[i].length; j++) {
                this.board[i][j] = new Square();
                // this.board[i][j].setLetter((char) asciiRowCount++);
                // this.board[i][j].setSquareType(type++);
            } 
        }
    }
    
    /**
     * Updates the board with a letter and a place
     * @param letter what letter to be added to the board
     * @param place what place the new letter should be added to
     * @throws PlaceStringIncorrect if place string is incorrect
     * @throws IndexOutOfBoundsException if place values is incorrect
     * @throws LetterIncorrectException if letter is incorrect
     * @throws PlaceAlreadyTaken if place is already taken with another letter
     */
    public void updateBoard(char letter, String place) throws 
            PlaceStringIncorrectException, 
            IndexOutOfBoundsException, 
            PlaceAlreadyTakenException,
            LetterIncorrectException {
        int row = 0;
        int col = 0;
        // parse the string for row and column index
        String[] placement = (place.contains(" ") ? place.split(" ") : place.split(""));
        if (placement.length != 2) {
            throw new PlaceStringIncorrectException("The placement string format is not correct");
        }
        row = ((int) Character.toLowerCase(placement[0].charAt(0)))-97; //ascii code for a
        if (row < 0) {
            throw new PlaceStringIncorrectException("The character given is wrong");
        }
        col = Integer.parseInt(placement[1]);
        if (row >= this.board.length) {
            throw new IndexOutOfBoundsException("Character given is to large");
        }
        if (col >= this.board[0].length) {
            throw new IndexOutOfBoundsException("Number given is to large");
        }
        letter = Character.toUpperCase(letter);
        if ((letter < 'A') || (letter > 'Z')) {
            throw new LetterIncorrectException("Letter chosen is not a correct character");
        }
        if (this.board[row][col].getLetter() != '\0') {
            throw new PlaceAlreadyTakenException("Cannot place letter on an already taken spot"); 
         }
        this.board[row][col].setLetter(letter);
    }

    public static void main(String[] args) throws PlaceStringIncorrectException, IndexOutOfBoundsException, PlaceAlreadyTakenException, LetterIncorrectException, WrongBoardSizeException {
        LetterValueHandler hand = LetterValueHandler.getInstance();
        hand.readFromFile(System.getProperty("user.dir") + "\\data\\letter.txt");
        Board board = new Board(5, 5);
        // board.updateBoard('B', "a3");
        System.out.println(board.toString(false, false));
    }

    public Square[][] getBoard() {
        return this.board;
    }

    public void setBoardType(int row, int col, int type) {
        if (row > this.getRowSize() || row < 0) {
            throw new IndexOutOfBoundsException("Row index out of bounds");
        }
        if (col > this.getColSize() || col < 0) {
            throw new IndexOutOfBoundsException("Column index out of bounds");
        }
        if (type < Square.RL || type > Square.TW) {
            type = Square.RL;
        }
        this.board[row][col].setSquareType(type);
    }

    public int getRowSize() {
        return this.board.length;
    }
    
    public int getColSize() {
        if (this.board.length == 0) {
            return 0;
        }
        return this.board[0].length;
    }

    /**
     * 
     * @param dispLetterVal if the string contains the vales of each letter
     * @param dispExtraPoints if the string contains what extra points give for each place on the grid
     * @return returns a string containing the board with all it's values and types
     */
    public String toString(boolean dispLetterVal, boolean dispExtraPoints) {
        int asciiRowCount = 97; //a
        String retStr = "";
        for(int i = 0; i < board[0].length; i++) {
            retStr += "\t" + RESET + HEADING + "  " + i + "  ";
        }
        for(Square[] cols : board) {
            retStr += "\t" + RESET + "\n" + HEADING + "  " + ((char) asciiRowCount++) + "  "; 
            for(Square letter : cols) {
                String coloring = "";
                switch (letter.getSquareType()) {
                    case (Square.RL):
                        coloring = REGULAR;
                        break;
                    case Square.DL:
                        coloring = DOUBLE_LETTER;
                        break;
                    case Square.TL:
                        coloring = TRIPLE_LETTER;
                        break;
                    case Square.DW:
                        coloring = DOUBLE_WORD;
                        break;
                    case Square.TW:
                        coloring = TRIPLE_WORD;
                        break;
                }
                String letterValue = letter.getLetter() == '\0' ? "     " : " [" + this.letterValueHandler.getValue(letter.getLetter()) + "]";
                String theLetter = "";
                if (dispLetterVal) {
                    theLetter = letter.getLetter() + letterValue;
                } else {
                    theLetter = "  "+ (letter.getLetter() == '\0' ? " " : letter.getLetter()) + "  ";
                }
                retStr += "\t" + RESET + coloring + theLetter;
            }
        }
        retStr += "\t";
        if(dispExtraPoints) {
            retStr += RESET + "\n\n\t" + REGULAR + " STD \t" + RESET + DOUBLE_LETTER + " DL  \t"
                + RESET + TRIPLE_LETTER + " TL  \t" + RESET + DOUBLE_WORD + " DW  \t" + RESET
                + TRIPLE_WORD + " TW  \t" + RESET;
        }
        return retStr + RESET + "\n";
    }    
   
}
