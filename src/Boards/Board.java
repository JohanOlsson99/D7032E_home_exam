package Boards;

import Handlers.LetterValueHandler;

import java.io.Serializable;

import Boards.Exceptions.*;

public class Board implements Serializable {

    public static final String REGULAR = "\u001B[47m\u001B[30m"; // white background black text
    public static final String DOUBLE_LETTER = "\u001B[46m\u001B[30m"; // cyan background black text
    public static final String TRIPLE_LETTER = "\u001B[42m\u001B[30m"; // green background black text
    public static final String DOUBLE_WORD = "\u001B[43m\u001B[30m"; // yellow background black text
    public static final String TRIPLE_WORD = "\u001B[45m\u001B[30m"; // magenta background black text
    public static final String HEADING = "\u001B[44m\u001B[37m"; // blue background white text
    public static final String RESET = "\u001B[0m"; // reset to default

    private Square[][] gameBoard;
    private LetterValueHandler letterValueHandler;

    /**
     * creates a new board with new squares in each board place
     * 
     * @param width  gameBoard width
     * @param height gameBoard height
     * @throws WrongBoardSizeException if board width or board height is less then 1
     */
    public Board(int width, int height) throws WrongBoardSizeException {
        if (width < 1 || height < 1) {
            throw new WrongBoardSizeException("Board have to be at least a 1 by 1 grid");
        }
        this.gameBoard = new Square[width][height];
        this.newGameBoard();
        this.letterValueHandler = LetterValueHandler.getInstance();
    }

    private void newGameBoard() {
        for (int i = 0; i < this.gameBoard.length; i++) {
            for (int j = 0; j < this.gameBoard[i].length; j++) {
                this.gameBoard[i][j] = new Square();
            }
        }
    }

    /**
     * Updates the board with a letter and a place
     * 
     * @param letter what letter to be added to the board
     * @param place  what place the new letter should be added to
     * @throws PlaceStringIncorrect      if place string is incorrect
     * @throws IndexOutOfBoundsException if place values is incorrect
     * @throws LetterIncorrectException  if letter is incorrect
     * @throws PlaceAlreadyTaken         if place is already taken with another
     *                                   letter
     */
    public void updateGameBoard(char letter, String place) throws PlaceStringIncorrectException,
            IndexOutOfBoundsException, PlaceAlreadyTakenException, LetterIncorrectException {
        int row = 0;
        int col = 0;
        // parse the string for row and column index
        String[] placement = (place.contains(" ") ? place.split(" ") : place.split(""));
        if (placement.length != 2) {
            throw new PlaceStringIncorrectException("The placement string format is not correct");
        }
        row = ((int) Character.toLowerCase(placement[0].charAt(0))) - 'a';
        if (row < 0) {
            throw new PlaceStringIncorrectException("The character given is wrong");
        }
        try {
            col = Integer.parseInt(placement[1]);
        } catch (NumberFormatException e) {
            throw new PlaceStringIncorrectException("The number given is wrong");
        }
        if (row >= this.gameBoard.length) {
            throw new IndexOutOfBoundsException("Character given is to large");
        }
        if (col >= this.gameBoard[0].length) {
            throw new IndexOutOfBoundsException("Number given is to large");
        }
        letter = Character.toUpperCase(letter);
        if ((letter < 'A') || (letter > 'Z')) {
            throw new LetterIncorrectException("Letter chosen is not a correct character");
        }
        if (this.gameBoard[row][col].getLetter() != '\0') {
            throw new PlaceAlreadyTakenException("Cannot place letter on an already taken spot");
        }
        this.gameBoard[row][col].setLetter(letter);
    }

    public Square[][] getGameBoard() {
        return this.gameBoard;
    }

    /**
     * sets the gameBoard type for a specific place on the board
     * 
     * @param row  the row place
     * @param col  the column place
     * @param type the type for this tile
     * @throws IndexOutOfBoundsException if the row or the column value passed in is
     *                                   to large or small
     */
    public void setGameBoardType(int row, int col, int type) throws IndexOutOfBoundsException {
        if (row > this.getRowSize() || row < 0) {
            throw new IndexOutOfBoundsException("Row index out of bounds");
        }
        if (col > this.getColSize() || col < 0) {
            throw new IndexOutOfBoundsException("Column index out of bounds");
        }
        if (type < Square.RL || type > Square.TW) {
            type = Square.RL;
        }
        this.gameBoard[row][col].setSquareType(type);
    }

    /**
     * 
     * @return Row size of the gameBoard
     */
    public int getRowSize() {
        return this.gameBoard.length;
    }

    /**
     * 
     * @return Column size of the gameBoard
     */
    public int getColSize() {
        if (this.gameBoard.length == 0) {
            return 0;
        }
        return this.gameBoard[0].length;
    }

    /**
     * 
     * @param dispLetterVal   if the string contains the vales of each letter
     * @param dispExtraPoints if the string contains what extra points give for each
     *                        place on the grid
     * @return returns a string containing the board with all it's values and types
     */
    public String toString(boolean dispLetterVal, boolean dispExtraPoints) {
        int asciiRowCount = 97; // a
        String retStr = "";
        for (int i = 0; i < gameBoard[0].length; i++) {
            retStr += "\t" + RESET + HEADING + "  " + i + "  ";
        }
        for (Square[] cols : gameBoard) {
            retStr += "\t" + RESET + "\n" + HEADING + "  " + ((char) asciiRowCount++) + "  ";
            for (Square letter : cols) {
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
                String letterValue = letter.getLetter() == '\0' ? "     "
                        : " [" + this.letterValueHandler.getValue(letter.getLetter()) + "]";
                String theLetter = "";
                if (dispLetterVal) {
                    theLetter = letter.getLetter() + letterValue;
                } else {
                    theLetter = "  " + (letter.getLetter() == '\0' ? " " : letter.getLetter()) + "  ";
                }
                retStr += "\t" + RESET + coloring + theLetter;
            }
        }
        retStr += "\t";
        if (dispExtraPoints) {
            retStr += RESET + "\n\n\t" + REGULAR + " STD \t" + RESET + DOUBLE_LETTER + " DL  \t" + RESET + TRIPLE_LETTER
                    + " TL  \t" + RESET + DOUBLE_WORD + " DW  \t" + RESET + TRIPLE_WORD + " TW  \t" + RESET;
        }
        return retStr + RESET + "\n";
    }

}
