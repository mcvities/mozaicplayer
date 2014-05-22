package com.butler.mozaicplayer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.IO.FlickGestureListener;
import com.butler.mozaicplayer.IO.FlickInputHandler;
import com.butler.mozaicplayer.IO.Renderers.FlickSpriteWorldRenderer;
import com.butler.mozaicplayer.Model.Puzzle;

public class FlickSpriteGameScreen extends GameScreen {
	
	public FlickSpriteGameScreen(MozaicPlayer game, Puzzle puzzle){	
		super(game, puzzle);
		render = new FlickSpriteWorldRenderer(puzzle);
		
		FlickInputHandler inputHandler = new FlickInputHandler(puzzle, this);
		FlickGestureListener flickGestureListener = new FlickGestureListener(puzzle, inputHandler);
		GestureDetector gestureDetector = new GestureDetector(flickGestureListener);
		
		InputMultiplexer mp = new InputMultiplexer();
		mp.addProcessor(gestureDetector);
		mp.addProcessor(inputHandler);
		
		Gdx.input.setInputProcessor(mp);		}
}
