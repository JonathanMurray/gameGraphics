package graphicsRelated;

import java.util.HashMap;


/**
 * Contains sprites that are used for different movement directions.
 */
public class SpriteSet{
	
	//Links each direction to one sprite. (Each sprite in turn consists of several images!)
	private HashMap<Direction, Sprite> sprites = new HashMap<Direction, Sprite>();
	
	private boolean singleSprite; //Some sprite sets consist of only one sprite!
	 
	/**
	 * Create a new spriteSet with one sprite for each direction
	 * @param n 
	 * @param nw
	 * @param w
	 * @param sw
	 * @param s
	 * @param se
	 * @param e
	 * @param ne
	 */
	public SpriteSet(Sprite n, Sprite nw, Sprite w, Sprite sw, Sprite s, Sprite se, Sprite e, Sprite ne){
		
		if(n == null || nw == null || w == null || sw == null || s == null || se == null || e == null || ne == null){
			throw new IllegalArgumentException("Some of the sprites are null!");
		}
		
		sprites.put(Direction.north, n);
		sprites.put(Direction.northEast, nw);
		sprites.put(Direction.east, w);
		sprites.put(Direction.southEast, sw);
		sprites.put(Direction.south, s);
		sprites.put(Direction.southWest, se);
		sprites.put(Direction.west, e);
		sprites.put(Direction.northWest, ne);
		
		singleSprite = false;
	}
	
	/**
	 * Create a new spriteSet with the same sprite for every direction.
	 * @param spr The only sprite.
	 */
	public SpriteSet(Sprite spr){
		sprites.put(Direction.north, spr);
		sprites.put(Direction.northEast, spr);
		sprites.put(Direction.east, spr);
		sprites.put(Direction.southEast, spr);
		sprites.put(Direction.south, spr);
		sprites.put(Direction.southWest, spr);
		sprites.put(Direction.west, spr);
		sprites.put(Direction.northWest, spr);
		
		singleSprite = true;
	}
	
	public Sprite getSprite(Direction dir) {
		return sprites.get(dir);
	}
	
	//Call this if only 1 sprite in spriteSet
	public Sprite getSprite(){
		if(!singleSprite){
			throw new IllegalStateException("Need direction parameter, this set consists of several unique sprites");
		}
		
		return sprites.get(Direction.north);
	}
	
	/**
	 * Specify size for this spriteSet. (Will set size for each of the sprites)
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height){
		for( Sprite spr : sprites.values()){
			spr.setSize(width, height);
		}
	}
	
	
	/**
	 * Return an identical copy of this spriteset
	 * @return
	 */
	public SpriteSet getCopy(){
		SpriteSet ss = new SpriteSet(sprites.get(Direction.north).getCopy(), sprites.get(Direction.northEast).getCopy(), 
				                     sprites.get(Direction.east).getCopy(),  sprites.get(Direction.southEast).getCopy(), 
				                     sprites.get(Direction.south).getCopy(), sprites.get(Direction.southWest).getCopy(), 
				                     sprites.get(Direction.west).getCopy(),  sprites.get(Direction.northWest).getCopy());
		
		return ss;
	}
	
	
	public enum Direction{
		north, northWest, west, southWest, south, southEast, east, northEast;
	}
}



