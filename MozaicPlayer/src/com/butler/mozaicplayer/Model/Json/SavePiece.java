package com.butler.mozaicplayer.Model.Json;

import com.badlogic.gdx.graphics.Color;

public class SavePiece {
	public int id;
	public float transX;
	public float transY;
	public float rot;
	public int ref;
	public Color color;
	
	public SavePiece(int id, float transX, float transY, float rot) {
		this.id = id;
		this.transX = transX;
		this.transY = transY;
		this.rot = rot;
	}
	
	public SavePiece(int id, float transX, float transY, float rot, Color color) {
		this.id = id;
		this.transX = transX;
		this.transY = transY;
		this.rot = rot;
		this.color = color;
	}
	
	public SavePiece() {
	}
}
