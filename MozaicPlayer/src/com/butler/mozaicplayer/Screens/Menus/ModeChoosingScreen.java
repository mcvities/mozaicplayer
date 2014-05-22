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

public class ModeChoosingScreen extends Menu {
	
	private TextButton classicButton, reflectButton, p1Button, p2Button, p3Button, pixelButton, flickButton;
	
	public ModeChoosingScreen(MozaicPlayer game){
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
		
		classicButton = new TextButton("Classic", style);
		reflectButton = new TextButton("Reflect", style);
		p1Button = new TextButton("Penrose P1", style);
		p2Button = new TextButton("Penrose P2", style);
		p3Button = new TextButton("Penrose P3", style);
		pixelButton = new TextButton("Pixels", style);
		flickButton = new TextButton("Flicking", style);
		
		classicButton.setWidth(440*MozaicPlayer.scale);
		classicButton.setHeight(Math.min(100*MozaicPlayer.scale, 0.1f*Gdx.graphics.getHeight()));
		reflectButton.setWidth(440*MozaicPlayer.scale);
		reflectButton.setHeight(Math.min(100*MozaicPlayer.scale, 0.1f*Gdx.graphics.getHeight()));
		p1Button.setWidth(440*MozaicPlayer.scale);
		p1Button.setHeight(Math.min(100*MozaicPlayer.scale, 0.1f*Gdx.graphics.getHeight()));
		p2Button.setWidth(440*MozaicPlayer.scale);
		p2Button.setHeight(Math.min(100*MozaicPlayer.scale, 0.1f*Gdx.graphics.getHeight()));
		p3Button.setWidth(440*MozaicPlayer.scale);
		p3Button.setHeight(Math.min(100*MozaicPlayer.scale, 0.1f*Gdx.graphics.getHeight()));
		pixelButton.setWidth(440*MozaicPlayer.scale);
		pixelButton.setHeight(Math.min(100*MozaicPlayer.scale, 0.1f*Gdx.graphics.getHeight()));
		flickButton.setWidth(440*MozaicPlayer.scale);
		flickButton.setHeight(Math.min(100*MozaicPlayer.scale, 0.1f*Gdx.graphics.getHeight()));

		classicButton.setX(Gdx.graphics.getWidth()/2 - classicButton.getWidth()/2);
		classicButton.setY(Gdx.graphics.getHeight()/2 - classicButton.getHeight()*(-1.5f));
		reflectButton.setX(Gdx.graphics.getWidth()/2 - reflectButton.getWidth()/2);
		reflectButton.setY(Gdx.graphics.getHeight()/2 - reflectButton.getHeight()*(-0.4f));
		p1Button.setX(Gdx.graphics.getWidth()/2 - p1Button.getWidth()/2);
		p1Button.setY(Gdx.graphics.getHeight()/2 - p1Button.getHeight()*0.7f);
		p2Button.setX(Gdx.graphics.getWidth()/2 - p2Button.getWidth()/2);
		p2Button.setY(Gdx.graphics.getHeight()/2 - p2Button.getHeight()*1.8f);
		p3Button.setX(Gdx.graphics.getWidth()/2 - p3Button.getWidth()/2);
		p3Button.setY(Gdx.graphics.getHeight()/2 - p3Button.getHeight()*2.9f);
		pixelButton.setX(Gdx.graphics.getWidth()/2 - pixelButton.getWidth()/2);
		pixelButton.setY(Gdx.graphics.getHeight()/2 - pixelButton.getHeight()*4f);
		flickButton.setX(Gdx.graphics.getWidth()/2 - flickButton.getWidth()/2);
		flickButton.setY(Gdx.graphics.getHeight()/2 - flickButton.getHeight()*5.1f);
		
		LabelStyle ls = new LabelStyle(white, Color.WHITE);
		label = new Label("Mozaic Player\n" + MozaicPlayer.VERSION, ls);
		label.setX(0);
		label.setY(Gdx.graphics.getHeight()*.75f);
		label.setWidth(width);
		label.setAlignment(Align.center);
		
		
		classicButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setGameMode(MozaicPlayer.CLASSIC);
				game.setScreen(new MainMenu(game));
			}
		});
		
		reflectButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setGameMode(MozaicPlayer.REFLECT);
				game.setScreen(new ReflectChoiceScreen(game));
			}
		});
		
		p1Button.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setGameMode(MozaicPlayer.ORIGINAL);
				game.setScreen(new MainMenu(game));
			}
		});
		
		p2Button.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setGameMode(MozaicPlayer.KITE_DART);
				game.setScreen(new MainMenu(game));
			}
		});
		
		p3Button.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setGameMode(MozaicPlayer.RHOMBS);
				game.setScreen(new MainMenu(game));
			}
		});
		
		pixelButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setGameMode(MozaicPlayer.PIXELS);
				game.setScreen(new MainMenu(game));
			}
		});
		
		flickButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new FlickChoiceScreen(game));
			}
		});
		
		stage.addActor(classicButton);
		stage.addActor(reflectButton);
		stage.addActor(p1Button);
		stage.addActor(p2Button);
		stage.addActor(p3Button);
		stage.addActor(pixelButton);
		stage.addActor(flickButton);
		stage.addActor(label);
	}
}
