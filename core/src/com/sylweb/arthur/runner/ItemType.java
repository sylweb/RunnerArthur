package com.sylweb.arthur.runner;

public enum ItemType {

	RED_ICE_CREAM(0,52,52,8);
	
	private int id;
	private float width;
	private float height;
	private int nbOfAnimFrame;
	
	ItemType(int id, float w, int h, int af) {
		this.id = id;
		this.width = w;
		this.height = h;
		this.nbOfAnimFrame = af;
	}
	
	public int getTypeId() {
		return this.id;
	}
	
	public float getWidth() {
		return this.width;
	}
	
	public float getHeight() {
		return this.height;
	}
	
	public int getNbOfAnimFrame() {
		return this.nbOfAnimFrame;
	}
}
