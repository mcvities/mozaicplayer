package com.butler.mozaicplayer;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.butler.mozaicplayer.IO.MozaicAudio;
import com.butler.mozaicplayer.Model.ColourPuzzle;
import com.butler.mozaicplayer.Model.FlickPuzzle;
import com.butler.mozaicplayer.Model.OriginalPuzzle;
import com.butler.mozaicplayer.Model.Puzzle;
import com.butler.mozaicplayer.Model.ReflectPuzzle;
import com.butler.mozaicplayer.Model.Factories.FlickPuzzlePiece;
import com.butler.mozaicplayer.Model.Factories.FlickSpritePuzzlePiece;
import com.butler.mozaicplayer.Model.Factories.PuzzlePiece;
import com.butler.mozaicplayer.Model.Factories.ReflectPuzzlePiece;
import com.butler.mozaicplayer.Model.Factories.SpritePuzzlePiece;
import com.butler.mozaicplayer.Model.Json.Save;
import com.butler.mozaicplayer.Model.Json.SavePiece;
import com.butler.mozaicplayer.Model.Json.SavePuzzle;
import com.butler.mozaicplayer.Model.Json.SavePuzzlePiece;
import com.butler.mozaicplayer.Screens.ColourGameScreen;
import com.butler.mozaicplayer.Screens.FlickSpriteGameScreen;
import com.butler.mozaicplayer.Screens.FlickingGameScreen;
import com.butler.mozaicplayer.Screens.GameScreen;
import com.butler.mozaicplayer.Screens.SpriteGameScreen;
import com.butler.mozaicplayer.Screens.StaticGameScreen;
import com.butler.mozaicplayer.Screens.Menus.MainMenu;

public class MozaicPlayer extends Game {

	private static float WIDTH, LHEIGHT, LSLIDE, SHEIGHT, SSLIDE, X1, X2, X3,
			Y1, Y2, COS54, COS36, COS72, SIN72, SX1, SX2, SX3, SY1, SY2, SY3,
			OX, OY, BX1, BX2, BX3, BY1, BY2, BOX, BOY, DX, DY, DOY, KX1, KX2,
			KY, KOX, KOY, DaX1, DaX2, DaY, DaOX, DaOY, SQUARE, RY;

	public static final String VERSION = "0.5.6";
//	public static final String LOG = "Mozaic Player";
	public static final int PIECES = 100;
	
	private static final int assumeWidth = 683;
	public static int width;
	public static int height;
	public static int size;
	public static float scale;
	public static int trans;

	private static int reflection = 0;

	public static final String CLASSIC = "classic";
	public static final String REFLECT = "reflect";
	public static final String ORIGINAL = "original";
	public static final String KITE_DART = "kite_dart";
	public static final String RHOMBS = "rhomb";
	public static final String PIXELS = "pixel";
	public static final String FLICK_CLASSIC = "flick_classic";
	public static final String FLICK_ORIGINAL = "flick_original";
	public static final String FLICK_KITE_DART = "flick_kite_dart";
	public static final String FLICK_RHOMBS = "flick_rhombs";

	private static String gameMode = CLASSIC;

	private static boolean musicOn = false;
	private static boolean sfxOn = false;

//	FPSLogger log;

	private static HashMap<String, Puzzle> puzzles = new HashMap<String, Puzzle>(10);
	private static HashMap<String, SavePuzzle> savePuzzles = new HashMap<String, SavePuzzle>(10);

	public static Puzzle puzzle;

	@Override
	public void create() {
		// reset();
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		size = width; // Math.min(width, height);
		scale = (float) size / assumeWidth;
		trans = height - width; // Math.max(width, height) - size;

//		log = new FPSLogger();

		// Rhombs
		WIDTH = (float) (MozaicPlayer.scale * 150);
		LHEIGHT = (float) (WIDTH * Math.sin(Math.toRadians(72)));
		LSLIDE = (float) (WIDTH * Math.cos(Math.toRadians(72)));
		SHEIGHT = (float) (WIDTH * Math.sin(Math.toRadians(36)));
		SSLIDE = (float) (WIDTH * Math.cos(Math.toRadians(36)));

		// Pentagons
		X1 = (float) (MozaicPlayer.scale * 100);
		X2 = (float) (MozaicPlayer.scale * 100 * Math.sin(Math.toRadians(18)));
		X3 = X1 / 2;
		Y1 = (float) (MozaicPlayer.scale * 100 * Math.cos(Math.toRadians(18)));
		Y2 = (float) (MozaicPlayer.scale * 100 * Math.sin(Math.toRadians(36)));

		// Stars
		COS54 = (float) Math.cos(Math.toRadians(54));
		COS36 = (float) Math.cos(Math.toRadians(36));
		COS72 = (float) Math.cos(Math.toRadians(72));
		SIN72 = (float) Math.sin(Math.toRadians(72));

		SX1 = (float) (MozaicPlayer.scale * 100 * (COS72 * (2 * COS72 + 2)));
		SX2 = (float) (MozaicPlayer.scale * 100 * COS72);
		SX3 = (float) (MozaicPlayer.scale * 100 * (COS36 - COS72));

		SY1 = (float) (MozaicPlayer.scale * 100 * (SIN72 * (2 * COS72 + 2)));
		SY2 = (float) (MozaicPlayer.scale * 100 * SIN72);
		SY3 = (float) (MozaicPlayer.scale * 100 * COS54);

		OX = (float) (MozaicPlayer.scale * 100 * Math.sin(Math.toRadians(54)));
		OY = (float) (MozaicPlayer.scale * 100 * (COS54 + (COS72 / Math
				.sin(Math.toRadians(36)))));

		// Boats
		BOX = (float) (MozaicPlayer.scale * 50);
		BOY = (float) (MozaicPlayer.scale * 100 * Math.sin(Math.toRadians(36)));

		BX1 = (float) (MozaicPlayer.scale * 50);
		BX2 = (float) (MozaicPlayer.scale * 100 * Math.cos(Math.toRadians(36)));
		BX3 = (float) ((MozaicPlayer.scale * 100) - BX2);

		BY1 = (float) (MozaicPlayer.scale * 100 * (Math.sin(Math.toRadians(72)) * (2 * COS72 + 1)));
		BY2 = (float) (MozaicPlayer.scale * 100 * Math.sin(Math.toRadians(36)));

		// Diamonds
		DX = MozaicPlayer.scale * 100 * COS72;
		DY = MozaicPlayer.scale * 100 * SIN72;
		DOY = DY;

		// Kites
		KX1 = (float) (MozaicPlayer.scale * 150);
		KX2 = (float) (MozaicPlayer.scale
				* ((Math.tan(Math.toRadians(36)))
						/ (Math.tan(Math.toRadians(72))) + 1) * 150);
		KY = (float) (MozaicPlayer.scale * (Math.tan(Math.toRadians(36))) * 150);

		KOX = (float) (MozaicPlayer.scale * (((Math.tan(Math.toRadians(36)))
				/ (Math.tan(Math.toRadians(72))) + 1) * 75));
		KOY = KY;

		// Darts
		DaX1 = (float) (MozaicPlayer.scale * 150);
		DaX2 = (float) (MozaicPlayer.scale * ((Math.tan(Math.toRadians(36)))
				/ (Math.tan(Math.toRadians(72))) * 150));
		DaY = (float) (MozaicPlayer.scale * (Math.tan(Math.toRadians(36))) * 150);

		DaOX = (float) (MozaicPlayer.scale * ((((Math.tan(Math.toRadians(36)))
				/ (Math.tan(Math.toRadians(72))) + 1) * 75)));
		DaOY = DaY;

		// Squares (and triangles)
		SQUARE = (float) (MozaicPlayer.scale * 100);

		// Rhombuses
		RY = (float) ((MozaicPlayer.scale * 100) / Math.sqrt(2));

		if (!Gdx.files.local("data/save_file.txt").exists())
			initialise();
		
		load();
		
		puzzle = puzzles.get(gameMode);
		
		setScreen(new MainMenu(this));
	}

	

	private void load() {
		Json json = new Json();
		FileHandle saveFile = Gdx.files.local("data/save_file.txt");
		String saveString = saveFile.readString();
		Save save = json.fromJson(Save.class, saveString);

		Array<SavePuzzle> savePuzzles_ = save.puzzles;
		gameMode = save.mode;
		reflection = save.reflection;
		sfxOn = save.sfxOn;
		musicOn = save.musicOn;
		Puzzle puzzle;
		PuzzlePiece puzzlePiece = null;

		for (SavePuzzle savePuzzle : savePuzzles_) {

			Array<SavePuzzlePiece> puzzlePieces = savePuzzle.puzzlePieces;
			Array<PuzzlePiece> pieces = new Array<PuzzlePiece>(false,
					puzzlePieces.size);
			if (savePuzzle.puzzleType == 1)
				puzzle = new OriginalPuzzle(puzzlePieces.size, pieces, savePuzzle.matches, savePuzzle.started);
			
			else if (savePuzzle.puzzleType == 2)
				puzzle = new ReflectPuzzle(puzzlePieces.size, pieces, savePuzzle.matches, savePuzzle.started);
			
			else if (savePuzzle.puzzleType == 3)
				puzzle = new ColourPuzzle(puzzlePieces.size, pieces, savePuzzle.matches, savePuzzle.started);
			
			else if (savePuzzle.puzzleType == 5 || savePuzzle.puzzleType == 6)
				puzzle = new FlickPuzzle(puzzlePieces.size, pieces, savePuzzle.matches, savePuzzle.started);
			
			else puzzle = new Puzzle(puzzlePieces.size, pieces, savePuzzle.matches, savePuzzle.started);

			for (SavePuzzlePiece savePuzzlePiece : puzzlePieces) {

				if (savePuzzle.puzzleType == 2)
					puzzlePiece = new ReflectPuzzlePiece(
							puzzle,
							savePuzzlePiece.oX,
							savePuzzlePiece.oY,
							savePuzzlePiece.coords.clone(), // CREATED ERRORS IF
															// NOT CLONE
							savePuzzlePiece.triangles.length / 3,
							savePuzzlePiece.triangles, savePuzzlePiece.color,
							savePuzzlePiece.id, -savePuzzlePiece.coords[0],
							-savePuzzlePiece.coords[1]);
				else if (savePuzzle.puzzleType == 4)
					puzzlePiece = new SpritePuzzlePiece(puzzle, savePuzzlePiece.oX,
							savePuzzlePiece.oY, savePuzzlePiece.coords,
							savePuzzlePiece.triangles.length / 3,
							savePuzzlePiece.triangles, savePuzzlePiece.color,
							savePuzzlePiece.id, savePuzzlePiece.textureAddress);
				else if (savePuzzle.puzzleType == 5) 
					puzzlePiece = new FlickPuzzlePiece(puzzle, savePuzzlePiece.oX, savePuzzlePiece.oY,
							savePuzzlePiece.coords, savePuzzlePiece.triangles.length / 3,
							savePuzzlePiece.triangles, savePuzzlePiece.color,
							savePuzzlePiece.id, savePuzzlePiece.mass);
				else if (savePuzzle.puzzleType == 6) 
					puzzlePiece = new FlickSpritePuzzlePiece(puzzle, savePuzzlePiece.oX, savePuzzlePiece.oY,
							savePuzzlePiece.coords, savePuzzlePiece.triangles.length / 3,
							savePuzzlePiece.triangles, savePuzzlePiece.color,
							savePuzzlePiece.id, savePuzzlePiece.textureAddress, savePuzzlePiece.mass);
				else
					puzzlePiece = new PuzzlePiece(puzzle, savePuzzlePiece.oX,
							savePuzzlePiece.oY, savePuzzlePiece.coords,
							savePuzzlePiece.triangles.length / 3,
							savePuzzlePiece.triangles, savePuzzlePiece.color,
							savePuzzlePiece.id);

				pieces.add(puzzlePiece);
			}

			puzzle.started = savePuzzle.started;
			puzzle.load(savePuzzle.savePieces);
			puzzle.initiate();
			puzzles.put(savePuzzle.name, puzzle);
			savePuzzles.put(savePuzzle.name, savePuzzle);
		}

	}

	// Save to use for custom puzzles
	// private void load() {
	// Json json = new Json();
	// FileHandle saveFile = Gdx.files.local("data/kite_dart_puzzle.txt");
	// String saveString = saveFile.readString();
	// SavePuzzle save = json.fromJson(SavePuzzle.class, saveString);
	//
	// Array<SavePuzzlePiece> puzzlePieces = save.puzzlePieces;
	// Array<PuzzlePiece> pieces = new Array<PuzzlePiece>(false,
	// puzzlePieces.size);
	// kiteDartPuzzle = new Puzzle(puzzlePieces.size, pieces, save.matches);
	//
	// for (SavePuzzlePiece savePuzzlePiece : puzzlePieces)
	// pieces.add(new PuzzlePiece(kiteDartPuzzle, savePuzzlePiece.oX,
	// savePuzzlePiece.oY, savePuzzlePiece.coords,
	// savePuzzlePiece.triangles.length/3, savePuzzlePiece.triangles,
	// savePuzzlePiece.color, savePuzzlePiece.id));
	//
	// kiteDartPuzzle.initiate();
	// }

	public void save() {
		SavePuzzle savePuzzle = savePuzzles.get(gameMode);
		savePuzzle.savePieces = puzzle.save();
		savePuzzle.started = puzzle.started;
	}

	public void saveAndQuit() {
		save(); // Save current
		Json json = new Json();
		Array<SavePuzzle> saveArray = new Array<SavePuzzle>(false,
				savePuzzles.size());
		for (SavePuzzle savePuzzle : savePuzzles.values())
			saveArray.add(savePuzzle);

		Save save = new Save(saveArray, gameMode, reflection, musicOn, sfxOn);
		String saveString = json.toJson(save, Save.class);
		FileHandle saveFile = Gdx.files.local("data/save_file.txt");
		saveFile.writeString(saveString, false);

		Gdx.app.exit();
	}

	public void reset() {
		Array<SavePuzzle> saveArray = new Array<SavePuzzle>(false,
				savePuzzles.size());
		for (SavePuzzle savePuzzle : savePuzzles.values()) {
			savePuzzle.savePieces = new Array<SavePiece>(false, 0);
			saveArray.add(savePuzzle);
		}

		for (Puzzle puzzle : puzzles.values()) {
			puzzle.reset();
		}

		Save save = new Save(saveArray, gameMode, reflection, musicOn, sfxOn);

		Json json = new Json();
		String saveString = json.toJson(save, Save.class);
		FileHandle saveFile = Gdx.files.local("data/save_file.txt");
		saveFile.writeString(saveString, false);
	}

	public static void setGameMode(String mode) {
		gameMode = mode;
		puzzle = puzzles.get(gameMode);
	}

	public void play() {
		Puzzle puzzle = puzzles.get(gameMode);
		// if (!initiated)
		// puzzle.initiate();

		if (gameMode.equals(CLASSIC) || gameMode.equals(REFLECT))
			setScreen(new StaticGameScreen(this, puzzle));
		else if (gameMode.equals(PIXELS))
			setScreen(new ColourGameScreen(this, puzzle));
		else if (gameMode.equals(RHOMBS) || gameMode.equals(KITE_DART))
			setScreen(new SpriteGameScreen(this, puzzle));
		else if (gameMode.startsWith("flick")) {
			if (gameMode.equals(FLICK_RHOMBS) || gameMode.equals(FLICK_KITE_DART))
				setScreen(new FlickSpriteGameScreen(this, puzzle));
			else 
				setScreen(new FlickingGameScreen(this, puzzle));
		}
		else
			setScreen(new GameScreen(this, puzzle));
	}

	@Override
	public void dispose() {
		super.dispose();
		MozaicAudio.dispose();
	}

	@Override
	public void render() {
		super.render();
		// log.log();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	public void toggleMusic() {
		musicOn = !musicOn;
		if (musicOn)
			MozaicAudio.playMusic(true);
		else
			MozaicAudio.stopMusic();
	}

	public void toggleSFX() {
		MozaicAudio.toggleSFX();
		sfxOn = !sfxOn;
	}

	public boolean isMusicMuted() {
		return !musicOn;
	}

	public boolean isSFXMuted() {
		return !sfxOn;
	}

	public static void setReflection(int ref) {
		if (reflection != ref) {
			reflection = ref;
			((ReflectPuzzle) puzzles.get(REFLECT)).setRef(ref);
			puzzles.get(REFLECT).reset();
		}
	}

	private void initialise() {
		Json json = new Json();
		Array<SavePuzzle> savePuzzles = new Array<SavePuzzle>(10);
		Save save = new Save(savePuzzles, CLASSIC, 0, false, false);

		Array<SavePiece> savePieces = new Array<SavePiece>(false, 100);
		Array<SavePuzzlePiece> puzzlePieces = new Array<SavePuzzlePiece>(false, 2);
		Array<SavePuzzlePiece> flickPuzzlePieces = new Array<SavePuzzlePiece>(false, 2);

		int[][][] matches = new int[][][] {
				{
						{ 1, 2, 1, 2, 1, 2, 3, 2, 2, 3, 2, 3, 2, 3, 2, 1, 0, 1,
								0, 1, 0, 1, 0, 3, 0, 3, 0, 3, 0, 3, 0, 1 },
						{ 0, 1, 1, 0, 0, 1, 3, 0, 0, 3, 1, 0, 0, 3, 3, 0, 2, 1,
								1, 2, 2, 1, 3, 2, 2, 3, 1, 2, 2, 3, 3, 2 } },

				{
						{ 0, 1, 1, 0, 0, 1, 3, 0, 0, 3, 1, 0, 0, 3, 3, 0, 2, 1,
								1, 2, 2, 1, 3, 2, 2, 3, 1, 2, 2, 3, 3, 2 },
						{ 0, 1, 0, 1, 0, 1, 0, 3, 0, 3, 0, 3, 0, 3, 0, 1 } } };

		float[] coords = new float[] { -KOX, -KOY + KY, -KOX + KX1, -KOY,
				-KOX + KX2, -KOY + KY, -KOX + KX1, -KOY + 2 * KY };
		int[] triangles = new int[] { 0, 1, 2, 0, 3, 2 };
		Color color1 = Color.BLUE;
//		puzzlePieces.add(new SavePuzzlePiece(0.33f, 0.5f, coords, triangles,
//				color1, 0));
		puzzlePieces.add(new SavePuzzlePiece(0.33f, 0.5f, coords, triangles,
				color1, 0, "data/kite.png"));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.33f, 0.5f, coords, triangles,
				color1, 0, "data/kite.png", 1.5f));

		coords = new float[] { -DaOX + DaX1, -DaOY + DaY, -DaOX, -DaOY,
				-DaOX + DaX2, -DaOY + DaY, -DaOX, -DaOY + 2 * DaY };
		Color color2 = Color.CYAN;
//		puzzlePieces.add(new SavePuzzlePiece(0.66f, 0.5f, coords, triangles,
//				color2, 1));
		puzzlePieces.add(new SavePuzzlePiece(0.66f, 0.5f, coords, triangles,
				color2, 1, "data/dart.png"));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.66f, 0.5f, coords, triangles,
				color2, 1, "data/dart.png", 1));
		
//		SavePuzzle savePuzzle = new SavePuzzle("kite_dart", savePieces,
//				puzzlePieces, matches, false);
		SavePuzzle savePuzzle = new SavePuzzle(KITE_DART, 4, savePieces, puzzlePieces, matches, false);
		savePuzzles.add(savePuzzle);
		
		savePuzzle = new SavePuzzle(FLICK_KITE_DART, 6, savePieces, flickPuzzlePieces, matches, false);
		savePuzzles.add(savePuzzle);


		puzzlePieces = new Array<SavePuzzlePiece>(false, 2);
		flickPuzzlePieces = new Array<SavePuzzlePiece>(false, 2);
		
		matches = new int[][][] {
				{ { 0, 1, 0, 3, 0, 3, 0, 1, 1, 2, 3, 2, 3, 2, 1, 2 },
						{ 0, 1, 3, 2, 0, 3, 3, 0, 1, 2, 1, 0, 3, 2, 1, 2 } },
				{ { 3, 2, 0, 1, 3, 0, 0, 3, 1, 0, 1, 2, 1, 2, 3, 2 },
						{ 0, 1, 2, 1, 2, 1, 0, 1, 0, 3, 2, 3, 2, 3, 0, 3 } } };

		coords = new float[] { (-WIDTH - LSLIDE) / 2, -LHEIGHT / 2,
				(WIDTH - LSLIDE) / 2, -LHEIGHT / 2, (WIDTH + LSLIDE) / 2,
				LHEIGHT / 2, (-WIDTH + LSLIDE) / 2, LHEIGHT / 2 };
		triangles = new int[] { 0, 1, 2, 0, 3, 2 };
		color1 = Color.BLUE;
		puzzlePieces.add(new SavePuzzlePiece(0.33f, 0.5f, coords, triangles,
				color1, 0, "data/large_rhomb.png"));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.33f, 0.5f, coords, triangles,
				color1, 0, "data/large_rhomb.png", 1.5f));

		coords = new float[] { (-WIDTH - SSLIDE) / 2, -SHEIGHT / 2,
				(WIDTH - SSLIDE) / 2, -SHEIGHT / 2, (WIDTH + SSLIDE) / 2,
				SHEIGHT / 2, (-WIDTH + SSLIDE) / 2, SHEIGHT / 2 };
		triangles = new int[] { 0, 1, 2, 0, 3, 2 };
		color2 = Color.CYAN;
		puzzlePieces.add(new SavePuzzlePiece(0.66f, 0.5f, coords, triangles,
				color2, 1, "data/small_rhomb.png"));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.66f, 0.5f, coords, triangles,
				color2, 1, "data/small_rhomb.png", 1));

		savePuzzle = new SavePuzzle(RHOMBS, 4, savePieces, puzzlePieces, matches, false);
		savePuzzles.add(savePuzzle);
		
		savePuzzle = new SavePuzzle(FLICK_RHOMBS, 6, savePieces, flickPuzzlePieces, matches, false);
		savePuzzles.add(savePuzzle);
		

		puzzlePieces = new Array<SavePuzzlePiece>(false, 1);

		matches = new int[][][] { { { 0, 1, 3, 2, 1, 2, 0, 3, 3, 2, 0, 1, 0, 3,
				1, 2 } } };

		coords = new float[] { -SQUARE, -SQUARE, SQUARE, -SQUARE, SQUARE,
				SQUARE, -SQUARE, SQUARE };
		triangles = new int[] { 0, 1, 2, 0, 3, 2 };
		color1 = Color.BLUE;
		puzzlePieces.add(new SavePuzzlePiece(0.25f, 0.5f, coords, triangles,
				color1, 0));

		savePuzzle = new SavePuzzle(PIXELS, 3, savePieces, puzzlePieces, matches, false);
		savePuzzles.add(savePuzzle);

		
		puzzlePieces = new Array<SavePuzzlePiece>(false, 6);
		flickPuzzlePieces = new Array<SavePuzzlePiece>(false, 6);

		matches = new int[][][] {
				{
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 0, 2, 3, 0, 4, 3, 4, 0, 1, 3, 4, 1, 0, 3, 4,
								1, 2, 3, 4, 2, 1, 3, 4, 2, 3, 3, 4, 3, 2, 3, 4,
								3, 4, 3, 4, 4, 3, 3, 4, 4, 0, 3, 4, 0, 4, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								2, 3, 4, 0, 3, 2, 4, 0, 3, 4, 4, 0, 4, 3, 4, 0,
								4, 0, 4, 0, 0, 4 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 0, 2, 3, 0, 4, 3, 4, 0, 1, 3, 4, 1, 0, 3, 4,
								1, 2, 3, 4, 2, 1, 3, 4, 2, 3, 3, 4, 3, 2, 3, 4,
								3, 4, 3, 4, 4, 3, 3, 4, 4, 0, 3, 4, 0, 4, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								2, 3, 4, 0, 3, 2, 4, 0, 3, 4, 4, 0, 4, 3, 4, 0,
								4, 0, 4, 0, 0, 4 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 0, 2, 3, 0, 4, 3, 4, 0, 1, 3, 4, 1, 0, 3, 4,
								1, 2, 3, 4, 2, 1, 3, 4, 2, 3, 3, 4, 3, 2, 3, 4,
								3, 4, 3, 4, 4, 3, 3, 4, 4, 0, 3, 4, 0, 4, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								2, 3, 4, 0, 3, 2, 4, 0, 3, 4, 4, 0, 4, 3, 4, 0,
								4, 0, 4, 0, 0, 4 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 5, 0, 1, 5, 4, 0, 1, 5, 6, 0, 1, 6, 5, 0, 1,
								6, 7, 0, 1, 7, 6, 0, 1, 7, 8, 0, 1, 8, 7, 0, 1,
								8, 9, 0, 1, 9, 8, 0, 1, 9, 0, 0, 1, 0, 9, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 1, 2, 1, 2, 2, 1, 1, 2,
								2, 3, 1, 2, 3, 2, 1, 2, 3, 4, 1, 2, 4, 3, 1, 2,
								4, 5, 1, 2, 5, 4, 1, 2, 5, 6, 1, 2, 6, 5, 1, 2,
								6, 7, 1, 2, 7, 6, 1, 2, 7, 8, 1, 2, 8, 7, 1, 2,
								8, 9, 1, 2, 9, 8, 1, 2, 9, 0, 1, 2, 0, 9, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 5, 2, 3, 5, 4, 2, 3, 5, 6, 2, 3, 6, 5, 2, 3,
								6, 7, 2, 3, 7, 6, 2, 3, 7, 8, 2, 3, 8, 7, 2, 3,
								8, 9, 2, 3, 9, 8, 2, 3, 9, 0, 2, 3, 0, 9, 3, 4,
								0, 1, 3, 4, 1, 0, 3, 4, 1, 2, 3, 4, 2, 1, 3, 4,
								2, 3, 3, 4, 3, 2, 3, 4, 3, 4, 3, 4, 4, 3, 3, 4,
								4, 5, 3, 4, 5, 4, 3, 4, 5, 6, 3, 4, 6, 5, 3, 4,
								6, 7, 3, 4, 7, 6, 3, 4, 7, 8, 3, 4, 8, 7, 3, 4,
								8, 9, 3, 4, 9, 8, 3, 4, 9, 0, 3, 4, 0, 9, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								2, 3, 4, 0, 3, 2, 4, 0, 3, 4, 4, 0, 4, 3, 4, 0,
								4, 5, 4, 0, 5, 4, 4, 0, 5, 6, 4, 0, 6, 5, 4, 0,
								6, 7, 4, 0, 7, 6, 4, 0, 7, 8, 4, 0, 8, 7, 4, 0,
								8, 9, 4, 0, 9, 8, 4, 0, 9, 0, 4, 0, 0, 9 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 5, 0, 1, 5, 4, 0, 1, 5, 6, 0, 1, 6, 5, 0, 1,
								6, 0, 0, 1, 0, 6, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 5, 1, 2, 5, 4, 1, 2,
								5, 6, 1, 2, 6, 5, 1, 2, 6, 0, 1, 2, 0, 6, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 5, 2, 3, 5, 4, 2, 3, 5, 6, 2, 3, 6, 5, 2, 3,
								6, 0, 2, 3, 0, 6, 3, 4, 0, 1, 3, 4, 1, 0, 3, 4,
								1, 2, 3, 4, 2, 1, 3, 4, 2, 3, 3, 4, 3, 2, 3, 4,
								3, 4, 3, 4, 4, 3, 3, 4, 4, 5, 3, 4, 5, 4, 3, 4,
								5, 6, 3, 4, 6, 5, 3, 4, 6, 0, 3, 4, 0, 6, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								2, 3, 4, 0, 3, 2, 4, 0, 3, 4, 4, 0, 4, 3, 4, 0,
								4, 5, 4, 0, 5, 4, 4, 0, 5, 6, 4, 0, 6, 5, 4, 0,
								6, 0, 4, 0, 0, 6 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 0, 0, 1, 0, 3, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 1, 2, 1, 2, 2, 1, 1, 2,
								2, 3, 1, 2, 3, 2, 1, 2, 3, 0, 1, 2, 0, 3, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 0, 2, 3, 0, 3, 3, 4,
								0, 1, 3, 4, 1, 0, 3, 4, 1, 2, 3, 4, 2, 1, 3, 4,
								2, 3, 3, 4, 3, 2, 3, 4, 3, 0, 3, 4, 0, 3, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								2, 3, 4, 0, 3, 2, 4, 0, 3, 0, 4, 0, 0, 3 } },

				{
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 0, 2, 3, 0, 4, 3, 4, 0, 1, 3, 4, 1, 0, 3, 4,
								1, 2, 3, 4, 2, 1, 3, 4, 2, 3, 3, 4, 3, 2, 3, 4,
								3, 4, 3, 4, 4, 3, 3, 4, 4, 0, 3, 4, 0, 4, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								2, 3, 4, 0, 3, 2, 4, 0, 3, 4, 4, 0, 4, 3, 4, 0,
								4, 0, 4, 0, 0, 4 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 0, 2, 3, 0, 4, 3, 4, 0, 1, 3, 4, 1, 0, 3, 4,
								1, 2, 3, 4, 2, 1, 3, 4, 2, 3, 3, 4, 3, 2, 3, 4,
								3, 4, 3, 4, 4, 3, 3, 4, 4, 0, 3, 4, 0, 4, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								2, 3, 4, 0, 3, 2, 4, 0, 3, 4, 4, 0, 4, 3, 4, 0,
								4, 0, 4, 0, 0, 4 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 0, 2, 3, 0, 4, 3, 4, 0, 1, 3, 4, 1, 0, 3, 4,
								1, 2, 3, 4, 2, 1, 3, 4, 2, 3, 3, 4, 3, 2, 3, 4,
								3, 4, 3, 4, 4, 3, 3, 4, 4, 0, 3, 4, 0, 4, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								2, 3, 4, 0, 3, 2, 4, 0, 3, 4, 4, 0, 4, 3, 4, 0,
								4, 0, 4, 0, 0, 4 },
						{},
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								6, 0, 0, 1, 0, 6, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 6, 0, 1, 2, 0, 6, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								6, 0, 2, 3, 0, 6, 3, 4, 0, 1, 3, 4, 1, 0, 3, 4,
								1, 2, 3, 4, 2, 1, 3, 4, 6, 0, 3, 4, 0, 6, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								6, 0, 4, 0, 0, 6 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 0, 0, 1, 0, 3, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 1, 2, 1, 2, 2, 1, 1, 2,
								2, 3, 1, 2, 3, 2, 1, 2, 3, 0, 1, 2, 0, 3, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 0, 2, 3, 0, 3, 3, 4,
								0, 1, 3, 4, 1, 0, 3, 4, 1, 2, 3, 4, 2, 1, 3, 4,
								2, 3, 3, 4, 3, 2, 3, 4, 3, 0, 3, 4, 0, 3, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								2, 3, 4, 0, 3, 2, 4, 0, 3, 0, 4, 0, 0, 3 } },

				{
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 0, 2, 3, 0, 4, 3, 4, 0, 1, 3, 4, 1, 0, 3, 4,
								1, 2, 3, 4, 2, 1, 3, 4, 2, 3, 3, 4, 3, 2, 3, 4,
								3, 4, 3, 4, 4, 3, 3, 4, 4, 0, 3, 4, 0, 4, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								2, 3, 4, 0, 3, 2, 4, 0, 3, 4, 4, 0, 4, 3, 4, 0,
								4, 0, 4, 0, 0, 4 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 0, 2, 3, 0, 4, 3, 4, 0, 1, 3, 4, 1, 0, 3, 4,
								1, 2, 3, 4, 2, 1, 3, 4, 2, 3, 3, 4, 3, 2, 3, 4,
								3, 4, 3, 4, 4, 3, 3, 4, 4, 0, 3, 4, 0, 4, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								2, 3, 4, 0, 3, 2, 4, 0, 3, 4, 4, 0, 4, 3, 4, 0,
								4, 0, 4, 0, 0, 4 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 0, 2, 3, 0, 4, 3, 4, 0, 1, 3, 4, 1, 0, 3, 4,
								1, 2, 3, 4, 2, 1, 3, 4, 2, 3, 3, 4, 3, 2, 3, 4,
								3, 4, 3, 4, 4, 3, 3, 4, 4, 0, 3, 4, 0, 4, 4, 0,
								0, 1, 4, 0, 1, 0, 4, 0, 1, 2, 4, 0, 2, 1, 4, 0,
								2, 3, 4, 0, 3, 2, 4, 0, 3, 4, 4, 0, 4, 3, 4, 0,
								4, 0, 4, 0, 0, 4 }, {}, {}, {} },

				{
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 0, 2, 3, 0, 4, 3, 4, 0, 1, 3, 4, 1, 0, 3, 4,
								1, 2, 3, 4, 2, 1, 3, 4, 2, 3, 3, 4, 3, 2, 3, 4,
								3, 4, 3, 4, 4, 3, 3, 4, 4, 0, 3, 4, 0, 4, 4, 5,
								0, 1, 4, 5, 1, 0, 4, 5, 1, 2, 4, 5, 2, 1, 4, 5,
								2, 3, 4, 5, 3, 2, 4, 5, 3, 4, 4, 5, 4, 3, 4, 5,
								4, 0, 4, 5, 0, 4, 5, 6, 0, 1, 5, 6, 1, 0, 5, 6,
								1, 2, 5, 6, 2, 1, 5, 6, 2, 3, 5, 6, 3, 2, 5, 6,
								3, 4, 5, 6, 4, 3, 5, 6, 4, 0, 5, 6, 0, 4, 6, 7,
								0, 1, 6, 7, 1, 0, 6, 7, 1, 2, 6, 7, 2, 1, 6, 7,
								2, 3, 6, 7, 3, 2, 6, 7, 3, 4, 6, 7, 4, 3, 6, 7,
								4, 0, 6, 7, 0, 4, 7, 8, 0, 1, 7, 8, 1, 0, 7, 8,
								1, 2, 7, 8, 2, 1, 7, 8, 2, 3, 7, 8, 3, 2, 7, 8,
								3, 4, 7, 8, 4, 3, 7, 8, 4, 0, 7, 8, 0, 4, 8, 9,
								0, 1, 8, 9, 1, 0, 8, 9, 1, 2, 8, 9, 2, 1, 8, 9,
								2, 3, 8, 9, 3, 2, 8, 9, 3, 4, 8, 9, 4, 3, 8, 9,
								4, 0, 8, 9, 0, 4, 9, 0, 0, 1, 9, 0, 1, 0, 9, 0,
								1, 2, 9, 0, 2, 1, 9, 0, 2, 3, 9, 0, 3, 2, 9, 0,
								3, 4, 9, 0, 4, 3, 9, 0, 4, 0, 9, 0, 0, 4 }, {},
						{}, {}, {}, {} },

				{
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 0, 2, 3, 0, 4, 3, 4, 0, 1, 3, 4, 1, 0, 3, 4,
								1, 2, 3, 4, 2, 1, 3, 4, 2, 3, 3, 4, 3, 2, 3, 4,
								3, 4, 3, 4, 4, 3, 3, 4, 4, 0, 3, 4, 0, 4, 4, 5,
								0, 1, 4, 5, 1, 0, 4, 5, 1, 2, 4, 5, 2, 1, 4, 5,
								2, 3, 4, 5, 3, 2, 4, 5, 3, 4, 4, 5, 4, 3, 4, 5,
								4, 0, 4, 5, 0, 4, 5, 6, 0, 1, 5, 6, 1, 0, 5, 6,
								1, 2, 5, 6, 2, 1, 5, 6, 2, 3, 5, 6, 3, 2, 5, 6,
								3, 4, 5, 6, 4, 3, 5, 6, 4, 0, 5, 6, 0, 4, 6, 0,
								0, 1, 6, 0, 1, 0, 6, 0, 1, 2, 6, 0, 2, 1, 6, 0,
								2, 3, 6, 0, 3, 2, 6, 0, 3, 4, 6, 0, 4, 3, 6, 0,
								4, 0, 6, 0, 0, 4 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 6, 0,
								0, 1, 6, 0, 1, 0, 6, 0, 1, 2, 6, 0, 2, 1, 6, 0,
								2, 3, 6, 0, 3, 2, 6, 0, 3, 4, 6, 0, 4, 3, 6, 0,
								4, 0, 6, 0, 0, 4 }, {}, {}, {}, {} },

				{
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 0, 2, 3, 0, 4, 3, 0, 0, 1, 3, 0, 1, 0, 3, 0,
								1, 2, 3, 0, 2, 1, 3, 0, 2, 3, 3, 0, 3, 2, 3, 0,
								3, 4, 3, 0, 4, 3, 3, 0, 4, 0, 3, 0, 0, 4 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 4, 0, 1, 4, 3, 0, 1,
								4, 0, 0, 1, 0, 4, 1, 2, 0, 1, 1, 2, 1, 0, 1, 2,
								1, 2, 1, 2, 2, 1, 1, 2, 2, 3, 1, 2, 3, 2, 1, 2,
								3, 4, 1, 2, 4, 3, 1, 2, 4, 0, 1, 2, 0, 4, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 4, 2, 3, 4, 3, 2, 3,
								4, 0, 2, 3, 0, 4, 3, 0, 0, 1, 3, 0, 1, 0, 3, 0,
								1, 2, 3, 0, 2, 1, 3, 0, 2, 3, 3, 0, 3, 2, 3, 0,
								3, 4, 3, 0, 4, 3, 3, 0, 4, 0, 3, 0, 0, 4 }, {},
						{}, {}, {} } };

		coords = new float[] { -X1 / 2, (-Y1 - Y2) / 2, X1 / 2, (-Y1 - Y2) / 2,
				X1 / 2 + X2, (Y1 - Y2) / 2, -X1 / 2 + X3, (Y1 + Y2) / 2,
				-X1 / 2 - X2, (Y1 - Y2) / 2 };
		triangles = new int[] { 0, 1, 2, 2, 3, 4, 0, 4, 2 };
		color1 = Color.RED;
		puzzlePieces.add(new SavePuzzlePiece(0.25f, 0.8f, coords, triangles,
				color1, 0));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.25f, 0.8f, coords, triangles,
				color1, 0, 2));

		color2 = Color.BLUE;
		puzzlePieces.add(new SavePuzzlePiece(0.25f, 0.5f, coords, triangles,
				color2, 1));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.25f, 0.5f, coords, triangles,
				color2, 1, 2));

		Color color3 = Color.GREEN;
		puzzlePieces.add(new SavePuzzlePiece(0.25f, 0.2f, coords, triangles,
				color3, 2));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.25f, 0.2f, coords, triangles,
				color3, 2, 2));

		coords = new float[] { -OX, -OY, -OX + SX1, -OY + SY3, -OX + 2 * SX1,
				-OY, -OX + 2 * SX1 - SX2, -OY + SY2, -OX + 2 * SX1 + SX3,
				-OY + SY1 - SY2, -OX + SX1 + SX2, -OY + SY1 - SY2, -OX + SX1,
				-OY + SY1, -OX + SX1 - SX2, -OY + SY1 - SY2, -OX - SX3,
				-OY + SY1 - SY2, -OX + SX2, -OY + SY2 };
		triangles = new int[] { 0, 6, 3, 8, 4, 1, 2, 3, 1, 5, 7, 9 };
		Color color4 = Color.YELLOW;
		puzzlePieces.add(new SavePuzzlePiece(0.7f, 0.65f, coords, triangles,
				color4, 3));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.7f, 0.65f, coords, triangles,
				color4, 3, 3));

		coords = new float[] { -BOX, -BOY, -BOX + 2 * BX1, -BOY,
				-BOX + 2 * BX1 + BX2, -BOY + BY2, -BOX + 2 * BX1 - BX3,
				-BOY + BY2, -BOX + BX1, -BOY + BY1, -BOX + BX3, -BOY + BY2,
				-BOX - BX2, -BOY + BY2 };
		triangles = new int[] { 0, 4, 1, 0, 6, 5, 1, 2, 3 };
		Color color5 = Color.MAGENTA;
		puzzlePieces.add(new SavePuzzlePiece(0.7f, 0.3f, coords, triangles,
				color5, 4));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.7f, 0.3f, coords, triangles,
				color5, 4, 2));

		coords = new float[] { 0, -DOY, DX, -DOY + DY, 0, -DOY + 2 * DY, -DX,
				-DOY + DY };
		triangles = new int[] { 0, 1, 3, 2, 1, 3 };
		Color color6 = Color.CYAN;
		puzzlePieces.add(new SavePuzzlePiece(0.45f, 0.55f, coords, triangles,
				color6, 5));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.45f, 0.55f, coords, triangles,
				color6, 5, 6));

		savePuzzle = new SavePuzzle(ORIGINAL, savePieces, puzzlePieces, matches, false);
		savePuzzles.add(savePuzzle);
		
		savePuzzle = new SavePuzzle(FLICK_ORIGINAL, 5, savePieces, flickPuzzlePieces, matches, false);
		savePuzzles.add(savePuzzle);	
		

		puzzlePieces = new Array<SavePuzzlePiece>(false, 10);
		flickPuzzlePieces = new Array<SavePuzzlePiece>(false, 10);

		int[][] triangleMatches = new int[][] {
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								1, 2, 1, 2, 2, 1, 2, 0, 0, 1, 2, 0, 1, 0, 2, 0,
								2, 0, 2, 0, 0, 2 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								1, 2, 1, 2, 2, 1, 2, 0, 0, 1, 2, 0, 1, 0, 2, 0,
								2, 0, 2, 0, 0, 2 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								1, 2, 1, 2, 2, 1, 2, 0, 0, 1, 2, 0, 1, 0, 2, 0,
								2, 0, 2, 0, 0, 2 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								1, 2, 1, 2, 2, 1, 2, 0, 0, 1, 2, 0, 1, 0, 2, 0,
								2, 0, 2, 0, 0, 2 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								1, 2, 1, 2, 2, 1, 2, 0, 0, 1, 2, 0, 1, 0, 2, 0,
								2, 0, 2, 0, 0, 2 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								1, 2, 1, 2, 2, 1, 2, 0, 0, 1, 2, 0, 1, 0, 2, 0,
								2, 0, 2, 0, 0, 2 },
								
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 0, 0, 1, 0, 3, 2, 0,
								0, 1, 2, 0, 1, 0, 2, 0, 1, 2, 2, 0, 2, 1, 2, 0,
								2, 3, 2, 0, 3, 2, 2, 0, 3, 0, 2, 0, 0, 3 }, 
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 0, 0, 1, 0, 3, 2, 0,
								0, 1, 2, 0, 1, 0, 2, 0, 1, 2, 2, 0, 2, 1, 2, 0,
								2, 3, 2, 0, 3, 2, 2, 0, 3, 0, 2, 0, 0, 3 }, 
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 0, 0, 1, 0, 3, 2, 0,
								0, 1, 2, 0, 1, 0, 2, 0, 1, 2, 2, 0, 2, 1, 2, 0,
								2, 3, 2, 0, 3, 2, 2, 0, 3, 0, 2, 0, 0, 3 }, 
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 0, 0, 1, 0, 3, 2, 0,
								0, 1, 2, 0, 1, 0, 2, 0, 1, 2, 2, 0, 2, 1, 2, 0,
								2, 3, 2, 0, 3, 2, 2, 0, 3, 0, 2, 0, 0, 3 } };
		
		int[][] rhombusMatches = new int[][] {
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 2, 0, 1, 2, 0, 2, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 2, 0, 2, 3, 0, 2, 3, 0,
								0, 1, 3, 0, 1, 0, 3, 0, 2, 0, 3, 0, 0, 2 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 2, 0, 1, 2, 0, 2, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 2, 0, 2, 3, 0, 2, 3, 0,
								0, 1, 3, 0, 1, 0, 3, 0, 2, 0, 3, 0, 0, 2 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 2, 0, 1, 2, 0, 2, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 2, 0, 2, 3, 0, 2, 3, 0,
								0, 1, 3, 0, 1, 0, 3, 0, 2, 0, 3, 0, 0, 2 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 2, 0, 1, 2, 0, 2, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 2, 0, 2, 3, 0, 2, 3, 0,
								0, 1, 3, 0, 1, 0, 3, 0, 2, 0, 3, 0, 0, 2 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 2, 0, 1, 2, 0, 2, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 2, 0, 2, 3, 0, 2, 3, 0,
								0, 1, 3, 0, 1, 0, 3, 0, 2, 0, 3, 0, 0, 2 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 2, 0, 1, 2, 0, 2, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 2, 0, 2, 3, 0, 2, 3, 0,
								0, 1, 3, 0, 1, 0, 3, 0, 2, 0, 3, 0, 0, 2 },
							
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 0, 0, 1, 0, 3, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 1, 2, 1, 2, 2, 1, 1, 2,
								2, 3, 1, 2, 3, 2, 1, 2, 3, 0, 1, 2, 0, 3, 3, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 0, 2, 3, 0, 3, 3, 0,
								0, 1, 3, 0, 1, 0, 3, 0, 1, 2, 3, 0, 2, 1, 3, 0,
								2, 3, 3, 0, 3, 2, 3, 0, 3, 0, 3, 0, 0, 3 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 0, 0, 1, 0, 3, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 1, 2, 1, 2, 2, 1, 1, 2,
								2, 3, 1, 2, 3, 2, 1, 2, 3, 0, 1, 2, 0, 3, 3, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 0, 2, 3, 0, 3, 3, 0,
								0, 1, 3, 0, 1, 0, 3, 0, 1, 2, 3, 0, 2, 1, 3, 0,
								2, 3, 3, 0, 3, 2, 3, 0, 3, 0, 3, 0, 0, 3 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 0, 0, 1, 0, 3, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 1, 2, 1, 2, 2, 1, 1, 2,
								2, 3, 1, 2, 3, 2, 1, 2, 3, 0, 1, 2, 0, 3, 3, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 0, 2, 3, 0, 3, 3, 0,
								0, 1, 3, 0, 1, 0, 3, 0, 1, 2, 3, 0, 2, 1, 3, 0,
								2, 3, 3, 0, 3, 2, 3, 0, 3, 0, 3, 0, 0, 3 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 0, 0, 1, 0, 3, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 1, 2, 1, 2, 2, 1, 1, 2,
								2, 3, 1, 2, 3, 2, 1, 2, 3, 0, 1, 2, 0, 3, 3, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 0, 2, 3, 0, 3, 3, 0,
								0, 1, 3, 0, 1, 0, 3, 0, 1, 2, 3, 0, 2, 1, 3, 0,
								2, 3, 3, 0, 3, 2, 3, 0, 3, 0, 3, 0, 0, 3 } };
		
		matches = new int[][][] {
				triangleMatches, triangleMatches, triangleMatches, triangleMatches, triangleMatches, triangleMatches,
				rhombusMatches, rhombusMatches, rhombusMatches, rhombusMatches };

		coords = new float[] { -SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3,
				-SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3 };
		triangles = new int[] { 0, 1, 2 };
		color1 = Color.BLUE;
		puzzlePieces.add(new SavePuzzlePiece(0.15f, 0.7f, coords, triangles,
				color1, 0));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.15f, 0.7f, coords, triangles,
				color1, 0, 1));

//		coords = new float[] { -SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3,
//				-SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3 };
//		triangles = new int[] { 0, 1, 2 };
		color2 = Color.GREEN;
		puzzlePieces.add(new SavePuzzlePiece(0.35f, 0.7f, coords, triangles,
				color2, 1));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.35f, 0.7f, coords, triangles,
				color2, 1, 1));

//		coords = new float[] { -SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3,
//				-SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3 };
//		triangles = new int[] { 0, 1, 2 };
		color3 = Color.YELLOW;
		puzzlePieces.add(new SavePuzzlePiece(0.15f, 0.45f, coords, triangles,
				color3, 2));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.15f, 0.45f, coords, triangles,
				color3, 2, 1));

//		coords = new float[] { -SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3,
//				-SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3 };
//		triangles = new int[] { 0, 1, 2 };
		color4 = Color.RED;
		puzzlePieces.add(new SavePuzzlePiece(0.35f, 0.45f, coords, triangles,
				color4, 3));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.35f, 0.45f, coords, triangles,
				color4, 3, 1));

//		coords = new float[] { -SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3,
//				-SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3 };
//		triangles = new int[] { 0, 1, 2 };
		color5 = Color.CYAN;
		puzzlePieces.add(new SavePuzzlePiece(0.15f, 0.2f, coords, triangles,
				color5, 4));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.15f, 0.2f, coords, triangles,
				color5, 4, 1));

//		coords = new float[] { -SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3,
//				-SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3 };
//		triangles = new int[] { 0, 1, 2 };
		color6 = Color.MAGENTA;
		puzzlePieces.add(new SavePuzzlePiece(0.35f, 0.2f, coords, triangles,
				color6, 5));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.35f, 0.2f, coords, triangles,
				color6, 5, 1));

		coords = new float[] { -(SQUARE + RY) / 2, -RY / 2, (SQUARE - RY) / 2,
				-RY / 2, (SQUARE + RY) / 2, RY / 2, (RY - SQUARE) / 2, RY / 2 };
		triangles = new int[] { 0, 1, 2, 0, 3, 2 };
		puzzlePieces.add(new SavePuzzlePiece(0.6f, 0.7f, coords, triangles,
				color1, 6));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.6f, 0.7f, coords, triangles,
				color1, 6, 1.5f));

//		coords = new float[] { -(SQUARE + RY) / 2, -RY / 2, (SQUARE - RY) / 2,
//				-RY / 2, (SQUARE + RY) / 2, RY / 2, (RY - SQUARE) / 2, RY / 2 };
//		triangles = new int[] { 0, 1, 2, 0, 3, 2 };
		puzzlePieces.add(new SavePuzzlePiece(0.8f, 0.7f, coords, triangles,
				color2, 7));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.8f, 0.7f, coords, triangles,
				color2, 7, 1.5f));

//		coords = new float[] { -(SQUARE + RY) / 2, -RY / 2, (SQUARE - RY) / 2,
//				-RY / 2, (SQUARE + RY) / 2, RY / 2, (RY - SQUARE) / 2, RY / 2 };
//		triangles = new int[] { 0, 1, 2, 0, 3, 2 };
		puzzlePieces.add(new SavePuzzlePiece(0.6f, 0.45f, coords, triangles,
				color3, 8));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.6f, 0.45f, coords, triangles,
				color3, 8, 1.5f));

//		coords = new float[] { -(SQUARE + RY) / 2, -RY / 2, (SQUARE - RY) / 2,
//				-RY / 2, (SQUARE + RY) / 2, RY / 2, (RY - SQUARE) / 2, RY / 2 };
//		triangles = new int[] { 0, 1, 2, 0, 3, 2 };
		puzzlePieces.add(new SavePuzzlePiece(0.8f, 0.45f, coords, triangles,
				color4, 9));
		flickPuzzlePieces.add(new SavePuzzlePiece(0.8f, 0.45f, coords, triangles,
				color4, 9, 1.5f));

		savePuzzle = new SavePuzzle(CLASSIC, 1, savePieces, puzzlePieces, matches, false);
		savePuzzles.add(savePuzzle);
		
		savePuzzle = new SavePuzzle(FLICK_CLASSIC, 5, savePieces, flickPuzzlePieces, matches, false);
		savePuzzles.add(savePuzzle);

		puzzlePieces = new Array<SavePuzzlePiece>(false, 10);

		matches = new int[][][] {
				{
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								1, 2, 1, 2, 2, 1, 2, 0, 0, 1, 2, 0, 1, 0, 2, 0,
								2, 0, 2, 0, 0, 2 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 0, 0, 1, 0, 3, 2, 0,
								0, 1, 2, 0, 1, 0, 2, 0, 1, 2, 2, 0, 2, 1, 2, 0,
								2, 3, 2, 0, 3, 2, 2, 0, 3, 0, 2, 0, 0, 3 } },

				{
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 0, 1, 0, 2, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 2, 0, 1, 2, 0, 2, 2, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 2, 0, 2, 3, 0, 2, 3, 0,
								0, 1, 3, 0, 1, 0, 3, 0, 2, 0, 3, 0, 0, 2 },
						{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 2, 0, 1, 2, 1, 0, 1,
								2, 3, 0, 1, 3, 2, 0, 1, 3, 0, 0, 1, 0, 3, 1, 2,
								0, 1, 1, 2, 1, 0, 1, 2, 1, 2, 1, 2, 2, 1, 1, 2,
								2, 3, 1, 2, 3, 2, 1, 2, 3, 0, 1, 2, 0, 3, 3, 3,
								0, 1, 2, 3, 1, 0, 2, 3, 1, 2, 2, 3, 2, 1, 2, 3,
								2, 3, 2, 3, 3, 2, 2, 3, 3, 0, 2, 3, 0, 3, 3, 0,
								0, 1, 3, 0, 1, 0, 3, 0, 1, 2, 3, 0, 2, 1, 3, 0,
								2, 3, 3, 0, 3, 2, 3, 0, 3, 0, 3, 0, 0, 3 } } };

		coords = new float[] { -SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3,
				-SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3 };
		triangles = new int[] { 0, 1, 2 };
		color1 = Color.BLUE;
		puzzlePieces.add(new SavePuzzlePiece(0.15f, 0.7f, coords, triangles,
				color1, 0));

//		coords = new float[] { -SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3,
//				-SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3 };
//		triangles = new int[] { 0, 1, 2 };
		color2 = Color.GREEN;
		puzzlePieces.add(new SavePuzzlePiece(0.35f, 0.7f, coords, triangles,
				color2, 1));

//		coords = new float[] { -SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3,
//				-SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3 };
//		triangles = new int[] { 0, 1, 2 };
		color3 = Color.YELLOW;
		puzzlePieces.add(new SavePuzzlePiece(0.15f, 0.45f, coords, triangles,
				color3, 2));

//		coords = new float[] { -SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3,
//				-SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3 };
//		triangles = new int[] { 0, 1, 2 };
		color4 = Color.RED;
		puzzlePieces.add(new SavePuzzlePiece(0.35f, 0.45f, coords, triangles,
				color4, 3));

//		coords = new float[] { -SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3,
//				-SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3 };
//		triangles = new int[] { 0, 1, 2 };
		color5 = Color.CYAN;
		puzzlePieces.add(new SavePuzzlePiece(0.15f, 0.2f, coords, triangles,
				color5, 4));

//		coords = new float[] { -SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3,
//				-SQUARE / 3, -SQUARE / 3, 2 * SQUARE / 3 };
//		triangles = new int[] { 0, 1, 2 };
		color6 = Color.MAGENTA;
		puzzlePieces.add(new SavePuzzlePiece(0.35f, 0.2f, coords, triangles,
				color6, 5));

		coords = new float[] { -(SQUARE + RY) / 2, -RY / 2, (SQUARE - RY) / 2,
				-RY / 2, (SQUARE + RY) / 2, RY / 2, (RY - SQUARE) / 2, RY / 2 };
		triangles = new int[] { 0, 1, 2, 0, 3, 2 };
		puzzlePieces.add(new SavePuzzlePiece(0.6f, 0.7f, coords, triangles,
				color1, 6));

//		coords = new float[] { -(SQUARE + RY) / 2, -RY / 2, (SQUARE - RY) / 2,
//				-RY / 2, (SQUARE + RY) / 2, RY / 2, (RY - SQUARE) / 2, RY / 2 };
//		triangles = new int[] { 0, 1, 2, 0, 3, 2 };
		puzzlePieces.add(new SavePuzzlePiece(0.8f, 0.7f, coords, triangles,
				color2, 7));

//		coords = new float[] { -(SQUARE + RY) / 2, -RY / 2, (SQUARE - RY) / 2,
//				-RY / 2, (SQUARE + RY) / 2, RY / 2, (RY - SQUARE) / 2, RY / 2 };
//		triangles = new int[] { 0, 1, 2, 0, 3, 2 };
		puzzlePieces.add(new SavePuzzlePiece(0.6f, 0.45f, coords, triangles,
				color3, 8));

//		coords = new float[] { -(SQUARE + RY) / 2, -RY / 2, (SQUARE - RY) / 2,
//				-RY / 2, (SQUARE + RY) / 2, RY / 2, (RY - SQUARE) / 2, RY / 2 };
//		triangles = new int[] { 0, 1, 2, 0, 3, 2 };
		puzzlePieces.add(new SavePuzzlePiece(0.8f, 0.45f, coords, triangles,
				color4, 9));

		savePuzzle = new SavePuzzle(REFLECT, 2, savePieces, puzzlePieces,
				matches, false);

		savePuzzles.add(savePuzzle);

		FileHandle saveFile = Gdx.files.local("data/save_file.txt");
		String saveString = json.toJson(save, Save.class);
		saveFile.writeString(saveString, false);
	}

}
