package Handlers;

import java.util.HashMap;
import java.io.*;

public class LetterValueHandler {
    private HashMap<Character, Integer> letterValues;
    private static LetterValueHandler INSTANCE;
    
    private LetterValueHandler() {
        this.letterValues = new HashMap<Character, Integer>();
    }

    /**
     * creates an instance if no instance exits otherwise does nothing
     * @return the instance of this class
     */
    public static LetterValueHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LetterValueHandler();
        }
        return INSTANCE;
    }

    /**
     * 
     * @param filePath file path to the letter values
     */
    public void readFromFile(String filePath) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(":");
                String letters = data[0].replace(" ", "");
                for (int i = 0; i < letters.length(); i++) {
                    this.setValue(letters.charAt(i), Integer.parseInt(data[1].replace(" ", "")));
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LetterValueHandler hand = LetterValueHandler.getInstance();
        hand.readFromFile(System.getProperty("user.dir") + "\\data\\letter.txt");
        System.out.println(hand.letterValues.toString());
    }

    /**
     * 
     * @param letter Char to get value for
     * @return Returns the value for a char, if char not found, returns 0
     */
    public int getValue(char letter) {
        letter = Character.toUpperCase(letter);
        int value;
        try {
            value = this.letterValues.get(letter);
        } catch (NullPointerException e){
            value = 0;
        }
        return value;
    }

    /**
     * 
     * @param key Character to be stored in a hashmap
     * @param value Value which the character gives as points
     */
    public void setValue(char key, int value) {
        this.letterValues.put(Character.toUpperCase(key), value);
    }
    
}
