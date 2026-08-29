import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animation {
	
	private BufferedImage[] sprites; 
	private int framesCount = 0;
	private int frameDelay;
	private boolean loopFrames;
	
	public Animation(String folder, int totalFrames, int frameDelay, boolean loopFrames) {
		this.frameDelay = frameDelay;
		this.loopFrames = loopFrames;
		this.populateSprites(folder, totalFrames);
	}
	
	public BufferedImage getFrame() {
		int frameNumber = framesCount / frameDelay;
		if (frameNumber >= sprites.length) {
			if (!loopFrames) {
				 return null;
			}
		}
		return this.sprites[frameNumber % sprites.length];
	}
	
	public void addFrame() {
		this.framesCount++;
	}
	
	public void nextSprite() {
		this.framesCount = frameDelay * (int) ((framesCount / frameDelay) + 1);
	}
	
	public void resetFrames() {
		this.framesCount = 0;
	}
	
	private void populateSprites(String folder, int totalFrames) {
		try {
			this.sprites = new BufferedImage[totalFrames];
			for (int i = 0; i < totalFrames; i++) {
				String filePath = "lib/" + folder + "/frame-" + i + ".png";
				sprites[i] = ImageIO.read(new File(filePath));
			}
		} catch (IOException e) {
			System.out.println("Issue processing image at " + folder);
		}
	}
	
}
