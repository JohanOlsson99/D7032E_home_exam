package Handlers;

import java.util.HashMap;
import java.io.*;

public class LetterValueHandler {
    private HashMap<Character, Integer> letterValues;

    public LetterValueHandler() {
        this.letterValues = new HashMap<Character, Integer>();
    }

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
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LetterValueHandler hand = new LetterValueHandler();
        hand.readFromFile(System.getProperty("user.dir") + "\\data\\letter.txt");
        System.out.println(hand.letterValues.toString());
    }

    /**
     * 
     * @param letter char to get value for
     * @return returns the value for a char, if char not found, returns 0
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

    public void setValue(char key, int value) {
        System.out.println("key: " + key + " value: " + value);
        this.letterValues.put(key, value);
    }
    
}
