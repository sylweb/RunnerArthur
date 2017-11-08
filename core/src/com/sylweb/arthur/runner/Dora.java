package com.sylweb.arthur.runner;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Dora extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	ShapeRenderer shape;
	BitmapFont font;
	
	Viewport viewport;
	
	Background myBack;
	Player myDora;
	Player myBabouch;
	Level lev1;
	Music mainTheme;
	
	public static ArrayList<Item> items = new ArrayList<Item>();
	
	public static OrthographicCamera camera;
	
	
	@Override
	public void create () {

		Gdx.input.setCatchBackKey(true);
		
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
	    
		
		camera= new OrthographicCamera();
		viewport = new StretchViewport(1280, 720, camera);
		viewport.apply();
		
		camera.position.set(viewport.getWorldWidth()/2f, viewport.getWorldWidth()/2f, 0);
		
		myBack = new Background();

		createDoraPlayer(true);

		RessourceContainer.loadRessources(1);

		lev1= new Level(1);

		font = new BitmapFont();
		
		mainTheme = Gdx.audio.newMusic(Gdx.files.internal("dora_main_theme.ogg"));
		mainTheme.setLooping(true);
		mainTheme.play();
		 
	}

	@Override
	public void render () {
		update();
		
		//** Camera management
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		shape.setProjectionMatrix(camera.combined);

		//** Draw background
		myBack.render(batch, shape);

		//** Draw level
		lev1.render(batch, shape);

		//** Draw dora
		myDora.render(batch, shape);

		//** Draw Babouch
		if(myBabouch!=null) myBabouch.render(batch, shape);
		
		for(int i=0; i < items.size(); i++) {
			items.get(i).render(batch);
		}
		

		//For debug or HMI
		batch.begin();
		
		if(GameSettings.DEBUG_MODE) {
			Vector2 center = new Vector2();
			myDora.getPosition().getCenter(center);

			float x_pos = camera.position.x - camera.viewportWidth/2f;
			float y_pos = camera.position.y + camera.viewportHeight/2f -10;
			font.draw(batch, "dora_x = "+center.x,x_pos , y_pos);
			y_pos -= 30;
			font.draw(batch, "dora_y = "+center.y,x_pos, y_pos);
			y_pos -= 20;
			font.draw(batch, "dora_y_sens = "+myDora.getYDirection(),x_pos,y_pos);
			y_pos -= 20;
			font.draw(batch, "dora_x_sens = "+myDora.getXDirection(),x_pos,y_pos);
			y_pos -= 20;
			font.draw(batch, "dora_fall = "+myDora.isFalling(),x_pos,y_pos);
			y_pos -= 20;
			font.draw(batch, "dora_jump = "+myDora.isJumping(),x_pos,y_pos);
			y_pos -= 20;
			font.draw(batch, "cam_x = "+camera.position.x,x_pos,y_pos);
			y_pos -= 20;
			font.draw(batch, "cam_y = "+camera.position.y,x_pos,y_pos);
			y_pos -= 20;
			font.draw(batch, "accell_x = "+Gdx.input.getAccelerometerX(),x_pos,y_pos);
			y_pos -= 20;
			font.draw(batch, "accell_y = "+Gdx.input.getAccelerometerY(),x_pos,y_pos);
			y_pos -= 20;
			font.draw(batch, "accell_z = "+Gdx.input.getAccelerometerZ(),x_pos,y_pos);
		}
		
		//** Player HMI
		batch.draw(RessourceContainer.arrowLeftSprite,camera.position.x - camera.viewportWidth/2f,camera.position.y - camera.viewportHeight/2f -40);
		batch.draw(RessourceContainer.arrowRightSprite,camera.position.x - camera.viewportWidth/2f + RessourceContainer.arrowLeftSprite.getWidth() + 50 ,camera.position.y - camera.viewportHeight/2f -40);
	
		batch.end();

	}
	
	public void update() {

		//** Test collision
		collisionManager();
		
		myBack.update();
		
		//** update character
		//** allways keep that update first as it moves the camera view
		myDora.update();
		if(myBabouch!=null) myBabouch.update();

		//** update level (especially level hitboxes)
		lev1.update();
		
		testKeys();
		
	}
	
	public void collisionManager() {
		
		//** level and player collision
		boolean doraFall = true;
		boolean babouchFall = true;
		
		ArrayList<Rectangle> landHitBoxes = lev1.getHitBoxes();
		if(landHitBoxes != null && landHitBoxes.size() > 0){
			for(int i=0 ; i < landHitBoxes.size(); i++) {
				Rectangle recLand = landHitBoxes.get(i);
				if(myDora.getJumpBox().overlaps(recLand) && !myDora.isJumping()) {
					doraFall=false;
				}
				if(myBabouch != null){
					if(myBabouch.getJumpBox().overlaps(recLand) && !myBabouch.isJumping()) {
						babouchFall=false;
					}
				}
			}
		}
		
		if(doraFall) myDora.fall();
		else myDora.stopFall();
		
		if(myBabouch != null) {
			if(babouchFall) myBabouch.fall();
			else myBabouch.stopFall();
		}
		
		//**ennemies and player collisions
		ArrayList<Ennemy> e = lev1.getEnnemies();
		for(int i=0; i< e.size(); i++){
			if(e.get(i).getHitBox().overlaps(myDora.getJumpBox()) && !myDora.isJumping()){
				e.get(i).hit();
				myDora.stopFall();
				myDora.smallBounce();
			}
		}
		
		for(int j=0; j < items.size(); j++) {
			Item temp = items.get(j);
			if(temp.isVisible()) {
				if(temp.getPosition().overlaps(myDora.getHitBox())){
					temp.setVisible(false);
				}
			}
		}
	}
	

	public void testKeys() {
		
		boolean LEFT_TOUCHED = false;
		boolean RIGHT_TOUCHED = false;
		boolean JUMP_TOUCHED = false; 

		for(int i=0; i < 3; i++){
			if(Gdx.input.isTouched(i) && Gdx.input.getX(i)>0 && Gdx.input.getX(i)<RessourceContainer.arrowLeftSprite.getWidth()) {
				LEFT_TOUCHED = true;
			}
			if(Gdx.input.isTouched(i) && Gdx.input.isTouched() && Gdx.input.getX(i)>RessourceContainer.arrowLeftSprite.getWidth()+50 && Gdx.input.getX(i)<2*RessourceContainer.arrowLeftSprite.getWidth()+50) {
				RIGHT_TOUCHED = true;
			}
			if(Gdx.input.isTouched(i) && Gdx.input.getX(i)>camera.viewportWidth/2.0f){
				JUMP_TOUCHED = true;
			}
		}


		if(Gdx.input.isKeyPressed(Keys.LEFT)||LEFT_TOUCHED) {
			myDora.setXDirection(GameConstants.MOVE_LEFT);
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)||RIGHT_TOUCHED) {
			myDora.setXDirection(GameConstants.MOVE_RIGHT);
		}
		if(Gdx.input.isKeyPressed(Keys.SPACE)||JUMP_TOUCHED) {
			myDora.jump();
		}
		if(Gdx.input.isKeyPressed(Keys.N)) {
			resetGame();
		}

		if(!(Gdx.input.isKeyPressed(Keys.LEFT)||LEFT_TOUCHED)) {
			if(myDora.getXDirection() == GameConstants.MOVE_LEFT) myDora.setXDirection(GameConstants.MOVE_NOT);
		}
		if(!(Gdx.input.isKeyPressed(Keys.RIGHT)||RIGHT_TOUCHED)) {
			if(myDora.getXDirection() == GameConstants.MOVE_RIGHT) myDora.setXDirection(GameConstants.MOVE_NOT);
		}
		
		//Exit
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}
	
	
	
	private void resetGame() {
		
		camera.position.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, 0);
		createDoraPlayer(true);
		
		ArrayList<Ennemy> e = lev1.getEnnemies();
		for(int i=0; i< e.size(); i++){
			e.get(i).reset(1);
		}
	}
	
	private void createDoraPlayer(boolean isMainPlayer) {
		
		myDora = new Player(new Rectangle(0.0f,Gdx.graphics.getHeight(),126,156), "dora_walk_med2.png", "dora_stand_med2.png", "dora_jump_med.png", 12, 5, 8);
		myDora.setAsMain(true);
		myDora.setVelocity(GameConstants.SCROLL_VELOCITY_X);
		myDora.stopJump();
		myDora.stopFall();

		float x = myDora.getPosition().getX()+45;
		float y = myDora.getPosition().getY()+20;
		float w = 50;
		float h = 5;

		myDora.setJumpBox(new Rectangle(x,y,w,h));

		Vector2 center = new Vector2();
		myDora.getPosition().getCenter(center);
		x = center.x-20;
		y = center.y-20;
		w = 40;
		h = 40;

		myDora.setHitBox(new Rectangle(x,y,w,h));
	}
	
	private void createBabouchPlayer(boolean isMainPlayer) {
		
		myBabouch = new Player(new Rectangle(0.0f,Gdx.graphics.getHeight(),126,96),"babouch_walk_med.png" , "babouch_stand_med.png", "babouch_jump_med.png", 8, 5, 8);
		myBabouch.setAsMain(false);
		myBabouch.setXDirection(GameConstants.MOVE_NOT);
		myBabouch.setVelocity(GameConstants.SCROLL_VELOCITY_X);
		myBabouch.stopJump();
		myBabouch.stopFall();

		float x = 37;
		float y = 97;
		float w = 56;
		float h = 6;

		myBabouch.setJumpBox(new Rectangle(x,y,w,h));
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.ANY_KEY){
			resetGame();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void resize(int width, int height) {
	    viewport.update(width, height);
	}
}
