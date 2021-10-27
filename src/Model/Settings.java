package Model;

import Boards.Exceptions.WrongBoardSizeException;
import Model.Exceptions.NumberOfBotsWrongException;
import Model.Exceptions.NumberOfPlayerWrongException;
import Model.Exceptions.PortNumberWrongException;

public class Settings {
    private static String language = "English";
    private static String ipAddress = "127.0.0.1";
    private static int rowSize = 3;
    private static int colSize = 3;
    private static int numOfPlayers = 1;
    private static int numOfBots = 1;
    private static int portNumber = 2048;

    private Settings() {
    }

    /**
     * 
     * @return what language the setting is at now
     */
    public static String getLanguage() {
        return language;
    }

    /**
     * 
     * @param language the new language to use
     */
    public static void setLanguage(String language) {
        Settings.language = language;
    }

    /**
     * 
     * @return what ip address the setting is at now
     */
    public static String getIpAddress() {
        return ipAddress;
    }

    /**
     * 
     * @param ipAddress the new ip address to set
     */
    public static void setIpAddress(String ipAddress) {
        Settings.ipAddress = ipAddress;
    }

    /**
     * 
     * @return what row size the setting is at now
     */
    public static int getRowSize() {
        return rowSize;
    }

    /**
     * 
     * @param rowSize the new row size to set
     * @throws WrongBoardSizeException if the size of the new row is less then 1 or
     *                                 larger then 9
     */
    public static void setRowSize(int rowSize) throws WrongBoardSizeException {
        if (rowSize < 1) {
            throw new WrongBoardSizeException("Row value to small");
        } else if (rowSize > 9) {
            throw new WrongBoardSizeException("Row value to high");
        }
        Settings.rowSize = rowSize;
    }

    /**
     * 
     * @return what column size the setting is at now
     */
    public static int getColSize() {
        return colSize;
    }

    /**
     * 
     * @param colSize the new column size to set
     * @throws WrongBoardSizeException if the size of the new column is less then 1
     *                                 or larger then 9
     */
    public static void setColSize(int colSize) throws WrongBoardSizeException {
        if (colSize < 1) {
            throw new WrongBoardSizeException("Column value to small");
        } else if (colSize > 9) {
            throw new WrongBoardSizeException("Column value to high");
        }
        Settings.colSize = colSize;
    }

    /**
     * 
     * @return what number of players the setting is at now
     */
    public static int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * 
     * @param numOfPlayers the new number of players to be set
     * @throws NumberOfPlayerWrongException if the new number of players is less
     *                                      then 1
     */
    public static void setNumOfPlayers(int numOfPlayers) throws NumberOfPlayerWrongException {
        if (numOfPlayers < 1) {
            throw new NumberOfPlayerWrongException("Number of players needs to be at least 1");
        }
        Settings.numOfPlayers = numOfPlayers;
    }

    /**
     * 
     * @return what number of bots the setting is at now
     */
    public static int getNumOfBots() {
        return numOfBots;
    }

    /**
     * 
     * @param numOfBots what the new number of bots to set
     * @throws NumberOfBotsWrongException if the new number of bots is less then 0
     */
    public static void setNumOfBots(int numOfBots) throws NumberOfBotsWrongException {
        if (numOfBots < 0) {
            throw new NumberOfBotsWrongException("Number of bots needs to non negative");
        }
        Settings.numOfBots = numOfBots;
    }

    /**
     * 
     * @return what port number the setting is at now
     */
    public static int getPortNumber() {
        return portNumber;
    }

    /**
     * 
     * @param portNumber the new port number to be set
     * @throws PortNumberWrongException if the port number is less then 1024 or
     *                                  larger then 65536
     */
    public static void setPortNumber(int portNumber) throws PortNumberWrongException {
        if (portNumber < 1024 || portNumber > 65536) {
            throw new PortNumberWrongException("Port needs to be between 1024 and 65536");
        }
        Settings.portNumber = portNumber;
    }
}
