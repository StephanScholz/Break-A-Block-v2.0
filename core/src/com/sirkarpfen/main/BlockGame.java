package com.sirkarpfen.main;

import java.awt.Dimension;
import java.util.ArrayList;

import com.sirkarpfen.entities.Ball;
import com.sirkarpfen.entities.Entity;
import com.sirkarpfen.entities.Paddle;
import com.sirkarpfen.entities.eventhandler.EntityContactEventHandler;
import com.sirkarpfen.entities.manager.EntityManager;
import com.sirkarpfen.screens.GameOverScreen;
import com.sirkarpfen.screens.GameScreen;
import com.sirkarpfen.screens.LevelEditorScreen;
import com.sirkarpfen.screens.MainMenuScreen;
import com.sirkarpfen.storage.LevelStorage;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class BlockGame extends Game {
	
	/**
	 * Debug mode. While true, game will start in debug mode. Only 1 level "DebugLevel" will be available.
	 * Future changes will probably include debug rendering and a more sophisticated TestMode.
	 */
	public static boolean debug = false;
	
	private static BlockGame instance;
	public static BlockGame getInstance() {
		if(instance == null) {
			instance = new BlockGame();
		}
		return instance;
	}
	
	private LevelEditorScreen levelEditorScreen;
	private GameScreen gameScreen;
	public GameScreen getGameScreen() { return gameScreen; }
	private GameOverScreen gameOverScreen;
	private MainMenuScreen menuScreen;
	public boolean isMenuScreen() {
		if(this.getScreen() instanceof MainMenuScreen) {
			return true;
		}
		return false;
	}
	
	private OrthographicCamera camera;
	public OrthographicCamera getCamera() {	return camera; }
	
	/**
	 * Checks whether the ball hit the ground edge, or not
	 */
	public boolean gameOver;
	
	/** Checks whether the demo-game (startmenu background) is running */
	private boolean isDemoGame = true;
	
	/** Is the Demo-Game on the menu-screen running? */
	public boolean isDemoGame() { return isDemoGame; }
	
	private int screenWidth;
	private int screenHeight;
	public Dimension getScreenSize() { return new Dimension(screenWidth, screenHeight); }
	
	private static int highscore;
	public static int getHighscore() { return highscore; }
	public static void setHighscore(int score) { highscore = score; }
	public static void addScore(int score) { highscore += score; }
	
	private int life = 3;
	public int getLife() { return life; }
	public void setLife(int life) { this.life = life; }
	public void addLife(int value) { this.life += value; }
	public void removeLife(int value) { this.life -= value; }
	
	// Stores LevelData according to Block-Size: 32x64
	private char[] demoCharArray;
	public char[] getDemoCharArray() { return demoCharArray; }
	
	//Data object holding all level Data
	private LevelStorage levelStorage;

	private EntityManager entityManager;
	
	private static World world;
	/**
	 * @return The world that contains all Box2D bodies.
	 */
	public static World getWorld() { return world; }
	
	private BlockGame() {}
	
	/**
	 * Sets up the Map/Player and Camera. Camera is always centered on the player,
	 * when a new game is started, or a savegame loaded.
	 */
	@Override
	public void create() {
		
		// Initialize variables here
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		levelStorage = LevelStorage.getInstance();
		demoCharArray = levelStorage.getDemoLevel().getLevelData().toCharArray();
		entityManager = EntityManager.getInstance();
		
		// use GL10 without "power of two" (e.g. 16 px or 32 px) enforcement.
		//Texture.setEnforcePotImages(false);
		
		Gdx.input.setInputProcessor(new KeyInputHandler());
		
		// prepares the OrthographicCamera, for projection.
		this.resetCamera();
		
		// Creates the world, for our Box2D Entities.
		this.createWorld();
		
		this.createScreens();
		
		this.createDemoGame();
		
	}
	
	/**
	 * Starts the menu and the demo game in the background.
	 */
	public void createDemoGame() {
		this.isDemoGame = true;
		menuScreen.resize(Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
		this.resetCamera();
		this.setScreen(menuScreen);
	}
	
	public void startDemoGame() {
		ArrayList<? extends Entity> balls = entityManager.getEntities(Ball.class);
		for(int i = 0; i < balls.size(); i++) {
			Ball ball = (Ball)balls.get(i);
			ball.setVisible(true);
			ball.start();
		}
	}
	
	public void endDemoGame() {
		this.isDemoGame = false;
		for(Entity e : entityManager.getEntities(Ball.class)) {
			e.lockMovement(true);
		}
	}
	
	public void startEditor() {
		this.isDemoGame = false;
		this.setScreen(levelEditorScreen);
	}
	
	public void endEditor() {
		this.isDemoGame = true;
		this.setScreen(menuScreen);
	}
	
	public void newGame() {
		this.setLife(3);
		BlockGame.setHighscore(0);
		
		//TODO: levelStorage.setCurrentLevel(1);
		demoCharArray = levelStorage.getDemoLevel().getLevelData().toCharArray();
		
		entityManager.createEntities();
		
		this.setScreen(getGameScreen());
	}
	
	public void nextLevel() {
		this.addLife(1);
		// TODO: levelStorage.nextLevel();
		demoCharArray = levelStorage.getDemoLevel().getLevelData().toCharArray();
	}
	public void gameOver() {
		this.removeLife(1);
		entityManager.resetPaddle();
		entityManager.resetBall();
		if(this.getLife() == 0) {
			for(Entity e : entityManager.getEntityList()) {
				e.lockMovement(true);
			}
			this.setScreen(gameOverScreen);
			BlockGame.setHighscore(0);
		}
	}
	
	public void backToMenu() {
		this.isDemoGame = true;
		// TODO: levelStorage.setCurrentLevel(0);
		demoCharArray = levelStorage.getDemoLevel().getLevelData().toCharArray();
		this.createDemoGame();
	}

	private void createWorld() {
		world = new World(new Vector2(0,0), true);
		world.setContactListener(new EntityContactEventHandler(this));
		World.setVelocityThreshold(1.0f);
	}
	
	private void createScreens() {
		gameScreen = new GameScreen(this);
		gameOverScreen = new GameOverScreen(this);
		menuScreen = new MainMenuScreen(this);
		levelEditorScreen = new LevelEditorScreen(this);
	}
	
	public void quitGame() {
		Gdx.app.exit();
	}

	/*
	 * Prepares the OrthographicCamera and sets it on the startPosition.
	 */
	public void resetCamera() {
		camera = new OrthographicCamera(screenWidth, screenHeight);
		camera.position.x = screenWidth/2;
		camera.position.y = screenWidth/2;
		camera.update();
	}
	
	/**
	 * Handles the key input from the player.
	 * 
	 * @author sirkarpfen
	 *
	 */
	private class KeyInputHandler extends InputAdapter {
		
		@Override
		public boolean keyDown(int keycode) {
			if(keycode == Input.Keys.SPACE) {
				Ball ball = (Ball)entityManager.getEntity(Ball.class);
				if(ball != null && !ball.hasStarted()) {
					ball.start();
				}
			} else if(keycode == Input.Keys.ESCAPE) {
				BlockGame.this.quitGame();
			}
			// TODO make paddle movable by key input
			return true;
			
		}
		
		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			if(BlockGame.this.getScreen().equals(gameScreen) && button == Buttons.LEFT) {
				for(Entity e : entityManager.getEntities(Paddle.class)) {
					Paddle p = (Paddle)e;
					if(p.checkTouchDown(screenX, screenHeight-screenY)) {
						p.setTouched(true);
					}
				}
			} else if(BlockGame.this.getScreen().equals(gameOverScreen) && button == Buttons.LEFT) {
				gameOverScreen.checkButtonPressed(screenX, screenHeight-screenY);
			} else if(BlockGame.this.getScreen().equals(menuScreen) && button == Buttons.LEFT) {
				menuScreen.checkButtonPressed(screenX, screenHeight-screenY);
			} else if(BlockGame.this.getScreen().equals(levelEditorScreen)) {
				levelEditorScreen.checkButtonPressed(screenX, screenHeight-screenY, button);
			}
			return true;
		}
		
		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			if(BlockGame.this.getScreen().equals(gameScreen) && button == Buttons.LEFT) {
				Paddle p = (Paddle)entityManager.getEntity(Paddle.class);
				if(p != null && p.isTouched()) {
					p.setTouched(false);
				}
			}
			return true;
		}
		
		@Override
		public boolean touchDragged (int screenX, int screenY, int pointer) {
			if(BlockGame.this.getScreen().equals(gameScreen)) {
				Paddle p = (Paddle)entityManager.getEntity(Paddle.class);
				if (p == null) return false;
				if(p.isTouched()) {
					p.setTouchX(screenX);
					Ball b = (Ball)entityManager.getEntity(Ball.class);
					if(b != null) {
						b.setTouchX(screenX);
					}
				}
			}
			return true;
		}
		
		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			if(BlockGame.this.getScreen().equals(gameOverScreen)) {
				gameOverScreen.checkMouseHover(screenX, screenHeight-screenY);
			} else if(BlockGame.this.getScreen().equals(menuScreen)) {
				menuScreen.checkMouseHover(screenX, screenHeight-screenY);
			} else if(BlockGame.this.getScreen().equals(levelEditorScreen)) {
				levelEditorScreen.checkMouseHover(screenX, screenHeight-screenY);
			}
			return true;
		}
	}
}
