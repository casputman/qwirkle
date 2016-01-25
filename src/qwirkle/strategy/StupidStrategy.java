package qwirkle.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import qwirkle.core.Game;
import qwirkle.core.Tile;

public class StupidStrategy implements Strategy {

	@Override
	public Map<String, Tile> determineMove(Game game, ArrayList<Tile> hand) {
		Map<String, Tile> toReturn = new HashMap<String, Tile>();
		Set<String> emptyCoords = SmartStrategy.emptyCoords(game);
		for(String coords: emptyCoords){
			System.err.println(coords);
			for(Tile tile : hand){
				System.err.println(tile);
				if(game.getRules().isMoveAllowed(coords, tile)){
					toReturn.put(coords, tile);
				}
				System.err.println(game.getRules().isMoveAllowed(coords, tile));
			}
		}
		return toReturn;
	}

}
