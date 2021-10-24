package Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import View.GameView;

public class GameController {
    private Scanner scanner;

    /**
     * creates an instance of gameController and creates a scanner for the class,
     * read files for the main text and settings text
     * 
     * @param mainPath     the path to main.txt
     * @param settingsPath the path to settings.txt
     */
    public GameController(String mainPath, String settingsPath) {
        this.parseMain(mainPath + "main.txt");
        this.parseSettings(settingsPath + "settings.txt");
        this.scanner = new Scanner(System.in);
    }

    /**
     * Listens util an input from the player is given
     * 
     * @return the input as a string
     */
    public String getInput() {
        return this.scanner.nextLine();
    }

    /**
     * Listens util an input from the user is given, if the input cannot be parsed
     * to a number continue listen
     * 
     * @return an int for the value the player typed in
     */
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

    /**
     * used to get a char out of an input, prints a message to the player so they
     * know what to do
     * 
     * @return a char from an input, removes the spaces and takes the first
     *         character for the input
     */
    public char pickLetter() {
        GameView view = GameView.getInstance();
        view.printPickLetter();
        return this.getInput().replace(" ", "").charAt(0);
    }

    /**
     * used to get a string of an input, prints a message to the player so they know
     * what to do
     * 
     * @param letter used to display what letter to place on the board
     * @return a string for which should be parse to a place
     */
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
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("#OPTIONS")) {
                    atOptionsConfig = true;
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
        } catch (Exception e) {
            gameView.printErr(e.getMessage());
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
            while ((line = bufferedReader.readLine()) != null) {
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
        } catch (Exception e) {
            GameView.getInstance().printErr(e.getMessage());
        }
        GameView.getInstance().setSettingsText(settingsText, optionsStrings.toArray(new String[0]));
    }

    /**
     * passes the string to the gameView print
     * 
     * @param str a string to print
     */
    public void print(String str) {
        GameView.getInstance().print(str);
    }

    /**
     * passes the string to the gameView printErr
     * 
     * @param str a string to print
     */
    public void printErr(String str) {
        GameView.getInstance().printErr(str);
    }
}
