package twee;

import twee.player.Mark;
import twee.player.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;


public class Rules extends Observable {

    public static final List<Integer> VERTICAL = Collections.unmodifiableList(Arrays.asList(0, 1,
            2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20));
    public static final List<Integer> HORIZONTAL = Collections.unmodifiableList(Arrays.asList(0, 1,
            2, 3, 7, 8, 9, 10, 14, 15, 16, 17, 21, 22, 23, 24, 28, 29, 30, 31, 35, 36, 37, 38));
    public static final List<Integer> DIAGONAL_ONE = Collections.unmodifiableList(Arrays.asList(21,
            22, 23, 24, 28, 29, 30, 31, 35, 36, 37, 38));
    public static final List<Integer> DIAGONAL_TWO = Collections.unmodifiableList(Arrays.asList(24,
            25, 26, 27, 31, 32, 33, 34, 38, 39, 40, 41));

    private Board board;
    private Game game;
    private Mark[] fields;

    public Rules(Game game) {
        this.board = game.getBoard();
        this.game = game;
    }

    //@ pure
    public boolean isMoveAllowed(int slot) {
        boolean possible = isOpenSlot(slot);

        while (slot < 35 && possible) {
            slot = slot + Board.COLUMNS;
            possible = !isOpenSlot(slot);
        }

        return possible;
    }

    //@ pure
    private boolean isOpenSlot(int slot) {
        return board.getFields()[slot].equals(Mark.EMPTY);
    }

    //@ pure
    public boolean isGameOver() {
        fields = board.getFields();
        return isFull() || hasWinner();
    }

    private boolean isFull() {
        boolean draw = true;
        for (int i = 0; i < fields.length; i++) {
            if (isOpenSlot(i)) {
                draw = false;
            }
        }

        if (draw) {
            setChanged();
            notifyObservers("draw");
        }

        return draw;
    }

    //@ pure
    public boolean hasWinner() {
        return isWinner(game.one) || isWinner(game.two);
    }

    public boolean isWinner(Player current) {
        boolean winner = false;
        Mark mark = current.getMark();

        for (int i = 0; i <= 41; i++) {
            winner = containsVertical(i, mark) || containsHorizontal(i, mark)
                    || containsDiagonal(i, mark);
            if (winner) {
                break;
            }
        }

        if (winner) {
            setChanged();
            notifyObservers("won&s=" + current.getName());
        }

        return winner;
    }

    private boolean containsVertical(int telInt, Mark mark) {
        boolean trues = VERTICAL.contains(telInt) && fields[telInt].equals(mark) 
                && fields[telInt + 7].equals(mark) && fields[telInt + 14].equals(mark) 
                && fields[telInt + 21].equals(mark);
        return trues;
    }

    private boolean containsHorizontal(int telInt, Mark mark) {
        return HORIZONTAL.contains(telInt) && fields[telInt].equals(mark) 
                && fields[telInt + 1].equals(mark) && fields[telInt + 2].equals(mark)
                && fields[telInt + 3].equals(mark);
    }

    private boolean containsDiagonal(int telInt, Mark mark) {
        boolean diagonalOne = DIAGONAL_ONE.contains(telInt) && fields[telInt].equals(mark)
                && fields[telInt - 6].equals(mark) && fields[telInt - 12].equals(mark)
                && fields[telInt - 18].equals(mark);
        boolean diagonalTwo = DIAGONAL_TWO.contains(telInt) && fields[telInt].equals(mark)
                && fields[telInt - 8].equals(mark) && fields[telInt - 16].equals(mark)
                && fields[telInt - 24].equals(mark);
        return diagonalOne || diagonalTwo;
    }
}
