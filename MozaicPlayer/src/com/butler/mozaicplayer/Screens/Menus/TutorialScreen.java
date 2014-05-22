package com.butler.mozaicplayer.Screens.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.MozaicPlayer;

public class TutorialScreen extends Menu {
	
	private Sprite background;
	private TextButton nextButton, previousButton;
	private int counter;
	private Array<Sprite> backgrounds;
	private Image image;
	
	public TutorialScreen(MozaicPlayer game){
		super(game);
		backgrounds = new Array<Sprite>(false, 10);
		
		for (int i = 1; i < 11; i++) {
			background = new Sprite(new Texture("data/" + Integer.toString(i) + ".png"));
			backgrounds.add(background);
		}
		
		background = backgrounds.first();
		image = new Image(background);
		image.setBounds(0, 0, MozaicPlayer.width, MozaicPlayer.height);
		counter = 0;
	}

	public void resize(int width, int height) {
		if(stage == null) {
			stage = new Stage(width, height, true) {
				@Override
				public boolean keyDown(int keyCode) {
					if (keyCode == Keys.BACK) {
						game.setScreen(new MainMenu(game));
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
		
		nextButton = new TextButton("Next", style);
		previousButton = new TextButton("Prev", style);
		
		nextButton.setWidth(200*MozaicPlayer.scale);
		nextButton.setHeight(80*MozaicPlayer.scale);
		previousButton.setWidth(200*MozaicPlayer.scale);
		previousButton.setHeight(80*MozaicPlayer.scale);
		
		nextButton.setX(Gdx.graphics.getWidth() - nextButton.getWidth()*1.3f);
		nextButton.setY(nextButton.getHeight()/2);
		previousButton.setX(previousButton.getWidth()*0.3f);
		previousButton.setY(previousButton.getHeight()/2);
		

		nextButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				counter++;
				if (counter == 10) {
					game.setScreen(new MainMenu(game));
				}
				else {
					background = backgrounds.get(counter);
					
					previousButton.remove();
					nextButton.remove();
					image.remove();
					
					image = new Image(background);
					image.setBounds(0, 0, MozaicPlayer.width, MozaicPlayer.height);
					stage.addActor(image);
					stage.addActor(nextButton);
					stage.addActor(previousButton);
				}
			}
		});
		
		previousButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				counter--;
				if (counter == -1) {
					game.setScreen(new MainMenu(game));
				}
				else {
					background = backgrounds.get(counter);
					
					previousButton.remove();
					nextButton.remove();
					image.remove();
					
					image = new Image(background);
					image.setBounds(0, 0, MozaicPlayer.width, MozaicPlayer.height);
					stage.addActor(image);
					stage.addActor(nextButton);
					stage.addActor(previousButton);
				}
			}
		});
		
		stage.addActor(image);
		stage.addActor(nextButton);
		stage.addActor(previousButton);
	}
}
