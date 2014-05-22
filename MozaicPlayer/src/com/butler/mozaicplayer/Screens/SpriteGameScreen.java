package com.butler.mozaicplayer.Screens;

import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.IO.Renderers.SpriteWorldRenderer;
import com.butler.mozaicplayer.Model.Puzzle;

public class SpriteGameScreen extends GameScreen {

	public SpriteGameScreen(MozaicPlayer game, Puzzle puzzle) {
		super(game, puzzle);
		render = new SpriteWorldRenderer(puzzle);
		gestureListener.updateCamera();
	}
}
