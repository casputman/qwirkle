package qwirkle.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import qwirkle.core.Game;
import qwirkle.core.Tile;
import qwirkle.protocol.Protocol;

public class StupidStrategy implements Strategy {

	@Override
	public Map<String, Tile> determineMove(Game game, ArrayList<Tile> hand) {
		Map<String, Tile> toReturn = new HashMap<String, Tile>();
		Set<String> emptyCoords = SmartStrategy.emptyCoords(game);
		for(String coords: emptyCoords){
			for(Tile tile : hand){
				if(game.getRules().isMoveAllowed(coords, tile)){
					System.err.println("playing");
					toReturn.put(coords, tile);
					break;
				}
			}
		}
		if(toReturn.isEmpty()){
			System.err.println("swapping");
			for(Tile tile : hand){
				toReturn.put(null, tile);
			}
		}
		return toReturn;
	}

}
