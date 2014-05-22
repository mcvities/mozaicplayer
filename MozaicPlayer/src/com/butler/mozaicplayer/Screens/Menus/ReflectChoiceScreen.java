package com.butler.mozaicplayer.Screens.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.butler.mozaicplayer.MozaicPlayer;

public class ReflectChoiceScreen extends Menu {
	
	private TextButton y0, x0, y_x, yx, y0x0, y_xyx;
	
	public ReflectChoiceScreen(MozaicPlayer game){
		super(game);
	}

	public void resize(int width, int height) {
		if(stage == null)
			stage = new Stage(width, height, true);
		stage.clear();
		
		Gdx.input.setInputProcessor(stage);
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("buttonnormal");
		style.down = skin.getDrawable("buttonpressed");
		style.font = black;
		
		y0 = new TextButton("Y = 0", style);
		x0 = new TextButton("X = 0", style);
		y_x = new TextButton("Y = -X", style);
		yx = new TextButton("Y = X", style);
		y0x0 = new TextButton("Y = 0\nX = 0", style);
		y_xyx = new TextButton("Y = -X\nY = X", style);
		
		y0.setWidth(440*MozaicPlayer.scale);
		y0.setHeight(Math.min(110*MozaicPlayer.scale, 0.11f*Gdx.graphics.getHeight()));
		x0.setWidth(440*MozaicPlayer.scale);
		x0.setHeight(Math.min(110*MozaicPlayer.scale, 0.11f*Gdx.graphics.getHeight()));
		y_x.setWidth(440*MozaicPlayer.scale);
		y_x.setHeight(Math.min(110*MozaicPlayer.scale, 0.11f*Gdx.graphics.getHeight()));
		yx.setWidth(440*MozaicPlayer.scale);
		yx.setHeight(Math.min(110*MozaicPlayer.scale, 0.11f*Gdx.graphics.getHeight()));
		y0x0.setWidth(440*MozaicPlayer.scale);
		y0x0.setHeight(Math.min(110*MozaicPlayer.scale, 0.11f*Gdx.graphics.getHeight()));
		y_xyx.setWidth(440*MozaicPlayer.scale);
		y_xyx.setHeight(Math.min(110*MozaicPlayer.scale, 0.11f*Gdx.graphics.getHeight()));

		y0.setX(Gdx.graphics.getWidth()/2 - y0.getWidth()/2);
		y0.setY(Gdx.graphics.getHeight()/2 - y0.getHeight()*(-1.3f));
		x0.setX(Gdx.graphics.getWidth()/2 - x0.getWidth()/2);
		x0.setY(Gdx.graphics.getHeight()/2 - x0.getHeight()*-0.2f);
		y_x.setX(Gdx.graphics.getWidth()/2 - y_x.getWidth()/2);
		y_x.setY(Gdx.graphics.getHeight()/2 - y_x.getHeight()*0.9f);
		yx.setX(Gdx.graphics.getWidth()/2 - yx.getWidth()/2);
		yx.setY(Gdx.graphics.getHeight()/2 - yx.getHeight()*2f);
		y0x0.setX(Gdx.graphics.getWidth()/2 - y0x0.getWidth()/2);
		y0x0.setY(Gdx.graphics.getHeight()/2 - y0x0.getHeight()*3.1f);
		y_xyx.setX(Gdx.graphics.getWidth()/2 - y_xyx.getWidth()/2);
		y_xyx.setY(Gdx.graphics.getHeight()/2 - y_xyx.getHeight()*4.2f);
		
		LabelStyle ls = new LabelStyle(white, Color.WHITE);
		label = new Label("Mozaic Player\n" + MozaicPlayer.VERSION, ls);
		label.setX(0);
		label.setY(Gdx.graphics.getHeight()*.75f);
		label.setWidth(width);
		label.setAlignment(Align.center);
		
		
		y0.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setReflection(1);
				game.setScreen(new MainMenu(game));
			}
		});
		
		x0.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setReflection(2);
				game.setScreen(new MainMenu(game));
			}
		});
		
		y_x.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setReflection(3);
				game.setScreen(new MainMenu(game));
			}
		});
		
		yx.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setReflection(4);
				game.setScreen(new MainMenu(game));
			}
		});
		
		y0x0.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setReflection(5);
				game.setScreen(new MainMenu(game));
			}
		});
		
		y_xyx.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setReflection(6);
				game.setScreen(new MainMenu(game));
			}
		});
		
		
		stage.addActor(y0);
		stage.addActor(x0);
		stage.addActor(y_x);
		stage.addActor(yx);
		stage.addActor(y0x0);
		stage.addActor(y_xyx);
		stage.addActor(label);
	}
}
