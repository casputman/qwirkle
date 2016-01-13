package qwirkle;

import java.util.ArrayList;

import twee.player.Player;

public class Game {
	private Board board;
	
	private Bag bag;
	private int player;
	private Player[] players = new Player[4];
	public Player current;
	
	
	private boolean running;
	public boolean offline;
	
	public Game(Player one, Player two, Player three, Player four){
		board = new Board();
		players[0] = one;
		players[1] = two;
		players[2] = three;
		players[3] = four;
		bag = new Bag();
		player = 0;
	}
	
	public void run(){
		running = true;
		board.printBoard();
		this.nextPlayer();
	}
	
	public void stopGame(){
		running = false;
	}
	
	public boolean getRunning(){
		return running;
	}
	
	public boolean takeTurn(int x, int y, Tile tile){
		String coords = x + "," + y;
		return getBoard().makeMove(coords,tile);
	}
	
	public Board getBoard(){
		return this.board;
	}
	
	public void nextPlayer(){
		if(players[player].equals(current) || !current.equals(null)){
			player += 1;
			player %= 4;
			current = players[player];
		} else if (current.equals(null)){
			int random = (int )(Math.random() * 4 + 1);
			current = players[random];
		} else {
			for (int i = 0; i < players.length; i++){
				if(players[i].equals(current)){
					i += 1;
					i %= 4;
					current = players[i];
				}
			}
		}
	}
	
	public void reset(){
		board.reset();
		running = true;
		current = null;
		board.printBoard();
		nextPlayer();
	}
	
	public Bag getBag(){
		return this.bag;
	}
	
	public void giveTile(Player player){
		ArrayList<Tile> tempHand = player.getHand();
		for (int i = 0; i < tempHand.size(); i++){
			player.addTile(bag.drawTile());
		}
	}
	
	public void takeTile(Player player, int index){
		player.takeTile(index);
	}
}
