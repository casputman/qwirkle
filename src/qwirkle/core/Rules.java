package qwirkle.core;

import java.util.ArrayList;
import java.util.Map;

import qwirkle.player.Player;

public class Rules {

	private Board board;
	private Game game;
	private Map<String, Tile> tiles;
	
	public Rules(Game game){
		this.board = game.getBoard();
		this.game = game;
		tiles = board.getTiles();
	}

	//TODO
	public boolean hasWinner(){
		return true;
	}
	public boolean isWinner(Player player){
		return true;
	}
	
	public boolean isMoveAllowed(String coords, Tile tile){
		boolean allowed = false;
		int[] coord = Board.splitString(coords);
		int x = coord[0];
		int y = coord[1];
		if(freeCoords(coords)){
			if(game.current.getHand().contains(tile)){
				ArrayList<Tile> surrounding = surrounding(coords);
				if(!surrounding.isEmpty()){
					int valid = 0;
					for(int i = 0; i < surrounding.size(); i++){
						Tile temp = surrounding.get(i);
						if(!temp.equals(tile)){
							if(temp.getShape().equals(tile.getShape()) || temp.getColor().equals(tile.getColor())){
								valid += 1;
							}
						}
					}
					if(valid == surrounding.size()){
						allowed = true;
					}
				}
			}
		}
		return allowed;
	}
	
	public boolean freeCoords(String coords){
		return board.getTiles().containsKey(coords);
	}
	
	public ArrayList<Tile> surrounding(String coords){
		ArrayList<Tile> surrounding = new ArrayList<Tile>();
		int x = Board.splitString(coords)[0];
		int y = Board.splitString(coords)[1];
		for (int i = -1; i < 2; i += 2){
			x += -1;
			if(!tiles.get(Board.makeString(x, y)).equals(null)){
				surrounding.add(tiles.get(Board.makeString(x, y)));
			}
		}
		x = Board.splitString(coords)[0];
		for (int i = -1; i < 2; i += 2){
			y += -1;
			if(!tiles.get(Board.makeString(x, y)).equals(null)){
				surrounding.add(tiles.get(Board.makeString(x, y)));
			}
		}
		
		return surrounding;
	}
	
}
