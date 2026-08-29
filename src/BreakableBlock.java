import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BreakableBlock extends Block {

	private Animation animation;
	private final int TOTAL_FRAMES = 3;
	private final int FRAME_DELAY = (int) (17 * 1.1);
	
	private int resetCounter = 0;
	private boolean isBreaking = false;
	private boolean blockBroken = false;
	
	// TODO: not the best use of inheritance & close is kinda unclean
	// TODO: draw function used as an update function
	public BreakableBlock(int x, int y, int width, int height) {
		super(x, y, width, height, null);
		this.animation = new Animation("breakable_blocks", TOTAL_FRAMES, FRAME_DELAY, false);
		super.setImage(animation.getFrame());
	}
	
	public static BreakableBlock getBreakableBlock(int i, int j) {
		return new BreakableBlock(j*BLOCK_SIZE, i*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
	}
	
	@Override
	public void update() {
		if (isBreaking) {
			animation.addFrame();
			BufferedImage currFrame = animation.getFrame();
			super.setImage(currFrame);
			if (currFrame == null) {
				blockBroken = true;
			}
		}

		if (blockBroken) {	
			resetCounter++;
			if (resetCounter >= FRAME_DELAY * TOTAL_FRAMES * 1.5) { 
				this.reset();
			}
		}
	}
	
	@Override
	public void reset() {
		animation.resetFrames();
		super.setImage(animation.getFrame());
		resetCounter = 0;
		isBreaking = false;
		blockBroken = false;
	}
	
	public void draw(Graphics g) {
		if (!blockBroken) {	
			super.draw(g);
		}
	}

	public void playerTouchBlock() {
		if (!isBreaking) {
			animation.nextSprite();
		}
		isBreaking = true;
	}
	
	public boolean getBlockBroken() {
		return blockBroken;
	}
}
