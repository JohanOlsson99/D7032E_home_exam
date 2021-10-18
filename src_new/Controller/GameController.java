package Controller;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import View.GameView;

public class GameController {
    Scanner scanner = new Scanner(System.in);
    
    public String getInput() {
        return this.scanner.nextLine();
    }

    public int getInputInt() {
        String input = this.getInput();
        if (input.equals("!")) {
            return -1;
        }
        int inputInt;
        while (true) {
            try {
                inputInt = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                GameView.getInstance().printWrongInput("Wrong input, cannot parse string to number");
                continue;
            }
        }
        if (inputInt < 0) {
            return 0;
        }
        return inputInt;
    }

    public GameController(String mainPath, String settingsPath) {
        this.parseMain(mainPath);
        this.parseSettings(settingsPath);
    }

    public char pickLetter() {
        GameView view = GameView.getInstance();
        view.printPickLetterText();
        return this.getInput().replace(" ", "").charAt(0);
    }

    private void parseMain(String filePath) {
        String mainText = "";
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                mainText += line + "\n";
            }
            bufferedReader.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        GameView.getInstance().setMainText(mainText);
    }
    
    private void parseSettings(String filePath) {
        String settingsText = "";
        ArrayList<String> optionsStrings = new ArrayList<String>();
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            Boolean atOptionsConfig = false;
            while((line = bufferedReader.readLine()) != null) {
                if (line.equals("#OPTIONS")) {
                    atOptionsConfig = true;
                    continue;
                }
                if (!atOptionsConfig) {
                    settingsText += line + "\n";
                } else {
                    optionsStrings.add(line);
                }
            }
            bufferedReader.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        GameView.getInstance().setSettingsText(settingsText, optionsStrings.toArray(new String[0]));
    }
}
