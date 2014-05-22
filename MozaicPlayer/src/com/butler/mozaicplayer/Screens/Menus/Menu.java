package com.butler.mozaicplayer.Screens.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.butler.mozaicplayer.MozaicPlayer;

abstract class Menu implements Screen {
	
	protected MozaicPlayer game;
	protected Stage stage;
	protected BitmapFont black;
	protected BitmapFont white;
	protected TextureAtlas atlas;
	protected Skin skin;
	protected Label label;
	protected SpriteBatch batch;

	
	public Menu(MozaicPlayer game){
		this.game = game;	
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		
		batch.begin();
		stage.draw();
		batch.end();
	}

	public abstract void resize(int width, int height);

	public void show() {
		batch = new SpriteBatch();
		atlas = new TextureAtlas("data/button.pack");
		skin = new Skin();
		skin.addRegions(atlas);
		black = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		black.setScale(1.5f*MozaicPlayer.scale);
		white = new BitmapFont(Gdx.files.internal("data/whitefont.fnt"), false);
		white.setScale(2f*MozaicPlayer.scale);
	}

	public void hide() {
		dispose();
	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
		batch.dispose();
		skin.dispose();
		atlas.dispose();
		black.dispose();
		white.dispose();
		stage.dispose();
	}
}
