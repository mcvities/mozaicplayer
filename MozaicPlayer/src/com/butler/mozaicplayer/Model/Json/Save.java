package com.butler.mozaicplayer.Model.Json;

import com.badlogic.gdx.utils.Array;

public class Save {
	public Array<SavePuzzle> puzzles;
	public String mode;
	public boolean musicOn, sfxOn;
	public int reflection;
	
	public Save(Array<SavePuzzle> puzzles, String mode, int reflection, boolean musicOn, boolean sfxOn) {
		this.puzzles = puzzles;
		this.mode = mode;
		this.musicOn = musicOn;
		this.sfxOn = sfxOn;
		this.reflection = reflection;
	}

	public Save() {
	}
}