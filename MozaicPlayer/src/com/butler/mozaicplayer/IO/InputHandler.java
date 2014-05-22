package com.butler.mozaicplayer.IO;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.butler.mozaicplayer.MozaicPlayer;
import com.butler.mozaicplayer.Model.ColourPuzzle;
import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Model.ReflectPuzzle;
import com.butler.mozaicplayer.Model.Pieces.Piece;
import com.butler.mozaicplayer.Model.Pieces.ReflectPiece;
import com.butler.mozaicplayer.Screens.GameScreen;

public class InputHandler implements InputProcessor {

	private Puzzle puzzle;
	private GameScreen screen;
	private Vector3 touch = new Vector3();
	private Vector3 touchO = new Vector3();
	
	private Piece dragging;
	private Piece rotating;
	private Piece drag;
	private float touchX, touchY, initX, initY;
	private float rotation, newRotation, originalRotation;
	private boolean isNew = false;
	private boolean scrolling = false;
	private boolean colourDragging = false;
	private boolean colourPicking = false;
	private Piece colourPick;
	private boolean pinch = false;
	private Piece rot;
	private float oR;
	
	
	public void gestureOverride() { // NO_UCD (use default)
		if (drag != null) {
			puzzle.delete(drag);
			dragging = null;
		}
	 }
	
	public void gestureOverride(Piece rot, float oR) { // NO_UCD (use default)
		if (drag != null) {
			puzzle.delete(drag);
			dragging = null;
		}
		
		this.oR = oR;
		this.rot = rot;
		pinch = true;
	 }
	
	public InputHandler(Puzzle puzzle, GameScreen screen) {
		this.puzzle = puzzle;
		this.screen = screen;
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
		if (pointer == 0) {
			if (button == 0) {
			
			if (pinch) {
				rot.fixRotation(oR);
				rot = null;
				pinch = false;
			}
			
			touch.set(screenX, screenY, 0);
			// Required for zoom implementations
			puzzle.getRenderer().getCameraO().unproject(touch);
			
			if (touch.y >= MozaicPlayer.trans) {
				touch.set(screenX, screenY, 0);
				puzzle.getRenderer().getCamera().unproject(touch);
				touchX = touch.x; touchY = touch.y;
				initX = touchX; initY = touchY;
				dragging = puzzle.underMouse(touchX, touchY);
				if (dragging != null)
					drag = dragging.clonef();
				else
					scrolling = true;
			}
			
			else {
				touchX = touch.x; touchY = touch.y;
				initX = touchX; initY = touchY;
				dragging = puzzle.underMouseInit(touchX, touchY);
				if (dragging != null) {
					drag = dragging.clonef();
					isNew = true;
				}
				else if (puzzle.getClass() == ColourPuzzle.class) {
						((ColourPuzzle) puzzle).setColor(touchX, touchY);
						colourDragging = true;
				}
			}
		}
		if (button == 1) {
			touch.set(screenX, screenY, 0);
			puzzle.getRenderer().getCamera().unproject(touch);
			touchX = touch.x; touchY = touch.y;
			
			if (puzzle.getClass() == ColourPuzzle.class) {
				colourPicking = true;
				colourPick = puzzle.underMouse(touchX, touchY);
				if (colourPick != null)
					((ColourPuzzle) puzzle).setColor(colourPick.getColor());
			}
			else {
				rotating = puzzle.underMouse(touchX, touchY);
				if (rotating != null) {
					rotation = rotating.getAngle(touchX, touchY);
					originalRotation = rotating.getRotation();
				}
			}
		}
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == 0) {
			
			
			if (pinch) {
				rot.fixRotation(oR);
				rot = null;
				pinch = false;
			}
			
			
			touch.set(screenX, screenY, 0);
			puzzle.getRenderer().getCamera().unproject(touch);
			float x = touch.x; float y = touch.y;
			touchO.set(0, screenY, 0);
			puzzle.getRenderer().getCameraO().unproject(touchO);
						
			
			if (dragging != null) {
				
				puzzle.delete(drag);
				
				// If in the black area, delete the piece
				if (touchO.y < MozaicPlayer.trans) {
					
					// Required for zooming implementation, since collision with black box is not easy to calculate
					if (isNew)
						replace(dragging);
					
					puzzle.delete(dragging);
					dragging = null;
					MozaicAudio.delete();
					return true;
				}
				
				
				// Allows for overlapping shapes to snap together
				dragging.translateWOC(x - initX, y - initY);
				
				// Prevents overlapping shapes if no snap-to takes place
				
//				snapped = SnapTo.snap(dragging, getIterator());
//				if (!snapped.l) {
				
				if (!puzzle.snap(dragging)) {
					dragging.translateWOC(initX - x, initY - y);
					// Attempts a translation with overlapping checks, in case shape is moved to 'empty space'
					if (dragging.translate(x - initX, y - initY)) {
						if(isNew) {
							if (puzzle.getClass() == ReflectPuzzle.class) {
								((ReflectPiece) dragging).spawn();
							}
							/*
							if (MozaicPlayer.getMode() == GameMode.Reflect) {
								try {
									replace(dragging.getRef(), dragging.getID());
									dragging.spawn();
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							else {
								replace(dragging.getID());
							}
							*/
							replace(dragging);
						}
						
						MozaicAudio.drag();						
					}
					else
						MozaicAudio.dragFail();
				}
				
				else {
					if(isNew) {
						if (puzzle.getClass() == ReflectPuzzle.class) {
							((ReflectPiece) dragging).spawn();
						}
					}

						if (puzzle.overlaps(dragging)) {
							dragging.translateWOC(initX - x, initY - y);
							
							// Necessary if the piece was originally bordering another shape, but may
							// cause 'new snaps' to take place, although in theory this should not happen
							
							// SnapTo.snap(dragging, getIterator());
							
							MozaicAudio.dragFail();
						}
						
						else {
							MozaicAudio.dragSnap();
							if (isNew) 
								replace(dragging);
						}
//					}
				}
								
				dragging = null;
				isNew = false;			
			}
			else
				scrolling = false;
		}
		if (button == 1) {
			
			if (rotating != null) {
				if (rotating.fixRotation(originalRotation))
					MozaicAudio.dragFail();
			}
			else if (colourPicking) {
				touch.set(screenX, screenY, 0);
				puzzle.getRenderer().getCamera().unproject(touch);
				touchX = touch.x; touchY = touch.y;
				colourPick = puzzle.underMouse(touchX, touchY);
				
				if (colourPick != null)
					((ColourPuzzle) puzzle).setColor(colourPick.getColor());
			}
			rotating = null;
		}
		colourDragging = false;
		colourPicking = false;
		
		return true;
	}
	
	private void replace(Piece piece) {
		puzzle.create(piece.getID());
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		
		if (pinch) {
			rot.fixRotation(oR);
			rot = null;
			pinch = false;
		}
		
		touch.set(screenX, screenY, 0);
		puzzle.getRenderer().getCamera().unproject(touch);
		
		float x = touch.x; float y = touch.y;
		if (scrolling == true) {
			puzzle.getRenderer().scroll((x - touchX), (y - touchY));
			touchX = x; touchY = y;
		}
		else if (dragging != null) {
			drag.translateWOC(x - touchX, y - touchY);
			touchX = x; touchY = y;
		}
		else if (rotating != null) {
			newRotation = rotating.getAngle(x, y);
			rotating.rotate(-(rotation - newRotation));
			rotation = newRotation;
		}
		else if (colourDragging) {
			touch.set(screenX, screenY, 0);
			puzzle.getRenderer().getCameraO().unproject(touch);
			((ColourPuzzle) puzzle).setColor(touch.x, touch.y);
		}
		else if (colourPicking) {
			touch.set(screenX, screenY, 0);
			puzzle.getRenderer().getCamera().unproject(touch);
			touchX = touch.x; touchY = touch.y;
			colourPick = puzzle.underMouse(touchX, touchY);
			
			if (colourPick != null)
				((ColourPuzzle) puzzle).setColor(colourPick.getColor());
		}

		return true;
	} 

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		puzzle.getRenderer().zoom(amount);
		return true;
	}
}
