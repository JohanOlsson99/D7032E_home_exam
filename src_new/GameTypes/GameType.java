package GameTypes;
import Boards.Board;

import java.util.Random;

public class GameType {
    public static void main(String[] args) {
        GameType gameType = new GameType();
        
    }
    
    private boolean showPoints;
    private Random random = new Random();

    public GameType() {
        this.showPoints = false;
    }

    public int getRandomStartPlayer(int length) {
        return this.random.nextInt(length);
    }

    public int calculatePoints(Board board) {
        return 1;
    }

    public boolean showPoints() {
        return this.showPoints;
    }
}
