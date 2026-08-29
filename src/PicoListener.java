import java.awt.event.*;

public class PicoListener implements KeyListener {

	private Gameboard gameboard;
	
	public PicoListener(Gameboard board) {
		gameboard = board;
		gameboard.addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (gameboard.getGameStatus().equals("OVER")) {
			return;
		} else if (gameboard.getGameStatus().equals("NOT STARTED")) {
			gameboard.startGame();
			return;
		} else if (gameboard.getGameStatus().equals("PAUSED")) {
			if (code == KeyEvent.VK_ESCAPE) {
				gameboard.setGameStatus("STARTED");
			}
			return;
		}
		
		// else game is going
		Madeline player = gameboard.getMadeline();
		if (code == KeyEvent.VK_LEFT) {
			player.setKeyLeft(true);
		} else if (code == KeyEvent.VK_RIGHT) {
			player.setKeyRight(true);
		} else if (code == KeyEvent.VK_C) {
			player.setKeyJump(true);
		} else if (code == KeyEvent.VK_UP) {
			player.setKeyUp(true);
		} else if (code == KeyEvent.VK_X) {
			player.setKeyDash(true);
		} else if (code == KeyEvent.VK_Q) {
			gameboard.setGameStatus("PAUSED");
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		Madeline player = gameboard.getMadeline();
		if (code == KeyEvent.VK_LEFT) {
			player.setKeyLeft(false);
		} else if (code == KeyEvent.VK_RIGHT) {
			player.setKeyRight(false);
		} else if (code == KeyEvent.VK_C) {
			player.setKeyJump(false);
		} else if (code == KeyEvent.VK_UP) {
			player.setKeyUp(false);
		} else if (code == KeyEvent.VK_X) {
			player.setKeyDash(false);
		}	
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
