package com.butler.mozaicplayer.Model;

import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.Model.Factories.PuzzlePiece;
import com.butler.mozaicplayer.Model.Factories.ReflectPuzzlePiece;
import com.butler.mozaicplayer.Model.Json.SavePiece;
import com.butler.mozaicplayer.Model.Pieces.Piece;
import com.butler.mozaicplayer.Model.Pieces.ReflectPiece;

public class ReflectPuzzle extends OriginalPuzzle {
	
	/* Overrides create, save, load, snap, clone, underMouse and delete */

	
	public ReflectPuzzle(int noPieces, Array<PuzzlePiece> puzzlePieces, int[][][] matches, boolean started) { // NO_UCD (use default)
		super(noPieces, puzzlePieces, matches, started);
	}
	
	@Override
	public Array<SavePiece> save() {
		Iterator<Piece> iter = getIterator();
		Piece p;
		Array<SavePiece> savePieces = new Array<SavePiece>(pieces.size);
		while (iter.hasNext()) {
			p = iter.next();
			if (((ReflectPiece) p).getRefs().first() == p)
				savePieces.add(new SavePiece(p.getID(), p.getTransX(), p.getTransY(), p.getRotation()));
		}
		return savePieces;
	}	
	
	@Override
	public void load(Array<SavePiece> savePieces) {
		Iterator<SavePiece> iter = savePieces.iterator();
		SavePiece s;
		Piece p;
		while (iter.hasNext()) {
			s = iter.next();
			p = createLoad(s.id);
			p.rotate(s.rot);
			p.translateWOC(s.transX, s.transY);
		}
	}
	
	
	
	
	
	private Piece createLoad(int id) {
		ReflectPuzzlePiece pp = (ReflectPuzzlePiece) puzzlePieces.get(id);
		return pp.createLoad();
	}
	
	
	

	// Required for snapping 4 (2 non-ref) pieces together
	private Iterator<ReflectPiece> getNonRefIterator(ReflectPiece piece) {
		Array<ReflectPiece> temp = new Array<ReflectPiece>(false, pieces.size);
		for(Piece p : pieces) {
			temp.add(((ReflectPiece) p));
		}
		temp.removeAll(piece.getRefs(), true);
		return temp.iterator();	
	}
	
	@Override
	public void delete(Piece piece) {
		ReflectPiece p = (ReflectPiece) piece;
		for (ReflectPiece q : p.getRefs())
			super.delete(q);
	}
	
	public ReflectPiece clone(ReflectPiece piece) {
		return ((ReflectPuzzlePiece) puzzlePieces.get(piece.getID())).createClone(piece);
	}

	@Override
	public Piece create(int id) {
		Piece p = puzzlePieces.get(id).create();
		if (initPieces.size == noPieces)
			initPieces.removeIndex(id);
		initPieces.insert(id, p);
		return p;
	}
	
	@Override
	public boolean snap(Piece piece2) {
		
		ReflectPiece piece = ((ReflectPiece) piece2).getRefs().first();
		
		int shape = 0;
		if (piece.getID() > 5)
			shape = 1;
		float[] verts = piece.getVerts();
		int size = verts.length;
	// Snap to pieces which are not reflections of this piece first, this is required for snapping 4 (2 non-ref) pieces together
		Iterator<ReflectPiece> pieces = getNonRefIterator(piece);
		float d1, d2;
		
		Array<Vector2> vs = new Array<Vector2>(false, size/2);
		Array<Vector2> us;
		Vector2 v, v0, u0, v1, v2, u1, u2;

		ReflectPiece u;
		int uSize;
		int uShape;
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
			verts = u.getVerts();
			uSize = verts.length;
			uShape = 0;
			if (u.getID() > 5)
				uShape = 1;

			us = new Array<Vector2>(false, uSize);
			uMatches = matches[shape][uShape];
			
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
						if (piece.getRef() > 4) {
							snap4(piece, u, uMatches);
						}
						return true;
					}
				}
			}
		}
		
	// Snapping to reflections, we already know the shape and only want half the translation
		Array<ReflectPiece> refs = piece.getRefs();
		pieces = refs.iterator();
		while (pieces.hasNext()) {
			u = pieces.next();
			if (u != piece) {
				verts = u.getVerts();

				us = new Array<Vector2>(false, size);
				uMatches = matches[shape][shape];
				
				for (int i = 0; i < size; i++) {
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
						// The translation is halved
							piece.fixHalf(v1, v2, u1, u2);
						// If possibility of 4 snapping together, call snap4
							if (piece.getRef() > 4) {
								snap4(piece, u, uMatches);
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	
// Effectively performs another snap if required because 4 pieces are close enough together
	private void snap4(ReflectPiece piece, ReflectPiece q, int[] uMatches) {
		ReflectPiece u = null;
		Iterator<ReflectPiece> iter = piece.getRefs().iterator();
		
		while (iter.hasNext()) {
			u = iter.next();
			if (u != q && u != piece)
				break;
		}
		
//		System.out.println(u);
//		System.out.println(q);
//		System.out.println(piece);

		Vector2 v, v1, v2, u1, u2;
		float d1, d2, d = MozaicPlayer.size/20;
		float[] verts = piece.getVerts();
		int size = verts.length;
		
	// Need to recalculate vertices of piece
		Array<Vector2> vs = new Array<Vector2>(false, size/2);
		Array<Vector2> us = new Array<Vector2>(false, size/2);
		
		for (int i = 0; i < verts.length; i++) {
			v = new Vector2(verts[i++], verts[i]);
			vs.add(v);
		}
		
		verts = u.getVerts();
		
		for (int i = 0; i < verts.length; i++) {
			v = new Vector2(verts[i++], verts[i]);
			us.add(v);
		}
		
		for (int i = 0; i < uMatches.length; i++) {
			v1 = vs.get(uMatches[i++]);
			v2 = vs.get(uMatches[i++]);
			u1 = us.get(uMatches[i++]);
			u2 = us.get(uMatches[i]);
			d1 = v1.dst(u1);
			d2 = v2.dst(u2);
			if (d1 < d && d2 < d) {
				piece.fixHalf(v1, v2, u1, u2);
			}
		}
	}
	
	@Override
	public Piece underMouseInit(float x, float y) {
		Iterator<Piece> iter = getInitIterator();
		ReflectPiece p;
		
		while (iter.hasNext()) {
			p = (ReflectPiece) iter.next();
			if (p.isSpawned() && p.contains(x, y)) {
				return p;
			}
		}
		return null;
	}

	@Override
	public Piece underMouse(float x, float y) {
		Iterator<Piece> iter = getIterator();
		ReflectPiece p;
		
		while (iter.hasNext()) {
			p = (ReflectPiece) iter.next();
			if (p.isSpawned() && p.contains(x, y)) {
				return p;
			}
		}
		return null;
	}

	public void setRef(int reflection) {
		for (PuzzlePiece piece : puzzlePieces)
			((ReflectPuzzlePiece) piece).ref = reflection;
	}
}
