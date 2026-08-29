import static java.util.Map.entry;
import java.util.Map;

public class Wall extends Block {
	
	public Wall(int x, int y, int width, int height, String imagePath) {
		super(x, y, width, height, imagePath); 
	}
	
	// TODO: make the solution a little less um silly and more optimal later
	public static Wall getWall(String[][] layoutData, int i, int j) {
		String blockKey = getWallKey(layoutData, i, j);
		Map<String, String> blockMap = Map.ofEntries(
			entry("0111", "block-mu"),
			entry("1110", "block-mb"),
			entry("1101", "block-mr"),
			entry("1011", "block-ml"),
			entry("0011", "block-ul"),
			entry("0101", "block-ur"),
			entry("1010", "block-bl"), 
			entry("1100", "block-br")
		);
		
		String blockBackground = "block-m"; // default fallback
		if (blockMap.get(blockKey) != null) {
			blockBackground = blockMap.get(blockKey);
		} else {
			// alternate & non-static cases
			if (blockKey.equals("1111")) 
				blockBackground = (Math.random() <= 0.05) ? "block-m-random" : "block-m";
			if (blockKey.charAt(0) == '0' && blockKey.charAt(3) == '0') 
				blockBackground = "block-single-h";
			if (blockKey.charAt(1) == '0' && blockKey.charAt(2) == '0')
				blockBackground = "block-single-v";
		}
		
		String image = "lib/blocks/" + blockBackground + ".png";
		return new Wall(j*BLOCK_SIZE, i*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, image);
	}
	
	private static String getWallKey(String[][] layoutData, int i, int j) {
		boolean blockAbove = i-1 < 0 || layoutData[i-1][j].equals("x");
		boolean blockBelow = i+1 >= layoutData.length || layoutData[i+1][j].equals("x");
		boolean blockLeft = j-1 < 0 || layoutData[i][j-1].equals("x");
		boolean blockRight = j+1 >= layoutData[0].length || layoutData[i][j+1].equals("x");
		
		String key = boolToStr(blockAbove) + boolToStr(blockLeft) + boolToStr(blockRight) + boolToStr(blockBelow);
		return key;
	}
	
	private static String boolToStr(boolean b) {
		return b ? "1" : "0";
	}
}
