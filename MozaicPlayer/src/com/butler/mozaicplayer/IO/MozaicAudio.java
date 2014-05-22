package com.butler.mozaicplayer.IO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MozaicAudio {

	private MozaicAudio() {}
		
	private static Music song = Gdx.audio.newMusic(Gdx.files.internal("data/determination.mp3"));
	private static Sound fail = Gdx.audio.newSound(Gdx.files.internal("data/fail.wav"));
	private static Sound clink = Gdx.audio.newSound(Gdx.files.internal("data/clink.wav"));
	private static Sound swish = Gdx.audio.newSound(Gdx.files.internal("data/swish.wav"));
	private static Sound trash = Gdx.audio.newSound(Gdx.files.internal("data/trash.wav"));
	private static Sound success = Gdx.audio.newSound(Gdx.files.internal("data/success.wav"));
	private static boolean sfxOn = false;
	
	public static void playMusic(boolean looping) {
		song.setLooping(looping);
		song.play();
	}
	
	public static void stopMusic(){
		song.stop();
	}
	
	public static void toggleSFX() {
		sfxOn = !sfxOn;
	}
	
	public static void dispose() {
		song.dispose();
		fail.dispose();
		clink.dispose();
		swish.dispose();
		trash.dispose();
		success.dispose();
	}

	public static void dragSnap() {
		if (sfxOn)
			clink.play();		
	}

	public static void dragFail() {
		if (sfxOn)
			fail.play();
	}
	
	public static void drag() {
		if (sfxOn)
			success.play();
	}
	
	public static void delete() {
		if (sfxOn)
			trash.play();
	}
	
	public static void rotate() {
		if (sfxOn)
			swish.play();
	}
}
