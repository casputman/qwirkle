package qwirkle.player;

import qwirkle.core.Game;
import qwirkle.core.Tile;
import qwirkle.protocol.Protocol;
import qwirkle.core.Bag;
import qwirkle.core.Board;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;


public class HumanPlayer extends Player {


	public HumanPlayer(String name) {
		super(name);
	}

	private boolean cantSwap = false;

	@Override
	public Map<String, Tile> determineMove(Game game) {
		Board board = new Board();
		StartTimer timer = new StartTimer(board, this);
		timer.start();
		Map<String, Tile> moveMap = new HashMap<String, Tile>();
		System.out.println(this.name + ", please make your move (x -space- y -space- tile) ");
		System.out.println(getHand());
		String input = "";
		try {
			input = new BufferedReader(new InputStreamReader(System.in)).readLine().trim()
					.toUpperCase();
		} catch (IOException e) {
			System.out.println("shit went down");
		}
		if ((input.startsWith("MOVE") && input.length() > 9 && cantSwap == false) ) {
			String coordinates = input.replace("MOVE ", "");
			String[] coordinatesArray = coordinates.split(Protocol.MESSAGESEPERATOR);

			int xCoordinate = Integer.parseInt(coordinatesArray[0]);
			int yCoordinate = Integer.parseInt(coordinatesArray[1]);
			int tileSelection = Integer.parseInt(coordinatesArray[2]) - 1;
			if (tileSelection > getHand().size()){
				System.err.println("You don't have that many tiles in your inventory");
				game.getBoard().printBoard();
				moveMap.putAll(this.determineMove(game));
			} else {
				tileSelection = Integer.parseInt(coordinatesArray[2]) - 1;
			}
			Tile tile = getHand().get(tileSelection);
			String parsedCoordinates  = Board.makeString(xCoordinate, yCoordinate);
			if (game.getRules().isMoveAllowed(parsedCoordinates, tile) && tileSelection < 6) {
				timer.done = true;
				game.getBoard().makeMove(parsedCoordinates, tile, game);
				game.current.getHand().remove(tileSelection);
				game.getBoard().printBoard();
				moveMap.putAll(this.determineMove(game));
			} else {
				System.err.println("Invalid move or already swapped, please try again\n");
				game.getBoard().printBoard();
				moveMap.putAll(this.determineMove(game));
			} 

		} else if (input.startsWith("SWAP")){
			ArrayList<Tile> hand = getHand();
			Bag bag = game.getBag();
			String tileSwap = input.replace("SWAP ", "");
			String[] tileArray = tileSwap.split(Protocol.MESSAGESEPERATOR);
			int tileSelection = Integer.parseInt(tileArray[0]) - 1;
			Tile tile = getHand().get(tileSelection);
			if (!bag.tileBag.containsValue(1) && !bag.tileBag.containsValue(2) && !bag.tileBag.containsValue(3)  || (!hand.contains(tile))){
				System.out.println(Protocol.SERVER_CORE_SWAP_DENIED);
				moveMap.putAll(this.determineMove(game));
			}
			else {
				moveMap.put("SWAP " + moveMap.size(), tile);
				System.out.println(Protocol.SERVER_CORE_SWAP_ACCEPTED);
				while(!input.startsWith("DONE")){
					while(input.startsWith("SWAP")){
						hand = getHand();
						bag = game.getBag();
						tileSwap = input.replace("SWAP ", "");
						tileArray = tileSwap.split(Protocol.MESSAGESEPERATOR);
						tileSelection = Integer.parseInt(tileArray[0]) - 1;
						tile = getHand().get(tileSelection);
						if (!bag.tileBag.containsValue(1) && !bag.tileBag.containsValue(2) && !bag.tileBag.containsValue(3)  || (!hand.contains(tile))){
							System.out.println(Protocol.SERVER_CORE_SWAP_DENIED);
							moveMap.putAll(this.determineMove(game));
						}
						else {
							moveMap.put("SWAP " + moveMap.size(), tile);
							System.out.println(Protocol.SERVER_CORE_SWAP_ACCEPTED);
							cantSwap = true;
						}
					}
				}
			}
		}  else if (input.startsWith("DONE")){
			cantSwap = false;
			System.out.println(game.current);
			game.nextPlayer();
			System.out.println(game.current);
			game.makeMoves(game.current.determineMove(game));
		} else {
			moveMap.putAll(this.determineMove(game));
		}
		return moveMap;

	}
	public class StartTimer extends Thread{

		public boolean done = false;
		private Board board;
		private Player player;
		private Game game;

		public StartTimer(Board board, Player player) {
			this.board = board;
			this.player = player;
		} 

		public void run() {

			try {
				Thread.sleep(10000);
				if (!done) {
					// Player hint = new ComputerPlayer("smart");
					//   System.out.println("Sadly we can't help you here, our AI is probably worse than you are" );
					done = true;
				}
			} catch (InterruptedException e) {
				System.err.println("interrupted");
			}

		}
	}
}
