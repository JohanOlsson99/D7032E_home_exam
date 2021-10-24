package View;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import Players.Player;

public class GameView {

    private String mainText;
    private String settingsText;
    private String[] settingOptionsText;
    private String letterChooseText;
    private String placeLetterText;
    private String boardSizeErrorText;
    private String winnerText;
    private String winnersText;

    public static final String RED_TEXT = "\u001B[31m";
    public static final String RESET_TEXT = "\u001B[0m";

    private static GameView INSTANCE = null;

    private GameView() {
    }

    /**
     * 
     * @return the single instance of this class
     */
    public static GameView getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameView();
        }
        return INSTANCE;
    }

    /**
     * 
     * @param mainText the main text to set
     */
    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    /**
     * prints the main text which replaces variable in the string with the
     * parameters passed is
     * 
     * @param boardRow     board row size
     * @param boardCol     board column size
     * @param language     language setting
     * @param numOfPlayers number of players setting
     * @param numOfBots    number of bots setting
     * @param portNumber   port number setting
     * @param ipAddress    ip address setting
     */
    public void printMainText(int boardRow, int boardCol, String language, int numOfPlayers, int numOfBots,
            int portNumber, String ipAddress) {
        String parsedMainText = this.mainText;
        parsedMainText = parsedMainText.replace("#BoardSize", boardRow + "/" + boardCol);
        parsedMainText = parsedMainText.replace("#Language", language);
        parsedMainText = parsedMainText.replace("#NumOfPlayers", Integer.toString(numOfPlayers));
        parsedMainText = parsedMainText.replace("#NumOfBots", Integer.toString(numOfBots));
        parsedMainText = parsedMainText.replace("#PortNumber", Integer.toString(portNumber));
        parsedMainText = parsedMainText.replace("#IpAddress", ipAddress);
        print(parsedMainText);
    }

    /**
     * 
     * @param settingsText       the settings text
     * @param settingOptionsText text for each option, ex change board size prints a
     *                           special message
     */
    public void setSettingsText(String settingsText, String[] settingOptionsText) {
        this.settingsText = settingsText;
        this.settingOptionsText = settingOptionsText;
    }

    /**
     * prints the settings text which replaces variable in the string with the
     * parameters passed is
     * 
     * @param boardRow     board row size
     * @param boardCol     board column size
     * @param language     the language
     * @param numOfPlayers the number of players
     * @param numOfBots    the number of bots
     * @param portNumber   the port number
     * @param ipAddress    the ip address
     */
    public void printSettingsText(int boardRow, int boardCol, String language, int numOfPlayers, int numOfBots,
            int portNumber, String ipAddress) {
        String parsedMainText = this.settingsText;
        parsedMainText = parsedMainText.replace("#BoardSize", boardRow + "/" + boardCol);
        parsedMainText = parsedMainText.replace("#Language", language);
        parsedMainText = parsedMainText.replace("#NumOfPlayers", Integer.toString(numOfPlayers));
        parsedMainText = parsedMainText.replace("#NumOfBots", Integer.toString(numOfBots));
        parsedMainText = parsedMainText.replace("#PortNumber", Integer.toString(portNumber));
        parsedMainText = parsedMainText.replace("#IpAddress", ipAddress);
        print(parsedMainText);
    }

    /**
     * print the settings option by a specific index
     * 
     * @param index
     * @throws IndexOutOfBoundsException when the index value is to small or to
     *                                   large
     */
    public void printSettingsOption(int index) throws IndexOutOfBoundsException {
        index -= 1; // removes 1 index since input is 1 - 5 and not 0 - 4
        if (index < 0 || index >= this.settingOptionsText.length) {
            throw new IndexOutOfBoundsException("Index for option text is out of bounds");
        }
        print(this.settingOptionsText[index]);
    }

    /**
     * prints a string to the user
     * 
     * @param str the string to print
     */
    public void print(String str) {
        System.out.println(str);
    }

    /**
     * prints an error message with the color red
     * 
     * @param str the string to print
     */
    public void printErr(String str) {
        this.print(RED_TEXT + str + RESET_TEXT);
    }

    /**
     * prints the text for pick a letter
     */
    public void printPickLetter() {
        this.print(this.letterChooseText);
    }

    /**
     * prints a pick place message
     * 
     * @param letter the letter to place and print
     */
    public void printPickPlace(char letter) {
        this.print(this.placeLetterText.replace("#1", Character.toString(letter)));
    }

    /**
     * 
     * @param winners  a list of player that won
     * @param maxScore the maximum score
     * @return the string to print when displaying winners
     */
    public String getWinnerText(ArrayList<Player> winners, int maxScore) {
        String winnersNames = "";
        if (winners.size() == 1) {
            return this.winnerText.replace("#1", winners.get(0).getName()).replace("#2", Integer.toString(maxScore));
        } else {
            for (int i = 0; i < winners.size(); i++) {
                if (i == 0) {
                    winnersNames = winners.get(i).getName();
                } else {
                    winnersNames += ", " + winners.get(i).getName();
                }
            }
            return this.winnersText.replace("#1", winnersNames).replace("#2", Integer.toString(maxScore));
        }
    }

    /**
     * print the winners text from get winner text
     * 
     * @param winners  the player that won
     * @param maxScore the maximum score
     */
    public void printWinners(ArrayList<Player> winners, int maxScore) {
        this.print(this.getWinnerText(winners, maxScore));
    }

    /**
     * 
     * @param str the string to set as letter choose text
     */
    public void setLetterChooseText(String str) {
        this.letterChooseText = str;
    }

    /**
     * 
     * @param str the string to set as place letter text
     */
    public void setPlaceLetterText(String str) {
        this.placeLetterText = str;
    }

    /**
     * 
     * @param str the string to set as board size error text
     */
    public void setBoardSizeErrorText(String str) {
        this.boardSizeErrorText = str;
    }

    /**
     * 
     * @param str the string to set as winner text
     */
    public void setWinnerText(String str) {
        this.winnerText = str;
    }

    /**
     * 
     * @param str the string to set as winners text
     */
    public void setWinnersText(String str) {
        this.winnersText = str;
    }

    /**
     * prints the letter choose text
     */
    public void printLetterChooseText() {
        this.print(this.letterChooseText);
    }

    /**
     * prints the place letter text
     */
    public void printPlaceLetterText() {
        this.print(this.placeLetterText);
    }

    /**
     * prints the board size error text
     */
    public void printBoardSizeErrorText() {
        this.printErr(this.boardSizeErrorText);
    }

    /**
     * prints the winner text
     */
    public void printWinnerText() {
        this.print(this.winnerText);
    }

    /**
     * prints the winners text
     */
    public void printWinnersText() {
        this.print(this.winnersText);
    }
}
