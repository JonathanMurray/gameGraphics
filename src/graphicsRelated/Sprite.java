package graphicsRelated;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Sprite {
	
	private ArrayList<SpriteImage> images = new ArrayList<SpriteImage>();
	private int imageIndex; //current image
	private long currentTime; //current time in animation
	private long totalTime; //total time for one animation loop
	
	private boolean scaleImages;
	private int width;
	private int height;
	
	
	/**
	 * Create a new sprite, without any images.
	 */
	public Sprite(){
		scaleImages = false;
		totalTime = 0;
		reset();
	}
	
	/**
	 * Create a new sprite, without any images, with given dimensions.
	 * @param width
	 * @param height
	 */
	public Sprite(int width, int height){
		this();
		scaleImages = true;
		this.width = width;
		this.height = height;
	}
	
	
	
	
	/**
	 * Create a new "1-tile" sprite consisting of given images (pathnames) with given frame rate (frames / s)
	 * The sprite will be scaled to the map큦 tile size.
	 * @param frameRate How many images will be shown each second
	 * @param tileWidth tile width of the map
	 * @param pathNames The path names of the images 
	 * @return A new Sprite
	 */
	public static Sprite createSprite(int frameRate, int tileWidth, String... pathNames){
		return createSprite(frameRate, 1,1, tileWidth, pathNames); //Covers 1 tile.
	}
	
	/**
	 * Create a new sprite consisting of given images (pathnames) with given frame rate (frames / s)
	 * The sprite will be scaled to the map큦 tile size, and it큦 dimensions are specified.
	 * @param frameRate How many images will be shown each second
	 * @param width The sprite큦 width (in tiles)
	 * @param height The sprite큦 height (in tiles)
	 * @param tileWidth tile width of the map
	 * @param pathNames The path names of the images 
	 * @return A new Sprite
	 */
	public static Sprite createSprite(int frameRate, int width,int height, int tileWidth, String... pathNames){
		int frameTime = 1000/frameRate; //time (ms) for each image/frame.
		
		Sprite spr = new Sprite(width * tileWidth, height * tileWidth);
		
		for(String pathName : pathNames){
			spr.addImage(new ImageIcon(pathName).getImage(), frameTime);
			
			//TODO
			if(new ImageIcon(pathName).getImage().getWidth(null) == -1){
				throw new IllegalArgumentException("Something's wrong with image-argument.  " + pathName);
			}
			
		}
		
		return spr;
	}
	
	/**
	 * Create a new sprite consisting of given images (pathnames) with given frame rate (frames / s)
	 * The dimensions are not specified, and will be those of the image-files, until setSize() is called.
	 * @param frameRate How many images will be shown each second
	 * @param pathNames The path names of the images 
	 * @return A new Sprite
	 */
	public static Sprite createSprite(int frameRate, String... pathNames){
		int frameTime = 1000/frameRate;
		
		Sprite spr = new Sprite();
		
		for(String pathName : pathNames){
			
			Image img = new ImageIcon(pathName).getImage();
			
			//If load image failed, add a BUG-image and print error-msg
			if(img.getWidth(null) == -1){
				System.err.println("Error in Sprite.createSprite() : Something's wrong with image-argument.  " + pathName);
				img = new ImageIcon("E:/Users/Jonathan.jonathans-dator/Documents/Programmering/Eclipse-workspace/RTS proj\\sprites\\sprBUG.png").getImage();
				spr.addImage(img, frameTime);
			}else{
				spr.addImage(img, frameTime);
			}
		}
		
		return spr;
	}
	
	/**
	 * Scale all the sprite's images to given size.
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height){
		this.width = width;
		this.height = height;
		
		for(SpriteImage i : images){
			i.image = i.image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		}

		scaleImages = true;
	}
	
	/**
	 * Get the width of the sprite's images.
	 * @return
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * Get the height of the sprite's images.
	 * @return
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 * Gets the total number of images / frames in this sprite.
	 * @return
	 */
	public int getNumberOfImages(){
		return images.size();
	}
	
	/**
	 * Return a copy of this sprite
	 * @return A new Sprite, that's equal to this.
	 */
	public Sprite getCopy(){
		Sprite s;
		if(scaleImages){
			s = new Sprite(width, height);
		}else{
			s = new Sprite();
		}

		int t = 0;
		for(SpriteImage i : images){
			s.addImage(i.image, i.endTime - t);
			t += (i.endTime -t); //TODO     lade till -t
		}
		
		return s;
	}
	
	
	/**
	 * Add a new frame with a set duration.
	 * @param i The image (scene)
	 * @param t The scene duration
	 */
	public synchronized void addImage(Image i, long t){
		totalTime += t; 
		
		//If fixed size, scale the image.
		if(scaleImages){
			i = i.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		}
		images.add(new SpriteImage(i, totalTime));
	}

	
	/**
	 * Reset the sprite animation, by starting from the beginning.
	 */
	public synchronized void reset(){
		currentTime = 0;
		imageIndex = 0;
	}
	
	/**
	 * Update the sprite, changing the image when appropriate 
	 * and restarting if totalTime is exceeded
	 * @param timePassed
	 */
	public synchronized void update(long timePassed){
		
		if(images.size() > 1){
			currentTime += timePassed;

			//Restart if sprite animation is over.
			if(currentTime >= totalTime){
				reset();
			}
			
			//Update scene index.
			if(currentTime >= images.get(imageIndex).endTime){
				imageIndex++;
			}
		}
	}
	
	/**
	 * Get the current image.
	 * @return current image
	 */
	public synchronized Image getCurrentImage(){
		if(images.isEmpty()){
			return null;
		}
		
		return images.get(imageIndex).image;
	}
	
	/**
	 * A sprite consist of several SpriteImages
	 * Each SpriteImage has an image and an end time.
	 */
	public class SpriteImage{
		public Image image;
		public long endTime;

		public SpriteImage(Image img, long endTime) {
			image = img;
			this.endTime = endTime;
		}
	}
	
	
	
	
	public String toString(){
		return "Sprite (" + width + "x" + height + ") No. images: " + images.size() + ".   First img: " + images.get(0).image.toString();
	}
	
	
}