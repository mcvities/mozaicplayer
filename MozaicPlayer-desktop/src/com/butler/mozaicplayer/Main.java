package com.butler.mozaicplayer;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Mozaic Player " + MozaicPlayer.VERSION;
		cfg.useGL20 = true;
		cfg.width = 506;
		cfg.height = 900;
		
		new LwjglApplication(new MozaicPlayer(), cfg);
	}
}
