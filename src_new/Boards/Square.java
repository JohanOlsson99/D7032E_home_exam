package Boards;

import java.io.Serializable;

public class Square implements Serializable {

    public static final int RL = 1; // regular letter
    public static final int DL = 2; // double letter
    public static final int TL = 3; // tripple letter
    public static final int DW = 4; // double word
    public static final int TW = 5; // tripple word

    // standard value is regular letter
    private int squareType = RL;
    private char letter;

    /**
     * Creates an instance of a square
     */
    public Square() {
    }
    
    /**
     * Creates an instance of a square with predefined values
     */
    public Square(char letter, int squareType) {
        this.letter = letter;
        this.squareType = squareType;
    }

    /**
     * 
     * @return this square type
     */
    public int getSquareType() {
        return this.squareType;
    }

    /**
     * sets the type value of this square
     * 
     * @param squareType the new type value
     */
    public void setSquareType(int squareType) {
        this.squareType = squareType;
    }

    /**
     * 
     * @return the character which this square contains
     */
    public char getLetter() {
        return this.letter;
    }

    /**
     * sets the letter value of this square
     * 
     * @param letter the new letter value to be placed at this square
     */
    public void setLetter(char letter) {
        this.letter = letter;
    }
}
