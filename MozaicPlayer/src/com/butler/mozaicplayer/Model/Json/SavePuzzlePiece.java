package com.butler.mozaicplayer.Model.Json;

import com.badlogic.gdx.graphics.Color;

public class SavePuzzlePiece {
	public float oX;
	public float oY;
	public float[] coords;
	public int[] triangles;
	public Color color;
	public int id;
	public String textureAddress;
	public float mass;
	
	public SavePuzzlePiece(float oX, float oY, float[] coords, int[] triangles, Color color, int id, String textureAddress, float mass) {
		this.oX = oX;
		this.oY = oY;
		this.coords = coords;
		this.triangles = triangles;
		this.color = color;
		this.id = id;	
		this.textureAddress = textureAddress;
		this.mass = mass;
	}
	
	public SavePuzzlePiece(float oX, float oY, float[] coords, int[] triangles, Color color, int id, float mass) {
		this.oX = oX;
		this.oY = oY;
		this.coords = coords;
		this.triangles = triangles;
		this.color = color;
		this.id = id;
		this.mass = mass;
	}
	
	public SavePuzzlePiece(float oX, float oY, float[] coords, int[] triangles, Color color, int id, String textureAddress) {
		this.oX = oX;
		this.oY = oY;
		this.coords = coords;
		this.triangles = triangles;
		this.color = color;
		this.id = id;	
		this.textureAddress = textureAddress;
		mass = 0;
	}
	
	public SavePuzzlePiece(float oX, float oY, float[] coords, int[] triangles, Color color, int id) {
		this.oX = oX;
		this.oY = oY;
		this.coords = coords;
		this.triangles = triangles;
		this.color = color;
		this.id = id;
		mass = 0;
	}

	public SavePuzzlePiece() {
	}
}
