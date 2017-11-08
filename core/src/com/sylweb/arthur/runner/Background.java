package com.sylweb.arthur.runner;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Background {

	protected Texture sprite;
	protected Rectangle position;
	
	public Background() {
		sprite = new Texture(Gdx.files.internal("back.png"));
		this.position  = new Rectangle(0,0,sprite.getWidth(), sprite.getHeight());
	}
	
	public void render(SpriteBatch batch, ShapeRenderer shape) {
		batch.begin();
		batch.draw(this.sprite,position.getX(),position.getY());
		batch.end();
	}
	
	public void update() {
		this.position.x = Dora.camera.position.x-Dora.camera.viewportWidth/2.0f;
		this.position.y = Dora.camera.position.y-Dora.camera.viewportHeight/2.0f;
	}
}
