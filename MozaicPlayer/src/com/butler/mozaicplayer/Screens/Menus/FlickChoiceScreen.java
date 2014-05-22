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


public class FlickChoiceScreen extends Menu {

	private TextButton classicButton, p1Button, p2Button, p3Button;
	
	public FlickChoiceScreen(MozaicPlayer game){
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
		p1Button = new TextButton("Penrose P1", style);
		p2Button = new TextButton("Penrose P2", style);
		p3Button = new TextButton("Penrose P3", style);
		
		classicButton.setWidth(440*MozaicPlayer.scale);
		classicButton.setHeight(110*MozaicPlayer.scale);
		p1Button.setWidth(440*MozaicPlayer.scale);
		p1Button.setHeight(110*MozaicPlayer.scale);
		p2Button.setWidth(440*MozaicPlayer.scale);
		p2Button.setHeight(110*MozaicPlayer.scale);
		p3Button.setWidth(440*MozaicPlayer.scale);
		p3Button.setHeight(110*MozaicPlayer.scale);

		classicButton.setX(Gdx.graphics.getWidth()/2 - classicButton.getWidth()/2);
		classicButton.setY(Gdx.graphics.getHeight()/2 - classicButton.getHeight()*(-0.1f));
		p1Button.setX(Gdx.graphics.getWidth()/2 - p1Button.getWidth()/2);
		p1Button.setY(Gdx.graphics.getHeight()/2 - p1Button.getHeight());
		p2Button.setX(Gdx.graphics.getWidth()/2 - p2Button.getWidth()/2);
		p2Button.setY(Gdx.graphics.getHeight()/2 - p2Button.getHeight()*2.1f);
		p3Button.setX(Gdx.graphics.getWidth()/2 - p3Button.getWidth()/2);
		p3Button.setY(Gdx.graphics.getHeight()/2 - p3Button.getHeight()*3.2f);
		
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
				MozaicPlayer.setGameMode(MozaicPlayer.FLICK_CLASSIC);
				game.setScreen(new MainMenu(game));
			}
		});
		
		p1Button.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setGameMode(MozaicPlayer.FLICK_ORIGINAL);
				game.setScreen(new MainMenu(game));
			}
		});
		
		p2Button.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setGameMode(MozaicPlayer.FLICK_KITE_DART);
				game.setScreen(new MainMenu(game));
			}
		});
		
		p3Button.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.setGameMode(MozaicPlayer.FLICK_RHOMBS);
				game.setScreen(new MainMenu(game));
			}
		});
		
		stage.addActor(classicButton);
		stage.addActor(p1Button);
		stage.addActor(p2Button);
		stage.addActor(p3Button);
		stage.addActor(label);
	}
}
