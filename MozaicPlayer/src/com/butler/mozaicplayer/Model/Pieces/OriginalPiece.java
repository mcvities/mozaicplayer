package com.butler.mozaicplayer.Model.Pieces;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.Model.Puzzle;

public class OriginalPiece extends Piece {

	public OriginalPiece(Puzzle puzzle, Polygon polygon, Array<Polygon> triangles, float rot, Color color, int id, float oX, float oY) {
		super(puzzle, polygon, triangles, rot, color, id, oX, oY);
	}
	
	@Override
	public void fix(Vector2 v0, Vector2 v1, Vector2 u0, Vector2 u1){
		translateWOC(u0.x - v0.x, u0.y - v0.y);
	}

	@Override
	public boolean fixRotation(float originalRot) {
		// set rotation to the nearest multiple of 45 that doesn't overlap
		boolean pos = false;
		int mul = 1;
		float rot = polygon.getRotation();
		float diff = rot % 45;
		if (diff < 22.5)
			rotate(-diff);
		else {
			rotate(45 - diff);
			pos = true;
		}
		while (puzzle.overlaps(this)) {
			if (pos)
				rotate(-45 * mul);
			else 
				rotate(45 * mul);
			mul += 1;
			//TODO
			// safety net, shouldn't be required
			if (mul == 8) {
				setRotation(originalRot);
				break;
			}
			pos = !pos;
		}
		return false;
	}
	
	
	// Reverse ordering of translating the polygon and the triangles, because of different overlaps methods.
	@Override
	public boolean translate(float x, float y) {
		polygon.translate(x, y);
		if (puzzle.overlaps(this)) {	
			polygon.translate(-x, -y);
			return false;
		}

		for (Polygon triangle : triangles)
			triangle.translate(x, y);
			
		return true;
	}
	
	public Polygon get() {
		return polygon;
	}
	
	@Override
	public Polygon get(int i) {
		return polygon;
	}
}
