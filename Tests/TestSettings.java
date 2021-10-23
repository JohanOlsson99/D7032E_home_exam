import static org.junit.Assert.*;

import org.junit.Test;

import Boards.Errors.WrongBoardSizeException;
import Model.Settings;
import Model.Errors.NumberOfBotsWrongException;
import Model.Errors.NumberOfPlayerWrongException;

public class TestSettings {

    @Test(expected = NumberOfPlayerWrongException.class)
    public void testNumberOfPlayers() throws NumberOfPlayerWrongException {
        Settings.setNumOfPlayers(0);
    }
    
    @Test(expected = NumberOfPlayerWrongException.class)
    public void testNumberOfPlayers2() throws NumberOfPlayerWrongException {
        Settings.setNumOfPlayers(-1);
    }
    
    @Test()
    public void testNumberOfPlayers3() throws NumberOfPlayerWrongException {
        Settings.setNumOfPlayers(1);
        assertEquals(1, Settings.getNumOfPlayers());
        Settings.setNumOfPlayers(10);
        assertEquals(10, Settings.getNumOfPlayers());
    }
    
    @Test(expected = NumberOfBotsWrongException.class)
    public void testNumberOfBots() throws NumberOfBotsWrongException {
        Settings.setNumOfBots(-1);
    }
    
    @Test
    public void testNumberOfBots2() throws NumberOfBotsWrongException {
        Settings.setNumOfBots(0);
        assertEquals(0, Settings.getNumOfBots());
        Settings.setNumOfBots(1);
        assertEquals(1, Settings.getNumOfBots());
    }

    @Test
    public void testBoardRow() throws WrongBoardSizeException {
        Settings.setRowSize(1);
        assertEquals(1, Settings.getRowSize());
        Settings.setRowSize(2);
        assertEquals(2, Settings.getRowSize());
        Settings.setRowSize(8);
        assertEquals(8, Settings.getRowSize());
        Settings.setRowSize(9);
        assertEquals(9, Settings.getRowSize());
    }
    
    @Test(expected = WrongBoardSizeException.class)
    public void testBoardRowError() throws WrongBoardSizeException {
        Settings.setRowSize(0);
    }

    @Test(expected = WrongBoardSizeException.class)
    public void testBoardRowError2() throws WrongBoardSizeException {
        Settings.setRowSize(10);
    }
    
    @Test
    public void testBoardCol() throws WrongBoardSizeException {
        Settings.setColSize(1);
        assertEquals(1, Settings.getColSize());
        Settings.setColSize(2);
        assertEquals(2, Settings.getColSize());
        Settings.setColSize(8);
        assertEquals(8, Settings.getColSize());
        Settings.setColSize(9);
        assertEquals(9, Settings.getColSize());
    }
    
    @Test(expected = WrongBoardSizeException.class)
    public void testBoardColError() throws WrongBoardSizeException {
        Settings.setColSize(0);
    }

    @Test(expected = WrongBoardSizeException.class)
    public void testBoardColError2() throws WrongBoardSizeException {
        Settings.setColSize(10);
    }
    
}
