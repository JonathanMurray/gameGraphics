package screenRelated;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class ScreenManager {
	
	private java.awt.GraphicsDevice vc; //Access to monitor / screen

	/**
	 * Create a new screen manager
	 */
	public ScreenManager(){
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = e.getDefaultScreenDevice();
	}
	
	/**
	 * Get all compatible display modes of the graphics card
	 * @return An array containing the display modes
	 */
	public DisplayMode[] getCompatibleDisplayModes(){
		return vc.getDisplayModes();
	}
	
	/**
	 * Get the first compatible display mode in given array of display modes.
	 * @param modes The display modes that are given
	 * @return The first found compatible display mode
	 * null if no display mode was compatible
	 */
	public DisplayMode getFirstCompatibleMode(DisplayMode[] modes){
		DisplayMode[] validModes = vc.getDisplayModes();
		
		for(int i = 0; i<modes.length; i++){
			for(int j = 0; j<validModes.length; j++){
				if(displayModesEqual(modes[i], validModes[j])){			
					//return modes[i];
					return validModes[j];
				}
			}
		}
		
		//None of the given display modes was compatible!
		return null;
	}
	
	/**
	 * Check if two display modes are equal
	 * @param m1 The first display mode
	 * @param m2 The second display mode
	 * @return true if they are equal, else false.
	 */
	public boolean displayModesEqual(DisplayMode m1, DisplayMode m2){
		//Compare resolution
		if(m1.getWidth() != m2.getWidth() || m1.getHeight() != m2.getHeight()){
			return false;
		}
		
		//Compare bit depth
		if(m1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && m2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && m1.getBitDepth() != m2.getBitDepth()){
			return false;
		}
		
		//Compare refresh rate
		if(m1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && m2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && m1.getRefreshRate() != m2.getRefreshRate()){
			return false;
		}

		return true;
	}
	
	/**
	 * Get current display mode of the graphics card 
	 * @return current display mode
	 */
	public DisplayMode getCurrentDisplayMode(){
		return vc.getDisplayMode();
	}
	
	/**
	 * Set full screen.
	 * @param dm Display mode to be used.
	 */
	public void setFullScreen(DisplayMode dm){
		JFrame f = new JFrame();
		f.setUndecorated(true);
		f.setIgnoreRepaint(true);
		f.setResizable(false);
		vc.setFullScreenWindow(f);
		
		if(dm != null && vc.isDisplayChangeSupported()){
			vc.setDisplayMode(dm);
		}
		
		f.createBufferStrategy(2);
	}
	
	/**
	 * Get Graphics2D to use for drawing
	 * Has to be in full screen mode.
	 * @return Graphics2D, null if device is not in full screen mode.
	 */
	public Graphics2D getGraphics(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			BufferStrategy s = w.getBufferStrategy();
			return (Graphics2D) s.getDrawGraphics();
		}else{
			return null;
		}
	}
	
	/**
	 * update display
	 */
	public void update(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			BufferStrategy s = w.getBufferStrategy();
			if(!s.contentsLost()){ //If s has something to show, show it.
				s.show();
			}
			
		}
	}
	
	/**
	 * Get full screen window
	 * @return reference to full screen window
	 */
	public Window getFullScreenWindow(){
		return vc.getFullScreenWindow();
	}
	
	/**
	 * Get width of window
	 * @return width of current full screen window
	 */
	public int getWidth(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			return w.getWidth();
		}
		return 0;
	}
	
	/**
	 * Get height of window
	 * @return height of current full screen window
	 */
	public int getHeight(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			return w.getHeight();
		}
		return 0;
	}
	
	/**
	 * Exit full screen mode.
	 */
	public void restoreScreen(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			w.dispose();
		}
		vc.setFullScreenWindow(null);
	}
	
	/**
	 * Create and return a compatible image.
	 * Must be in fullscreen mode.
	 * @param w Width
	 * @param h Height
	 * @param t Transparency 
	 * @return The compatible image, or null if not in full screen mode.
	 */
	public BufferedImage createCompatibleImage(int w, int h, int t){
		Window window = vc.getFullScreenWindow();
		if(window != null){
			GraphicsConfiguration gc = window.getGraphicsConfiguration();
			return gc.createCompatibleImage(w,h,t);
		}
		return null;
	}
	
}
