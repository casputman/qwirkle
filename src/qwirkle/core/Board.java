package qwirkle.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import qwirkle.core.Tile;
import qwirkle.core.Tile.Color;
import qwirkle.core.Tile.Shape;

public class Board {
	
	private static final String EMPTY = "   ";
	
	private Map<String, Tile> tiles;
	
	public Board(){
		tiles = new HashMap<>();
		reset();
	}
	
	public boolean makeMove(String coords, Tile tile, Game game){
		boolean moveMade;
		if(game.getRules().isMoveAllowed(coords, tile)){
			tiles.put(coords, tile);
			int tileNumber = 0;
			for(int i = 0; i < game.current.getHand().size(); i++){
				System.err.println("3");
				if(game.current.getHand().get(i).equals(tile)){
					tileNumber = i;
					break;
				}
			}
			game.takeTile(game.current, tileNumber);
			moveMade = true;
		} else {
			moveMade = false;
		}
		return moveMade;
	}
	public Tile makeTile(int x, int y){
		Shape shape = Shape.values()[x];
		Color color = Color.values()[y];
		Tile tile = new Tile(shape, color);
		return tile;
	}
	
	public void reset(){
		tiles.clear();
	}
	
	public static int[] getLimits(Map<String, Tile> input){
		int[] limits = new int[4];
		for (String coords : input.keySet()){
			int[] coord = splitString(coords);
			
			//Onderste Y
			limits[0] = Math.min(limits[0], coord[1]);
			//Meest linkse X
			limits[1] = Math.min(limits[1], coord[0]);
			//Bovenste Y
			limits[2] = Math.max(limits[2], coord[1]);
			//Meest rechtse X
			limits[3] = Math.max(limits[3], coord[0]);
		}
		return limits;
	}
	
	public void printBoard(){
		System.out.println(toString());
	}
	
	public String toString(){
		String toReturn = "";
		int[] limits = getLimits(this.tiles);
		for(int i = limits[2]; i <= limits[0]; i--){
			for(int j = limits[1]; j <= limits[3]; j++){
				String coords = makeString(j,i);
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
	
	public Map<String, Tile> getTiles(){
		return tiles;
	}
	
	public static String makeString(int x, int y){
		return x + "," + y;
	}
	
	public static int[] splitString(String coords){
		int[] toReturn = new int[2];
		String[] coord = coords.split(",");
		toReturn[0] = Integer.parseInt(coord[0]);
		toReturn[1] = Integer.parseInt(coord[1]);
		return toReturn;
	}
	
	public static Map<String, int[]> surrounding(Map<String, Tile> tiles, Set<String> coords){
		Map<String, int[]> surroundings = new HashMap<String, int[]>();
		Set<String> boardCoords = tiles.keySet();
		for (String coord : coords){
			int[] surround = new int[3];
			int surroundX = 0;
			int surroundY = 0;
			int[]xy = Board.splitString(coord);
			for(int i = -1; i < 2; i+= 2){
				if(boardCoords.contains(Board.makeString(xy[0] + i, xy[1]))){
					surroundX = 1;
				}
			}
			for(int i = -1; i < 2; i+= 2){
				if(boardCoords.contains(Board.makeString(xy[0], xy[1] + i))){
					surroundY = 1;
				}
			}
			surround[0] = surroundX;
			surround[1] = surroundY;
			surround[2] = surroundX + surroundY;
			surroundings.put(coord, surround);
		}
		return surroundings;
	}
		
}
