package com.butler.mozaicplayer.IO;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Model.Pieces.FlickPiece;
import com.butler.mozaicplayer.Model.Pieces.Piece;
import com.butler.mozaicplayer.Screens.GameScreen;

public class FlickInputHandler implements InputProcessor {

	private Puzzle puzzle;
	private GameScreen screen;
	private Vector3 touch = new Vector3();
	
	
	private float currentX, currentY, previousX, previousY;
	private float currentRotation, previousRotation;
	private Piece flicking, rotating;
	
	public FlickInputHandler(Puzzle puzzle, GameScreen screen) {
		this.puzzle = puzzle;
		this.screen = screen;
	}
	
	public void gestureOverride() { // NO_UCD (use default)
		if (flicking != null) {
			puzzle.create(flicking.getID());
			puzzle.delete(flicking);
			flicking = null;
		}
	 }
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			screen.mainMenu();
		}
		else if (keycode == Keys.MENU || keycode == Keys.O) {
			screen.optionsScreen();
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == 0) {
			touch.set(screenX, screenY, 0);
			puzzle.getRenderer().getCameraO().unproject(touch);
			
			if (touch.y < MozaicPlayer.trans) {
				currentX = touch.x; currentY = touch.y;
				previousX = currentX; previousY = currentY;
				flicking = puzzle.underMouseInit(currentX, currentY);
			}
		}
		if (button == 1) {
			touch.set(screenX, screenY, 0);
			puzzle.getRenderer().getCameraO().unproject(touch);
			
			if (touch.y < MozaicPlayer.trans) {
				currentX = touch.x; currentY = touch.y;
				rotating = puzzle.underMouseInit(currentX, currentY);
				if (rotating != null) {
					currentRotation = rotating.getAngle(currentX, currentY);
					previousRotation = currentRotation;
				}
			}
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == 0) {
						
			if (flicking != null) {
				replace(flicking);
				((FlickPiece) flicking).setVelocity(currentX - previousX, currentY - previousY);
				((FlickPiece) flicking).init();
				flicking = null;
			}
		}
		if (button == 1) {
			
			if (rotating != null) {
				((FlickPiece) rotating).setRotationSpeed(currentRotation - previousRotation);
				rotating = null;
			}
		}		
		return true;
	}
	
	private void replace(Piece piece) {
		puzzle.create(piece.getID());
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		touch.set(screenX, screenY, 0);
		puzzle.getRenderer().getCameraO().unproject(touch);
		
		if (flicking != null) {
			previousX = currentX; previousY = currentY;
			currentX = touch.x; currentY = touch.y;
			flicking.translateWOC(currentX - previousX, currentY - previousY);
		}
		else if (rotating != null) {
			previousRotation = currentRotation;
			currentRotation = rotating.getAngle(touch.x, touch.y);
			rotating.rotate(currentRotation - previousRotation);
		}
		return true;
	} 

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
