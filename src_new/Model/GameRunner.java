package Model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import Controller.GameController;
import GameTypes.GameType;
import Players.Bot;
import Players.OnlinePlayer;
import Players.Player;
import Players.Errors.PlayerDisconnectedException;
import View.GameView;

public class GameRunner {
    private Player[] players;
    private Player thisPlayer;
    private GameController gameController;
    private GameView gameView;
    private GameType gameType;

    /**
     * instantiate the class with all it's parameters
     * 
     * @param players        all players that are in the game
     * @param thisPlayer     the player that are on this computer
     * @param gameType       the game type
     * @param gameController the game controller for this player
     */
    public GameRunner(Player[] players, Player thisPlayer, GameType gameType, GameController gameController) {
        this.players = players;
        this.thisPlayer = thisPlayer;
        this.gameType = gameType;
        this.gameView = GameView.getInstance();
        this.gameController = gameController;
    }

    /**
     * Runs the game sequentially, decide which player to place and pick letter.
     * 
     * @throws PlayerDisconnectedException if the player has disconnected
     */
    public void run() throws PlayerDisconnectedException {
        int turn = 0;
        int index = this.gameType.getRandomStartPlayer(this.players.length);
        while (turn < (Settings.getRowSize() * Settings.getColSize())) {
            index = index % this.players.length;
            if (this.thisPlayer != null) {
                this.gameView.print(this.thisPlayer.getBoard().toString(this.gameType.showPoints(),
                        this.gameType.showMultiplyPoints()));
            }
            char letter = '\0';
            if (this.players[index] instanceof OnlinePlayer) {
                letter = ((OnlinePlayer) this.players[index]).pickLetter();
            } else if (this.players[index] instanceof Bot) {
                letter = ((Bot) this.players[index]).pickLetter();
            } else if (this.players[index] instanceof Player) {
                letter = this.players[index].pickLetter(gameController);
            }
            this.placeLetterForAllPlayers(letter);
            turn++;
            index++;
        }
        if (this.thisPlayer != null) {
            this.gameView.print(this.thisPlayer.getBoard().toString(this.gameType.showPoints(),
                    this.gameType.showMultiplyPoints()));
        }
    }

    /**
     * 
     * @return the players
     */
    public Player[] getPlayers() {
        return this.players;
    }

    /**
     * Runs the game sequentially, gets from the server what this player are suppose
     * to do.
     * 
     * @throws PlayerDisconnectedException if you have disconnected from the server
     */
    public void runOnline() throws PlayerDisconnectedException {
        while (true) {
            String message;
            message = (String) this.thisPlayer.getNextMessage();
            if (message.contains(Player.pickLetterOnline)) {
                // pick letter
                char letter = this.thisPlayer.pickLetter(this.gameController);
                this.thisPlayer.sendMessage(letter);
            } else if (message.contains(Player.placeLetterOnline)) {
                char letter = message.split(":")[1].replaceAll(" ", "").charAt(0);
                String place = this.thisPlayer.placeLetter(letter, this.gameController);
                this.thisPlayer.sendMessage(place);
                this.gameView.print(this.thisPlayer.getBoard().toString(this.gameType.showPoints(),
                        this.gameType.showMultiplyPoints()));
            } else if (message.contains(Player.winnerMessageOnline)) {
                String winMsg = message.replace(Player.winnerMessageOnline, "");
                winMsg = winMsg.replace(this.thisPlayer.getName(), "You");
                winMsg = winMsg.replaceAll("Player", "Online player");
                this.gameView.print(winMsg);
                break;
            }
        }
        this.thisPlayer.closeConnection();
        // this.mainMenu();
    }

    private void placeLetterForAllPlayers(char letter) {
        ExecutorService threadpool = Executors.newFixedThreadPool(this.players.length);
        for (Player player : this.players) {
            Runnable task = null;
            if (player instanceof Player && player == this.thisPlayer) {
                task = new Runnable() {
                    @Override
                    public void run() {
                        player.placeLetter(letter, gameController);
                    }
                };
            } else if (player instanceof Bot) {
                task = new Runnable() {
                    @Override
                    public void run() {
                        ((Bot) player).placeLetter(letter, gameController);
                    }
                };
            } else if (player instanceof OnlinePlayer) {
                task = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ((OnlinePlayer) player).placeLetter(letter);
                        } catch (PlayerDisconnectedException e) {
                            threadpool.shutdownNow();
                            return;
                        }
                    }
                };
            }
            try {
                threadpool.execute(task);
            } catch (RejectedExecutionException e) {
                continue;
            }
        }

        threadpool.shutdown();
        // wait for all the answers to come in
        while (!threadpool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }

}
