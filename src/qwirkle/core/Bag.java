package qwirkle.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Bag {
	public int i;
	
	public Bag(){
		tileBag = new HashMap<Tile, Integer>();{
			for(i = 0; i < 6; i++){
				Tile tile = new Tile(Tile.Shape.values()[1], Tile.Color.values()[i]);
				tileBag.put(tile, 3);
			}
			for(i = 0; i < 6; i++){
				Tile tile = new Tile(Tile.Shape.values()[2], Tile.Color.values()[i]);
				tileBag.put(tile, 3);
			}
			for(i = 0; i < 6; i++){
				Tile tile = new Tile(Tile.Shape.values()[3], Tile.Color.values()[i]);
				tileBag.put(tile, 3);
			}
			for(i = 0; i < 6; i++){
				Tile tile = new Tile(Tile.Shape.values()[4], Tile.Color.values()[i]);
				tileBag.put(tile, 3);
			}
			for(i = 0; i < 6; i++){
				Tile tile = new Tile(Tile.Shape.values()[5], Tile.Color.values()[i]);
				tileBag.put(tile, 3);
			}
			for(i = 0; i < 6; i++){
				Tile tile = new Tile(Tile.Shape.values()[6], Tile.Color.values()[i]);
				tileBag.put(tile, 3);
			}
		}
	}
	
	public Bag(Map<Tile, Integer> tileBag){
		this.tileBag = tileBag;
	}
	
	public Map<Tile, Integer> tileBag;

	public Tile drawTile(){
		Random s = new Random();
		int range = 6;
		int randomS =  s.nextInt(range) + 1;
		Random c = new Random();
		int randomC = c.nextInt(range) + 1;
		Tile.Shape shape = null;
		Tile.Color color = null;
		shape = Tile.Shape.values()[randomS];
		color = Tile.Color.values()[randomC];
		Tile tile = new Tile(shape, color);
		int amount = tileBag.get(tile);
		tileBag.put(tile, amount -1);
		return tile;
	}

	public Tile getTile(int s, int c){
		Tile.Shape shape = null;
		Tile.Color color = null;
		shape = Tile.Shape.values()[s];
		color = Tile.Color.values()[c];
		Tile tile = new Tile(shape, color);
		return tile;
	}
	
	public ArrayList<Tile> getTiles(){
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		Set<Tile> bagTiles = tileBag.keySet();
		for(Tile tile: bagTiles){
			for(int i = 0; i < tileBag.get(tile); i++){
				tiles.add(tile);
			}
		}
		return tiles;
	}

}
