package com.butler.mozaicplayer.IO.Renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Model.Pieces.Piece;

public class SpriteWorldRenderer extends WorldRenderer {
	
	public SpriteWorldRenderer(Puzzle puzzle) {
		super(puzzle);
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		iter = getIterator();
		while (iter.hasNext()) {
			Piece next = iter.next();
			if (next == getClone())
				clone = next;
			else
				next.draw(batch);
		}
		batch.end();
		
		sr.setProjectionMatrix(camO.combined);
		sr.begin(ShapeType.FilledRectangle);
		sr.setColor(Color.BLACK);
		sr.filledRect(0, 0, MozaicPlayer.size, MozaicPlayer.trans);
		sr.end(); 
		
		batch.setProjectionMatrix(camO.combined);
		batch.begin();
		iter = getInitIterator();
		while (iter.hasNext()) {
			iter.next().draw(batch);
		}
		batch.end();
		
		if (clone != null) {
			batch.setProjectionMatrix(cam.combined);
			batch.begin();
			clone.draw(batch);
			clone = null;
			batch.end();
		}
		
	    Gdx.gl.glDisable(GL10.GL_BLEND);
	}
}
