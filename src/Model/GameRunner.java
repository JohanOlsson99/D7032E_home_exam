package Model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import Controller.GameController;
import GameTypes.GameType;
import Players.Bot;
import Players.OnlinePlayer;
import Players.Player;
import Players.Exceptions.PlayerDisconnectedException;
import View.GameView;

public class GameRunner {
    private GameController gameController;
    private GameView gameView;
    private GameType gameType;

    /**
     * instantiate the class with all it's parameters
     * 
     * @param gameType       the game type
     * @param gameController the game controller for this player
     */
    public GameRunner(GameType gameType, GameController gameController) {
        this.gameType = gameType;
        this.gameView = GameView.getInstance();
        this.gameController = gameController;
    }

    /**
     * Runs the game sequentially, decide which player to place and pick letter.
     * 
     * @param players    all players that are in the game
     * @param thisPlayer the player that are on this computer
     * @throws PlayerDisconnectedException if the player has disconnected
     */
    public void run(Player[] players, Player thisPlayer) throws PlayerDisconnectedException {
        int turn = 0;
        int index = this.gameType.getRandomStartPlayer(players.length);
        while (turn < (Settings.getRowSize() * Settings.getColSize())) {
            index = index % players.length;
            if (thisPlayer != null) {
                this.gameView.print(
                        thisPlayer.getBoard().toString(this.gameType.showPoints(), this.gameType.showMultiplyPoints()));
            }
            char letter = '\0';
            if (players[index] instanceof OnlinePlayer) {
                letter = ((OnlinePlayer) players[index]).pickLetter();
            } else if (players[index] instanceof Bot) {
                letter = ((Bot) players[index]).pickLetter();
            } else if (players[index] instanceof Player) {
                letter = players[index].pickLetter(gameController);
            }
            this.placeLetterForAllPlayers(letter, players, thisPlayer);
            turn++;
            index++;
        }
        if (thisPlayer != null) {
            this.gameView.print(
                    thisPlayer.getBoard().toString(this.gameType.showPoints(), this.gameType.showMultiplyPoints()));
        }
    }

    /**
     * Runs the game sequentially, gets from the server what this player are suppose
     * to do.
     * 
     * @param player the player that are on this computer
     * @throws PlayerDisconnectedException if you have disconnected from the server
     */
    public void runOnline(Player player) throws PlayerDisconnectedException {
        while (true) {
            String message;
            message = (String) player.getNextMessage();
            if (message.contains(Player.PICK_LETTER_ONLINE)) {
                // pick letter
                char letter = player.pickLetter(this.gameController);
                player.sendMessage(letter);
            } else if (message.contains(Player.PLACE_LETTER_ONLINE)) {
                char letter = message.split(":")[1].replaceAll(" ", "").charAt(0);
                String place = player.placeLetter(letter, this.gameController);
                player.sendMessage(place);
                this.gameView.print(
                        player.getBoard().toString(this.gameType.showPoints(), this.gameType.showMultiplyPoints()));
            } else if (message.contains(Player.WINNER_MESSAGE_ONLINE)) {
                String winMsg = message.replace(Player.WINNER_MESSAGE_ONLINE, "");
                winMsg = winMsg.replace(player.getName(), "You");
                winMsg = winMsg.replaceAll("Player", "Online player");
                this.gameView.print(winMsg);
                break;
            }
        }
        player.closeConnection();
    }

    private void placeLetterForAllPlayers(char letter, Player[] players, Player thisPlayer) {
        ExecutorService threadpool = Executors.newFixedThreadPool(players.length);
        for (Player player : players) {
            Runnable task = null;
            if (player instanceof Player && player == thisPlayer) {
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
