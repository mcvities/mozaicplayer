package com.butler.mozaicplayer.Model.Json;

import com.badlogic.gdx.utils.Array;

public class SavePuzzle {
	
	// 1 = classic, 2 = reflection, 3 = colour, 4 = sprite, 5 = flick, 6 = sprite+flick
	public int puzzleType;
	public String name;
	public Array<SavePiece> savePieces;
	public Array<SavePuzzlePiece> puzzlePieces;
	public int[][][] matches;
	public boolean started;
	
	public SavePuzzle(String name, int puzzleType, Array<SavePiece> savePieces,	Array<SavePuzzlePiece> puzzlePieces, int[][][] matches, boolean started) {
		this.name = name;
		this.puzzleType = puzzleType;
		this.savePieces = savePieces;
		this.puzzlePieces = puzzlePieces;
		this.matches = matches;
		this.started = started;
	}
	
	public SavePuzzle(String name, Array<SavePiece> savePieces,	Array<SavePuzzlePiece> puzzlePieces, int[][][] matches, boolean started) {
		this.name = name;
		this.puzzleType = 0;
		this.savePieces = savePieces;
		this.puzzlePieces = puzzlePieces;
		this.matches = matches;
		this.started = started;
	}

	public SavePuzzle() {
	}
}