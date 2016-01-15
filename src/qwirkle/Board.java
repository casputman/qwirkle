package qwirkle;

import java.util.HashMap;
import java.util.Map;

import qwirkle.Tile;

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
	
	public void reset(){
		tiles.clear();
	}
	
	public int[] getLimits(Map<String, Tile> input){
		int[] limits = new int[4];
		for (String coords : input.keySet()){
			int[] coord = splitString(coords);
			
			limits[0] = Math.min(limits[0], coord[1]);
			limits[1] = Math.min(limits[1], coord[0]);
			limits[2] = Math.max(limits[2], coord[1]);
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
		
		for(int i = limits[0]; i <= limits[2]; i++){
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
		
}
