package com.butler.mozaicplayer.IO.Renderers;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Model.Pieces.Piece;

public class WorldRenderer {
	
	Puzzle puzzle;
	SpriteBatch batch;
	OrthographicCamera cam, camO;
	float width, height;
	ShapeRenderer sr;
	Iterator<Piece> iter;
	Piece clone;
	
	
	public WorldRenderer(Puzzle puzzle) {
		this.puzzle = puzzle;
		
		puzzle.setRenderer(this);
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width, height);		
		cam.update();
		
		camO = new OrthographicCamera();
		camO.setToOrtho(false, width, height);
		camO.update();

		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		
		sr = new ShapeRenderer();
	}
	
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		iter = getIterator();
		
		sr.setProjectionMatrix(cam.combined);
		
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
	
	protected Iterator<Piece> getIterator() {
		return puzzle.getIterator();
	}
	
	protected Iterator<Piece> getInitIterator() {
		return puzzle.getInitIterator();
	}
	
	protected Piece getClone() {
		return puzzle.getClone();
	}

	public OrthographicCamera getCamera() {
		return cam;
	}
	
	public OrthographicCamera getCameraO() {
		return camO;
	}
	
	public void dispose(){
		batch.dispose();
	}

	public void zoom(int amount) {
		cam.zoom += (float) amount/10;
		if (cam.zoom <= 0)
			cam.zoom = 0.1f;
		cam.update();
	}
	
	public void scroll(float x, float y) {
		cam.translate(-x/2, -y/2);
		cam.update();
	}
}
