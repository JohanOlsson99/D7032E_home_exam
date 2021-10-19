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
                GameView.getInstance().print("Wrong input, cannot parse string to number");
                continue;
            }
        }
        if (inputInt < 0) {
            return 0;
        }
        return inputInt;
    }

    public GameController(String mainPath, String settingsPath) {
        this.parseMain(mainPath + "main.txt");
        this.parseSettings(settingsPath + "settings.txt");
    }

    public char pickLetter() {
        GameView view = GameView.getInstance();
        view.printPickLetter();
        return this.getInput().replace(" ", "").charAt(0);
    }

    public String pickPlace(char letter) {
        GameView view = GameView.getInstance();
        view.printPickPlace(Character.toUpperCase(letter));
        return this.getInput();
    }

    private void parseMain(String filePath) {
        String mainText = "";
        GameView gameView = GameView.getInstance();
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            Boolean atOptionsConfig = false;
            int index = 0;
            while((line = bufferedReader.readLine()) != null) {
                if (line.equals("#OPTIONS")) {
                    atOptionsConfig = true;
                    System.out.println("TEST");
                    continue;
                }
                if (atOptionsConfig) {
                    switch (index) {
                        case 0:
                            gameView.setLetterChooseText(line);
                            break;
                        case 1:
                            gameView.setPlaceLetterText(line);
                            break;
                        case 2:
                            gameView.setBoardSizeErrorText(line);
                            break;
                        case 3:
                            gameView.setWinnerText(line);
                            break;
                        case 4:
                            gameView.setWinnersText(line);
                            break;
                    }
                    index++;
                } else {
                    mainText += line + "\n";
                }
            }
            bufferedReader.close();
        } catch(Exception e) {
            gameView.print(e.getMessage());
        }
        gameView.setMainText(mainText);
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
            GameView.getInstance().print(e.getMessage());
        }
        GameView.getInstance().setSettingsText(settingsText, optionsStrings.toArray(new String[0]));
    }
}
