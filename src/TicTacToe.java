package ss.week5;

public class TicTacToe {

	public static void main(String[] args) {
		if (args[0].equals("-N")){
			Player player1 = new ComputerPlayer(Mark.XX);
			if (args[1].equals("-N")){
				Player player2 = new ComputerPlayer(Mark.OO);
				Game game = new Game(player1, player2);
				game.start();
			}
			else if(args[1].equals("-S")){
				Player player2 = new ComputerPlayer(Mark.OO, new SmartStrategy());
				Game game = new Game(player1, player2);
				game.start();
			}
			else{
			HumanPlayer player2 = new HumanPlayer(args[1], Mark.OO);
			Game game = new Game(player1, player2);
			game.start();
			}
		}
		else if(args[0].equals("-S")){
			Player player1 = new ComputerPlayer(Mark.XX, new SmartStrategy());
			if (args[1].equals("-N")){
				Player player2 = new ComputerPlayer(Mark.OO);
				Game game = new Game(player1, player2);
				game.start();
			}
			else if(args[1].equals("-S")){
				Player player2 = new ComputerPlayer(Mark.OO, new SmartStrategy());
				Game game = new Game(player1, player2);
				game.start();
			}
			else{
			HumanPlayer player2 = new HumanPlayer(args[1], Mark.OO);
			Game game = new Game(player1, player2);
			game.start();
			}
		}
		else{
			HumanPlayer player1 = new HumanPlayer(args[0], Mark.XX);
			if (args[1].equals("-N")){
				Player player2 = new ComputerPlayer(Mark.OO);
				Game game = new Game(player1, player2);
				game.start();
			}
			else if(args[1].equals("-S")){
				Player player2 = new ComputerPlayer(Mark.OO, new SmartStrategy());
				Game game = new Game(player1, player2);
				game.start();
			}
			else{
			HumanPlayer player2 = new HumanPlayer(args[1], Mark.OO);
			Game game = new Game(player1, player2);
			game.start();
			}
		}
		
	}
	
}
