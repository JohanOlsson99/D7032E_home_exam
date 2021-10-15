import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Handlers.LetterValueHandler;


public class TestLetterHandler {
    private LetterValueHandler letterValueHandler;

    @Before
    public void init() {
        this.letterValueHandler = LetterValueHandler.getInstance();
        this.letterValueHandler.setValue('A', 1);
        this.letterValueHandler.setValue('D', 2);
        this.letterValueHandler.setValue('b', 3);
        this.letterValueHandler.setValue('f', 4);
        this.letterValueHandler.setValue('k', 5);
        this.letterValueHandler.setValue('x', 8);
        this.letterValueHandler.setValue('z', 10);
    }

    @Test
    public void test_get_value() {
        assertEquals(1, this.letterValueHandler.getValue('A'));
        assertEquals(2, this.letterValueHandler.getValue('d'));
        assertEquals(3, this.letterValueHandler.getValue('B'));
        assertEquals(4, this.letterValueHandler.getValue('F'));
        assertEquals(5, this.letterValueHandler.getValue('K'));
        assertEquals(8, this.letterValueHandler.getValue('X'));
        assertEquals(10, this.letterValueHandler.getValue('Z'));
    }

    @Test
    public void test_set_already_defined() {
        this.letterValueHandler.setValue('A', 2);
        this.letterValueHandler.setValue('D', 3);
        this.letterValueHandler.setValue('b', 4);
        this.letterValueHandler.setValue('f', 5);
        this.letterValueHandler.setValue('k', 6);
        this.letterValueHandler.setValue('x', 9);
        this.letterValueHandler.setValue('z', 11);
        
        assertEquals(2, this.letterValueHandler.getValue('A'));
        assertEquals(3, this.letterValueHandler.getValue('d'));
        assertEquals(4, this.letterValueHandler.getValue('B'));
        assertEquals(5, this.letterValueHandler.getValue('F'));
        assertEquals(6, this.letterValueHandler.getValue('K'));
        assertEquals(9, this.letterValueHandler.getValue('X'));
        assertEquals(11, this.letterValueHandler.getValue('Z'));
    }
}
