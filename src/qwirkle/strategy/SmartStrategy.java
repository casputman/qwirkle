package qwirkle.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
		//TODO body
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
