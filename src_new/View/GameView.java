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

    private static GameView INSTANCE = null;

    private GameView() { }

    public static GameView getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameView();
        }
        return INSTANCE;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public void printMainText(int boardRow, int boardCol, String language, int numOfPlayers, int numOfBots) {
        String parsedMainText = this.mainText;
        parsedMainText = parsedMainText.replace("#BoardSize", boardRow + "/" + boardCol);
        parsedMainText = parsedMainText.replace("#Language", language);
        parsedMainText = parsedMainText.replace("#NumOfPlayers", Integer.toString(numOfPlayers));
        parsedMainText = parsedMainText.replace("#NumOfBots", Integer.toString(numOfBots));
        print(parsedMainText);
    }

    public void setSettingsText(String settingsText, String[] settingOptionsText) {
        this.settingsText = settingsText;
        this.settingOptionsText = settingOptionsText;
    }

    public void printSettingsText(int boardRow, int boardCol, String language, int numOfPlayers, int numOfBots) {
        String parsedMainText = this.settingsText;
        parsedMainText = parsedMainText.replace("#BoardSize", boardRow + "/" + boardCol);
        parsedMainText = parsedMainText.replace("#Language", language);
        parsedMainText = parsedMainText.replace("#NumOfPlayers", Integer.toString(numOfPlayers));
        parsedMainText = parsedMainText.replace("#NumOfBots", Integer.toString(numOfBots));
        print(parsedMainText);
    }
    
    public void printSettingsOption(int index) {
        index -= 1; // removes 1 index since input is 1 - 5 and not 0 - 4
        if (index < 0 || index >= this.settingOptionsText.length) {
            throw new IndexOutOfBoundsException("Index for option text is out of bounds");
        }
        print(this.settingOptionsText[index]);
    }

    public void print(String str) {
        System.out.println(str);
    }

    public void printPickLetter() {
        this.print(this.letterChooseText);
    }
    
    public void printPickPlace(char letter) {
        this.print(this.placeLetterText.replace("#1", Character.toString(letter)));
    }

    public void printWinners(ArrayList<Player> winners, int maxScore) {
        String winnersNames = "";
        if (winners.size() == 1) {
            this.print(this.winnerText
                .replace("#1", winners.get(0).getName())
                .replace("#2", Integer.toString(maxScore)));
            // this.print("The winner is: " + winners.get(0).getName() + ", with a score of " + maxScore);
        } else {
            for (int i = 0; i < winners.size(); i++) {
                if (i == 0) {
                    winnersNames = winners.get(i).getName();
                } else {
                    winnersNames += ", " + winners.get(i).getName();
                }
            }
            this.print(this.winnersText
                .replace("#1", winnersNames)
                .replace("#2", Integer.toString(maxScore)));
            // this.print("the winners are: " + winnersName + ", with a score of " + maxScore);
        }
    }


    public void setLetterChooseText(String str) {
        this.letterChooseText = str;
    }

    public void setPlaceLetterText(String str) {
        this.placeLetterText = str;
    }

    public void setBoardSizeErrorText(String str) {
        this.boardSizeErrorText = str;
    }
    
    public void setWinnerText(String str) {
        this.winnerText = str;
    }

    public void setWinnersText(String str) {
        this.winnersText = str;
    }
    
    public void printLetterChooseText() {
        this.print(this.letterChooseText);
    }

    public void printPlaceLetterText() {
        this.print(this.placeLetterText);
    }

    public void printBoardSizeErrorText() {
        this.print(this.boardSizeErrorText);
    }
    
    public void printWinnerText() {
        this.print(this.winnerText);
    }

    public void printWinnersText() {
        this.print(this.winnersText);
    }

    
}
