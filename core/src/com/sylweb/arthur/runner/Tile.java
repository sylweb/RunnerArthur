package com.sylweb.arthur.runner;

import com.badlogic.gdx.math.Rectangle;

public class Tile extends Rectangle{
	
	public int id;
	
	public Tile(){
		super(0,0,0,0);
		id=-1;
	}
	
	public Tile(float x, float y, float w, float h, int id){
		super(x,y,w,h);
		this.id = id;
	}

}
