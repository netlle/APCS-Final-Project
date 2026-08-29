public class Spring extends Block {
	
	// TODO: possibly make a new class for spring & breakable block of change on player touch
	// TODO: this is almost copy paste spring 
	// TODO: not the best use of inheritance but i wanted to reuse the classes

	Animation animation; 
	private final int TOTAL_FRAMES = 2;
	private final int RESET_DELAY = (int)(17 * 1.5); // frames until spring resets
	private int resetCount = 0;
	private boolean playerHasHit = false;
	
	public Spring(int x, int y, int width, int height) {
		super(x, y, width, height, null);
		this.animation = new Animation("spring", TOTAL_FRAMES, 1, false);
		super.setImage(animation.getFrame());
	}
	
	public static Spring getSpring(int i, int j) {
		return new Spring(j*BLOCK_SIZE, i*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
	}

	public void playerTouchSpring() {
		animation.resetFrames();
		animation.addFrame();
		super.setImage(animation.getFrame());
		playerHasHit = true;
	}
	
	@Override
	public void update() {
		if (playerHasHit) {
			resetCount++;
			if (resetCount >= RESET_DELAY) {
				this.reset();
			}
		}
	}
	
	@Override
	public void reset() {
		resetCount = 0;
		animation.resetFrames();
		super.setImage(animation.getFrame());
		playerHasHit = false;
	}
}
