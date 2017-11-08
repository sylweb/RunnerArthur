package com.sylweb.arthur.runner;

import com.badlogic.gdx.math.Rectangle;

public class IceCreamTruck extends Ennemy {
	
	public static float SPRITE_WIDTH = 416.0f;
	public static float SPRITE_HEIGHT = 250.0f;
	public static float VELOCITY_X = 1.0f;
	public static int ANIMATION_SPEED = 60;
	public static int ANIM_NB_OF_IMG = 1;
	public static String ANIM_IMG = "ice_cream_truck.png";
	public static String DEATH_IMG = "ice_cream_truck.png";
	public static float DELTA_X_MOVE = 100.0f;
	public static float DELTA_Y_MOVE = 0.0f;
	public static int LIFE = 1;
	
	public IceCreamTruck(float x, float y) {
		super(x,y,SPRITE_WIDTH, SPRITE_HEIGHT, LIFE, DELTA_X_MOVE, DELTA_Y_MOVE,
				  RessourceContainer.icTruckAnimLeft,
				  RessourceContainer.icTruckAnimRight,
				  RessourceContainer.icTruckDeadSprite,
				  RessourceContainer.icTruckDeadSprite,
				  VELOCITY_X);
			
			float hx =0;
			float hy =0;
			float hw =0;
			float hh =0;
			this.hitBox = new Rectangle(hx,hy,hw,hh);
	}
	
	
}
