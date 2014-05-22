package com.butler.mozaicplayer.IO;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Model.Pieces.FlickPiece;

public class FlickGestureListener implements GestureListener {
	
	private Puzzle puzzle;
	private Vector3 touch = new Vector3();
	private OrthographicCamera camO;
	
	private FlickPiece rotating;
	private float currentRotation, previousRotation;
	private FlickInputHandler inputHandler;
	
	public FlickGestureListener(Puzzle puzzle, FlickInputHandler inputHandler) {
		this.puzzle = puzzle;
		this.camO = puzzle.getRenderer().getCameraO();
		this.inputHandler = inputHandler;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2)  {
	
		if (rotating == null) {
			touch.set(initialPointer1.x, initialPointer1.y, 0);
			camO.unproject(touch);
			rotating = (FlickPiece) puzzle.underMouseInit(touch.x, touch.y);
			
			if (rotating == null) {
				return false;
			}
			
			touch.set(pointer2.x, pointer2.y, 0);
			camO.unproject(touch);
			currentRotation = rotating.getAngle(touch.x, touch.y);
			previousRotation = currentRotation;
		}
		
		inputHandler.gestureOverride();

		touch.set(pointer1.x, pointer1.y, 0);
		camO.unproject(touch);
		
		if (rotating != puzzle.underMouseInit(touch.x, touch.y)) {
			rotating = null;
			return true;
		}
		
		touch.set(pointer2.x, pointer2.y, 0);
		camO.unproject(touch);
		
		previousRotation = currentRotation;
		currentRotation = rotating.getAngle(touch.x, touch.y);
		rotating.rotate(currentRotation - previousRotation);
		rotating.setRotationSpeed(currentRotation - previousRotation);
		return true;
	}

}
