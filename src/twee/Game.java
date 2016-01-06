package twee;

import twee.player.Mark;
import twee.player.Player;
import twee.tui.GameTUIView;
import twee.tui.GameView;

public class Game extends Thread {

    private Board board;
    private Rules rules;
    public GameView view;
    private boolean running;
    public boolean offline;

    public Player current;
    public Player one;
    public Player two;

    public Game() {
        this(Player.createPlayer(Mark.YELLOW), Player.createPlayer(Mark.RED));
    }

    public Game(Player one, Player two) {
        view = new GameTUIView(this);
        board = new Board();
        rules = new Rules(this);

        this.one = one;
        this.two = two;

        board.addObserver(view);
        rules.addObserver(view);
    }

    @Override
    public void run() {
        running = true;
        board.printBoard();
        this.nextPlayer();

        view.start();
    }

    public void stopGame() {
        running = false;
    }

    //@pure
    public boolean getRunning() {
        return running;
    }

    public boolean takeTurn(int column) {
        return board.makeMove(column, rules, current.getMark());
    }

    //@pure
    public Board getBoard() {
        return this.board;
    }

    public void nextPlayer() {
        if (current == null || current.equals(two)) {
            current = one;
        } else if (current.equals(one)) {
            current = two;
        }

        if (this.offline) {
            current.determineMove(this);
        } 
    }

    public void reset() {
        board.reset();
        running = true;
        current = null;
        board.printBoard();
        nextPlayer();
    }

    //@pure
    public Rules getRules() {
        return rules;
    }
}
