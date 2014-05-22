package com.butler.mozaicplayer.Screens.Menus;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.butler.mozaicplayer.MozaicPlayer;

public class OptionsScreen extends Menu { 
	
	private BitmapFont white2;
	private TextureAtlas atlas2;
	private Skin skin2;
	private CheckBox muteMusicButton, muteSFXButton;
	private TextButton okayButton, resetCurrentButton, resetAllButton;
	private boolean inGame = false;
	
	public OptionsScreen(MozaicPlayer game){
		super(game);	
	}
	
	public OptionsScreen(MozaicPlayer game, boolean inGame) {
		super(game);
		this.inGame = inGame;
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
		
		CheckBoxStyle style2 = new CheckBoxStyle();
		style2.checkboxOff = skin2.getDrawable("unchecked");
		style2.checkboxOn = skin2.getDrawable("checked");
		style2.font = white2;
		
		
		muteMusicButton = new CheckBox("Mute Music", style2);
		muteSFXButton = new CheckBox("Mute SFX", style2);
		okayButton = new TextButton("Okay", style);
		resetCurrentButton = new TextButton("Reset Current\nPuzzle", style);
		resetAllButton = new TextButton("Reset All\nPuzzles", style);
		
		muteMusicButton.setWidth(440*MozaicPlayer.scale);
		muteMusicButton.setHeight(84);
		muteMusicButton.getCells().get(0).size(90*MozaicPlayer.scale, 90*MozaicPlayer.scale);
		muteSFXButton.setWidth(440*MozaicPlayer.scale);
		muteSFXButton.setHeight(84);
		muteSFXButton.getCells().get(0).size(90*MozaicPlayer.scale, 90*MozaicPlayer.scale);
		okayButton.setWidth(440*MozaicPlayer.scale);
		okayButton.setHeight(110*MozaicPlayer.scale);
		resetCurrentButton.setWidth(440*MozaicPlayer.scale);
		resetCurrentButton.setHeight(110*MozaicPlayer.scale);
		resetAllButton.setWidth(440*MozaicPlayer.scale);
		resetAllButton.setHeight(110*MozaicPlayer.scale);
		
		muteMusicButton.setChecked(game.isMusicMuted());
		muteSFXButton.setChecked(game.isSFXMuted());
		
		muteMusicButton.setX(Gdx.graphics.getWidth()/2 - muteMusicButton.getWidth()/2);
		muteMusicButton.setY(Gdx.graphics.getHeight()/2);
		muteSFXButton.setX(Gdx.graphics.getWidth()/2 - muteSFXButton.getWidth()/2);
		muteSFXButton.setY(Gdx.graphics.getHeight()/2 - muteSFXButton.getHeight()*1.1f);
		resetCurrentButton.setX(Gdx.graphics.getWidth()/2 - resetCurrentButton.getWidth()/2);
		resetCurrentButton.setY(Gdx.graphics.getHeight()/2 - resetCurrentButton.getHeight()*2.3f);
		resetAllButton.setX(Gdx.graphics.getWidth()/2 - resetCurrentButton.getWidth()/2);
		resetAllButton.setY(Gdx.graphics.getHeight()/2 - resetCurrentButton.getHeight()*3.4f);
		okayButton.setX(Gdx.graphics.getWidth()/2 - okayButton.getWidth()/2);
		okayButton.setY(Gdx.graphics.getHeight()/2 - okayButton.getHeight()*4.5f);
		
		LabelStyle ls = new LabelStyle(white, Color.WHITE);
		label = new Label("Mozaic Player\n" + MozaicPlayer.VERSION, ls);
		label.setX(0);
		label.setY(Gdx.graphics.getHeight()*.65f);
		label.setWidth(width);
		label.setAlignment(Align.center);
		
		
		muteMusicButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.toggleMusic();
			}
		});
		
		muteSFXButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.toggleSFX();
			}
		});
		
		okayButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (inGame)
					game.play();
				else game.setScreen(new MainMenu(game));
			}
		});
		
		resetCurrentButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				MozaicPlayer.puzzle.reset();
			}
		});
		
		resetAllButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.reset();			
			}
		});
		
		
		stage.addActor(muteMusicButton);
		stage.addActor(muteSFXButton);
		stage.addActor(okayButton);
		stage.addActor(resetCurrentButton);
		stage.addActor(resetAllButton);
		stage.addActor(label);
	}

	@Override
	public void show() {
		super.show();
		atlas2 = new TextureAtlas("data/checkbox.pack");
		Iterator<Texture> iter = atlas2.getTextures().iterator();
		while (iter.hasNext()) {
			iter.next().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		skin2 = new Skin();
		skin2.addRegions(atlas2);
		white2 = new BitmapFont(Gdx.files.internal("data/whitefont.fnt"), false);
		white2.setScale(1.5f*MozaicPlayer.scale);
	}

	@Override
	public void dispose() {
		super.dispose();
		atlas2.dispose();
		skin2.dispose();
		white2.dispose();
	}

}
