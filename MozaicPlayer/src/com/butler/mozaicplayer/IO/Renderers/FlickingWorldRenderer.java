package com.butler.mozaicplayer.IO.Renderers;

import java.util.Iterator;

import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Model.Pieces.FlickPiece;
import com.butler.mozaicplayer.Model.Pieces.Piece;

public class FlickingWorldRenderer extends WorldRenderer {
	
	public FlickingWorldRenderer(Puzzle puzzle) {
		super(puzzle);
		cam.setToOrtho(false, width*2, height*2);		
		cam.update();	
	}

	@Override
	public void render() {
		updatePieces();
		super.render();
	}

	private void updatePieces() {
		Iterator<Piece> iter = puzzle.getIterator();
		FlickPiece piece;
		while (iter.hasNext()) {
			piece = (FlickPiece) iter.next();
			piece.update();
		}
		iter = puzzle.getInitIterator();
		while (iter.hasNext()) {
			piece = (FlickPiece) iter.next();
			piece.update();
		}
	}
	
	@Override
	public void zoom(int amount) {
	}
	
	@Override
	public void scroll(float x, float y) {
	}
}
