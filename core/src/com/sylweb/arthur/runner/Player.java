package com.sylweb.arthur.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Player {
	
	protected float xVelocity;
	protected float yVelocity;
	
	protected float lastXDirection = 0;
	
	protected Rectangle position = null;
	
	protected boolean isMain = false;
	
	protected Texture walkingSpriteSheet = null;
	protected Texture standSpriteSheet = null;
	protected Texture jumpSpriteSheet = null;
	
	protected Animation walkingAnimLeft;
	protected Animation walkingAnimRight;
	protected Animation standAnimRight;
	protected Animation standAnimLeft;
	protected Animation jumpAnimLeft;
	protected Animation jumpAnimRight;
	
	protected boolean isJumping = false;
	protected boolean isFalling = false;
	
	protected Rectangle jumpBox = new Rectangle(0, 0, 0, 0);
	protected Rectangle hitBox = new Rectangle(0, 0, 0, 0);

	protected float xDirection = 0;
	protected float yDirection = 0;
	
	protected float stateTime = 0.0f;
	
	protected int points = 0;
	
	public Player(Rectangle pos, String walkAnimSprite, String standAnimSprite, int walkAnimFrameNb, int standAnimFrameNb){
		this.position = pos;
		walkingSpriteSheet = new Texture(Gdx.files.internal(walkAnimSprite));
		standSpriteSheet = new Texture(Gdx.files.internal(standAnimSprite));
		
		TextureRegion[][] tmp = TextureRegion.split(walkingSpriteSheet, walkingSpriteSheet.getWidth()/walkAnimFrameNb, walkingSpriteSheet.getHeight());
		TextureRegion[][] tmp1 = TextureRegion.split(walkingSpriteSheet, walkingSpriteSheet.getWidth()/walkAnimFrameNb, walkingSpriteSheet.getHeight());
		
		TextureRegion[] temp2 = new TextureRegion[walkAnimFrameNb * 1];
		TextureRegion[] temp3 = new TextureRegion[walkAnimFrameNb * 1];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < walkAnimFrameNb; j++) {
            	tmp[i][j].flip(false, false);
            	temp2[index] = tmp[i][j];
            	tmp1[i][j].flip(true, false);
            	temp3[index] = tmp1[i][j];
            	index++;
            }
        }
        walkingAnimRight = new Animation(0.1f, temp2);
        walkingAnimLeft =  new Animation(0.1f, temp3);
        walkingAnimRight.setPlayMode(PlayMode.LOOP);
        walkingAnimLeft.setPlayMode(PlayMode.LOOP);
		
        
        tmp = TextureRegion.split(standSpriteSheet, standSpriteSheet.getWidth()/standAnimFrameNb, standSpriteSheet.getHeight());
		tmp1 = TextureRegion.split(standSpriteSheet, standSpriteSheet.getWidth()/standAnimFrameNb, standSpriteSheet.getHeight());
        temp2 = new TextureRegion[standAnimFrameNb * 1];
        temp3 = new TextureRegion[standAnimFrameNb * 1];
        index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < standAnimFrameNb; j++) {
            	tmp[i][j].flip(false, false);
            	temp2[index] = tmp[i][j];
            	tmp1[i][j].flip(true, false);
            	temp3[index] = tmp1[i][j];
            	index++;
            }
        }
        standAnimRight = new Animation(0.5f, temp2); 
        standAnimLeft = new Animation(0.5f, temp3); 
        standAnimRight.setPlayMode(PlayMode.LOOP);
        standAnimLeft.setPlayMode(PlayMode.LOOP);
		
		this.lastXDirection = GameConstants.MOVE_RIGHT;
	}
	
	public Player(Rectangle pos, String walkAnimSprite, String standAnimSprite, String jumpAnimSprite, int walkAnimFrameNb, int standAnimFrameNb, int jumpAnimFrameNb){
		this(pos, walkAnimSprite, standAnimSprite, walkAnimFrameNb, standAnimFrameNb);
		
		jumpSpriteSheet = new Texture(Gdx.files.internal(jumpAnimSprite));
		
		TextureRegion[][] tmp = TextureRegion.split(jumpSpriteSheet, jumpSpriteSheet.getWidth()/jumpAnimFrameNb, jumpSpriteSheet.getHeight());
		TextureRegion[][] tmp1 = TextureRegion.split(jumpSpriteSheet, jumpSpriteSheet.getWidth()/jumpAnimFrameNb, jumpSpriteSheet.getHeight());
		
		TextureRegion[] temp2 = new TextureRegion[jumpAnimFrameNb * 1];
		TextureRegion[] temp3 = new TextureRegion[jumpAnimFrameNb * 1];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < jumpAnimFrameNb; j++) {
            	tmp[i][j].flip(false, false);
            	temp2[index] = tmp[i][j];
            	tmp1[i][j].flip(true, false);
            	temp3[index] = tmp1[i][j];
            	index++;
            }
        }
        jumpAnimRight = new Animation(0.1f, temp2);
        jumpAnimLeft =  new Animation(0.1f, temp3);
        jumpAnimRight.setPlayMode(PlayMode.NORMAL);
        jumpAnimLeft.setPlayMode(PlayMode.NORMAL);
	}
	
	public void render(SpriteBatch batch, ShapeRenderer shape) {
		
		stateTime += Gdx.graphics.getDeltaTime();
		
		batch.begin();
		
		Rectangle drawingPos = new Rectangle(this.position.getX(), this.position.getY(), this.position.getWidth(), this.position.getHeight());
		if(!isMain) {
			drawingPos.setX(drawingPos.getX());
			drawingPos.setY(drawingPos.getY());
		}
		
		if(isJumping){
			if(this.lastXDirection== GameConstants.MOVE_RIGHT || this.xDirection == GameConstants.MOVE_RIGHT) {
				batch.draw((TextureRegion)jumpAnimRight.getKeyFrames()[3], drawingPos.getX(), drawingPos.getY());
			}
			else if(this.lastXDirection== GameConstants.MOVE_LEFT || this.xDirection == GameConstants.MOVE_LEFT) {
				batch.draw((TextureRegion)jumpAnimLeft.getKeyFrames()[3], drawingPos.getX(), drawingPos.getY());
			}
		}else if(isFalling) {
			if(this.lastXDirection== GameConstants.MOVE_RIGHT || this.xDirection == GameConstants.MOVE_RIGHT) {
				batch.draw((TextureRegion)jumpAnimRight.getKeyFrames()[6], drawingPos.getX(), drawingPos.getY());
			}
			else if(this.lastXDirection== GameConstants.MOVE_LEFT || this.xDirection == GameConstants.MOVE_LEFT) {
				batch.draw((TextureRegion)jumpAnimLeft.getKeyFrames()[6], drawingPos.getX(), drawingPos.getY());
			}
		}else{

			if(this.xDirection < 0) {
				batch.draw((TextureRegion) walkingAnimLeft.getKeyFrame(stateTime, true), drawingPos.getX(), drawingPos.getY());
			}
			else if(this.xDirection > 0) {
				batch.draw((TextureRegion)walkingAnimRight.getKeyFrame(stateTime, true), drawingPos.getX(), drawingPos.getY());
			}
			else if(this.xDirection == 0) {
				if(this.yDirection> 0 || this.yDirection < 0) {
					if(this.lastXDirection > 0 || this.lastXDirection == 0) {
						batch.draw((Texture)walkingAnimRight.getKeyFrame(stateTime, true), drawingPos.getX(), drawingPos.getY());
					}
					else if(this.lastXDirection < 0) {
						batch.draw((TextureRegion)walkingAnimLeft.getKeyFrame(stateTime, true), drawingPos.getX(), drawingPos.getY());
					}
				}
				else {
					if(this.lastXDirection > 0 || this.lastXDirection == 0) {
						batch.draw((TextureRegion)standAnimRight.getKeyFrame(stateTime), drawingPos.getX(), drawingPos.getY());
					}
					else if(this.lastXDirection < 0) {
						batch.draw((TextureRegion)standAnimLeft.getKeyFrame(stateTime), drawingPos.getX(), drawingPos.getY());
					}

				}
			}
		}
		
		batch.end();
		
		//** Draw hitboxes (jump and damage)
		if(GameSettings.DEBUG_MODE) {
			if(isMain) {
				if(jumpBox != null){
					shape.begin(ShapeType.Line);
					shape.rect(jumpBox.getX(), jumpBox.getY(), jumpBox.getWidth(), jumpBox.getHeight());
					shape.end();
				}
				if(hitBox != null){
					shape.begin(ShapeType.Line);
					shape.rect(hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
					shape.end();
				}
			}
			else {
				if(jumpBox!=null){
					drawingPos.setX(this.jumpBox.getX());
					drawingPos.setY(this.jumpBox.getY());
					drawingPos.setWidth(this.jumpBox.getWidth());
					drawingPos.setHeight(this.jumpBox.getHeight());
					shape.begin(ShapeType.Line);
					shape.rect(drawingPos.getX(), drawingPos.getY(), drawingPos.getWidth(), drawingPos.getHeight());
					shape.end();
				}
				if(hitBox!=null){
					drawingPos.setX(this.hitBox.getX());
					drawingPos.setY(this.hitBox.getY());
					drawingPos.setWidth(this.hitBox.getWidth());
					drawingPos.setHeight(this.hitBox.getHeight());
					shape.begin(ShapeType.Line);
					shape.rect(drawingPos.getX(), drawingPos.getY(), drawingPos.getWidth(), drawingPos.getHeight());
					shape.end();
				}
			}
		}
	}
	
	public void update() {

		Vector2 center = new Vector2();
		this.position.getCenter(center);
		
		//** moving logic
		if(isMain){
			
			if(center.x > 0 || this.xDirection == GameConstants.MOVE_RIGHT){
				this.position.setX(this.position.getX()+(xVelocity*xDirection));
				this.jumpBox.setX(this.jumpBox.getX()+(xVelocity*xDirection));
				this.hitBox.setX(this.hitBox.getX()+(xVelocity*xDirection));
				this.hitBox.setWidth(2*0.1f*this.position.getWidth());

				if (center.x > Dora.camera.position.x){
					Dora.camera.position.x = center.x;
				}
				else if(center.x < Dora.camera.position.x && Dora.camera.position.x > Dora.camera.viewportWidth/2.0f){
					Dora.camera.position.x = center.x;
				}
			}

			//** when not scrolling -> just move the character
			this.position.setY(this.position.getY()+(this.yDirection*this.yVelocity));
			this.jumpBox.setY(this.jumpBox.getY()+(yVelocity*yDirection));
			this.hitBox.setY(this.hitBox.getY()+(yVelocity*yDirection));
			
			
			if (center.y > Dora.camera.position.y){
				Dora.camera.position.y = center.y;
			}
			else if(center.y < Dora.camera.position.y && Dora.camera.position.y > Dora.camera.viewportHeight/2.0f){
				Dora.camera.position.y = center.y;
			}
			
		}
		else {
			this.position.setX(this.position.getX()+(xVelocity*xDirection));
			this.position.setY(this.position.getY()+(yVelocity*yDirection));
			this.jumpBox.setX(this.jumpBox.getX()+(xVelocity*xDirection));
			this.jumpBox.setY(this.jumpBox.getY()+(yVelocity*yDirection));
			this.hitBox.setX(this.hitBox.getX()+(xVelocity*xDirection));
			this.hitBox.setY(this.hitBox.getY()+(yVelocity*yDirection));
		}
		
	
		//** jumping logic
		if(this.isJumping) {
			if(yVelocity > 0.0f) yVelocity -= 0.5f;
			else stopJump();
		}

		//** falling logic 
		if(isFalling) {
			if(yVelocity < 10.0f) yVelocity += 0.5f;
		}
		
	}
	
	public void setXDirection(float direction) {
		
		if(direction != 0) this.lastXDirection = direction;
		this.xDirection = direction;
	}
	
	public void setYDirection(int direction) {
		//** during jumping we can't control it anymore
		if(isJumping) return; 
		
		this.yDirection = direction;
	}
	
	public float getXDirection() {
		return this.xDirection;
	}
	
	public float getYDirection() {
		return this.yDirection;
	}
	
	public void jump() {
		if((!isJumping && !isFalling)) {
		
			//** change direction
			this.yDirection = GameConstants.MOVE_UP;
			
			//** load jump velocity
			yVelocity = 15.0f;
			
			this.isJumping = true;
		}
	}
	
	public boolean isJumping() {
		return this.isJumping;
	}
	
	public boolean isFalling() {
		return this.isFalling;
	}
	
	public Rectangle getPosition() {
		return this.position;
	}
	
	public void setJumpBox(Rectangle jumpHitBox) {
		this.jumpBox = jumpHitBox;
	}
	
	public Rectangle getJumpBox() {
		if(isMain) return this.jumpBox;
		else {
			
			return new Rectangle(this.jumpBox.getX(),
								 this.jumpBox.getY(),
								 this.jumpBox.getWidth(),
								 this.jumpBox.getHeight());
		}
	}
	
	public void stopJump() {
		this.yDirection = GameConstants.MOVE_NOT;
		this.isJumping = false;
		this.isFalling = true;
	}
	
	public void fall() {
		
		if(!isJumping) {
			this.isFalling=true;
			this.yDirection = GameConstants.MOVE_DOWN;
		}
	}
	
	public void stopFall() {
		this.yDirection = GameConstants.MOVE_NOT;
		yVelocity = 0.0f;
		this.isFalling = false;
	}
	
	public void setPosition(Rectangle rec){
		this.position.setX(rec.getX());
		this.position.setY(rec.getY());
	}

	public void setAsMain(boolean m){
		this.isMain = m;
	}
	
	public void setVelocity(float velocity) {
		this.xVelocity = velocity;
	}
	
	public Rectangle getHitBox() {
		if(isMain) return hitBox;
		else {
			return new Rectangle(this.hitBox.getX(),
					 this.hitBox.getY(),
					 this.hitBox.getWidth(),
					 this.hitBox.getHeight());
		}
	}

	public void setHitBox(Rectangle hitBox) {
		this.hitBox = hitBox;
	}
	
	public void smallBounce() {
	
		//** change direction
		this.yDirection = GameConstants.MOVE_UP;
		
		//** load velocity
		yVelocity = 7.0f;
		
		this.isJumping = true;
	}
	
	
	public void addPoints(int points) {
		this.points += points;
	}
}
