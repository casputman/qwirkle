package qwirkle.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import qwirkle.protocol.Protocol;

public class Bag {
	public int i;

	public Bag() {
		tileBag = new HashMap<Tile, Integer>();
		{
			for (i = 0; i < 6; i++) {
				Tile tile = new Tile(Tile.Shape.values()[0], Tile.Color.values()[i]);
				tileBag.put(tile, 3);
			}
			for (i = 0; i < 6; i++) {
				Tile tile = new Tile(Tile.Shape.values()[1], Tile.Color.values()[i]);
				tileBag.put(tile, 3);
			}
			for (i = 0; i < 6; i++) {
				Tile tile = new Tile(Tile.Shape.values()[2], Tile.Color.values()[i]);
				tileBag.put(tile, 3);
			}
			for (i = 0; i < 6; i++) {
				Tile tile = new Tile(Tile.Shape.values()[3], Tile.Color.values()[i]);
				tileBag.put(tile, 3);
			}
			for (i = 0; i < 6; i++) {
				Tile tile = new Tile(Tile.Shape.values()[4], Tile.Color.values()[i]);
				tileBag.put(tile, 3);
			}
			for (i = 0; i < 6; i++) {
				Tile tile = new Tile(Tile.Shape.values()[5], Tile.Color.values()[i]);
				tileBag.put(tile, 3);
			}
		}
	}

	public Bag(Map<Tile, Integer> tileBag) {
		this.tileBag = tileBag;
	}

	public Map<Tile, Integer> tileBag;

	public Tile drawTile() {
		Set<Tile> tiles = tileBag.keySet();
		Tile[] tileArray = new Tile[36];
		tiles.toArray(tileArray);
		Random s = new Random();
		int random = s.nextInt(32);
		Tile tile = tileArray[random];
		int amount = tileBag.get(tile);
		if (amount > 0) {
			tileBag.put(tile, amount - 1);
		} else {
			tile = drawTile();
		}
		return tile;
	}

	public Tile getTile(int s, int c) {
		Tile.Shape shape = null;
		Tile.Color color = null;
		shape = Tile.Shape.values()[s];
		color = Tile.Color.values()[c];
		Tile tile = new Tile(shape, color);
		return tile;
	}

	public ArrayList<Tile> getTiles() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		Set<Tile> bagTiles = tileBag.keySet();
		for (Tile tile : bagTiles) {
			for (int i = 0; i < tileBag.get(tile); i++) {
				tiles.add(tile);
			}
		}
		return tiles;
	}

	public Tile swapTile(Tile tile) {
		int value = tileBag.get(tile);
		tileBag.put(tile, value + 1);
		return this.drawTile();
	}

}
