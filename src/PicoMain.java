import javax.swing.JFrame;

public class PicoMain {
	
	private static final int FRAME_SIZE = 50 * 15;
	
	public static void main(String[] args) {
		JFrame window = new JFrame("Celeste Pico-8 (but worse)");
		Gameboard game = new Gameboard();
		new PicoListener(game);
		window.setContentPane(game);
		window.setSize(FRAME_SIZE, FRAME_SIZE);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false); 
        window.setVisible(true);
        game.requestFocusInWindow();
	}	
}
