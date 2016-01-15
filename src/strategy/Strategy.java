package strategy;

import java.util.ArrayList;
import java.util.Map;

import qwirkle.Board;
import qwirkle.Game;
import qwirkle.Tile;

public interface Strategy {

	public Map<String, Tile> determineMove(Game game, ArrayList<Tile> hand);
}