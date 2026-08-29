import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Gameboard extends JPanel {

	private final int LEVEL_DIMENSIONS = 15;
	
	private int currLevel = 1;
	private Madeline madeline;
	private ArrayList<Level> levels = new ArrayList<>();
	private Timer gameTimer;
	// NOT STARTED, STARTED, PAUSED, OVER
	private String gameStatus = "NOT STARTED"; 
	
	private ImageIcon background;
	private ImageIcon startScreen;
	private ImageIcon helpScreen;
	private ImageIcon endingScreen;

	public Gameboard() {
		this.createLevels();
		this.madeline = new Madeline(this, this.getCurrLevel().getStartingX(), 
				this.getCurrLevel().getStartingY());
		this.background = new ImageIcon("lib/animated_background.gif");
		this.startScreen = new ImageIcon("lib/start_screen.gif");
		this.helpScreen = new ImageIcon("lib/help_screen.png");
		this.endingScreen = new ImageIcon("lib/ending_screen.gif");
		
	}
	
	public void startGame() {
		gameStatus = "STARTED";
		gameTimer = new Timer();
		// timer runs once ever 17 ms => 60 fps
		gameTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (gameStatus.equals("STARTED")) { // maybe not the best solution BUT
					madeline.update();
					repaint();
				} 
			}
		}, 0, 17); 
	}
	
	public void nextLevel() {
		currLevel++; 
		this.resetMadelinePosition();
		if (currLevel > levels.size()) { 
			gameStatus = "OVER";
		}
		this.repaint();
	}
	
	public void resetMadelinePosition() {
		int x = this.getCurrLevel().getStartingX();
		int y = this.getCurrLevel().getStartingY();
		madeline.setX(x);
		madeline.setY(y);
	}
	
	private void createLevels() {
		try {
			String line;
			BufferedReader reader;
			boolean startRecording = false;
			String[][] currLevel = new String[LEVEL_DIMENSIONS][LEVEL_DIMENSIONS];
			
			int line_index = 0;
			int startingX = 0; 
			int startingY = 0;  	
			reader = new BufferedReader(new FileReader("levels.txt"));
			while ((line = reader.readLine()) != null) {
				if (line.contains("#")) {
					startRecording = true;
					startingX = Integer.valueOf(line.substring(line.indexOf("(")+1, line.indexOf(",")));
					startingY = Integer.valueOf(line.substring(line.indexOf(",")+1, line.indexOf(")")));
				} else if (startRecording) {
					if (line_index < 15) {
						for (int i = 0; i < currLevel.length; i++) {
							currLevel[line_index][i] = line.charAt(i) + "";
						}
						
						line_index++;
						if (line_index == LEVEL_DIMENSIONS) {
							levels.add(new Level(currLevel, startingX, startingY)); 
							currLevel = new String[LEVEL_DIMENSIONS][LEVEL_DIMENSIONS];
							line_index = 0;
							startRecording = false;
						}
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) { 
			System.out.println("File levels.txt could not be found.");
		} catch (IOException e) {
			System.out.println("Error processing files");
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (gameStatus.equals("NOT STARTED")) {
			g.drawImage(startScreen.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
		} else if (gameStatus.equals("STARTED")) {
			if (background != null) {
				g.drawImage(background.getImage(), 0, 0, this.getWidth(), this.getHeight(), this); 
			}	
			for (Block block : this.getLevelBlocks()) {
				block.draw(g);
			}
			madeline.draw(g);
		} else if (gameStatus.equals("PAUSED")) {
			g.drawImage(helpScreen.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
		} else if (gameStatus.equals("OVER")) {
			g.drawImage(endingScreen.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}

	public Madeline getMadeline() {
		return madeline;
	}

	public Level getCurrLevel() {
		int levelNum = (currLevel > levels.size()) ? levels.size() - 1 : currLevel - 1;
		return levels.get(levelNum);
	}
	
	public String getGameStatus() {
		return gameStatus;
	}
	
	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}
	
	public ArrayList<Block> getLevelBlocks() {
		return this.getCurrLevel().getBlocks();
	}
}
