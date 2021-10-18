package View;

import java.io.FileReader;
import java.io.BufferedReader;

public class GameView {
    
    private String mainText;
    private String settingsText;
    private String[] settingOptionsText;

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
        System.out.println(parsedMainText);
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
        System.out.println(parsedMainText);
    }
    
    public void printSettingsOption(int index) {
        index -= 1; // removes 1 index since input is 1 - 5 and not 0 - 4
        if (index < 0 || index >= this.settingOptionsText.length) {
            throw new IndexOutOfBoundsException("Index for option text is out of bounds");
        }
        System.out.println(this.settingOptionsText[index]);
    }

    public void printWrongInput(String str) {
        System.out.println(str);
    }

    public void printPickLetterText() {
        System.out.println("Pick a letter");
    }

    
}
