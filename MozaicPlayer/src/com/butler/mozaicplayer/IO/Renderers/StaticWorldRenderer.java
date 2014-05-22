package com.butler.mozaicplayer.IO.Renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Model.Pieces.Piece;

public class StaticWorldRenderer extends WorldRenderer {
	private Vector3 pos;
	
	public StaticWorldRenderer(Puzzle puzzle) {
		super(puzzle);
		pos = cam.position.cpy();
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		iter = getIterator();
		
		sr.setProjectionMatrix(cam.combined);
	
		sr.begin(ShapeType.FilledRectangle);
		sr.setColor(Color.WHITE);
		sr.filledRect(0, MozaicPlayer.trans, MozaicPlayer.size, MozaicPlayer.height - MozaicPlayer.trans);
		sr.end();
		
		sr.begin(ShapeType.FilledTriangle);
		while (iter.hasNext()) {
			Piece next = iter.next();
			if (next == getClone())
				clone = next;
			else
				next.draw(sr);
		}
		sr.end();
		
		sr.setProjectionMatrix(camO.combined);
		sr.begin(ShapeType.FilledRectangle);
		sr.setColor(Color.BLACK);
		sr.filledRect(0, 0, MozaicPlayer.size, MozaicPlayer.trans);
		sr.end();   

		sr.begin(ShapeType.FilledTriangle);
		iter = getInitIterator();
		while (iter.hasNext()) {
			iter.next().draw(sr);
		}
		sr.end();
		
		if (clone != null) {
			sr.setProjectionMatrix(cam.combined);
			sr.begin(ShapeType.FilledTriangle);
			clone.draw(sr);
			clone = null;
			sr.end();
		}
		
	    Gdx.gl.glDisable(GL10.GL_BLEND);
	}
	
	@Override
	public void zoom(int amount) {
		cam.zoom += (float) amount/10;

		if (cam.zoom > 1) {
			cam.zoom = 1;
			cam.translate(pos.cpy().sub(cam.position));
		}
		
		else if (cam.zoom <= 0)
			cam.zoom = 0.1f;
		
		cam.update();
	}
	
	@Override
	public void scroll(float x, float y) {
		cam.translate(-x/2, -y/2);
		if (pos.dst(cam.position) > MozaicPlayer.size) {
			cam.translate(x/2, y/2);
		}
		cam.update();
	}
}