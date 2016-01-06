package twee;

import twee.player.Mark;

import java.util.Observable;

public class Board extends Observable {

    public static final int COLUMNS = 7;
    public static final int ROWS = 6;

    private Mark[] fields;

    public static final String TOP = "| 1 | 2 | 3 | 4 | 5 | 6 | 7 |" + "\n";
    public static final String SEPERATION = "-----------------------------" + "\n";

    public Board() {
        fields = new Mark[COLUMNS * ROWS];
        reset();
    }

    public Board(String[] board) {
        this();
        for (int i = 0; i < board.length; i++) {
            board[i] = board[i].replace("Mark.", "").replace("MARK.", "");
            if (board[i].equals("YELLOW")) {
                fields[i] = Mark.YELLOW;
            } else if (board[i].equals("RED")) {
                fields[i] = Mark.RED;
            } else {
                fields[i] = Mark.EMPTY;
            }
        }
    }

    public boolean makeMove(int column, Rules rules, Mark mark) {
        boolean movemade = true;

        for (int row = 0; row < Board.ROWS; row++) {
            int slot = row * Board.COLUMNS + column - 1;

            if (rules.isMoveAllowed(slot)) {
                fields[slot] = mark;
                System.out.println(mark.name() + " put a mark in column: " + column);
                setChanged();

                if (!rules.isGameOver()) {
                    notifyObservers("move");
                }
            } else {
                movemade = false;
            }
        }

        return movemade;
    }

    public void printBoard() {
        System.out.println(this.toString());
    }

    public Mark[] getFields() {
        return fields;
    }

    public String toString() {
        String toReturn = "\n";
        toReturn = toReturn + SEPERATION + TOP + SEPERATION;
        int telInt = 0;

        while (telInt < ROWS) {
            toReturn = toReturn + "|";
            for (int j = 0; j < COLUMNS; j++) {
                toReturn = toReturn
                        + this.fields[(telInt * COLUMNS + j)].toString().replace("EMPTY", "   ")
                                .replace("YELLOW", "YEL") + "|";
            }
            toReturn = toReturn + "\n";
            telInt++;
        }

        return toReturn + SEPERATION;
    }

    public void reset() {
        for (int slot = 0; slot < fields.length; slot++) {
            fields[slot] = Mark.EMPTY;
        }
    }

    public static int getColumn(int decision) {
        int column = 0;

        column = (decision + 1) % 7;

        if (column == 0) {
            column = 7;
        }

        return column;
    }
}
