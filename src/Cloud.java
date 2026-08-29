import java.awt.Rectangle;

public class Cloud extends Block {

	public static final int CLOUD_SPEED = -3;
	private static final int CLOUD_SIZE = 20; 
	private int startingX;
	
	public Cloud(int x, int y, int width, int height, Rectangle hitBox) {
		super(x, y, width, height, "lib/cloud.png", hitBox);
		startingX = x;
	}

	public static Cloud getCloud(int i, int j) {
		// one drawing pixel = 10 actual pixels
		Rectangle hitBox = new Rectangle(j*BLOCK_SIZE, i*BLOCK_SIZE+10, BLOCK_SIZE*2, 1);
		return new Cloud(j*BLOCK_SIZE, i*BLOCK_SIZE, BLOCK_SIZE*2, CLOUD_SIZE, hitBox);
	}
	
	@Override
	public void update() {
		int newX = super.getX() + CLOUD_SPEED;
		if (newX < -BLOCK_SIZE*2) { 
			newX = 750 + BLOCK_SIZE*2; // TODO: change to be not hardcoded later
		}
		super.setX(newX); 
		super.setHitboxX(newX);
	}
	
	@Override
	public void reset() {
		super.setX(startingX);
	}
}
