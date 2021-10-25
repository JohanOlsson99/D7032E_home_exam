import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import Handlers.LetterValueHandler;


public class TestLetterHandler {
    private LetterValueHandler letterValueHandler;

    @Before
    public void init() throws FileNotFoundException {
        this.letterValueHandler = LetterValueHandler.getInstance();
        this.letterValueHandler.readFromFile(TestBoardHandler.path);
    }

    @Test
    public void test_get_value() {
        assertEquals(2, this.letterValueHandler.getValue('A'));
        assertEquals(2, this.letterValueHandler.getValue('L'));
        assertEquals(2, this.letterValueHandler.getValue('K'));
        assertEquals(2, this.letterValueHandler.getValue('F'));

        assertEquals(3, this.letterValueHandler.getValue('B'));
        assertEquals(4, this.letterValueHandler.getValue('c'));
        // D is defined twice so latest is used
        assertEquals(5, this.letterValueHandler.getValue('D'));
        assertEquals(6, this.letterValueHandler.getValue('E'));

        assertEquals(0, this.letterValueHandler.getValue('M'));
        assertEquals(0, this.letterValueHandler.getValue('N'));
        assertEquals(0, this.letterValueHandler.getValue('}'));
    }
}
