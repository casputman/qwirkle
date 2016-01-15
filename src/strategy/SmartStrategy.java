package strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import qwirkle.Board;
import qwirkle.Game;
import qwirkle.Tile;

public class SmartStrategy implements Strategy {

	@Override
	public Map<String, Tile> determineMove(Game game, ArrayList<Tile> hand) {
		Map<String, Tile> toReturn = new HashMap<String,Tile>();
		//Our own copy of the game to do things in without actually making moves
		Game copyGame = game;
		while(!copyGame.current.getHand().isEmpty()){
			Map<String,Tile> temp = new HashMap<String,Tile>();
			while(true){
				//first make any Qwirkles you can
				temp = findQwirkle(copyGame, copyGame.current.getHand());
				if(!temp.isEmpty()){
					break;
				}
			}
			Set<String> coords = temp.keySet();
			for(String coord: coords){
				if(feasible(coord, temp.get(coord), game)){
					if(copyGame.takeTurn(Board.splitString(coord)[0], Board.splitString(coord)[1], temp.get(coord))){
						toReturn.put(coord, temp.get(coord));
						temp.remove(coord);
					}
				}
			}
		}
		return toReturn;
	}

	public static Map<String, Tile> findQwirkle(Game game, ArrayList<Tile> hand){
		Map<String,Tile> toReturn = new HashMap<String, Tile>();
		//TODO body
		return toReturn;
	}
	
	//checks whether or not this move gives good openings to the next player
	public static boolean feasible(String coords, Tile tile, Game game){
		boolean feasible = true;
		if(setUpQwirkle(coords, tile, game) || setUpScoring(coords, tile, game)){
			feasible = false;
		}
		return feasible;
	}
	
	//Checks if due to this move the next person could get Qwirkle
	public static boolean setUpQwirkle(String coords, Tile tile, Game game){
		boolean setUp = false;
		if(!findQwirkle(game, game.getBag().getTiles()).isEmpty()){
			setUp = true;
		}
		return setUp;
	}
	
	//Checks if due to this move the next person could get at least 3.5 points more than us
	public static boolean setUpScoring(String coords, Tile tile, Game game){
		boolean setUp = false;
		//TODO body
		return setUp;
	}
}
