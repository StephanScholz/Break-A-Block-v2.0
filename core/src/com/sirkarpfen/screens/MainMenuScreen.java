package com.sirkarpfen.screens;

import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import com.sirkarpfen.entities.Entity;
import com.sirkarpfen.main.BlockGame;
import com.sirkarpfen.main.Constants;
import com.sirkarpfen.screens.ui.MenuButton;
import com.sirkarpfen.storage.TextureStorage;
/**
 * Menu screen class.
 * @author sirkarpfen
 *
 */
public class MainMenuScreen extends ScreenAdapter {
	
	private Texture fog;

	private MenuButton startGameButton, levelEditorButton, quitGameButton;
	
	private Rectangle mouseRect;
	
	private boolean gameStarted = false;
	private boolean demoGameStarted = false;
	
	private Texture background;
	
	private float opacity;

	public MainMenuScreen(BlockGame game) {
		super(game);
		// loads the fog for gradual shading in the background of the menu
		fog = TextureStorage.getInstance().getTexture("fog");
		// loads the "demo" map for the background
		background = TextureStorage.getInstance().getTexture("demo_background");
		this.opacity = 0f;
		mouseRect = new Rectangle(0, 0, 1, 1);
		
		this.createMenuButtons();
		
	}
	
	// This is where the "show" starts. All variables are reset
	// and the menu creates all necessary entities for the background action.
	@Override
	public void show() {
		Gdx.graphics.setWindowedMode(Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
		game.getCamera().setToOrtho(false, Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
		spriteBatch.setProjectionMatrix(game.getCamera().combined);
		this.opacity = 0;
		this.gameStarted = false;
		this.demoGameStarted = false;
		entityManager.createEntities();
	}
	
	/**
	 * Creates all fonts used in the menu entries
	 */
	private void createMenuButtons() {
		
		startGameButton = new MenuButton("START GAME");
		startGameButton.setPosition(
				screenWidth/2 - startGameButton.getWidth()/2, 
				screenHeight/2 + startGameButton.getHeight()*3);
		levelEditorButton = new MenuButton("EDITOR");
		levelEditorButton.setPosition(
				screenWidth/2 - levelEditorButton.getWidth()/2, 
				startGameButton.getPosition().y - levelEditorButton.getHeight()*3);
		quitGameButton = new MenuButton("QUIT");
		quitGameButton.setPosition(
				screenWidth/2 - quitGameButton.getWidth()/2, 
				levelEditorButton.getPosition().y - quitGameButton.getHeight()*3);
	}
	
	@Override
	public void render(float delta) {
		//System.out.println(entityManager.getEntityList().size());
		super.render(delta);
		
		spriteBatch.begin();
		spriteBatch.draw(background, 0, 0);
		
		this.createWorldBodies();
		
		world.step(1f/60f, 10, 8);
		
		if(gameStarted) {
			game.endDemoGame();
			game.newGame();
			gameStarted = false;
		}
		
		this.renderWorldBodies(delta);
		
		Color c = spriteBatch.getColor();
		spriteBatch.setColor(c.r,c.g,c.b,opacity);
		spriteBatch.draw(
				fog, 
				0, 
				0);
		spriteBatch.setColor(c.r,c.g,c.b,1f);
		
		// Renders the menu animation or starts the demo game
		if(opacity <= 0.9f) {
			opacity += 0.01f;
		} else {
			if(!demoGameStarted) {
				game.startDemoGame();
				demoGameStarted = true;
			}
			this.drawMenu();
		}
		
		this.destroyWorldBodies();
		spriteBatch.end();
		
	}
	
	private void drawMenu() {
		startGameButton.render(spriteBatch);
		levelEditorButton.render(spriteBatch);
		quitGameButton.render(spriteBatch);
	}
	
	private void createWorldBodies() {
		CopyOnWriteArrayList<Entity> createList = entityManager.getCreateList();
		
		for(Entity e : createList) {
			e.create();
			createList.remove(e);
			entityManager.addEntity(e);
		}
	}
	
	private void renderWorldBodies(float delta) {
		CopyOnWriteArrayList<Entity> entityList = entityManager.getEntityList();
		
		for(Entity e : entityList) {
			if(!e.isMovementLocked() && e.isVisible()) {
    			e.move(delta);
    		}
			if(e.isVisible())
				e.render(spriteBatch, delta);
		}
	}
	
	private void destroyWorldBodies() {
		CopyOnWriteArrayList<Entity> destroyList = entityManager.getDestroyList();
		
		for(Entity e : destroyList) {
			e.destroy();
			destroyList.remove(e);
			entityManager.removeEntity(e);
		}
	}
	
	// Event handling the menu
	public void checkButtonPressed(int screenX, int screenY) {
		mouseRect.x = screenX;
		mouseRect.y = screenY;
		if(startGameButton.collidesWith(mouseRect)) {
			if(!gameStarted) {
				gameStarted  = true;
			}
		} else if(levelEditorButton.collidesWith(mouseRect)) {
			game.startEditor();
		} else if(quitGameButton.collidesWith(mouseRect)) {
			game.quitGame();
		}
	}
	
	/**
	 * Changes the color of the menu entry, when the mouse pointer hovers over it
	 * @param screenX pointers x-coordinate relative to screen dimensions
	 * @param screenY pointers y-coordinate relative to screen dimensions
	 */
	public void checkMouseHover(int screenX, int screenY) {
		mouseRect.x = screenX;
		mouseRect.y = screenY;
		if(startGameButton.collidesWith(mouseRect)) {
			startGameButton.setFontColor(Color.WHITE);
		} else if(levelEditorButton.collidesWith(mouseRect)) {
			levelEditorButton.setFontColor(Color.WHITE);
		} else if(quitGameButton.collidesWith(mouseRect)) {
			quitGameButton.setFontColor(Color.WHITE);
		} else {
			startGameButton.setFontColor(MenuButton.BASE_COLOR);
			levelEditorButton.setFontColor(MenuButton.BASE_COLOR);
			quitGameButton.setFontColor(MenuButton.BASE_COLOR);
		}
	}
	
	@Override
	public void resize(int width, int height) {
		
	}
	
	@Override
	public void hide() {
	}

}
