package com.butler.mozaicplayer.Model.Factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Model.Pieces.Piece;
import com.butler.mozaicplayer.Model.Pieces.SpritePiece;

public class SpritePuzzlePiece extends PuzzlePiece {

	private Texture texture;
	
	public SpritePuzzlePiece(Puzzle puzzle, float oX, float oY, float[] coords,
			int noTriangles, int[] triangles, Color color, int id, String textureAddress) {
		super(puzzle, oX, oY, coords, noTriangles, triangles, color, id);
		
		texture = new Texture(textureAddress);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear); // Should work...
	}
	
	@Override
	public Piece create() {
		Polygon temp;
		Polygon polygon;
		Array<Polygon> tris = new Array<Polygon> (false, noTriangles);
		
		polygon = new Polygon(coords);
		for (int i = 0; i < noTriangles; i++) {
			temp = new Polygon(new float[] {coords[triangles[3*i]*2], coords[triangles[3*i]*2 + 1], coords[triangles[3*i + 1]*2], coords[triangles[3*i + 1]*2 + 1],
													coords[triangles[3*i + 2]*2], coords[triangles[3*i + 2]*2 + 1]});
			tris.add(temp);
		}
		return new SpritePiece(puzzle, polygon, tris, 0, color, id, oX, oY, texture);
	}

}
