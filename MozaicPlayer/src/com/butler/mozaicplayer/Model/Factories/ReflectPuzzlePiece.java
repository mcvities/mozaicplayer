package com.butler.mozaicplayer.Model.Factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Model.Pieces.ReflectPiece;

public class ReflectPuzzlePiece extends PuzzlePiece { // NO_UCD (use default)

	public int ref = 5;
	private float x;
	private float y;
	private float offX;
	private float offY;
	
	public ReflectPuzzlePiece(Puzzle puzzle, float oX, float oY, float[] coords, int noTriangles, int[] triangles, Color color, int id, float offX, float offY) { // NO_UCD (use default)
		super(puzzle, oX, oY, coords, noTriangles, triangles, color, id);
		this.coords = coords;
		for (int i = 1; i < coords.length; i+=2) {
			this.coords[i] = coords[i] + MozaicPlayer.trans;
		}
		x = this.oX;
		y = this.oY - MozaicPlayer.trans;
		
		this.offX = offX;
		this.offY = offY;
	}

	public ReflectPiece create() {
		Polygon temp;
		Polygon polygon;
		Array<Polygon> tris = new Array<Polygon> (false, noTriangles);
		
		polygon = new Polygon(coords);
		for (int i = 0; i < noTriangles; i++) {
			temp = new Polygon(new float[] {coords[triangles[3*i]*2], coords[triangles[3*i]*2 + 1], coords[triangles[3*i + 1]*2], coords[triangles[3*i + 1]*2 + 1],
													coords[triangles[3*i + 2]*2], coords[triangles[3*i + 2]*2 + 1]});
			tris.add(temp);
		}
		
		Array<ReflectPiece> refPieces = new Array<ReflectPiece>(false, 4);
		ReflectPiece piece =  new ReflectPiece(puzzle, polygon, tris, 0, color, id, 0, MozaicPlayer.trans, ref, refPieces);
		
		createReflections(refPieces);
		piece.translateWOC(x, y);
		
		piece.spawn_();
		return piece;
	}
	
	private void createReflections(Array<ReflectPiece> refPieces) {
		if (id < 6) {
			if (ref == 1 || ref == 5)
				reflect(0, MozaicPlayer.height - MozaicPlayer.trans, 1, -1, refPieces);
			
			if (ref == 2 || ref == 5)
				reflect(MozaicPlayer.width, 0, -1, 1, refPieces);
			
			if (ref == 3 || ref == 6)
				reflect(MozaicPlayer.width, MozaicPlayer.height - MozaicPlayer.trans, -1, -1, refPieces);
	
			if (ref == 4 || ref == 6)
				reflect(0, 0, 1, 1, refPieces);
			
			if (ref == 5 || ref == 6)
				reflect(MozaicPlayer.width, MozaicPlayer.height - MozaicPlayer.trans, -1, -1, refPieces);
		}
		else {
			if (ref == 1 || ref == 5)
				reflect(0, MozaicPlayer.height - MozaicPlayer.trans, 1, -1, refPieces);
			
			if (ref == 2 || ref == 5)
				reflect(MozaicPlayer.width, 0, -1, 1, refPieces);
			
			if (ref == 3 || ref == 6)
				reflect(MozaicPlayer.width, MozaicPlayer.height - MozaicPlayer.trans, 1, 1, refPieces).srotate(45);

			if (ref == 4 || ref == 6)
				reflect(0, 0, 1, 1, refPieces).srotate(45);
			
			if (ref == 5 || ref == 6)
				reflect(MozaicPlayer.width, MozaicPlayer.height - MozaicPlayer.trans, -1, -1, refPieces);
		}
	}
	
	private ReflectPiece reflect(float transX, float transY, float scaleX, float scaleY, Array<ReflectPiece> refPieces) {
		Polygon temp;
		Polygon polygon;
		Array<Polygon> tris = new Array<Polygon> (false, noTriangles);
		
		polygon = new Polygon(coords);
		for (int i = 0; i < noTriangles; i++) {
			temp = new Polygon(new float[] {coords[triangles[3*i]*2], coords[triangles[3*i]*2 + 1], coords[triangles[3*i + 1]*2], coords[triangles[3*i + 1]*2 + 1],
													coords[triangles[3*i + 2]*2], coords[triangles[3*i + 2]*2 + 1]});
			tris.add(temp);
		}
		
		polygon.setScale(scaleX, scaleY);
		polygon.translate(transX, transY);
		
		for (Polygon tri : tris) {
			tri.setScale(scaleX, scaleY);
			tri.translate(transX, transY);
		}
		
		return new ReflectPiece(puzzle, polygon, tris, 0, color, id, 0, MozaicPlayer.trans, ref, refPieces);
	}
	
	public ReflectPiece createClone(ReflectPiece piece) { // NO_UCD (use default)
		ReflectPiece clone = create_();
		
//		clone.translateWOC(-x, -y);
		float rot = piece.getRotation();
		piece.rotate(-rot);
		
		float[] verts = piece.getVerts();
		clone.translateWOC(verts[0] + offX, verts[1] - MozaicPlayer.trans + offY);
		
		piece.rotate(rot);
		clone.rotate(rot);
		return clone;
	}
	
	public ReflectPiece createLoad() { // NO_UCD (use default)
		Polygon temp;
		Polygon polygon;
		Array<Polygon> tris = new Array<Polygon> (false, noTriangles);
		
		polygon = new Polygon(coords);
		for (int i = 0; i < noTriangles; i++) {
			temp = new Polygon(new float[] {coords[triangles[3*i]*2], coords[triangles[3*i]*2 + 1], coords[triangles[3*i + 1]*2], coords[triangles[3*i + 1]*2 + 1],
													coords[triangles[3*i + 2]*2], coords[triangles[3*i + 2]*2 + 1]});
			tris.add(temp);
		}
		
		Array<ReflectPiece> refPieces = new Array<ReflectPiece>(false, 4);
		ReflectPiece piece =  new ReflectPiece(puzzle, polygon, tris, 0, color, id, 0, MozaicPlayer.trans, ref, refPieces);
		
		createReflections(refPieces);
		
		piece.spawn();
		return piece;
	}
	
	private ReflectPiece create_() {
		Polygon temp;
		Polygon polygon;
		Array<Polygon> tris = new Array<Polygon> (false, noTriangles);
		
		polygon = new Polygon(coords);
		for (int i = 0; i < noTriangles; i++) {
			temp = new Polygon(new float[] {coords[triangles[3*i]*2], coords[triangles[3*i]*2 + 1], coords[triangles[3*i + 1]*2], coords[triangles[3*i + 1]*2 + 1],
													coords[triangles[3*i + 2]*2], coords[triangles[3*i + 2]*2 + 1]});
			tris.add(temp);
		}
		
		Color c = color;
		color = color.tmp();
		color.a = 0.5f;

		
		Array<ReflectPiece> refPieces = new Array<ReflectPiece>(false, 4);
		ReflectPiece piece =  new ReflectPiece(puzzle, polygon, tris, 0, color, id, 0, MozaicPlayer.trans, ref, refPieces);
		
		createReflections(refPieces);
		color = c;
		
		return piece;
	}
}
