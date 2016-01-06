package twee.strategy;

import twee.Board;
import twee.Rules;
import twee.player.Mark;

/**
 * 
 * @author Wouter Bolhuis & Sebastiaan den Boer
 * @version 1.4
 */
public class Smart implements Strategy {

    private /*@ spec_public @*/ Mark[] marks = new Mark[42];
    private Board tempBoard;
    private String[] tempBoardArray = new String[42];

    //@ ensures \result > 0 && \result < 8;
    @Override
    public int determineMoveInt(String[] board, Mark mark) {
        int result = 0;

        reset(board);
        // our win?
        int ourWin = moveLoop(mark, board);
        int theirWin = moveLoop(swapMark(mark), board);
        if (ourWin > -1) {
            result = ourWin;
        // enemy win?
        } else if (theirWin > -1) {
            result = theirWin;
            
        // checking for enemy win
        // middle
        } else if (placeInColumn(3) && !win(board, 3, mark)) {
            result = 3;
        // corners
        } else if (marks[41].equals(Mark.EMPTY) && !win(board, 6, mark)) {
            result = 6;
        } else if (marks[35].equals(Mark.EMPTY) && !win(board, 0, mark)) {
            result = 0;
        } else if (!marks[35].equals(Mark.EMPTY) && !marks[28].equals(Mark.EMPTY)
                && !marks[21].equals(Mark.EMPTY) && !marks[14].equals(Mark.EMPTY)
                && !marks[7].equals(Mark.EMPTY) && marks[0].equals(Mark.EMPTY)
                && !win(board, 0, mark)) {
            result = 0;
        } else if (!marks[41].equals(Mark.EMPTY) && !marks[34].equals(Mark.EMPTY)
                && !marks[27].equals(Mark.EMPTY) && !marks[20].equals(Mark.EMPTY)
                && !marks[13].equals(Mark.EMPTY) && marks[6].equals(Mark.EMPTY)  
                && !win(board, 6, mark)) {
            result = 6;
        // first columns closest to the middle
        } else if (placeInColumn(2) && !win(board, 2, mark)) {
            result = 2;
        } else if (placeInColumn(4) && !win(board, 4, mark)) {
            result = 4;
        } else if (placeInColumn(1) && !win(board, 1, mark)) {
            result = 1;
        } else if (!win(board, 5, mark) && placeInColumn(5)) {
            result = 5;
        } else if (placeInColumn(0) && !win(board, 0, mark)) {
            result = 0;
        } else if (placeInColumn(6) && !win(board, 6, mark)) {
            result = 6;
        
        // not checking for enemy win 
        // middle
        } else if (placeInColumn(3)) {
            result = 3;
        // corners
        } else if (marks[41].equals(Mark.EMPTY)) {
            result = 6;
        } else if (marks[35].equals(Mark.EMPTY)) {
            result = 0;
        } else if (!marks[35].equals(Mark.EMPTY) && !marks[28].equals(Mark.EMPTY)
                && !marks[21].equals(Mark.EMPTY) && !marks[14].equals(Mark.EMPTY)
                && !marks[7].equals(Mark.EMPTY) && marks[0].equals(Mark.EMPTY)) {
            result = 0;
        } else if (!marks[41].equals(Mark.EMPTY) && !marks[34].equals(Mark.EMPTY)
                && !marks[27].equals(Mark.EMPTY) && !marks[20].equals(Mark.EMPTY)
                && !marks[13].equals(Mark.EMPTY) && marks[6].equals(Mark.EMPTY)) {
            result = 6;
        // first columns closest to the middle
        } else if (placeInColumn(2)) {
            result = 2;
        } else if (placeInColumn(4)) {
            result = 4;
        } else if (placeInColumn(1)) {
            result = 1;
        } else if (placeInColumn(5)) {
            result = 5;
        } else if (placeInColumn(0)) {
            result = 0;
        } else {
            result = 6;
        }

        return result + 1;
    }

    private void reset(String[] board) {
        tempBoardArray = board;
        tempBoard = new Board(board);
        marks = tempBoard.getFields();
    }

    //@ requires marks[column] == Mark.EMPTY;
    //@ ensures \result == true;
    //@ also
    //@ requires marks[column] != Mark.EMPTY;
    //@ ensures \result == false;
    // check if there is still space left in the column
    public boolean placeInColumn(int column) {
        if (marks[column].equals(Mark.EMPTY)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isWinner(Mark mark) {
        boolean winner = false;

        for (int i = 0; i <= 41; i++) {
            winner = containsVertical(i, mark) || containsHorizontal(i, mark)
                    || containsDiagonal(i, mark);
            if (winner) {
                break;
            }
        }
        return winner;
    }

    //@ requires board.length == 42;
    private int moveLoop(Mark mark, String[] board) {
        int move = -1;
        for (int i = 0; i < 7; i++) {
            if (tempBoardArray[i].equals("EMPTY")) {
                marks[gravity(i)] = mark;
            }
            if (isWinner(mark)) {
                move = i;
                break;
            }
            reset(board);
        }
        return move;
    }

    private boolean containsVertical(int telInt, Mark mark) {
        boolean trues = Rules.VERTICAL.contains(telInt) && marks[telInt].equals(mark)
                && marks[telInt + 7].equals(mark) && marks[telInt + 14].equals(mark)
                      && marks[telInt + 21].equals(mark);
        return trues;
    }

    private boolean containsHorizontal(int telInt, Mark mark) {
        return Rules.HORIZONTAL.contains(telInt) && marks[telInt].equals(mark) 
                && marks[telInt + 1].equals(mark) && marks[telInt + 2].equals(mark)
                && marks[telInt + 3].equals(mark);
    }

    private boolean containsDiagonal(int telInt, Mark mark) {
        boolean diagonalOne = Rules.DIAGONAL_ONE.contains(telInt) && marks[telInt].equals(mark)
                && marks[telInt - 6].equals(mark) && marks[telInt - 12].equals(mark) 
                && marks[telInt - 18].equals(mark);
        boolean diagonalTwo = Rules.DIAGONAL_TWO.contains(telInt) && marks[telInt].equals(mark)
                && marks[telInt - 8].equals(mark) && marks[telInt - 16].equals(mark) 
                && marks[telInt - 24].equals(mark);
        return diagonalOne || diagonalTwo;
    }

    private int gravity(int keuze) {
        int slot = keuze;
        while (slot < 41 && tempBoardArray[slot].equals("EMPTY")) {
            if (slot + 7 < 41 && tempBoardArray[slot + 7].equals("EMPTY")) {
                slot += 7;
            } else {
                break;
            }
        }
        return slot;
    }
    
    public static int gravity(int keuze, String[] tempBoard) {
        int slot = Board.getColumn(keuze) - 1;
        while (slot < 41 && tempBoard[slot].equals("EMPTY")) {
            if (slot < 41 && tempBoard[slot].equals("EMPTY")) {
                slot += 7;
            } else {
                break;
            }
        }
        return slot;
    }

    //@ requires mark == Mark.YELLOW;
    //@ ensures \result == Mark.RED;
    //@ also
    //@ requires mark == Mark.RED;
    //@ ensures \result == Mark.YELLOW;
    private Mark swapMark(Mark mark) {
        if (mark.equals(Mark.YELLOW)) {
            return Mark.RED;
        } else {
            return Mark.YELLOW;
        }
    }
    
    private boolean win(String[] board, int zet, Mark mark) {
        boolean toReturn = false;
        board[gravity(zet)] = mark.name();
        int loop = moveLoop(swapMark(mark), board);
        if (loop > -1) {
            toReturn = true;
        }
        return toReturn;        
    }
}
