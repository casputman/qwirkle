package qwirkle.core;

import java.util.ArrayList;
import java.util.Map;

import com.sun.swing.internal.plaf.synth.resources.synth_es;

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

	public boolean hasWinner(){
		boolean hasWinner = false;
		if(game.getBag().tileBag.isEmpty()){
			int players = 0;
			for(Player player : game.getPlayers()){
				if(player.getHand().isEmpty()){
					players++;
				}
			}
			if(players == game.getPlayers().size()){
				hasWinner = true;
			}
		}
		return hasWinner;
	}
	
	public boolean isWinner(Player player){
		boolean isWinner = true;
		if(hasWinner()){
			for(Player enemy : game.getPlayers()){
				if(!enemy.equals(player)){
					if(enemy.score > player.score){
						isWinner = false;
					}
				}
			}
		} else {
			isWinner = false;
		}
		return isWinner;
	}
	
	public boolean isMoveAllowed(String coords, Tile tile){
		boolean allowed = true;
		int[] coord = Board.splitString(coords);
		int x = coord[0];
		int y = coord[1];
		Map<String, Tile> tiles = game.getBoard().getTiles();
		boolean one = true;
		boolean two = true;
		boolean three = true;
		boolean four = true;
		if(freeCoords(coords)){
			if(game.current.getHand().contains(tile)){
				ArrayList<Tile> surrounding = surrounding(coords);
				if(!surrounding.isEmpty()){
					for(int j = 1; j < 6; j++){
						Tile temp1 = tiles.get(Board.makeString(x+j, y));
						if(temp1 == null){
							one = false;
						}
						if(one && ((!temp1.getShape().equals(tile.getShape()) && !temp1.getColor().equals(tile.getColor())) || (temp1.getShape().equals(tile.getShape()) && temp1.getColor().equals(tile.getColor())))){
							allowed = false;
							break;
						}
						Tile temp2 = tiles.get(Board.makeString(x-j, y));
						if(temp2 == null){
							two = false;
						}
						if(two && ((!temp2.getShape().equals(tile.getShape()) && !temp2.getColor().equals(tile.getColor())) || (temp2.getShape().equals(tile.getShape()) && temp2.getColor().equals(tile.getColor())))){							
							allowed = false;
							break;
						}
						Tile temp3 = tiles.get(Board.makeString(x, y+j));
						if(temp3 == null){
							three = false;
						}
						if(three && ((!temp3.getShape().equals(tile.getShape()) && !temp3.getColor().equals(tile.getColor())) || (temp3.getShape().equals(tile.getShape()) && temp3.getColor().equals(tile.getColor())))){							
							allowed = false;
							break;
						}
						Tile temp4 = tiles.get(Board.makeString(x, y-j));
						if(temp4 == null){
							four = false;
						}
						if(four && ((!temp4.getShape().equals(tile.getShape()) && !temp4.getColor().equals(tile.getColor())) || (temp4.getShape().equals(tile.getShape()) && temp4.getColor().equals(tile.getColor())))){							
							allowed = false;
							break;
						}
					}
				} else if(game.getBoard().getTiles().isEmpty()){
					allowed = true;
				} else {
					allowed = false;
				}
			} else {
				allowed = false;
			}
		} else {
			allowed = false;
		}
		return allowed;
	}
	
	public boolean freeCoords(String coords){
		return !board.getTiles().containsKey(coords);
	}
	
	public ArrayList<Tile> surrounding(String coords){
		ArrayList<Tile> surrounding = new ArrayList<Tile>();
		int x = Board.splitString(coords)[0];
		int y = Board.splitString(coords)[1];
		for (int i = -1; i < 3; i += 3){
			x += i;
			if(tiles.containsKey(Board.makeString(x, y)) && !tiles.get(Board.makeString(x, y)).equals(null)){
				surrounding.add(tiles.get(Board.makeString(x, y)));
			}
		}
		x = Board.splitString(coords)[0];
		for (int i = -1; i < 3; i += 3){
			y += i;
			if(tiles.containsKey(Board.makeString(x, y)) && !tiles.get(Board.makeString(x, y)).equals(null)){
				surrounding.add(tiles.get(Board.makeString(x, y)));
			}
		}
		
		return surrounding;
	}
	
}
