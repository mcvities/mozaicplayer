package com.butler.mozaicplayer.Model;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.IO.Renderers.ColourWorldRenderer;
import com.butler.mozaicplayer.Model.Factories.PuzzlePiece;
import com.butler.mozaicplayer.Model.Json.SavePiece;
import com.butler.mozaicplayer.Model.Pieces.Piece;

public class ColourPuzzle extends Puzzle {
	
	private Color currentColor;
	
	public ColourPuzzle(int noPieces, Array<PuzzlePiece> puzzlePieces, int[][][] matches, boolean started) { // NO_UCD (use default)
		super(noPieces, puzzlePieces, matches, started);
	}

	@Override
	public void load(Array<SavePiece> savePieces) {
		Iterator<SavePiece> iter = savePieces.iterator();
		SavePiece s;
		Piece p;
		while (iter.hasNext()) {
			s = iter.next();	
			p = create(s.id);
			p.setColor(s.color);
			p.translateWOC(s.transX, s.transY);
		}
	}
	
	@Override
	public Array<SavePiece> save() {
		Iterator<Piece> iter = getIterator();
		Piece p;
		Array<SavePiece> savePieces = new Array<SavePiece>(pieces.size);
		while (iter.hasNext()) {
			p = iter.next();
			savePieces.add(new SavePiece(p.getID(), p.getTransX(), p.getTransY(), p.getRotation(), p.getColor()));
		}
		return savePieces;
	}
	
	public void setColor(Color color) {
		currentColor = color;
		puzzlePieces.get(0).color = currentColor;
		initPieces.get(0).setColor(currentColor.cpy());
	}
	
	public void setColor(float x, float y) {
		if (ColourWorldRenderer.sprite.getBoundingRectangle().contains(x, y)) {
			setColor(ColourWorldRenderer.getColor(x, y));
		}
	}
}
	
