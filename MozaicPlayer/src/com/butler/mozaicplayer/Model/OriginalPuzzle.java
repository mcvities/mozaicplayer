package com.butler.mozaicplayer.Model;

import java.util.Iterator;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.Model.Factories.PuzzlePiece;
import com.butler.mozaicplayer.Model.Pieces.OriginalPiece;
import com.butler.mozaicplayer.Model.Pieces.Piece;

public class OriginalPuzzle extends Puzzle {
	
	/* Overrides create, snap and overlaps */
	
	private static final Vector2 bl = new Vector2(0, MozaicPlayer.trans);
	private static final Vector2 tl = new Vector2(0, MozaicPlayer.height);
	private static final Vector2 tr = new Vector2(MozaicPlayer.size, MozaicPlayer.height);
	private static final Vector2 br = new Vector2(MozaicPlayer.size, MozaicPlayer.trans);
	protected static final Vector2[] corners = {bl, tl, tr, br};

	private static final int bottom1 = MozaicPlayer.trans;
	private static final int bottom2 = -100;
	private static final int left1 = 0;
	private static final int left2 = -100;
	private static final int right1 = MozaicPlayer.size;
	private static final int right2 = right1 + 100;
	private static final int top1 = MozaicPlayer.height;
	private static final int top2 = top1 + 100;
	
	protected static final Polygon LEFT = new Polygon(new float[] {left1, bottom2, left1, top2, left2, top2, left2, bottom2} );
	protected static final Polygon RIGHT = new Polygon(new float[] {right1, bottom2, right1, top2, right2, top2, right2, bottom2} );
	protected static final Polygon TOP = new Polygon(new float[] {left2, top1, right2, top1, right2, top2, left2, top2} );
	protected static final Polygon BOTTOM = new Polygon(new float[] {left2, bottom1, right2, bottom1, right2, bottom2, left2, bottom2} );
	
	public OriginalPuzzle(int noPieces, Array<PuzzlePiece> puzzlePieces, int[][][] matches, boolean started) {
		super(noPieces, puzzlePieces, matches, started);
	}

	@Override
	public Piece create(int id) {
		Piece p = puzzlePieces.get(id).createOri();
		if (initPieces.size == noPieces)
			initPieces.removeIndex(id);
		initPieces.insert(id, p);
		return p;
	}
	
	@Override
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
		
		for (Vector2 corner : corners) {
			if (v0.dst(corner) < dMaxSnap) {
				for (Vector2 v_ : vs) {
					if (v_.dst(corner) < d) {
						piece.translateWOC(corner.x - v_.x, corner.y - v_.y);
						return true;
					}
				}
			}
		}
		
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
		Array<MinimumTranslationVector> mtvs = new Array<MinimumTranslationVector>(false, MozaicPlayer.PIECES + 4);
		
		OriginalPiece oPiece = ((OriginalPiece) piece);
		
		mtv = new MinimumTranslationVector();
		Intersector.overlapConvexPolygons(oPiece.get(), LEFT, mtv);
		mtvs.add(mtv);
		mtv = new MinimumTranslationVector();
		Intersector.overlapConvexPolygons(oPiece.get(), RIGHT, mtv);
		mtvs.add(mtv);
		mtv = new MinimumTranslationVector();		
		Intersector.overlapConvexPolygons(oPiece.get(), TOP, mtv);
		mtvs.add(mtv);
		mtv = new MinimumTranslationVector();		
		Intersector.overlapConvexPolygons(oPiece.get(), BOTTOM, mtv);
		mtvs.add(mtv);
		
		float max = 0;
		for (int i = 0; i < mtvs.size; i++) {
			mtv = mtvs.get(i);
			// If we have already checked all values
			if (mtv == null)
				break;
						
			max = Math.max(max, mtv.depth);
		}
		
		// Should 'tighten' this value later
		if (max > 1)
			return true;
		else return super.overlaps(piece);
	}
	
}
