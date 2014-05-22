package com.butler.mozaicplayer.IO.Renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.Model.Puzzle;

public class ColourWorldRenderer extends WorldRenderer {
	
	private static final FileHandle fileHandle = Gdx.files.internal("data/colour_picker.png"); 
	private static final Pixmap pixmap = new Pixmap(fileHandle);
	private static final Texture texture = new Texture("data/colour_picker.png");
	public static Sprite sprite;
	private static float x, y, w, h;
	
	public ColourWorldRenderer(Puzzle puzzle) {
		super(puzzle);

		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		w = MozaicPlayer.scale*264;
		h = MozaicPlayer.scale*231;
		x = MozaicPlayer.width*0.5f;
		y =  MozaicPlayer.trans*0.5f - h/2;
		
		sprite = new Sprite(texture);
		sprite.setBounds(x, y, w, h);
		sprite.setColor(1, 1, 1, 1);
	}
	
	@Override
	public void render() {
		super.render();
		
		batch.setProjectionMatrix(camO.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();
		
	    Gdx.gl.glDisable(GL10.GL_BLEND);
	}

	public static Color getColor(float x2, float y2) {
		float pixX = x2 - x;
		float pixY = h - (y2 - y); // inverse y coordinates
		pixX *= 256;
		pixX /= w;
		pixY *= 224;
		pixY /= h;
		int x3 = Math.round(pixX);
		int y3 = Math.round(pixY);
		int c = pixmap.getPixel(x3, y3);
		
		Color color = new Color();
		color.r = ((c & 0xff000000) >>> 24) / 255f;
		color.g = ((c & 0x00ff0000) >>> 16) / 255f;
		color.b = ((c & 0x0000ff00) >>> 8) / 255f;
		color.a = ((c & 0x000000ff)) / 255f;
		return color;
	}
}
