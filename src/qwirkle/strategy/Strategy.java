package qwirkle.strategy;

import java.util.ArrayList;
import java.util.Map;

import qwirkle.core.Board;
import qwirkle.core.Game;
import qwirkle.core.Tile;

public interface Strategy {

	public Map<String, Tile> determineMove(Game game, ArrayList<Tile> hand);
}