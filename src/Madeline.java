import java.awt.*;
import java.awt.image.BufferedImage;

public class Madeline {
	
	private int x, y; 
	private double xspeed, yspeed;
	private Rectangle hitBox;
	private Gameboard gameboard;
	private boolean keyLeft, keyRight, keyJump, keyDash, keyUp;
	
	private boolean facingRight = true;
	private boolean isFalling = true; 
	private boolean canDash = true; 
	private boolean isDashing = false;
	private boolean isWallJumping = false;
	
	private int dashTime = 0;
	private int wallJumpTime = 0;
	private int springJumpTime = 0;
	
	private final double GRAVITY = 0.55;
	private final double JUMP_SPEED = 10;
	private final int MAX_GRAVITY_VELOCITY = 11;
	
	// TODO: these numbers are not relative to each other
	private final double WALL_JUMP_SPEED = 11.5;
	private final double SPRING_SPEED = 8; 
	private final double DASH_SPEED = 11.5;
	private final int WALL_JUMP_LENGTH = 10;
	private final int SPRING_JUMP_LENGTH = 8;
	private final int DASH_LENGTH = 10;
	
	// movement buffers 
	private final int COYOTE_TIME = 8; 
	private int coyoteTimeCounter;
	private final int WALL_JUMP_BUFFER = 2;
	 
	private final int MADELINE_WIDTH = (int)(50 * 0.9);
	private final int MADELINE_HEIGHT = (int)(50 * 0.8);
	
	private MadelineAnimation animationController;
	
	public Madeline(Gameboard gameboard, int x, int y) {
		this.x = x;
		this.y = y;
		this.gameboard = gameboard;
		this.hitBox = new Rectangle(x, y, MADELINE_WIDTH, MADELINE_HEIGHT); 
		this.animationController = new MadelineAnimation(this);
	}
	
	// MAIN movement/physics function
	public void update() {
		this.handleDash();
		this.handleWallJump();
		this.handleMovement();
		
		for (Block wall : gameboard.getLevelBlocks()) {
			wall.update();
		}
		if (isFalling) yspeed += GRAVITY;
		
		hitBox.x += xspeed;
		this.handleHorizontalCollision();
		hitBox.y += yspeed;
		this.handleVerticalCollision();

		if (isFalling) {
			coyoteTimeCounter--;
		} else {
			coyoteTimeCounter = COYOTE_TIME;
		}

		x += xspeed;
		y += yspeed;
		
		// left and right borders
		if (x < 0) {
			x = 0;
		} else if (x > gameboard.getWidth() - hitBox.width) {
			x = gameboard.getWidth() - hitBox.width;
		}
		
		hitBox.x = x;
		hitBox.y = y;
		this.checkNewLevel();
		this.checkVoidDeath();
	}
	
	public void draw(Graphics g) {
		for (DashTrail trail : animationController.getDashTrails()) { 
			trail.draw(g);
		}
		animationController.cleanTrails();
		
		BufferedImage currentFrame = animationController.getFrame();
		g.drawImage(currentFrame, x, y, MADELINE_WIDTH, MADELINE_HEIGHT, null);
	}
	
	// helper functions below
	// TODO: these functions are a bit repetitive, but i didn't have the time to refactor them 
	// TODO: maybe seperate out block behavior into block classes
	private void handleHorizontalCollision() {
		// handle breaking blocks first so that >1 blocks can break at once
		for (Block wall : gameboard.getLevelBlocks()) {
			if (hitBox.intersects(wall.getHitBox())) {
				if (wall instanceof BreakableBlock) {
					if (!((BreakableBlock) wall).getBlockBroken()) {
						((BreakableBlock) wall).playerTouchBlock(); 
					}
				} 
			}
		}
		
		for (Block wall : gameboard.getLevelBlocks()) {
			if (hitBox.intersects(wall.getHitBox())) {
				if (wall instanceof Spike) { 
					this.madelineReset();
					gameboard.resetMadelinePosition();
					break;
				} else if (wall instanceof BreakableBlock) {
					if (((BreakableBlock) wall).getBlockBroken()) {
						continue;  // TODO
					}
				} else if (wall instanceof DashRefill) {
					if (!((DashRefill) wall).getRefillUsed()) {
						((DashRefill) wall).playerTouchDashRefill();
						canDash = true;
					}
					continue;
				} else if (wall instanceof Cloud) {
					continue; 
				}
				
				hitBox.x -= xspeed;
				while (!wall.getHitBox().intersects(hitBox)) {
					hitBox.x += Math.signum(xspeed);
				}
				hitBox.x -= Math.signum(xspeed);
				xspeed = 0;
				x = hitBox.x;
	
				if (isFalling && (keyRight || keyLeft) && yspeed > 0) {
					yspeed = 3; // slowed falling if touching wall
				} 
			}
		}
	}
	
	private void handleVerticalCollision() {
		// handle breaking blocks first so that >1 blocks can break at once
		for (Block wall : gameboard.getLevelBlocks()) {
			if (hitBox.intersects(wall.getHitBox())) {
				if (wall instanceof BreakableBlock) {
					if (!((BreakableBlock) wall).getBlockBroken()) {
						((BreakableBlock) wall).playerTouchBlock(); 
					}
				} 
			}
		}
		
		for (Block wall : gameboard.getLevelBlocks()) {
			if (hitBox.intersects(wall.getHitBox())) {
				if (wall instanceof Spike) {
					this.madelineReset();
					gameboard.resetMadelinePosition();
					break;
				} else if (wall instanceof BreakableBlock) {
					if (((BreakableBlock) wall).getBlockBroken()) {
						continue;  // TODO
					}
				} else if (wall instanceof Spring) {
					((Spring) (wall)).playerTouchSpring();
					springJumpTime = SPRING_JUMP_LENGTH;
				} else if (wall instanceof DashRefill) {
					if (!((DashRefill) wall).getRefillUsed()) {
						((DashRefill) wall).playerTouchDashRefill();
						canDash = true;
					}
					continue;
				} else if (wall instanceof Cloud) {
					// continue if either jumping from below or not the feet which intersect with cloud
					Rectangle feetHitBox = new Rectangle(hitBox.x, hitBox.y + hitBox.height - 15, hitBox.width, 15);
				    if (!feetHitBox.intersects(wall.getHitBox()) || yspeed < 0) {
				    	continue;
				    }
				} 
				
				hitBox.y -= yspeed;
				while (!(wall.getHitBox().intersects(hitBox))) {
					hitBox.y += Math.signum(yspeed);
				}
				hitBox.y -= Math.signum(yspeed);
				yspeed = 0;
				y = hitBox.y;
				
				// reset dash if touching ground
				if (wall.getHitBox().y == hitBox.y + hitBox.height) {
					canDash = true;
					isFalling = false;
				}	
			}			
		}	
		

		// handle travelling on clouds if touching it
		for (Block wall : gameboard.getLevelBlocks()) {
			if (wall instanceof Cloud) {
				
			    Rectangle cloudHitBox = wall.getHitBox();
			    boolean standingOnCloud = 
			    		hitBox.x + hitBox.width >= cloudHitBox.x &&
	                    hitBox.x <= cloudHitBox.x + cloudHitBox.width && 
	                    hitBox.y + hitBox.height <= cloudHitBox.y &&
	                    hitBox.y + hitBox.height >= cloudHitBox.y;
	                    
	            // check if moving by cloud speed will intersect with a wall
	            boolean block = false;
	            Rectangle futureHitBox = new Rectangle(hitBox) ;
	            futureHitBox.x += Cloud.CLOUD_SPEED;
                for (Block w : gameboard.getLevelBlocks()) {
                	if (futureHitBox.intersects(w.getHitBox())) {
                		block = true;
                	}
                }
                
			    if (standingOnCloud && !block) {
			        x += Cloud.CLOUD_SPEED;
			    }
			}
		}
	}
	
	private void handleDash() {
		if (keyDash && canDash) {
			isDashing = true;
			canDash = false;
			isFalling = true;
			dashTime = DASH_LENGTH;
			
			if (keyUp && keyRight && !keyLeft) {
				yspeed = -DASH_SPEED / Math.sqrt(1.5); 
				xspeed = DASH_SPEED / Math.sqrt(2);
			} else if (keyUp && keyLeft && !keyRight) {
				yspeed = -DASH_SPEED / Math.sqrt(1.5);
				xspeed = -DASH_SPEED / Math.sqrt(2);
			} else if (keyUp) {
				yspeed = -DASH_SPEED;
				xspeed = 0;
			} else if (keyRight && !keyLeft) {
				xspeed = DASH_SPEED;
				yspeed = 0;
				isFalling = false;
			} else if (keyLeft && !keyRight) {
				xspeed = -DASH_SPEED;
				yspeed = 0;
				isFalling = false;
			} else { // if not given a direction, dash in direction player is facing
				xspeed = facingRight ? DASH_SPEED : -DASH_SPEED;
				yspeed = 0;
				isFalling = false;
			}
			keyDash = false;
		}
	}
	
	private void handleWallJump() {
		if (!isWallJumping) {
			for (Block wall : gameboard.getLevelBlocks()) {
				
				// TODO: in the future, would make an instance variable for if can pass through
				if ((wall instanceof BreakableBlock && ((BreakableBlock) wall).getBlockBroken())
						|| wall instanceof Cloud || wall instanceof DashRefill) {
					continue;
				}
					
				Rectangle wallHitbox = wall.getHitBox();
				Rectangle rightSensor = new Rectangle(wallHitbox.x + wallHitbox.width, wallHitbox.y, WALL_JUMP_BUFFER, wallHitbox.height);
				Rectangle leftSensor = new Rectangle(wallHitbox.x - WALL_JUMP_BUFFER, wallHitbox.y, WALL_JUMP_BUFFER, wallHitbox.height);
				boolean touchLeft = hitBox.intersects(leftSensor);
				boolean touchRight = hitBox.intersects(rightSensor);
				
				if (touchLeft && !touchRight && keyJump && yspeed > 0.1) { 
					isWallJumping = true;
					wallJumpTime = WALL_JUMP_LENGTH;
					yspeed = -WALL_JUMP_SPEED / Math.sqrt(1.5);
					xspeed = -WALL_JUMP_SPEED / Math.sqrt(2);
					facingRight = false; 
				} else if (!touchLeft && touchRight && keyJump && yspeed > 0.1) {
					isWallJumping = true;
					wallJumpTime = WALL_JUMP_LENGTH;
					yspeed = -WALL_JUMP_SPEED / Math.sqrt(1.5);
					xspeed = WALL_JUMP_SPEED / Math.sqrt(2);
					facingRight = true; 
				}
			}
		}
	}
	
	private void handleMovement() {
		if (isDashing) { 
			coyoteTimeCounter = 0;
			dashTime--;
			animationController.addDashTrail(x, y);
			if (dashTime <= 0) {
				isDashing = false;
				isFalling = true;
			}
		} else if (isWallJumping) {
			wallJumpTime--;
			if (wallJumpTime <= 0) {
				isWallJumping = false;
			}
		} else {
			isFalling = true;
			if ((keyLeft && keyRight) || (!keyLeft && !keyRight)) {
				xspeed *= 0.8;
				animationController.resetFrames(); // if not moving, reset walking animation
			} else {
				animationController.addFrame();
				if (keyLeft) {
					xspeed--;
					facingRight = false;
				} else {
					xspeed++;
					facingRight = true;
				}
			}

			if (xspeed > 0 && xspeed < 1) xspeed = 0;
			if (xspeed < 0 && xspeed > -1) xspeed = 0;
			if (xspeed > 5) xspeed = 5;
			if (xspeed < -5) xspeed = -5;
			if (yspeed > MAX_GRAVITY_VELOCITY) yspeed = MAX_GRAVITY_VELOCITY; 
			if (yspeed < -MAX_GRAVITY_VELOCITY) yspeed = -MAX_GRAVITY_VELOCITY;
			
			if (springJumpTime > 0) {
				springJumpTime--;
				yspeed = -SPRING_SPEED;
			} else if (keyJump) {
				if (coyoteTimeCounter > 0) {
					coyoteTimeCounter = 0;
					yspeed = -JUMP_SPEED;
					keyJump = false; 
					// TODO: add trail animation
				}
			} 
		}
	}
	
	// death + resetting blocks
	private void madelineReset() {
		xspeed = 0; 
		yspeed = 0;
		facingRight = true;
		isFalling = true;
		canDash = true;
		isDashing = false;
		isWallJumping = false;
		
		for (Block block : gameboard.getLevelBlocks()) {
			block.reset();
		}
	}
	
	private void checkNewLevel() {
		if (y + MADELINE_HEIGHT <= 0) {
			gameboard.nextLevel();
			hitBox.x = x;
			hitBox.y = y;
			this.madelineReset();
		}
	}
	
	private void checkVoidDeath() {
		if (y > gameboard.getHeight()) {
			this.madelineReset();
			gameboard.resetMadelinePosition();
		}
	}
	
	public String getFrameKey() {
		String key = "";
		key += canDash ? "RED-" : "BLUE-";
		key += facingRight ? "WALK-RIGHT" : "WALK-LEFT";
		return key;
	}
	
	public Rectangle getHitBox() {
		return this.hitBox;
	}
	
	public void changeX(int x) {
		this.x += x;
	}
	
	public void changeY(int y) {
		this.y += y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setKeyDash(boolean keyDash) {
		this.keyDash = keyDash;
	}
	
	public void setKeyLeft(boolean keyLeft) {
		this.keyLeft = keyLeft;
	}
	
	public void setKeyRight(boolean keyRight) {
		this.keyRight = keyRight;
	}
	
	public void setKeyUp(boolean keyUp) {
		this.keyUp = keyUp;
	}
	
	public void setKeyJump(boolean keyJump) {
		this.keyJump = keyJump;
	}
}
