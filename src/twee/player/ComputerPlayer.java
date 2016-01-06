package twee.player;

import twee.Game;
import twee.player.Player;
import twee.strategy.Smart;
import twee.strategy.Strategy;
import twee.strategy.Stupid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ComputerPlayer extends Player implements Strategy {

    public static final List<String> NAMES = Collections.unmodifiableList(Arrays.asList(
            "Bolhuis", "Serbaas", "Samaritan", "Machine"
             + ""));
    private Strategy strategy;
    private String ai;
    private int waitTime = 1000;

    public ComputerPlayer(Mark mark) {
        
        super(NAMES.get((int) (System.currentTimeMillis() % NAMES.size())), mark);
        try {
            System.out.println("\nPlease choose an ai (either Smart or Stupid)");
            this.ai = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
        } catch (IOException e) {
            System.out.println("That didn't work...please try again.");
            setName();
        }
        if (ai.equalsIgnoreCase("smart")) {
            this.strategy = new Smart();
            System.out.println("\nPlease choose a waiting time (in seconds)");
            try {
                this.waitTime = Integer.parseInt(new BufferedReader(
                        new InputStreamReader(System.in)).readLine().trim()) * 1000;
            } catch (NumberFormatException e) {
                System.err.println("Please input a number, try again");
                new ComputerPlayer(mark);
            } catch (IOException e) {
                System.err.println("An error occured");
                new ComputerPlayer(mark);
            }
        } else if (ai.equalsIgnoreCase("stupid")) {
            this.strategy = new Stupid();
            System.out.println("\nPlease choose a waiting time (in seconds)");
            try {
                this.waitTime = Integer.parseInt(new BufferedReader(
                        new InputStreamReader(System.in)).readLine().trim()) * 1000;
            } catch (NumberFormatException e) {
                System.err.println("Please input a number, try again");
                new ComputerPlayer(mark);
            } catch (IOException e) {
                System.err.println("An error occured");
                new ComputerPlayer(mark);
            }
        } else {
            System.err.println("'" + ai + "' is not one of the possibilities");
            new ComputerPlayer(mark);
        }
    }

    public ComputerPlayer(Mark mark, String difficulty) {
        super(NAMES.get(mark.ordinal()), mark);
        if (difficulty.equalsIgnoreCase("smart")) {
            this.strategy = new Smart();
        } else {
            this.strategy = new Stupid();
        }
    }
    
    @Override
    public boolean determineMove(Game game) {
        String[] board = new String[42];
        Mark[] mark = game.getBoard().getFields();
        for (int i = 0; i < mark.length; i++) {
            if (mark[i].equals(Mark.YELLOW)) {
                board[i] = "YELLOW";
            } else if (mark[i].equals(Mark.RED)) {
                board[i] = "RED";
            } else {
                board[i] = "EMPTY";
            }
        }
        int column = this.determineMoveInt(board, game.current.getMark());
        if (game.takeTurn(column) && game.getRunning()) {
           determineMoveInt(board, game.current.getMark());
        }
        return true;
    }

    @Override
    public int determineMoveInt(String[] game, Mark mark) {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return strategy.determineMoveInt(game, mark);
    }
    
}
