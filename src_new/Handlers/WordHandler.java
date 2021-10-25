package Handlers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class WordHandler {
    private HashSet<String> words;

    private static WordHandler INSTANCE;

    private WordHandler() {
        this.words = new HashSet<String>();
    }

    /**
     * creates an instance if no instance exits otherwise returns the already
     * created instance
     * 
     * @return the instance of this class
     */
    public static WordHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WordHandler();
        }
        return INSTANCE;
    }

    /**
     * Read a file which should contain words, the wordHandler store these words
     * 
     * @param filePath filepath to words.txt
     * @throws FileNotFoundException if the file words.txt was not found or error
     *                               while reading it
     */
    public void readFromFile(String filePath) throws FileNotFoundException {
        this.words = new HashSet<String>();
        try {
            FileReader fileReader = new FileReader(filePath + "words.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                this.addWord(line.toUpperCase());
            }
            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            throw new FileNotFoundException("The file words.txt not found");
        }
    }

    /**
     * checks if the word exists and return true or false accordingly
     * 
     * @param word the word which you want to check exists
     * @return true if and only if the word exists, otherwise false
     */
    public boolean wordExists(String word) {
        return this.words.contains(word);
    }

    private void addWord(String word) {
        this.words.add(word);
    }
}
