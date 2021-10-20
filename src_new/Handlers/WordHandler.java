package Handlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

import View.GameView;

public class WordHandler {
    private HashSet<String> words;

    private static WordHandler INSTANCE;
    
    private WordHandler() {
        this.words = new HashSet<String>();
    }

    /**
     * creates an instance if no instance exits otherwise does nothing
     * @return the instance of this class
     */
    public static WordHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WordHandler();
        }
        return INSTANCE;
    }


    public void readFromFile(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath + "words.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                this.addWord(line.toUpperCase());
            }
            // System.out.println(this.words.size());
            bufferedReader.close();
        } catch(Exception e) {
            GameView.getInstance().printErr(e.getMessage());
        }
    }

    public static void main(String[] args) {
        WordHandler word = WordHandler.getInstance();
        word.readFromFile(System.getProperty("user.dir") + "\\src\\CollinsScrabbleWords2019.txt");
    }

    private void addWord(String word) {
        this.words.add(word);
    }

    public boolean wordExists(String word) {
        return this.words.contains(word);
    }
}
