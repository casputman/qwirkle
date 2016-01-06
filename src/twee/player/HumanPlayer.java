package twee.player;

import twee.Game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


public class HumanPlayer extends Player {

    public HumanPlayer(Mark mark) {
        super(mark);
    }

    public HumanPlayer(Mark mark, String name) {
        super(name, mark);
    }
    

    @Override
    public boolean determineMove(Game game) {
        String[] board = new String[42];
        for (int i = 0; i < board.length; i++) {
            if (game.getBoard().getFields()[i].equals(Mark.YELLOW)) {
                board[i] = "YELLOW";
            } else if (game.getBoard().getFields()[i].equals(Mark.RED)) {
                board[i] = "RED";
            } else {
                board[i] = "EMPTY";
            }
        }
        StartTimer timer = new StartTimer(board, this.getMark(), this);
        timer.start();
            boolean determined = false;
            System.out.println(game.current.getName() + ", please make your move (column 1-7)...");
    
            String input = "";
    
            try {
                input = new BufferedReader(new InputStreamReader(System.in)).readLine().trim()
                        .toUpperCase();
            } catch (IOException e) {
                System.out.println("shit went down");
            }
    
            if (input.equals("SHOW BOARD")) {
                game.getBoard().printBoard();
            } else if ((input.startsWith("SLOT") && input.length() > 4) || input.length() == 1) {
                int column = Integer.parseInt(input.replaceAll(" +", "").replace("SLOT", ""));
                if (column >= 1 && column <= 7) {
                    timer.done = true;
                    determined = game.takeTurn(column);
                } else {
                    System.out.println("Invalid column, please select one of the 7 columns.\n");
                    this.determineMove(game);
                }
            }

            return determined;
        
}

    @Override
    public int determineMoveInt(String[] board, Mark mark) {
        StartTimer timer = new StartTimer(board, mark, this);
        timer.start();
        
            System.out.println("Please choose a column (1-7): ");
            String input = "";
            int column = -1;
            try {
                input = new BufferedReader(new InputStreamReader(System.in)).readLine().trim()
                        .toUpperCase();
            } catch (IOException e) {
                System.out.println("shit went down");
            }
    
            if (!input.isEmpty()) {
                try {
                    column = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.err.println("Please input a number");
                    determineMoveInt(board, mark);
                }
                if (column > 0 && column < 8) {
                    timer.done = true;
                    return column;
                }
            }
            timer.done = true;
            return 0;
    }
    
    public class StartTimer extends Thread{
        
        public boolean done = false;
        private String[] board;
        private Player player;
        
        public StartTimer(String[] board, Mark mark, Player player) {
            this.board = board;
            this.player = player;
        } 
        
        public void run() {
            
            try {
                Thread.sleep(10000);
                if (!done) {
                    Player hint = new ComputerPlayer(player.getMark(), "smart");
                    System.out.println("I would suggest this move: " 
                    +  hint.determineMoveInt(board, hint.getMark()));
                    done = true;
                }
            } catch (InterruptedException e) {
               System.err.println("interrupted");
            }
        
        }
    }
}
