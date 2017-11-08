package com.sylweb.arthur.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Item {
	
	protected Rectangle position;
	protected Animation animatedSprite; 
	protected boolean visible;
	protected int typeId;
	protected float stateTime = 0.0f;
	
	public Item(float x, float y, int typeId) {
		this.typeId = typeId;
		
		float w = 0.0f;
		float h = 0.0f;
		if(this.typeId == ItemType.RED_ICE_CREAM.getTypeId()) {
			w = ItemType.RED_ICE_CREAM.getWidth();
			h = ItemType.RED_ICE_CREAM.getHeight();
			this.animatedSprite = RessourceContainer.redIceCreamAnim;
		}

		this.position = new Rectangle(x,y,w,h);
		this.visible = true;
	}
	
	public void render(SpriteBatch batch) {
		
		Rectangle recCam = new Rectangle(Dora.camera.position.x-Dora.camera.viewportWidth/2.f, 
				Dora.camera.position.y-Dora.camera.viewportHeight/2.0f,
				Dora.camera.viewportWidth, Dora.camera.viewportHeight);

		if(recCam.overlaps(this.position) && this.visible) {
			stateTime += Gdx.graphics.getDeltaTime();
			
			batch.begin();
			if(this.typeId == ItemType.RED_ICE_CREAM.getTypeId()){
				batch.draw((TextureRegion)animatedSprite.getKeyFrame(stateTime, true), position.getX(), position.getY());
			}
			batch.end();
		}
	}
	
	public Rectangle getPosition() {
		return this.position;
	}

	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return this.visible;
	}
}
