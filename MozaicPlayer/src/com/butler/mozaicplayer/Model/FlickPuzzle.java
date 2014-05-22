package com.butler.mozaicplayer.Model;

import java.util.Iterator;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.Model.Factories.PuzzlePiece;
import com.butler.mozaicplayer.Model.Pieces.FlickPiece;
import com.butler.mozaicplayer.Model.Pieces.Piece;

public class FlickPuzzle extends Puzzle {
	
	/* Changes overlaps */
	
	private static final int bottom1 = MozaicPlayer.trans*2;
	private static final int bottom2 = bottom1 - 1000;
	private static final int left1 = 0;
	private static final int left2 = -1000;
	private static final int right1 = MozaicPlayer.size*2;
	private static final int right2 = right1 + 1000;
	private static final int top1 = MozaicPlayer.height*2;
	private static final int top2 = top1 + 1000;
	
	protected static final Polygon LEFT = new Polygon(new float[] {left1, bottom2, left1, top2, left2, top2, left2, bottom2} );
	protected static final Polygon RIGHT = new Polygon(new float[] {right1, bottom2, right1, top2, right2, top2, right2, bottom2} );
	protected static final Polygon TOP = new Polygon(new float[] {left2, top1, right2, top1, right2, top2, left2, top2} );
	protected static final Polygon BOTTOM = new Polygon(new float[] {left2, bottom1, right2, bottom1, right2, bottom2, left2, bottom2} );
	
	public FlickPuzzle(int noPieces, Array<PuzzlePiece> puzzlePieces, int[][][] matches, boolean started) { // NO_UCD (use default)
		super(noPieces, puzzlePieces, matches, started);
	}

	
	public boolean snap(Piece piece) {
		
		int id = piece.getID();
		float[] verts = piece.getVerts();
		int size = verts.length;
		Iterator<Piece> pieces = getIterator();
		float d1, d2;
		
		Array<Vector2> vs = new Array<Vector2>(false, size/2);
		Array<Vector2> us;
		Vector2 v, v0, u0, v1, v2, u1, u2;

		Piece u;
		int uSize;
		int[] uMatches;
		float d = MozaicPlayer.size/20;
		
		for (int i = 0; i < size; i ++) {
			v = new Vector2(verts[i++], verts[i]);
			vs.add(v);
		}
		
		v0 = vs.first();
		
		while (pieces.hasNext()) {
			u = pieces.next();
			if (u != piece) {
				verts = u.getVerts();
				uSize = verts.length;
				us = new Array<Vector2>(false, uSize);
				uMatches = matches[id][u.getID()];
				
				for (int i = 0; i < uSize; i++) {
					v = new Vector2(verts[i++], verts[i]);
					us.add(v);
				}
				
				u0 = us.first();
				
				// Small optimisation
				if (v0.dst(u0) < dMaxSnap) {
					for (int i = 0; i < uMatches.length; i++) {
						v1 = vs.get(uMatches[i++]);
						v2 = vs.get(uMatches[i++]);
						u1 = us.get(uMatches[i++]);
						u2 = us.get(uMatches[i]);
						d1 = v1.dst(u1);
						d2 = v2.dst(u2);
						if (d1 < d && d2 < d) {
							piece.fix(v1, v2, u1, u2);
							if (!overlaps(piece)) {
								FlickPiece u_ = (FlickPiece) u;
								u_.setRotationSpeed(0);
								u_.setVelocity(0, 0);
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	
	@Override
	public boolean overlaps(Piece piece) {
		MinimumTranslationVector mtv;
		Array<MinimumTranslationVector> mtvs = new Array<MinimumTranslationVector>(4 * piece.getSize());
		
		for (int i = 0; i < piece.getSize(); i++) {
			mtv = new MinimumTranslationVector();
			Intersector.overlapConvexPolygons(piece.get(i), LEFT, mtv);
			mtvs.add(mtv);
			mtv = new MinimumTranslationVector();
			Intersector.overlapConvexPolygons(piece.get(i), RIGHT, mtv);
			mtvs.add(mtv);
			mtv = new MinimumTranslationVector();		
			Intersector.overlapConvexPolygons(piece.get(i), TOP, mtv);
			mtvs.add(mtv);
			mtv = new MinimumTranslationVector();		
			Intersector.overlapConvexPolygons(piece.get(i), BOTTOM, mtv);
			mtvs.add(mtv);
		}
		
		float max = 0;
		for (int i = 0; i < mtvs.size; i++) {
			mtv = mtvs.get(i);
			// If we have already checked all values
			if (mtv == null)
				break;
						
			max = Math.max(max, mtv.depth);
		}
		
		// Should 'tighten' this value later
		return max > 1 || super.overlaps(piece);
//		if (max > 1)
//			return true;
//		else return super.overlaps(piece);
	}
	
	
	public FlickPiece overlapsFlick(FlickPiece piece) {
		FlickPiece q;
		FlickPiece maxPiece = null;
		float max = 0;
		MinimumTranslationVector mtv;

		Iterator<Piece> iter = getIterator();
		while (iter.hasNext()) {
			q = (FlickPiece) iter.next();
			if (piece != q && new Vector2(q.getVerts()[0], q.getVerts()[1]).dst
					(piece.getVerts()[0], piece.getVerts()[1]) < dMaxOverlaps) {
				for (int i = 0; i < piece.getSize(); i++) {
					for (int j = 0; j < q.getSize(); j++) {
						mtv = new MinimumTranslationVector();
						Intersector.overlapConvexPolygons(piece.get(i), q.get(j), mtv);
						if (mtv.depth > max) {
							max = mtv.depth;
							maxPiece = q;
						}
					}
				}
			}
		}
		
		return maxPiece;
	}

	public boolean overlapsHor(FlickPiece piece) {
		MinimumTranslationVector mtv;
		Array<MinimumTranslationVector> mtvs = new Array<MinimumTranslationVector>(8);
		
		for (int i = 0; i < piece.getSize(); i++) {
			mtv = new MinimumTranslationVector();
			Intersector.overlapConvexPolygons(piece.get(i), LEFT, mtv);
			mtvs.add(mtv);
			mtv = new MinimumTranslationVector();
			Intersector.overlapConvexPolygons(piece.get(i), RIGHT, mtv);
			mtvs.add(mtv);
		}
		
		float max = 0;
		for (int i = 0; i < mtvs.size; i++) {
			mtv = mtvs.get(i);
			max = Math.max(max, mtv.depth);
		}
		
		// Should 'tighten' this value later
		if (max > 1)
			return true;
		else return false;
	}

	public boolean overlapsVert(FlickPiece piece) {
		MinimumTranslationVector mtv;
		Array<MinimumTranslationVector> mtvs = new Array<MinimumTranslationVector>(8);
		
		for (int i = 0; i < piece.getSize(); i++) {
			mtv = new MinimumTranslationVector();
			Intersector.overlapConvexPolygons(piece.get(i), TOP, mtv);
			mtvs.add(mtv);
			mtv = new MinimumTranslationVector();
			Intersector.overlapConvexPolygons(piece.get(i), BOTTOM, mtv);
			mtvs.add(mtv);
		}
		
		float max = 0;
		for (int i = 0; i < mtvs.size; i++) {
			mtv = mtvs.get(i);
			max = Math.max(max, mtv.depth);
		}
		
		// Should 'tighten' this value later
		if (max > 1)
			return true;
		else return false;
	}
}
