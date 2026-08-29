import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DashTrail {

	private int x, y;
	private int framesLeft;
	private int frameLife;
	private BufferedImage image;
	
	public DashTrail(int x, int y, int frameLife) {
		this.x = x;
		this.y = y;
		this.frameLife = frameLife;
		this.framesLeft = frameLife;
		
		try {
			image = ImageIO.read(new File("lib/madeline/dash-trail.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Composite original = g2d.getComposite();
		if (image != null) {
			float alpha = (float)(framesLeft + frameLife) / (frameLife + frameLife);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2d.drawImage(image, x, y, 50, 50, null); // TODO: hardcoded size (just madeline size)
			g2d.setComposite(original);
		}
		framesLeft--;
	}
	
	public boolean hasFramesLeft() {
		return framesLeft > 0;
	}
	
}
