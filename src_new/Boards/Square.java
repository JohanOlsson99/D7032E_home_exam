package Boards;

public class Square {

    public static final int RL = 1; //regular letter
    public static final int DL = 2; //double letter
    public static final int TL = 3; //tripple letter
    public static final int DW = 4; //double word
    public static final int TW = 5; //tripple word

    private char letter;
    // standard value is regular letter
    private int squareType = RL;

    public Square() { }

    public Square(char letter, int type) {
        this.letter = letter;
        this.squareType = type;
    }

    public int getSquareType() {
        return this.squareType;
    }

    public void setSquareType(int squareType) {
        this.squareType = squareType;
    }

    public char getLetter() {
        return this.letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }
}
