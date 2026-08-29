import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MadelineAnimation {

	private Madeline madeline;
	private final int WALK_FRAME_DELAY = 6;
	private final int WALK_FRAME_NUM = 4;
	private final int JUMP_TRAIL_NUM = 3;
	private final int JUMP_TRAIL_FRAME_DELAY = 10;
	private final int DASH_TRAIL_LENGTH = 7;
	
	private Map<String, Animation> animations = new HashMap<>();
	private ArrayList<DashTrail> trails = new ArrayList<>();
	
	public MadelineAnimation(Madeline madeline) {
		this.madeline = madeline;
		this.populateFrames();
	}
	
	public void addDashTrail(int x, int y) {
		trails.add(new DashTrail(x, y, DASH_TRAIL_LENGTH));
	}

	public ArrayList<DashTrail> getDashTrails() {
		return new ArrayList<>(trails);
	}
	
	public void cleanTrails() {
		for (int i = 0; i < trails.size(); i++) {
			if (!trails.get(i).hasFramesLeft()) {
				trails.remove(i);
				i--;
			}
		}
	}
	
	public BufferedImage getFrame() {
		return animations.get(madeline.getFrameKey()).getFrame();
	}
	
	// TODO: i probably should have used a spritesheet or smth like that buti didnt and now im here
	private void populateFrames() { 
		animations.put("RED-WALK-RIGHT", new Animation("madeline/red-hair/walk/facing-right", WALK_FRAME_NUM, WALK_FRAME_DELAY, true));
		animations.put("RED-WALK-LEFT", new Animation("madeline/red-hair/walk/facing-left", WALK_FRAME_NUM, WALK_FRAME_DELAY, true));
		animations.put("BLUE-WALK-RIGHT", new Animation("madeline/blue-hair/walk/facing-right", WALK_FRAME_NUM, WALK_FRAME_DELAY, true));
		animations.put("BLUE-WALK-LEFT", new Animation("madeline/blue-hair/walk/facing-left", WALK_FRAME_NUM, WALK_FRAME_DELAY, true));
		animations.put("JUMP-TRAIL", new Animation("madeline/jump-trail", JUMP_TRAIL_NUM, JUMP_TRAIL_FRAME_DELAY, false));
	}
	
	public void addFrame() {
		animations.get(madeline.getFrameKey()).addFrame();
	}
	
	public void resetFrames() {
		animations.get(madeline.getFrameKey()).resetFrames();
	}
}
