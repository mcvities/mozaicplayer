package com.butler.mozaicplayer.Screens.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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

public class MainMenu extends Menu {
	
	private TextButton playButton, modeButton, optionsButton, tutorialButton, quitButton;
	
	public MainMenu(MozaicPlayer game){
		super(game);	
	}

	public void resize(int width, int height) {
		if(stage == null) {
			stage = new Stage(width, height, true) {
				@Override
				public boolean keyDown(int keyCode) {
					if (keyCode == Keys.BACK) {
						game.saveAndQuit();
					}
					return super.keyDown(keyCode);
				}
			};
		}
		stage.clear();
		
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);

		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("buttonnormal");
		style.down = skin.getDrawable("buttonpressed");
		style.font = black;
		
		String play = "Play";
		if (MozaicPlayer.puzzle.started)
			play = "Resume";
		playButton = new TextButton(play, style);
		modeButton = new TextButton("Choose Mode", style);
		optionsButton = new TextButton("Options", style);
		tutorialButton = new TextButton("Tutorial", style);
		quitButton = new TextButton("Quit", style);
		
		playButton.setWidth(440*MozaicPlayer.scale);
		playButton.setHeight(110*MozaicPlayer.scale);
		modeButton.setWidth(440*MozaicPlayer.scale);
		modeButton.setHeight(110*MozaicPlayer.scale);
		optionsButton.setWidth(440*MozaicPlayer.scale);
		optionsButton.setHeight(110*MozaicPlayer.scale);
		tutorialButton.setWidth(440*MozaicPlayer.scale);
		tutorialButton.setHeight(110*MozaicPlayer.scale);
		quitButton.setWidth(440*MozaicPlayer.scale);
		quitButton.setHeight(110*MozaicPlayer.scale);
		
		playButton.setX(Gdx.graphics.getWidth()/2 - playButton.getWidth()/2);
		playButton.setY(Gdx.graphics.getHeight()/2 - playButton.getHeight()*(-0.1f));
		modeButton.setX(Gdx.graphics.getWidth()/2 - modeButton.getWidth()/2);
		modeButton.setY(Gdx.graphics.getHeight()/2 - modeButton.getHeight());
		optionsButton.setX(Gdx.graphics.getWidth()/2 - optionsButton.getWidth()/2);
		optionsButton.setY(Gdx.graphics.getHeight()/2 - optionsButton.getHeight()*2.1f);
		tutorialButton.setX(Gdx.graphics.getWidth()/2 - quitButton.getWidth()/2);
		tutorialButton.setY(Gdx.graphics.getHeight()/2 - quitButton.getHeight()*3.2f);
		quitButton.setX(Gdx.graphics.getWidth()/2 - quitButton.getWidth()/2);
		quitButton.setY(Gdx.graphics.getHeight()/2 - quitButton.getHeight()*4.3f);
		
		LabelStyle ls = new LabelStyle(white, Color.WHITE);
		label = new Label("Mozaic Player\n" + MozaicPlayer.VERSION, ls);
		label.setX(0);
		label.setY(Gdx.graphics.getHeight()*.7f);
		label.setWidth(width);
		label.setAlignment(Align.center);
		
		
		playButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.puzzle.started = true;
				game.play();
			}
		});
		
		modeButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.save();
				game.setScreen(new ModeChoosingScreen(game));
			}
		});
		
		optionsButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new OptionsScreen(game));
			}
		});
		
		tutorialButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new TutorialScreen(game));
			}
		});
		
		quitButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.saveAndQuit();
			}
		});		
		
		stage.addActor(playButton);
		stage.addActor(modeButton);
		stage.addActor(optionsButton);
		stage.addActor(tutorialButton);
		stage.addActor(quitButton);
		stage.addActor(label);
	}
}
