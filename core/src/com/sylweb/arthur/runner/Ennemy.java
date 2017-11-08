package com.sylweb.arthur.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public abstract class Ennemy {
	
	protected Rectangle position;
	protected Rectangle hitBox;
	
	protected float deltaXMove;
	protected float deltaYMove;
	protected float xOrig;
	protected float yOrig;
	protected float VELOCITY_X=0.0f;
	protected float VELOCITY_Y=0.0f;
	protected float origVelocityX;
	
	protected Animation animLeft = null;
	protected Animation animRight = null;
	protected Texture deadSpriteRight = null;
	protected Texture deadSpriteLeft = null;
	
	protected float stateTime = 0.0f;

	
	protected int life = 1;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param life
	 * @param deltaX
	 * @param deltaY
	 * @param ressourceAnim
	 * @param timeForAnim
	 * @param nbOfAnimFrame
	 * @throws Exception
	 */
	public Ennemy(float x, float y, float w, float h, int life, float deltaX, float deltaY, Animation animLeft, Animation animRight, Texture deadSpriteLeft, Texture deadSpriteRight, float velocityX) {
		
		this.animLeft = animLeft;
		this.animRight = animRight;
		this.deadSpriteLeft = deadSpriteLeft;
		this.deadSpriteRight= deadSpriteRight;
		
		
		this.life = life;
		this.deltaXMove = deltaX;
		this.deltaYMove = deltaY;
		
		
		this.position = new Rectangle(x,y,w,h);
		
		Vector2 vec = new Vector2();
		position.getCenter(vec);
		this.xOrig = vec.x;
		this.yOrig = vec.y;
		
		this.VELOCITY_X = -1*velocityX;
		origVelocityX = this.VELOCITY_X;
	}
	
	public void update(){

		Vector2 center = new Vector2();
		this.position.getCenter(center);

		if(deltaXMove > 0 && life > 0) {
			if(center.x > xOrig+deltaXMove
					|| center.x < xOrig-deltaXMove) {
				this.VELOCITY_X *=-1 ;
			}
		}

		if(deltaYMove > 0 && life > 0) {
			if(center.y > yOrig+deltaYMove
					|| center.y < yOrig-deltaYMove) {
				this.VELOCITY_Y *=-1 ;
			}
		}

		this.position.setCenter(new Vector2(center.x+this.VELOCITY_X, center.y+this.VELOCITY_Y));
		center = new Vector2();
		this.hitBox.getCenter(center);
		this.hitBox.setCenter(new Vector2(center.x+this.VELOCITY_X, center.y+this.VELOCITY_Y));

		if(life <=0 && this.VELOCITY_Y > 10.0f*GameConstants.MOVE_DOWN) this.VELOCITY_Y += 0.5f*GameConstants.MOVE_DOWN;
	}
	
	public void render(SpriteBatch batch, ShapeRenderer shape) {

		stateTime += Gdx.graphics.getDeltaTime();
		
		Rectangle recCam = new Rectangle(Dora.camera.position.x-Dora.camera.viewportWidth/2.f, 
				Dora.camera.position.y-Dora.camera.viewportHeight/2.0f,
				Dora.camera.viewportWidth, Dora.camera.viewportHeight);

		if(recCam.overlaps(this.position)) {

			batch.begin();

			//** display ennemy (only if it is in the camera field)
			if(this.life <= 0) {
				if(VELOCITY_X > 0) {
					batch.draw(this.deadSpriteRight, this.position.getX(), this.position.getY());
				}
				else batch.draw(this.deadSpriteLeft, this.position.getX(), this.position.getY());
			}
			else if(this.VELOCITY_X > 0) {
				TextureRegion currentFrame = (TextureRegion) animRight.getKeyFrame(stateTime, true);
				batch.draw(currentFrame, this.position.getX(), this.position.getY());
			}
			else if(this.VELOCITY_X < 0) {
				TextureRegion currentFrame = (TextureRegion) animLeft.getKeyFrame(stateTime, true);
				batch.draw(currentFrame, this.position.getX(), this.position.getY());
			}

			batch.end();

			//** for debug : display hitbox
			if(hitBox != null && GameSettings.DEBUG_MODE) {
				shape.begin(ShapeType.Line);
				shape.rect(this.hitBox.getX(),
						   this.hitBox.getY(),
						   this.hitBox.getWidth(),this.hitBox.getHeight());

				shape.end();
			} 

		}
	}
	
	public Rectangle getHitBox(){
		return new Rectangle(this.hitBox.getX(),
				this.hitBox.getY(),
				this.hitBox.getWidth(),
				this.hitBox.getHeight());

	}
	
	public void hit() {
		this.life -= 1;
		if(life <= 0) {
			this.VELOCITY_X = 0.0f;
			this.VELOCITY_Y = 4.0f*GameConstants.MOVE_DOWN;
			Vector2 center=new Vector2(); 
			this.position.getCenter(center);
			Dora.items.add(new Item(center.x-0.5f*ItemType.RED_ICE_CREAM.getWidth(),this.position.y,ItemType.RED_ICE_CREAM.getTypeId()));
		}
	}
	
	public Rectangle getSpriteRect() {
		return this.position;
	}
	
	
	public void reset(int life){
		this.life = life;
		this.position.setCenter(new Vector2(xOrig, yOrig));
		this.VELOCITY_Y = 0.0f;
		this.VELOCITY_X = origVelocityX;
	}

}
