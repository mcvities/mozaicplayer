package com.butler.mozaicplayer.Model.Factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Model.Pieces.OriginalPiece;
import com.butler.mozaicplayer.Model.Pieces.Piece;

public class PuzzlePiece {

	protected Puzzle puzzle;
	
	protected float oX;
	protected float oY;
	protected float[] coords;
	protected int noTriangles;
	protected int[] triangles;
	public Color color;
	protected int id;
	
	public PuzzlePiece(Puzzle puzzle, float oX, float oY, float[] coords, int noTriangles, int[] triangles, Color color, int id) { // NO_UCD (use default)
		this.puzzle = puzzle;
		this.oX = oX*MozaicPlayer.width;
		this.oY = oY*MozaicPlayer.trans;
		this.coords = new float[coords.length];
		for (int i = 0; i < coords.length; i++) {
			this.coords[i] = coords[i++] + this.oX;
			this.coords[i] = coords[i] + this.oY;
		}
		this.noTriangles = noTriangles;
		this.triangles = triangles;
		this.color = color;
		this.id = id;
	}
	
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
		return new Piece(puzzle, polygon, tris, 0, color, id, oX, oY);
	}
	
	public OriginalPiece createOri() {
		Polygon temp;
		Polygon polygon;
		Array<Polygon> tris = new Array<Polygon> (false, noTriangles);
		
		polygon = new Polygon(coords);
		for (int i = 0; i < noTriangles; i++) {
			temp = new Polygon(new float[] {coords[triangles[3*i]*2], coords[triangles[3*i]*2 + 1], coords[triangles[3*i + 1]*2], coords[triangles[3*i + 1]*2 + 1],
													coords[triangles[3*i + 2]*2], coords[triangles[3*i + 2]*2 + 1]});
			tris.add(temp);
		}
		return new OriginalPiece(puzzle, polygon, tris, 0, color, id, oX, oY);
	}
}
