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
					toReturn.put(coords, tile);
					System.err.println(coords + tile);
					break;
				}
			}
		}
		if(toReturn.isEmpty()){
			for(int i = 0; i < hand.size(); i++){
				Tile tile = hand.get(i);
				hand.remove(tile);
				hand.add(game.getBag().drawTile());
				int amount = game.getBag().tileBag.get(tile);
				game.getBag().tileBag.put(tile, amount + 1);
			}
		}
		return toReturn;
	}

}
