package strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import qwirkle.Board;
import qwirkle.Game;
import qwirkle.Tile;

public class StupidStrategy implements Strategy {

	@Override
	public Map<String, Tile> determineMove(Game game, ArrayList<Tile> hand) {
		Map<String, Tile> toReturn = new HashMap<String, Tile>();
		Random rand = new Random();
		int tile = rand.nextInt(6);
		String coords = Board.makeString(0,0);
		if(game.getBoard().getTiles().isEmpty()){
			toReturn.put(coords, hand.get(tile));
		} else {
			boolean ready = false;
			while(!ready){
				tile = rand.nextInt(6);
				coords = generateRandomCoords(game);
				if(game.getRules().isMoveAllowed(coords, hand.get(tile))){
					ready = true;
				}
			}
		}
		toReturn.put(coords, hand.get(tile));
		return toReturn;
	}

	public String generateRandomCoords(Game game) {
		int[] limits = game.getBoard().getLimits(game.getBoard().getTiles());
       	Random rand = new Random();
		int x = rand.nextInt(Math.max(-1*limits[0], limits[2]));
		int y = rand.nextInt(Math.max(-1*limits[1], limits[3]));
        x *= (-1)^rand.nextInt(2);
        y *= (-1)^rand.nextInt(2);
        return Board.makeString(x, y);
    }
}
