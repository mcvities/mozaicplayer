package com.butler.mozaicplayer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.input.GestureDetector;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.IO.InputHandler;
import com.butler.mozaicplayer.IO.MyGestureListener;
import com.butler.mozaicplayer.IO.Renderers.WorldRenderer;
import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Screens.Menus.MainMenu;
import com.butler.mozaicplayer.Screens.Menus.OptionsScreen;

public class GameScreen implements Screen {
	
	private MozaicPlayer game;
//	protected World world;
	protected WorldRenderer render;
	protected MyGestureListener gestureListener;
	
	public GameScreen(MozaicPlayer game, Puzzle puzzle){	
		this.game = game;
		render = new WorldRenderer(puzzle);
		

//		Gdx.input.setInputProcessor(new InputHandler(puzzle, this));
		
		InputHandler inputHandler = new InputHandler(puzzle, this);
		gestureListener = new MyGestureListener(puzzle, inputHandler);
		GestureDetector gestureDetector = new GestureDetector(gestureListener);
		
		InputMultiplexer mp = new InputMultiplexer();
		mp.addProcessor(gestureDetector);
		mp.addProcessor(inputHandler);
		
		Gdx.input.setInputProcessor(mp);
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
	}
	
	
	public void mainMenu() {
		game.setScreen(new MainMenu(game));
	}
	
	public void optionsScreen() {
		game.setScreen(new OptionsScreen(game, true));
	}
	

	//protected abstract World createWorld(MozaicPlayer game);
	
	@Override
	public void render(float delta) {
//		world.update();			//TODO this never did anything anyway...
		render.render();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
//		world.dispose();		//TODO never did anything either...
		render.dispose();
	}
}
