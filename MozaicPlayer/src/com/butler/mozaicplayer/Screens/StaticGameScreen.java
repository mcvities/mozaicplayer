package com.butler.mozaicplayer.Screens;

import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.IO.Renderers.StaticWorldRenderer;
import com.butler.mozaicplayer.Model.Puzzle;

public class StaticGameScreen extends GameScreen {
	
	public StaticGameScreen(MozaicPlayer game, Puzzle puzzle){	
		super(game, puzzle);
		render = new StaticWorldRenderer(puzzle);
		gestureListener.updateCamera();
		gestureListener.setStatic();
	}
}
