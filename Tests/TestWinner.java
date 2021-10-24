import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import Model.Game;
import Players.Player;

public class TestWinner {

    public Player[] getPlayers() {
        Player[] players = new Player[10];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(null, null);
        }
        return players;
    }

    public void setPointsSame(Player[] players) {
        for (int i = 0; i < players.length; i++) {
            players[i].setPoints(100);
        }
    }
    public void setPointsFor10(Player[] players) {
        players[0].setPoints(101);
        players[1].setPoints(99);
        players[2].setPoints(100);
        players[3].setPoints(50);
        players[4].setPoints(0);
        players[5].setPoints(-1);
        players[6].setPoints(13);
        players[7].setPoints(32);
        players[8].setPoints(7);
        players[9].setPoints(62);
    }
    
    @Test
    public void testWinners() {
        Game game = new Game();
        Player[] players = this.getPlayers();
        this.setPointsSame(players);
        ArrayList<Player> winners = game.getWinners(players, 100);
        for (int i = 0; i < winners.size(); i++) {
            assertEquals(players[i], winners.get(i));
        }
    }
    
    @Test
    public void testWinners2() {
        Game game = new Game();
        Player[] players = this.getPlayers();
        this.setPointsFor10(players);
        ArrayList<Player> winners = game.getWinners(players, 101);
        assertEquals(players[0], winners.get(0));
    }
}
