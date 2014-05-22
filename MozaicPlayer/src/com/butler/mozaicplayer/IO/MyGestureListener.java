package com.butler.mozaicplayer.IO;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.butler.mozaicplayer.Model.ColourPuzzle;
import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Model.Pieces.Piece;

public class MyGestureListener implements GestureListener {
	
	private Puzzle puzzle;
	private Vector3 touch = new Vector3();
	private Piece rotating;
	private OrthographicCamera cam;
	
	
	private float initialScale;
	private float rotation;
	private float newRotation;
	private float originalRotation;
	private Piece colourPick;
	private InputHandler inputHandler;
	private boolean isRotating, zoomOverride;
	private boolean isStatic = false;
	private Vector3 pos;
	
	public MyGestureListener(Puzzle puzzle, InputHandler inputHandler) {
		this.puzzle = puzzle;
		this.inputHandler = inputHandler;
		cam = puzzle.getRenderer().getCamera();
		pos = cam.position.cpy();
	}
	
	public void updateCamera() {
		cam = puzzle.getRenderer().getCamera();
		pos = cam.position.cpy();
	}
	
	public void setStatic() {
		isStatic = true;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		initialScale = cam.zoom;
		if (rotating != null) {
			if (rotating.fixRotation(originalRotation))
				MozaicAudio.dragFail();
			rotating = null;
		}
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		touch.set(x, y, 0);
		cam.unproject(touch);
		
		if (puzzle.getClass() == ColourPuzzle.class) {
			colourPick = puzzle.underMouse(touch.x, touch.y);
			if (colourPick != null) {
				((ColourPuzzle) puzzle).setColor(colourPick.getColor());
				inputHandler.gestureOverride();
				return true;
			}
		}		
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
//		if (!isStatic) {
		if (zoomOverride || !isRotating) {
			float ratio = initialDistance/distance;
			cam.zoom = initialScale*ratio;
			if (isStatic && cam.zoom > 1) {
				cam.zoom = 1;
				cam.translate(pos.cpy().sub(cam.position));
			}
			cam.update();
		}
//		}
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2)  {
		
		if (puzzle.getClass() == ColourPuzzle.class) {
			inputHandler.gestureOverride();
			return false;	
		}
		if (rotating == null) {
			touch.set(initialPointer1.x, initialPointer1.y, 0);
			cam.unproject(touch);
			rotating = puzzle.underMouse(touch.x, touch.y);
			
			if (rotating == null) {
				isRotating = false;
				return false;
			}
			
			System.out.println("HAY");
			
			touch.set(pointer2.x, pointer2.y, 0);
			cam.unproject(touch);
			rotation = rotating.getAngle(touch.x, touch.y);
			originalRotation = rotating.getRotation();
			isRotating = true;
			
			inputHandler.gestureOverride(rotating, originalRotation);
		}
		
		touch.set(pointer1.x, pointer1.y, 0);
		cam.unproject(touch);
		
		if (rotating != puzzle.underMouse(touch.x, touch.y)) {
			rotating = null;
			isRotating = false;
			return true;
		}
		
		touch.set(pointer2.x, pointer2.y, 0);
		cam.unproject(touch);
		
		newRotation = rotating.getAngle(touch.x, touch.y);
		rotating.rotate(-(rotation - newRotation));
		rotation = newRotation;
		return true;
	}

}
