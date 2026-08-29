public class Spike extends Block {

	private static final int SPIKE_SIZE = 25;
	
	public Spike(int x, int y, int width, int height, String imagePath) {
		super(x, y, width, height, imagePath); 
	}
	
	public static Spike getSpike(String[][] layoutData, int i, int j) {
		boolean blockAbove = i-1 < 0 || layoutData[i-1][j].equals("x");
		boolean blockBelow = i+1 >= layoutData.length || layoutData[i+1][j].equals("x");
		boolean blockLeft = j-1 < 0 || layoutData[i][j-1].equals("x");
		boolean blockRight = j+1 >= layoutData[0].length || layoutData[i][j+1].equals("x");
		
		if (blockAbove) { 
			String image = "spikes/spike-u-" + getSpikeKey(layoutData, i, j, true) + ".png";
			return new Spike(j*BLOCK_SIZE, i*BLOCK_SIZE, BLOCK_SIZE, SPIKE_SIZE, image);
		} else if (blockBelow) {
			String image = "spikes/spike-b-" + getSpikeKey(layoutData, i, j, true) + ".png";
			return new Spike(j*BLOCK_SIZE, i*BLOCK_SIZE + (BLOCK_SIZE - SPIKE_SIZE), BLOCK_SIZE, SPIKE_SIZE, image);
		} else if (blockLeft) {
			String image = "spikes/spike-r-" + getSpikeKey(layoutData, i, j, true) + ".png";
			return new Spike(j*BLOCK_SIZE, i*BLOCK_SIZE, SPIKE_SIZE, BLOCK_SIZE, image);
		} else if (blockRight){
			String image = "spikes/spike-l-" + getSpikeKey(layoutData, i, j, true) + ".png";
			return new Spike(j*BLOCK_SIZE + (BLOCK_SIZE - SPIKE_SIZE), i*BLOCK_SIZE, SPIKE_SIZE, BLOCK_SIZE, image);
		}
		return null;
	}
	
	private static int getSpikeKey(String[][] layoutData, int i, int j, boolean checkHorizontal) {		
		int count = 0;
		if (checkHorizontal) {
			while (j >= 0 && layoutData[i][j].equals("^")) {
				count++;
				j--;
			}
		} else {
			while (i >= 0 && layoutData[i][j].equals("^")) {
				count++;
				i--;
			}
		}
		return count % 2;
	}
	
}
