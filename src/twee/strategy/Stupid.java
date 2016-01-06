package twee.strategy;

import twee.Board;
import twee.player.Mark;

import java.util.Random;

public class Stupid implements Strategy {

    public int determineMoveInt(String[] board, Mark mark) {
        int randomNum = generateRandom();
        return randomNum;
    }

    public int generateRandom() {
        Random rand = new Random();
        return rand.nextInt(Board.COLUMNS) + 1;
    }

}