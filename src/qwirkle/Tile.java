package qwirkle;

public class Tile {
	
	public Shape shape;
	public Color color;
	
	public Tile(Shape shape, Color color){
		this.shape = shape;
		this.color = color;
	}
	public Color getColor(){
		return color;
	}
	
	public Shape getShape(){
		return shape;
	}
	
	public enum Shape {
		SQUARE(1, '\u25A0'), CIRCLE(2, '\u25CF'), STAR(3, '\u2738'), DIAMOND(4, '\u25C6'), CROSS(5, '\u2716'), CLUB(6, '\u2663');

		public int code;
		public char symbol;

		Shape(int i, char s){
			this.code = i;
			this.symbol = s;	
		}
	}
	public enum Color {
		BLUE(1), GREEN(2), RED(3), YELLOW(4), PURPLE(5), ORANGE(6);
		
		public int code;
		
		Color(int i){
			this.code = i;
		}
	}
	
	@Override
	public String toString(){
		String toReturn = "";
		toReturn = toReturn + String.valueOf(this.getShape().symbol);
		return toReturn;
	}
}
	
	