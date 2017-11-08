package com.sylweb.arthur.runner;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Pirate extends Ennemy {
	
	public static float SPRITE_WIDTH = 123.0f;
	public static float SPRITE_HEIGHT = 135.0f;
	public static float VELOCITY_X = 2.0f;
	public static int ANIMATION_SPEED = 50;
	public static int ANIM_NB_OF_IMG = 6;
	public static String ANIM_IMG = "pirate_walk.png";
	public static String DEATH_IMG = "pirate_dead";
	public static float DELTA_X_MOVE =90.0f;
	public static float DELTA_Y_MOVE = 0.0f;
	public static int LIFE = 1;
	
	public Pirate(float x, float y) {
		super(x,y,SPRITE_WIDTH,SPRITE_HEIGHT,LIFE,DELTA_X_MOVE,DELTA_Y_MOVE,
				RessourceContainer.pirateAnimLeft,
				RessourceContainer.pirateAnimRight, 
				RessourceContainer.pirateDeadSpriteLeft,
				RessourceContainer.pirateDeadSpriteRight,
				VELOCITY_X);
		
		Vector2 vec = new Vector2();
		this.position.getCenter(vec);
		
		float hx = (float)(vec.x-0.2f*this.position.getWidth());
		float hy = (float)(vec.y+0.05f*this.position.getHeight());
		float hw = 0.4f*this.position.getWidth();
		float hh = 0.4f*this.position.getHeight();
		this.hitBox = new Rectangle(hx,hy,hw,hh);
	}

}
