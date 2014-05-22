package com.butler.mozaicplayer.Screens;

import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.IO.Renderers.ColourWorldRenderer;
import com.butler.mozaicplayer.Model.Puzzle;

public class ColourGameScreen extends GameScreen {

	public ColourGameScreen(MozaicPlayer game, Puzzle puzzle) {
		super(game, puzzle);
		render = new ColourWorldRenderer(puzzle);
		gestureListener.updateCamera();
	}
}
