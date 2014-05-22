package com.butler.mozaicplayer.Model.Pieces;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Model.ReflectPuzzle;

public class ReflectPiece extends OriginalPiece {

	// An array containing all the reflected pieces corresponding to this one
	private Array<ReflectPiece> refPieces;
	// An integer identifying the type of reflection
	private int ref;
	
	// Variables required later
	private OriginalPiece p;
	private Polygon poly;
	private Iterator<ReflectPiece> iter1;
	private Iterator<Piece> iter2;
	private boolean spawned;

	
	// Works as one might expect, note that piece.spawn() is called to make all the pieces visible, and which piece 
	// is returned depends which piece was cloned, since the ReflectPiece is created based on the first piece.
	public Piece clonef() {
		Color c = color.cpy();
		c.a = 0.5f;
		
		ReflectPiece piece = ((ReflectPuzzle) puzzle).clone(refPieces.first());
		piece.spawn();
		
		if (this == refPieces.get(0))
			return piece;
		if (this == refPieces.get(1))
			return piece.getRefPiece(1);
		if (this == refPieces.get(2))
			return piece.getRefPiece(2);
		else return piece.getRefPiece(3);
	}
	
	
	public ReflectPiece(Puzzle puzzle, Polygon polygon, Array<Polygon> triangles, float rot, Color color, int id, float oX, float oY, int ref, Array<ReflectPiece> refPieces) {
		super(puzzle, polygon, triangles, rot, color, id, oX, oY);
		this.ref = ref;
		this.refPieces = refPieces;
		refPieces.add(this);
		spawned = false;
	}
	
	public void spawn_() {
		spawned = true;
	}
	
	public void spawn() {
		for (ReflectPiece p : refPieces) {
			p.spawn_();
		}
	}
	
	public boolean isSpawned() {
		return spawned;
	}
	
	public Array<ReflectPiece> getRefs() {
		return refPieces;
	}
	
	public int getRef() {
		return ref;
	}
	
	private void stranslateWOC(float x, float y) {
		super.translateWOC(x, y);
	}
	
	// This helper method translates all the pieces in the refPieces array appropriately given the translation for the first piece.
	private void translateWOC_(float x, float y) {
		ReflectPiece p = refPieces.get(0);
//		p.get().translate(x, y);
		p.stranslateWOC(x, y);
		p = refPieces.get(1);
		if (ref == 1 || ref == 5)
			p.stranslateWOC(x, -y);
		if (ref == 2)
			p.stranslateWOC(-x, y);
		if (ref == 3 || ref == 6)
			p.stranslateWOC(-y, -x);
		if (ref == 4)
			p.stranslateWOC(y, x);
			
		if (ref > 4) {
			p = refPieces.get(2);
			if (ref == 5)
				p.stranslateWOC(-x, y);
			if (ref == 6)
				p.stranslateWOC(y, x);
			
			p = refPieces.get(3);
			if (ref == 5)
				p.stranslateWOC(-x, -y);
			if (ref == 6)
				p.stranslateWOC(-x, -y);
		}
	}
	
	
	// This calculates the appropriate translation to be applied to the first piece, given a translation on a different piece,
	// and calls the helper method translateWOC_() with this new translation.
	@Override
	public void translateWOC(float x, float y) {
		if (refPieces.get(0) == this) 
			translateWOC_(x, y);
		if (refPieces.get(1) == this) {
			if (ref == 1 || ref == 5)
				translateWOC_(x, -y);
			if (ref == 2)
				translateWOC_(-x, y);
			if (ref == 3 || ref == 6)
				translateWOC_(-y, -x);
			if (ref == 4)
				translateWOC_(y, x);
		}
		if (ref > 4) {
			if (refPieces.get(2) == this) {
				if (ref == 5)
					translateWOC_(-x, y);
				if (ref == 6)
					translateWOC_(y, x);
			}
			if (refPieces.get(3) == this) {
				if (ref == 5)
					translateWOC_(-x, -y);
				if (ref == 6)
					translateWOC_(-x, -y);
			}
		}
	}

	
	// This is a translation that also checks for collisions and undoes the translation if a collision exists, returning
	// true if the translation was successful, and false if there was a collision and the translation was undone.
	@Override
	public boolean translate(float x, float y) {
		translateWOC(x, y);
		
		OriginalPiece q;
		
		boolean f = true;
		iter1 = refPieces.iterator();
		
		if (!f) {
			translateWOC(-x, -y);
			return false;
		}
		
		iter1 = refPieces.iterator();
		
		while (iter1.hasNext() && f) {
			p = iter1.next();
			poly = p.get();
			iter2 = puzzle.getIterator();

			while (iter2.hasNext() && f) {					
				q = (OriginalPiece) iter2.next();
				
				if (p != q && Intersector.overlapConvexPolygons(poly, q.get()))		
					f = false;
			}
		}
		
		
		if (!f) {
			translateWOC(-x, -y);
			return false;
		}
		
		return true;
	}
	
	
	private OriginalPiece getRefPiece(int i) {
		return refPieces.get(i);
	}

	
	public void srotate(float degrees) {
		super.rotate(degrees);
	}
	
	private void rotate_(float degrees) {
		//TODO safety net
		assert (refPieces.get(0) == this);
		srotate(degrees);
		if (ref == 1 || ref == 2 || ref == 3 || ref == 4 || ref == 5 || ref == 6)
			refPieces.get(1).srotate(-degrees);
		
		if (ref == 5 || ref == 6) {
			refPieces.get(2).srotate(-degrees);
			refPieces.get(3).srotate(degrees);
		}
	}
	
	@Override
	public void rotate(float degrees) {	
		if (refPieces.get(0) == this) 
			rotate_(degrees);
		else if (refPieces.get(1) == this) 
			refPieces.get(0).rotate_(-degrees);
		else if (refPieces.get(2) == this) {
			if (ref == 5 || ref == 6)
				refPieces.get(0).rotate_(-degrees);
		}
		else if (refPieces.get(3) == this) {
			if (ref == 5 || ref == 6)
				refPieces.get(0).rotate_(degrees);
		}
	}
	
	@Override
	public float getRotation() {
		return refPieces.get(0).get().getRotation();
	}


	public void fixHalf(Vector2 v0, Vector2 v1, Vector2 u0, Vector2 u1) {
		translateWOC((u0.x - v0.x)/2, (u0.y - v0.y)/2);
	}
}
