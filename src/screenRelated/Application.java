package screenRelated;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public abstract class Application 
	extends KeyAdapter{

	//Should cover commonly found display mode settings.
	private static final DisplayMode modes[] = {
		new DisplayMode(1280,1024, 32, 0),
		new DisplayMode(1280,1024, 24, 0),
		new DisplayMode(1280,1024, 16, 0),
		new DisplayMode(800,600, 32, 0),
		new DisplayMode(800,600, 24, 0),
		new DisplayMode(800,600, 16, 0),
		new DisplayMode(640,480, 32, 0),
		new DisplayMode(640,480, 24, 0),
		new DisplayMode(640,480, 16, 0)
	};
	

	
	
	
	private static final int FPS_RATE = 30;
	private static final int SLEEP_TIME = 1000/FPS_RATE;
	
	private boolean running;
	protected ScreenManager screen;
	
	/**
	 * Create a new Application
	 */
	public Application(){
		screen = new ScreenManager();
		DisplayMode dm = screen.getFirstCompatibleMode(modes);

		
		
		screen.setFullScreen(dm);
		
		Window w = screen.getFullScreenWindow();
		w.setFont(new Font("Arial", Font.PLAIN, 20));
		w.setBackground(Color.BLACK);
		w.setForeground(Color.WHITE);
		
		w.addKeyListener(this);
	}
	
	/**
	 * Start the game loop
	 */
	public void run(){
		try{
			running = true;
			gameLoop();
		}finally{
			screen.restoreScreen();
		}
	}
	
	/**
	 * Stop the game loop
	 */
	public void stop(){
		running = false;
	}
	
	/**
	 * update application and graphics. Internally calls update() and draw().
	 */
	public void gameLoop(){
		long totalTime = System.currentTimeMillis(); 
		
		while(running){
			long timePassed = System.currentTimeMillis() - totalTime; //time passed since last a.update()
			totalTime += timePassed;
			update(timePassed);
			
			Graphics2D g = screen.getGraphics();
			draw(g);
			g.dispose();
			screen.update();
			
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public abstract void update(long timePassed);
	public abstract void draw(Graphics2D g);
	
	/**
	 * Exit on escape
	 */
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
	}
}

