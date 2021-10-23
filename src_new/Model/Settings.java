package Model;

import Boards.Errors.WrongBoardSizeException;
import Model.Errors.NumberOfBotsWrongException;
import Model.Errors.NumberOfPlayerWrongException;
import Model.Errors.PortNumberWrongException;

public class Settings {
    private static String language = "English";
    private static String ipAddress = "127.0.0.1";
    private static int rowSize = 3;
    private static int colSize = 3;
    private static int numOfPlayers = 1;
    private static int numOfBots = 1;
    private static int portNumber = 2048;

    public static String getLanguage() {
        return language;
    }
    public static void setLanguage(String language) {
        Settings.language = language;
    }
    public static String getIpAddress() {
        return ipAddress;
    }
    public static void setIpAddress(String ipAddress) {
        Settings.ipAddress = ipAddress;
    }
    public static int getRowSize() {
        return rowSize;
    }
    public static void setRowSize(int rowSize) throws WrongBoardSizeException {
        if (rowSize < 1) {
            throw new WrongBoardSizeException("Row value to small");
        } else if (rowSize > 9) {
            throw new WrongBoardSizeException("Row value to high");
        }
        Settings.rowSize = rowSize;
    }
    public static int getColSize() {
        return colSize;
    }
    public static void setColSize(int colSize) throws WrongBoardSizeException {
        if (colSize < 1) {
            throw new WrongBoardSizeException("Column value to small");
        } else if (colSize > 9) {
            throw new WrongBoardSizeException("Column value to high");
        }
        Settings.colSize = colSize;
    }
    public static int getNumOfPlayers() {
        return numOfPlayers;
    }
    public static void setNumOfPlayers(int numOfPlayers) throws NumberOfPlayerWrongException {
        if (numOfPlayers < 1) {
            throw new NumberOfPlayerWrongException("Number of players needs to be at least 1");
        }
        Settings.numOfPlayers = numOfPlayers;
    }
    public static int getNumOfBots() {
        return numOfBots;
    }
    public static void setNumOfBots(int numOfBots) throws NumberOfBotsWrongException {
        if (numOfBots < 0) {
            throw new NumberOfBotsWrongException("Number of bots needs to non negative");
        }
        Settings.numOfBots = numOfBots;
    }
    public static int getPortNumber() {
        return portNumber;
    }
    public static void setPortNumber(int portNumber) throws PortNumberWrongException {
        if (portNumber < 1024 || portNumber > 65536) {
            throw new PortNumberWrongException("Port needs to be between 1024 and 65536");
        }
        Settings.portNumber = portNumber;
    }

}