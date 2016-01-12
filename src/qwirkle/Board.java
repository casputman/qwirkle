package qwirkle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import qwirkle.Tile;

public class Board {
	
	private static final String EMPTY = "   ";
	private Map<String, Tile> tiles = new HashMap<>();
	
	public boolean makeMove(String coords, Tile tile){
		boolean moveMade;
		if(!tiles.containsKey(coords)){
			tiles.put(coords, tile);
			moveMade = true;
		} else {
			moveMade = false;
		}
		return moveMade;
	}
	
		
}
