import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Block {
	
	public static final int BLOCK_SIZE = 50;
	private int x, y;
	private int width, height;
	private Rectangle hitBox;
	private BufferedImage image;
	
	public Block(int x, int y, int width, int height, String imagePath) {
		this(x, y, width, height, imagePath, new Rectangle(x, y, width, height));
	}
	
	public Block(int x, int y, int width, int height, String imagePath, Rectangle hitBox) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hitBox = hitBox;
		
		try {
			if (imagePath != null) {
				this.image = ImageIO.read(new File(imagePath));
			}
		} catch (IOException e) {
			System.out.println("Issue processing image at " + imagePath);
		}
	}
	
	public void draw(Graphics g) {
		if (image != null) {
			g.drawImage(image, x, y, width, height, null);
		} else {
			g.setColor(Color.BLUE);
			g.fillRect(x, y, width, height);
		}
	}
	
	// update & reset: no behavior unless otherwise specified in subclasses
	public void update() {
		return;
	}
	
	public void reset() {
		return;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	
	}
	
	public void setHitboxX(int x) {
		this.hitBox.x = x;
	}
	
	public void setHitboxY(int y) {
		this.hitBox.y = y;
	}
	
	public Rectangle getHitBox() {
		return this.hitBox;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}	
}
