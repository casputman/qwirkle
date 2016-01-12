package qwirkle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import qwirkle.Tile;

public class Board {
	
	private static final String EMPTY = "   ";
	private Map<String, Tile> tiles = new HashMap<>();
	
	public boolean makeMove(String coords, Tile tile){
		boolean moveMade;
		if(!tiles.containsKey(coords)){
			tiles.put(coords, tile);
			moveMade = true;
		} else {
			moveMade = false;
		}
		return moveMade;
	}
	
	public int[] getLimits(Map<String, Tile> input){
		int[] limits = new int[4];
		for (String coords : input.keySet()){
			String[] coord = coords.split(",");
			
			limits[0] = Math.min(limits[0], Integer.parseInt(coord[1]));
			limits[1] = Math.min(limits[1], Integer.parseInt(coord[0]));
			limits[2] = Math.max(limits[2], Integer.parseInt(coord[1]));
			limits[3] = Math.max(limits[3], Integer.parseInt(coord[0]));
		}
		return limits;
	}
	
	public String toString(){
		String toReturn = "";
		int[] limits = getLimits(this.tiles);
		
		for(int i = limits[0]; i <= limits[2]; i++){
			for(int j = limits[1]; j <= limits[3]; j++){
				String coords = j + "," + i;
				if(tiles.containsKey(coords)){
					toReturn = toReturn + tiles.get(coords).toString() + " ";
				} else {
					toReturn = toReturn + EMPTY;
				}
			}
			toReturn = toReturn + "\n";
		}
		return toReturn;
	}
	
		
}
