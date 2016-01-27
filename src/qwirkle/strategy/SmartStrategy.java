package qwirkle.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import qwirkle.core.Board;
import qwirkle.core.Game;
import qwirkle.core.Tile;

public class SmartStrategy implements Strategy {

	@Override
	public Map<String, Tile> determineMove(Game game, ArrayList<Tile> hand) {
		Map<String, Tile> toReturn = new HashMap<String,Tile>();
		//Our own copy of the game to do things in without actually making moves
		Game copyGame = game;
		Map<String,Tile> temp = new HashMap<String,Tile>();
		if(!copyGame.getBag().getTiles().isEmpty()){
			while(!copyGame.current.getHand().isEmpty()){
				temp = move(copyGame, copyGame.current.getHand());
			}
		} else {
			temp = move(copyGame, copyGame.current.getHand());
		}
		Set<String> coords = temp.keySet();
		for(String coord: coords){
			if(copyGame.takeTurn(Board.splitString(coord)[0], Board.splitString(coord)[1], temp.get(coord))){
			}
		}
		if(feasible(copyGame)){
			for(String coord: coords){
				toReturn.put(coord, temp.get(coord));
				temp.remove(coord);
			}
		}
		return toReturn;
	}
	
	public Map<String, Tile> move(Game game, ArrayList<Tile> hand){
		Map<String,Tile> toReturn = new HashMap<String,Tile>();
		while(true){
			//first make any Qwirkles you can
			toReturn = findQwirkle(game, hand);
			if(!toReturn.isEmpty()){
				break;
			}
			//second block any potential Qwirkles
			toReturn = blockQwirkle(game, hand);
			if(!toReturn.isEmpty()){
				break;
			}
			//third go for multiple lines
			toReturn = findDouble(game, hand);
			if(!toReturn.isEmpty()){
				break;
			}
			
		}
		return toReturn;
	}

	public static Map<String, Tile> findQwirkle(Game game, ArrayList<Tile> hand){
		Map<String,Tile> toReturn = new HashMap<String, Tile>();
		Map<String,Tile> qwirkle = possibleQwirkle(game);
		Set<String> qwirkleCoords = qwirkle.keySet();
		Map<String, int[]> qwirkleSurrounds = Board.surrounding(game.getBoard().getTiles(), qwirkleCoords);
		for(String coord: qwirkleCoords){
			if(qwirkleSurrounds.get(coord)[0] != 0){
				//TODO body
			}
		}
		return toReturn;
	}
	
	public static Map<String, Tile> blockQwirkle(Game game, ArrayList<Tile> hand){
		Map <String, Tile> toReturn = new HashMap<String, Tile>();
		//TODO body
		return toReturn;
	}
	
	public static Map<String, Tile> findDouble(Game game, ArrayList<Tile> hand){
		Map<String, Tile> toReturn = new HashMap<String, Tile>();
		//TODO body
		return toReturn;
	}
	
	public static Map<String, Tile> findHighScore(Game game, ArrayList<Tile> hand){
		Map<String, Tile> toReturn = new HashMap<String, Tile>();
		//TODO body
		return toReturn;
	}
	
	public static Map<String, Tile> possibleQwirkle(Game game){
		Map<String, Tile> toReturn = new HashMap<String,Tile>();
		Map<String, Tile> board = game.getBoard().getTiles();
		Set<String> boardCoords = board.keySet();
		Map<String, int[]> emptySurrounding = Board.surrounding(game.getBoard().getTiles(), emptyCoords(game));
		Set<String> emptyCoords = emptySurrounding.keySet();
		for(String coord : emptyCoords){
			int[] xy = Board.splitString(coord);
			if(emptySurrounding.get(coord)[0] != 0){
				int minX;
				int plusX;
				for(minX = 0; boardCoords.contains(Board.makeString(xy[0] + minX, xy[1])); minX--){
				}
				for(plusX = 0; boardCoords.contains(Board.makeString(xy[0] + plusX, xy[1])); plusX++){
				}
				Set<Tile> tiles = new TreeSet<Tile>();
				if(plusX - minX == 5){
					if(plusX != 0 && minX != 0){
						if(board.get(Board.makeString(xy[0] + plusX, xy[1])).getShape().equals(board.get(Board.makeString(xy[0] + minX, xy[1])).getShape()) || 
								board.get(Board.makeString(xy[0] + plusX, xy[1])).getColor().equals(board.get(Board.makeString(xy[0] + minX, xy[1])).getColor())){
							if(board.get(Board.makeString(xy[0] + 1, xy[1])).getShape().equals(board.get(Board.makeString(xy[0] - 1, xy[1])).getShape())){
								Tile.Shape shape = board.get(Board.makeString(xy[0] + 1, xy[1])).getShape();
								for(Tile.Color color : Tile.Color.values()){
									tiles.add(new Tile(shape, color));
								}
								
								//TODO
							}
							//TODO same as above, but we know the color
						}
					} else {
						//TODO toReturn.add(coord);
					}
				}
			}
			if(emptySurrounding.get(coord)[1] != 0){
				int minY;
				int plusY;
				for(minY = 0; boardCoords.contains(Board.makeString(xy[0], xy[1] + minY)); minY--){
				}
				for(plusY = 0; boardCoords.contains(Board.makeString(xy[0], xy[1] + plusY)); plusY++){
				}
				if(plusY - minY == 5){
					if(plusY != 0 && minY != 0){
						if(board.get(Board.makeString(xy[0], xy[1] + plusY)).getShape().equals(board.get(Board.makeString(xy[0], xy[1] + minY)).getShape()) || 
								board.get(Board.makeString(xy[0], xy[1] + plusY)).getColor().equals(board.get(Board.makeString(xy[0], xy[1] + minY)).getColor())){
							//TODO toReturn.add(coord);
						}
					} else {
						//TODO toReturn.add(coord);
					}
				}
			}
		}
		return toReturn;
	}
	
	public static Set<String> emptyCoords(Game game){
		Set<String> toReturn = new TreeSet<String>();
		if(game.getBoard().getTiles().isEmpty()){
			toReturn.add(Board.makeString(0, 0));
		} else {
			int[] limits = Board.getLimits(game.getBoard().getTiles());
			
			for(int i = limits[2]+1; i >= limits[0]-1; i--){
				for(int j = limits[1]+1; j <= limits[3]-1; j++){
					String coords = Board.makeString(j,i);
					if(!game.getBoard().getTiles().containsKey(coords)){
						toReturn.add(coords);
					}
				}
			}
		}
		
		return toReturn;
	}
	
	public static Set<String> possibleDouble(Game game){
		Set<String> toReturn = new TreeSet<String>();
		Map<String, int[]> emptySurrounding = Board.surrounding(game.getBoard().getTiles(), emptyCoords(game));
		Set<String> emptyCoords = emptySurrounding.keySet();
		for(String coord : emptyCoords){
			if(emptySurrounding.get(coord)[3] == 2){
				toReturn.add(coord);
			}
		}
		return toReturn;
	}
	
	//checks whether or not this move gives good openings to the next player
	public static boolean feasible(Game game){
		boolean feasible = true;
		if(setUpQwirkle(game) || setUpScoring(game)){
			feasible = false;
		}
		return feasible;
	}
	
	//Checks if due to this move the next person could get Qwirkle
	public static boolean setUpQwirkle(Game game){
		boolean setUp = false;
		if(!findQwirkle(game, game.getBag().getTiles()).isEmpty()){
			setUp = true;
		}
		return setUp;
	}
	
	//Checks if due to this move the next person could get at least 3.5 points more than us
	public static boolean setUpScoring(Game game){
		boolean setUp = false;
		//TODO body
		return setUp;
	}	
}
