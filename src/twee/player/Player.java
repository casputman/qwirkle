package twee.player;

import twee.Game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


public abstract class Player {

    private String name;
    private Mark mark;

    public Player(String name, Mark mark) {
        this.name = name;
        this.mark = mark;
    }

    public Player(Mark mark) {
        this.mark = mark;
        setName();
    }

    public void setName() {
        try {
            System.out.println("\nPlease enter your name:");
            this.name = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
        } catch (IOException e) {
            System.out.println("That didn't work...please try again.");
            setName();
        }
    }

    public String getName() {
        return name;
    }

    public Mark getMark() {
        return mark;
    }

    public abstract boolean determineMove(Game game);

    public abstract int determineMoveInt(String[] board, Mark mark);

    public static Player createPlayer(Mark mark) {
        Player newplayer = null;
        try {
            System.out.println("\nDo you want Player " + mark + " to be a Human Player?");
            String input = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
            if (input.contains("y")) {
                newplayer = new HumanPlayer(mark);
            } else if (input.contains("n")) {
                newplayer = new ComputerPlayer(mark);
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            System.out
                    .println("Do you want this player to be a Human Player? Just say yes or no.");
            createPlayer(mark);
        }
        return newplayer;
    }

    public static Player createPlayer(Mark mark, String name) {
        return new HumanPlayer(mark, name);
    }
}
