package Boards;

import java.util.Arrays;

import Boards.Errors.*;
import Handlers.LetterValueHandler;

public class Board {
    
    public static final String REGULAR = "\u001B[47m\u001B[30m"; //white background black text
    public static final String DOUBLE_LETTER = "\u001B[46m\u001B[30m"; //cyan background black text
    public static final String TRIPLE_LETTER = "\u001B[42m\u001B[30m"; //green background black text
    public static final String DOUBLE_WORD = "\u001B[43m\u001B[30m"; //yellow background black text
    public static final String TRIPLE_WORD = "\u001B[45m\u001B[30m"; //magenta background black text
    public static final String HEADING = "\u001B[44m\u001B[37m"; //blue background white text
    public static final String RESET = "\u001B[0m"; //reset to default

    private Square[][] board;
    private LetterValueHandler letterValueHandler;

    public Board(int width, int height, LetterValueHandler letterValueHandler) {
        this.board = new Square[width][height];
        this.newBoard();
        this.letterValueHandler = letterValueHandler;
    }

    private void newBoard() {        
        for (int i = 0; i < this.board.length; i++) {
            int asciiRowCount = 97;
            int type = 1;
            for (int j = 0; j < this.board[i].length; j++) {
                this.board[i][j] = new Square();
                this.board[i][j].setLetter((char) asciiRowCount++);
                this.board[i][j].setSquareType(type++);
            } 
        }
    }
    
    /**
     * Updates the board with a letter and a place
     * @param letter what letter to be added to the board
     * @param place what place the new letter should be added to
     * @throws PlaceStringIncorrect if place string is incorrect
     * @throws IndexOutOfBoundsException if place values is incorrect
     * @throws PlaceAlreadyTaken if place is already taken with another letter
     */
    public void updateBoard(char letter, String place) throws 
            PlaceStringIncorrect, 
            IndexOutOfBoundsException, 
            PlaceAlreadyTaken {
        int row = 0;
        int col = 0;
        // parse the string for row and column index
        String[] placement = (place.contains(" ") ? place.split(" ") : place.split(""));
        if (placement.length != 2) {
            throw new PlaceStringIncorrect("The placement string format is not correct!");
        }
        row = ((int) placement[0].charAt(0))-97; //ascii code for a
        col = Integer.parseInt(placement[1]);
        if (row >= this.board.length) {
            throw new IndexOutOfBoundsException("Character given is to large");
        }
        for (int i = 0; i < row; i++) {
            if (col >= this.board[i].length) {
                throw new IndexOutOfBoundsException("Number given is to large");
            }
        }
        if (this.board[row][col].getLetter() != '\0') {
           throw new PlaceAlreadyTaken("Cannot place letter on an already taken spot"); 
        }
        this.board[row][col].setLetter(letter);
    }

    public static void main(String[] args) throws PlaceStringIncorrect, IndexOutOfBoundsException, PlaceAlreadyTaken {
        LetterValueHandler hand = new LetterValueHandler();
        hand.readFromFile(System.getProperty("user.dir") + "\\data\\letter.txt");
        Board board = new Board(5, 5, hand);
        // board.updateBoard('K', "b3");
        System.out.println(board.toString(true, true));
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
        for(int i=0; i<board[0].length; i++) {
            retStr += "      "+ RESET + HEADING + "  " + i + "  ";
        }
        for(Square[] cols : board) {
            retStr += "\t" + RESET +"\n"+HEADING + "  "+ ((char) asciiRowCount++)+"  "; 
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
                String letterValue = letter.getLetter() == '\0' ? "     ":" [" + this.letterValueHandler.getValue(letter.getLetter()) + "]";
                String theLetter = "";
                if (dispLetterVal) {
                    theLetter = letter.getLetter() + letterValue;
                } else {
                    theLetter = "  "+ (letter.getLetter() == '\0' ? " ": letter.getLetter()) + "  ";
                }
                retStr += "\t" + RESET + coloring + theLetter;
            }
        }
        retStr += "\t";
        if(dispExtraPoints) {
            retStr += RESET + "\n\n\t" +REGULAR + " STD \t" + RESET+DOUBLE_LETTER + " DL  \t" + RESET+TRIPLE_LETTER + " TL  \t" + RESET+DOUBLE_WORD + " DW  \t" + RESET+TRIPLE_WORD + " TW  \t" + RESET;
        }
        return retStr + RESET + "\n";
    }
    
}
