import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Level {

	private ArrayList<Block> blocks = new ArrayList<>();
	private int startingX, startingY;
	
	public Level(String[][] layoutData, int startingX, int startingY) {
		this.startingX = startingX*Block.BLOCK_SIZE;
		this.startingY = startingY*Block.BLOCK_SIZE;
		
		for (int i = 0; i < layoutData.length; i++) {
			for (int j = 0; j < layoutData[i].length; j++) {
				this.setBlock(layoutData, i, j);
			}
		}
		
		this.sortBlocks();
	}
	
	private void sortBlocks() {
		Collections.sort(blocks, new Comparator<Block>() {
			@Override
			public int compare(Block b1, Block b2) {
				return Integer.compare(getBlockPriority(b1), getBlockPriority(b2));
			}
		});
	}
	
	private int getBlockPriority(Block b) {
		if (b instanceof Wall) return 2;
		if (b instanceof Spike) return 1;
		return 0;
	}
	
	// TODO: factory methods for the simple stuff is kinda weird but its ok
	private void setBlock(String[][] layoutData, int i, int j) {
		switch (layoutData[i][j]) {
			case "x":
				blocks.add(Wall.getWall(layoutData, i, j));
				break;
			case "^":
				blocks.add(Spike.getSpike(layoutData, i, j));
				break;
			case "b": 
				blocks.add(BreakableBlock.getBreakableBlock(i, j));
				break;
			case "=":
				blocks.add(Spring.getSpring(i, j));
				break;
			case "o":
				blocks.add(DashRefill.getDashRefill(i, j));
				break;
			case "c":
				blocks.add(Cloud.getCloud(i, j));
				break;
		}
	}
	
	public int getStartingX() { 
		return this.startingX;
	}
	
	public int getStartingY() { 
		return this.startingY;
	}
	
	
	public ArrayList<Block> getBlocks() {
		return this.blocks;
	}
}
