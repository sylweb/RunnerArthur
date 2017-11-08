package com.sylweb.arthur.runner;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class Level {

	protected int tileWidth = 0;
	protected int tileHeight =0;
	protected int mapXElement = 0;
	protected int mapYElement = 0;
	protected int nbOfTilesInSet=0;
	
	protected Tile map[][];
	protected Rectangle hitBoxes[][];
	public ArrayList<Ennemy> ennemies = new ArrayList<Ennemy>();
	
	public Level(int levelNb){
		switch(levelNb) {
		
		case 1 : 
			tileWidth=90;
			tileHeight=90;
			mapXElement=100;
			mapYElement=100;
			nbOfTilesInSet=3;
			try{
				readMap("dora-lvl1_land.csv");
				readEnnemies("dora-lvl1_ennemies.csv");
			}catch(Exception ex) {
				System.exit(-1);
			}
			break;
			
			
			default : System.exit(1);
		}
	}
	
	private void readMap(String mapData) throws Exception{
		
		String cvsSplitBy = ",";
		int data[][] = new int[mapXElement][mapYElement];

		FileHandle file = Gdx.files.internal(mapData);
		String text = file.readString();
		
		String[] lines = text.split("\r\n");
		
		for(int i=0 ; i < lines.length; i++){
			
			String[] values = lines[i].split(cvsSplitBy);
			for(int j=0; j < values.length; j++) {
				
				data[i][j]=Integer.valueOf(values[j]);
			}
		}
		
		//** Calculate land hitboxes
		hitBoxes = new Rectangle[mapXElement][mapYElement];
		map = new Tile[mapXElement][mapYElement];
		for(int i=0; i < mapYElement; i++) {
			for(int j=0; j < mapXElement; j++) {
				if(data[i][j] == -1) {
					hitBoxes[i][j] = null;
					map[i][j]=null;
				}
				else {
					hitBoxes[i][j] = new Rectangle(j*tileWidth,(mapYElement-(i+1))*tileHeight+tileHeight-10, tileWidth, 10);
					map[i][j]= new Tile(j*tileWidth, (mapYElement-(i+1))*tileHeight, tileWidth, tileHeight, data[i][j]);
				}
				
			}
		}

	}
	
	private void readEnnemies(String ennemyData) throws Exception{
		
		String cvsSplitBy = ",";

		int data[][] = new int[mapXElement][mapYElement];
		
		FileHandle file = Gdx.files.internal(ennemyData);
		String text = file.readString();
		
		String[] lines = text.split("\r\n");
		
		for(int i=0 ; i < lines.length; i++){
			
			String[] values = lines[i].split(cvsSplitBy);
			for(int j=0; j < values.length; j++) {
				
				data[i][j]=Integer.valueOf(values[j]);
			}
		}
		
		//** Calculate position for each ennemy
		for(int i=0; i < mapYElement; i++) {
			for(int j=0; j < mapXElement; j++) {
				if(data[i][j] != -1) {
					float x = j*tileWidth;
					float y = (mapYElement-(i+1))*tileHeight;
					switch(data[i][j]){
					
						case 0 : x = x-(Bee.SPRITE_WIDTH-tileWidth);
								 y = y+(2*Bee.SPRITE_HEIGHT-tileHeight);
								 ennemies.add(new Bee(x,y));
								 break;
								 
						case 1 : x = x-(Pirate.SPRITE_WIDTH-tileWidth);
							     ennemies.add(new Pirate(x,y));
							     break;
							     
						case 2 : x = x;
					             ennemies.add(new IceCreamTruck(x, y));
					             break;
					}
				}
			}
		}

	}
	
	public void render(SpriteBatch batch, ShapeRenderer shape){

		Rectangle recCam = new Rectangle(Dora.camera.position.x-Dora.camera.viewportWidth/2.f, 
                Dora.camera.position.y-Dora.camera.viewportHeight/2.0f,
                Dora.camera.viewportWidth, Dora.camera.viewportHeight);
		
		//** display all tiles that are visible (within camera range and not transparent)
		for(int i=0; i < mapYElement; i++) {
			for(int j=0; j < mapXElement; j++) {
				if(map[i][j] == null) continue;

				if(recCam.overlaps(map[i][j])) {
					batch.begin();
					batch.draw((TextureRegion)RessourceContainer.levelTiles.getKeyFrames()[map[i][j].id], map[i][j].getX(), map[i][j].getY());
					batch.end();

					if(GameSettings.DEBUG_MODE && hitBoxes[i][j]!=null) {

						float x= hitBoxes[i][j].getX();
						float y= hitBoxes[i][j].getY();
						float w = hitBoxes[i][j].getWidth();
						float h = hitBoxes[i][j].getHeight();
						shape.begin(ShapeType.Line);
						shape.rect(x,y,w,h);
						shape.end();
					}
				}
			}
		}
		
		
		
		//** render ennemies
		for(int i=0 ; i < ennemies.size(); i++) {
			ennemies.get(i).render(batch, shape);
		}
	}
	
	public void update() {
		for(int i=0 ; i < ennemies.size(); i++) {
			ennemies.get(i).update();
		}
	}
	
	public ArrayList<Rectangle> getHitBoxes(){
		Rectangle recCam = new Rectangle(Dora.camera.position.x-Dora.camera.viewportWidth/2.f, 
                Dora.camera.position.y-Dora.camera.viewportHeight/2.0f,
                Dora.camera.viewportWidth, Dora.camera.viewportHeight);
		
		
		//** return only hitboxes that are currently on the camera view
		ArrayList<Rectangle> rec = new ArrayList<Rectangle>();
		for(int i = 0; i < mapYElement; i++){
			for(int j=0; j < mapXElement; j++) {
				if(hitBoxes[i][j]!=null) {
					float x= hitBoxes[i][j].getX();
					float y= hitBoxes[i][j].getY();
					float w = hitBoxes[i][j].getWidth();
					float h = hitBoxes[i][j].getHeight();
					Rectangle temp = new Rectangle(x,y,w,h);
					if(recCam.overlaps(temp)) rec.add(temp);
				}
			}
		}
		return rec;
		
		
	}
	
	public int getMapXElement() {
		return mapXElement;
	}

	public int getMapYElement() {
		return mapYElement;
	}

	public ArrayList<Ennemy> getEnnemies() {
		return ennemies;
	}
}
