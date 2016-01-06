package twee.tui;

import twee.Game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class GameTUIView implements GameView, Observer {

    private Game game;
    private int telInt;

    public GameTUIView(Game game) {
        System.out
                .println("\n----------------------------------------\n"
                        + "--------------CONNECT FOUR--------------\n"
                        + "----------------------------------------");
        this.game = game;
    }

    public void start() {
        try {
            for (telInt = 1; telInt >= 1; telInt++) {
                BufferedReader answer = new BufferedReader(new InputStreamReader(System.in));
                String input = answer.readLine();
                checkInput(input.trim().toUpperCase());
            }
        } catch (IOException e) {
            System.out.println("I failed");
        }
    }

    public void stop() {
        game.stopGame();
    }

    public void reset() {
        game.reset();
    }

    public void showBoard() {
        game.getBoard().printBoard();
    }

    public void checkInput(String input) {
        if (input.startsWith("RESTART")) {
            if (!game.getRunning()) {
                this.reset();
            } else {
                System.out
                        .println("\nA game is already running.\n"
                                + "You can only restart a game when you've finished one.\n");
            } 
        } else if (input.equalsIgnoreCase("EXIT")) {
            telInt = -1;
            System.err.println("\n'Connect Four' terminated.\n");
        } else {
            System.err
                    .println("\n-----------------------------------\n"
                            + "YOU CAN ONLY GIVE THESE COMMANDS:\n\nx (where 'x' equals a column)\n"
                            + "SLOT x (where 'x' equals a column)\nSHOW BOARD\n"
                            + "RESTART (when a game has finished)\nEXIT"
                            + "\n-----------------------------------\n");
        }
    }

    @Override
    public void update(Observable observable, Object arg) {
        String arguments = arg.toString();

        if (arguments.startsWith("won")) {
            this.stop();
            showBoard();
            System.out.println(arguments.replace("won&s=", "") + " has won the game.\n");
        } else if (arguments.startsWith("draw")) {
            this.stop();
            showBoard();
            System.out.println("The game has ended in a draw.\n");
        } else if (arg.equals("move")) {
            showBoard();
            game.nextPlayer();
        } else {
            System.out.println("Something went wrong here...\n");
        }
    }
}
