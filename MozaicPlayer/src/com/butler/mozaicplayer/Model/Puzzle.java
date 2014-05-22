package com.butler.mozaicplayer.Model;

import java.util.Iterator;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.IO.Renderers.WorldRenderer;
import com.butler.mozaicplayer.Model.Factories.PuzzlePiece;
import com.butler.mozaicplayer.Model.Json.SavePiece;
import com.butler.mozaicplayer.Model.Pieces.Piece;

public class Puzzle {
	int noPieces;
	protected Array<PuzzlePiece> puzzlePieces;
	
	public Array<Piece> pieces = new Array<Piece>(false, MozaicPlayer.PIECES);
	protected Array<Piece> initPieces;
	private Piece clone;
	
	public boolean started;
	
	private WorldRenderer wr;
	
//	float dMaxOverlaps = MozaicPlayer.scale*524;
	float dMaxOverlaps = MozaicPlayer.scale*1000;

//	float dMaxSnap = MozaicPlayer.scale*356;
	float dMaxSnap = MozaicPlayer.scale*400;

	
	int[][][] matches;
		
	public Puzzle(int noPieces, Array<PuzzlePiece> puzzlePieces, int[][][] matches, boolean started) {
		this.noPieces = noPieces;
		this.puzzlePieces = puzzlePieces;
		this.matches = matches;
		this.started = started;
		initPieces = new Array<Piece>(false, noPieces);
	}

	public void initiate() {
		for (int i = 0; i < noPieces; i++)
			create(i);
	}
	
	public Piece create(int id) {
		Piece p = puzzlePieces.get(id).create();
		if (initPieces.size == noPieces)
			initPieces.removeIndex(id);
		initPieces.insert(id, p);
		return p;
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
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	
	
	public boolean overlaps(Piece piece) {
		Piece q;
		float max;
		MinimumTranslationVector mtv;
		
		Array<MinimumTranslationVector> mtvs = new Array<MinimumTranslationVector>(false, 9*MozaicPlayer.PIECES); //TODO choose value
		
		Iterator<Piece> iter = getIterator();
		while (iter.hasNext()) {
			q = iter.next();

			if (piece != q && new Vector2(q.getVerts()[0], q.getVerts()[1]).dst
					(piece.getVerts()[0], piece.getVerts()[1]) < dMaxOverlaps) {
				for (int i = 0; i < piece.getSize(); i++) {
					for (int j = 0; j < q.getSize(); j++) {
						mtv = new MinimumTranslationVector();
						Intersector.overlapConvexPolygons(piece.get(i), q.get(j), mtv);
						mtvs.add(mtv);
					}
				}
			}
		}
		
		max = 0;
		for (int i = 0; i < mtvs.size; i++) {
			mtv = mtvs.get(i);
			// If we have already checked all values //TODO Is this required??
			if (mtv == null)
				break;
						
			max = Math.max(max, mtv.depth);
		}
		
		// Should 'tighten' this value later
		if (max > 1)
			return true;
		else return false;
	}
	
	
	
	
	public Array<SavePiece> save() {
		Iterator<Piece> iter = getIterator();
		Piece p;
		Array<SavePiece> savePieces = new Array<SavePiece>(pieces.size);
		while (iter.hasNext()) {
			p = iter.next();
			savePieces.add(new SavePiece(p.getID(), p.getTransX(), p.getTransY(), p.getRotation()));
		}
		return savePieces;
	}
	
	public void load(Array<SavePiece> savePieces) {
//		reset();
		Iterator<SavePiece> iter = savePieces.iterator();
		SavePiece s;
		Piece p;
		while (iter.hasNext()) {
			s = iter.next();
			p = create(s.id);
			p.rotate(s.rot);
			p.translateWOC(s.transX, s.transY);
		}
	}
	
	
	public void reset() {
		pieces = new Array<Piece>(false, MozaicPlayer.PIECES);
		initPieces = new Array<Piece>(false, 6);
		clone = null;
		started = false;
		initiate();
	}
	
	public Piece getClone() {
		return clone;
	}
	
	public void setClone(Piece piece) {
		clone = piece;
	}
	
	public Iterator<Piece> getIterator() {
		Array<Piece> temp = new Array<Piece>(false, pieces.size);
		for(Piece p : pieces) {
			temp.add(p);
		}
		temp.removeAll(initPieces, true);
		return temp.iterator();	
	}
	
	public Iterator<Piece> getInitIterator() {
		return initPieces.iterator();
	}
	
	public Iterator<Piece> getFullIterator() {
		return pieces.iterator();
	}
	
	public void delete(Piece piece) {
		pieces.removeValue(piece, true);
		initPieces.removeValue(piece, true);
	}
	

	public void setRenderer(WorldRenderer wr) {
		this.wr = wr;
	}
	
	public WorldRenderer getRenderer() {
		return wr;
	}
	
	
	public Piece underMouseInit(float x, float y) {
		Iterator<Piece> iter = getInitIterator();
		Piece p;
		
		while (iter.hasNext()) {
			p = iter.next();
			if (p.contains(x, y))
				return p;
		}
		return null;
	}
	
	public Piece underMouse(float x, float y) {
		Iterator<Piece> iter = getIterator();
		Piece p;
		
		while (iter.hasNext()) {
			p = iter.next();
			if (p.contains(x, y))
				return p;
		}
		return null;
	}
}
