package twee.strategy;

import twee.player.Mark;

public interface Strategy {

    public int determineMoveInt(String[] board, Mark mark);
}
