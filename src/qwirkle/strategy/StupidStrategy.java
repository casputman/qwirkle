package qwirkle.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.swing.internal.plaf.synth.resources.synth_es;

import qwirkle.core.Game;
import qwirkle.core.Tile;
import qwirkle.protocol.Protocol;

public class StupidStrategy implements Strategy {

	@Override
	public Map<String, Tile> determineMove(Game game, ArrayList<Tile> hand) {
		Map<String, Tile> toReturn = new HashMap<String, Tile>();
		Set<String> emptyCoords = SmartStrategy.emptyCoords(game);
		boolean foundOne = false;
		while (!foundOne) {
			for (String coords : emptyCoords) {
				for (Tile tile : hand) {
					if (game.getRules().isMoveAllowed(coords, tile)) {
						toReturn.put(coords, tile);
						foundOne = true;
						break;
					}
				}
				if (foundOne) {
					break;
				}
			}
			break;
		}
		if (toReturn.isEmpty()) {
			for (Tile tile : hand) {
				toReturn.put("SWAP " + toReturn.size(), tile);
			}
		}
		return toReturn;
	}

}
