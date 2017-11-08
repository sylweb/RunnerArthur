package com.sylweb.arthur.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class RessourceContainer {
	
	public static Texture levelTileSet = null;
	public static  Animation levelTiles;
	
	public static  Texture beeSprites;
	public static  Texture beeDeadSpriteRight;
	public static  Texture beeDeadSpriteLeft;
	public static  Animation beeAnimLeft;
	public static  Animation beeAnimRight;
	
	public static  Texture pirateSprites;
	public static  Texture pirateDeadSpriteRight;
	public static  Texture pirateDeadSpriteLeft;
	public static  Animation pirateAnimLeft;
	public static  Animation pirateAnimRight;
	
	public static  Texture icTruckSprites;
	public static  Texture icTruckDeadSprite;
	public static  Animation icTruckAnimLeft;
	public static  Animation icTruckAnimRight;
	
	public static Animation redIceCreamAnim; 
	
	public static Texture arrowRightSprite;
	public static Texture arrowLeftSprite;
	
	public RessourceContainer() {
		
	}
	
	public static void loadRessources(int levelNb) {
			try {
				
				//Load ennemies
				beeSprites = new Texture(Gdx.files.internal(Bee.ANIM_IMG));
				beeDeadSpriteRight = new Texture(Gdx.files.internal(Bee.DEATH_IMG+"_right.png"));
				beeDeadSpriteLeft = new Texture(Gdx.files.internal(Bee.DEATH_IMG+"_left.png"));
				
				pirateSprites = new Texture(Gdx.files.internal(Pirate.ANIM_IMG));
				pirateDeadSpriteRight = new Texture(Gdx.files.internal(Pirate.DEATH_IMG+"_right.png"));
				pirateDeadSpriteLeft = new Texture(Gdx.files.internal(Pirate.DEATH_IMG+"_left.png"));
				
				icTruckSprites = new Texture(Gdx.files.internal(IceCreamTruck.ANIM_IMG));
				icTruckDeadSprite = new Texture(Gdx.files.internal(IceCreamTruck.DEATH_IMG));
				
				arrowRightSprite = new Texture(Gdx.files.internal("arrow_right_red.png"));
				arrowLeftSprite = new Texture(Gdx.files.internal("arrow_left_blue.png"));
				
				//RED ICE CREAM
				Texture redIceCreamSprites = new Texture(Gdx.files.internal("ice_cream_red.png"));
				TextureRegion[][] tmp = TextureRegion.split(redIceCreamSprites, (int)ItemType.RED_ICE_CREAM.getWidth(), (int)ItemType.RED_ICE_CREAM.getHeight());
				TextureRegion[] temp2 = new TextureRegion[ItemType.RED_ICE_CREAM.getNbOfAnimFrame()];
				int index = 0;
				for (int i = 0; i < 1; i++) {
					for (int j = 0; j < ItemType.RED_ICE_CREAM.getNbOfAnimFrame(); j++) {
						tmp[i][j].flip(false, false);
						temp2[index] = tmp[i][j];
						index++;
					}
				}
				redIceCreamAnim = new Animation(0.1f, temp2);
				redIceCreamAnim.setPlayMode(PlayMode.LOOP);
				
				//LEVELS
				switch(levelNb) {
					case 1: levelTileSet = new Texture(Gdx.files.internal("land_lvl1_90_90.png"));
							tmp = TextureRegion.split(levelTileSet, 90, 90);
							temp2 = new TextureRegion[3 /*COLS*/ * 1 /*ROWS*/];
					        index = 0;
					        for (int i = 0; i < 1; i++) {
					            for (int j = 0; j < 3; j++) {
					            	temp2[index++] = tmp[i][j];
					            }
					        }
					        levelTiles = new Animation(0.025f, temp2); 
					
					        break;
				}
				
			}
			catch(Exception ex ) {
				System.exit(-1);
			}
			
	        
	        TextureRegion[][] tmp = TextureRegion.split(beeSprites, (int)Bee.SPRITE_WIDTH, (int)Bee.SPRITE_HEIGHT);
			TextureRegion[][] tmp1 =  TextureRegion.split(beeSprites, (int)Bee.SPRITE_WIDTH, (int)Bee.SPRITE_HEIGHT);
			
			TextureRegion[] temp2 = new TextureRegion[Bee.ANIM_NB_OF_IMG * 1];
			TextureRegion[] temp3 = new TextureRegion[Bee.ANIM_NB_OF_IMG * 1];
	        int index = 0;
	        for (int i = 0; i < 1; i++) {
	            for (int j = 0; j < Bee.ANIM_NB_OF_IMG; j++) {
	            	tmp[i][j].flip(false, false);
	            	temp2[index] = tmp[i][j];
	            	tmp1[i][j].flip(true, false);
	            	temp3[index] = tmp1[i][j];
	            	index++;
	            }
	        }
	        beeAnimRight = new Animation(0.1f, temp3);
	        beeAnimLeft =  new Animation(0.1f, temp2);
	        beeAnimRight.setPlayMode(PlayMode.LOOP);
	        beeAnimLeft.setPlayMode(PlayMode.LOOP);
			
	        tmp = TextureRegion.split(pirateSprites, (int)Pirate.SPRITE_WIDTH, (int)Pirate.SPRITE_HEIGHT);
			tmp1 =  TextureRegion.split(pirateSprites, (int)Pirate.SPRITE_WIDTH, (int)Pirate.SPRITE_HEIGHT);
	        
	        temp2 = new TextureRegion[Pirate.ANIM_NB_OF_IMG * 1];
			temp3 = new TextureRegion[Pirate.ANIM_NB_OF_IMG * 1];
	        index = 0;
	        for (int i = 0; i < 1; i++) {
	            for (int j = 0; j < Pirate.ANIM_NB_OF_IMG; j++) {
	            	tmp[i][j].flip(false, false);
	            	temp2[index] = tmp[i][j];
	            	tmp1[i][j].flip(true, false);
	            	temp3[index] = tmp1[i][j];
	            	index++;
	            }
	        }
	        pirateAnimRight = new Animation(0.1f, temp3);
	        pirateAnimLeft =  new Animation(0.1f, temp2);
	        pirateAnimRight.setPlayMode(PlayMode.LOOP);
	        pirateAnimLeft.setPlayMode(PlayMode.LOOP);
	        
	        tmp = TextureRegion.split(icTruckSprites, (int)IceCreamTruck.SPRITE_WIDTH, (int)IceCreamTruck.SPRITE_HEIGHT);
			tmp1 =  TextureRegion.split(icTruckSprites, (int)IceCreamTruck.SPRITE_WIDTH, (int)IceCreamTruck.SPRITE_HEIGHT);
	        
	        temp2 = new TextureRegion[IceCreamTruck.ANIM_NB_OF_IMG * 1];
			temp3 = new TextureRegion[IceCreamTruck.ANIM_NB_OF_IMG * 1];
	        index = 0;
	        for (int i = 0; i < 1; i++) {
	            for (int j = 0; j < IceCreamTruck.ANIM_NB_OF_IMG; j++) {
	            	tmp[i][j].flip(false, false);
	            	temp2[index] = tmp[i][j];
	            	tmp1[i][j].flip(true, false);
	            	temp3[index] = tmp1[i][j];
	            	index++;
	            }
	        }
	        icTruckAnimRight= new Animation(0.1f, temp2);
	        icTruckAnimLeft =  new Animation(0.1f, temp3);
	        icTruckAnimRight.setPlayMode(PlayMode.LOOP);
	        icTruckAnimLeft.setPlayMode(PlayMode.LOOP);
	}

}
