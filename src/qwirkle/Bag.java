package qwirkle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Bag {
	public Map<Tile, Integer> tileBag1;
	public int i;

	// Creates a new tile bag with 36 combinations, of each tile 3 will be placed in the bag.
	public Map<Tile, Integer> tileBag = new HashMap<Tile, Integer>();{
		this.tileBag = tileBag1;
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
		int amount = tileBag1.get(tile);
		tileBag1.put(tile, amount -1);
		return tile;
	}

	public int getAmount(int i){
		this.i = i;		
		return tileBag1.get(i);
	}

	public Tile getTile(int s, int c){
		Tile.Shape shape = null;
		Tile.Color color = null;
		shape = Tile.Shape.values()[s];
		color = Tile.Color.values()[c];
		Tile tile = new Tile(shape, color);
		return tile;
	}

}
