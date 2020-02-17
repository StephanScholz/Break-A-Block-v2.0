package com.sirkarpfen.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sirkarpfen.entities.Brick;
import com.sirkarpfen.entities.enums.ItemProperty;
import com.sirkarpfen.levels.Level;
import com.sirkarpfen.levels.levelEditor.BrickDummy;
import com.sirkarpfen.levels.levelEditor.LevelEditor;
import com.sirkarpfen.main.BlockGame;
import com.sirkarpfen.main.Constants;
import com.sirkarpfen.screens.ui.MenuButton;
import com.sirkarpfen.storage.LevelStorage;
import com.sirkarpfen.storage.TextureStorage;

/**
 * Still a work in progress. Work on the level editor will continue,
 * once all bugs are fixed in the main game.
 * @author sirka
 *
 */
public class LevelEditorScreen extends ScreenAdapter {

	private LevelEditor levelEditor;
	private Texture background;
	public void setBackground(Texture background) { this.background = background; }
	private Texture toolbar;
	private ShapeRenderer shapeRenderer;
	private Brick selectedBrick;
	// Rectangles for collision detection
	private Rectangle drawRect, toolbarSelectionRect, editableField, mouseRect;
	// Buttons
	private MenuButton saveButton, loadButton, backButton;
	/**
	 * List of Bricks that need to be drawn on the game field
	 */
	private List<Brick> brickList;
	/**
	 * List of Bricks in the toolbar, that can be chosen to color the game field
	 */
	private List<Brick> toolbarBrickList;
	private boolean levelSaved = false;
	private boolean levelLoaded = false;

	public LevelEditorScreen(BlockGame game) {
		super(game);
		// Instantiate everything needed
		// Background texture
		background = TextureStorage.getInstance().getTexture("background");
		// Toolbar texture
		toolbar = TextureStorage.getInstance().getTexture("toolbar");
		
		shapeRenderer = new ShapeRenderer();
		drawRect = new Rectangle(0,0,1,1);
		levelEditor = new LevelEditor(this);
		
		brickList = new ArrayList<Brick>();
		toolbarBrickList = new ArrayList<Brick>();
		this.createRects();
		this.createButtons();
		this.createToolbarBricks();
	}
	
	// Create the Rectangles for mouse collision detection
	private void createRects() {
		editableField = new Rectangle(32, 320, Constants.MAP_WIDTH-64, 256);
		mouseRect = new Rectangle(0,0,1,1);
	}
	
	private void createButtons() {
		saveButton = new MenuButton("SAVE");
		saveButton.setPosition(64, screenHeight/3);
		loadButton = new MenuButton("LOAD");
		loadButton.setPosition(saveButton.getPosition().x + saveButton.getWidth() + 64, screenHeight/3);
		backButton = new MenuButton("BACK");
		backButton.setPosition(loadButton.getPosition().x + loadButton.getWidth() + 64, screenHeight/3);
	}
	
	private void createToolbarBricks() {
		toolbarBrickList.add(new Brick(Constants.MAP_WIDTH+64, Constants.MAP_HEIGHT-64, ItemProperty.NORMAL, false));
		toolbarBrickList.add(new Brick(Constants.MAP_WIDTH+64, Constants.MAP_HEIGHT-128, ItemProperty.SCORE, false));
		toolbarBrickList.add(new Brick(Constants.MAP_WIDTH+64, Constants.MAP_HEIGHT-192, ItemProperty.HEALTH, false));
	}

	@Override
	public void show() {
		// Resize window
		Gdx.graphics.setWindowedMode(Constants.MAP_WIDTH+toolbar.getWidth(), Constants.MAP_HEIGHT);
		// update camera with new display dimensions
		game.getCamera().setToOrtho(false, Constants.MAP_WIDTH+toolbar.getWidth(), Constants.MAP_HEIGHT);
		// updating all projection matrices to the new camera viewport dimensions
		shapeRenderer.setProjectionMatrix(game.getCamera().combined);
		spriteBatch.setProjectionMatrix(game.getCamera().combined);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		spriteBatch.begin();
		// Draw Background
		spriteBatch.draw(background, 0, 0);
		// Draw Toolbar
		spriteBatch.draw(toolbar, background.getWidth(), 0, toolbar.getWidth(), toolbar.getHeight());
		
		spriteBatch.draw(TextureStorage.getInstance().getTexture("fog"), 
				32, 
				320,
				background.getWidth()-64,
				256);
		// Draw Bricks in Toolbar
		for(Brick b : toolbarBrickList) {
			spriteBatch.draw(
					b.getTextureRegion(), 
					b.getStartPosition().x, 
					b.getStartPosition().y,
					b.getWidth() * Constants.PIXEL_TO_METER,
					b.getHeight() * Constants.PIXEL_TO_METER);
			// Draw Toolbar overlay for selected bricks
			if (b.equals(selectedBrick)) {
				spriteBatch.end();
				shapeRenderer.begin(ShapeType.Line);
				shapeRenderer.box(
						toolbarSelectionRect.x,
						toolbarSelectionRect.y,
						0,
						toolbarSelectionRect.width,
						toolbarSelectionRect.height,
						0);
				shapeRenderer.end();
				spriteBatch.begin();
			}
			
		}
		// Draw all bricks that were already placed in the editable field
		if (!brickList.isEmpty()) {
			for (Brick b : brickList) {
				spriteBatch.draw(
						b.getTextureRegion(),
						b.getStartPosition().x,
						b.getStartPosition().y,
						b.getWidth() * Constants.PIXEL_TO_METER, 
						b.getHeight() * Constants.PIXEL_TO_METER);
			}
		}
		
		// Draw levelDialog window
		//levelDialog.render(spriteBatch);
		
		renderButtons();
		
		// TODO: Make this more beautiful. Maybe adding a component system in the ui-package
		if (levelSaved ) {
			BitmapFont font = new BitmapFont();
			font.setColor(Color.WHITE);
			font.getData().setScale(2f);
			font.draw(spriteBatch, "LEVEL SAVED!!", saveButton.getPosition().x, saveButton.getPosition().y - 64);
		}
		
		if (levelLoaded) {
			BitmapFont font = new BitmapFont();
			font.setColor(Color.WHITE);
			font.getData().setScale(2f);
			font.draw(spriteBatch, "LEVEL LOADED!!", saveButton.getPosition().x, saveButton.getPosition().y - 64);
		}
		spriteBatch.end();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.box(drawRect.x, drawRect.y, 0, drawRect.width, drawRect.height, 0);
		shapeRenderer.end();
	}
	
	private void renderButtons() {
		saveButton.render(spriteBatch);
		loadButton.render(spriteBatch);
		backButton.render(spriteBatch);
	}
	
	public void checkMouseHover(int screenX, int screenY) {
		mouseRect.x = screenX;
		mouseRect.y = screenY;
		
		checkButtonHover();
		
		// Overlay in the editable field
		for(int i = 0; i < levelEditor.getBrickArray().length; i++) {
			if(levelEditor.getBrickArray()[i].overlaps(mouseRect)) {
				drawRect = levelEditor.getBrickArray()[i];
			}
		}
	}
	
	private void checkButtonHover() {
		if(saveButton.collidesWith(mouseRect)) {
			saveButton.setFontColor(Color.WHITE);
		} else if(loadButton.collidesWith(mouseRect)) {
			loadButton.setFontColor(Color.WHITE);
		} else if(backButton.collidesWith(mouseRect)) {
			backButton.setFontColor(Color.WHITE);
		} else {
			saveButton.setFontColor(MenuButton.BASE_COLOR);
			loadButton.setFontColor(MenuButton.BASE_COLOR);
			backButton.setFontColor(MenuButton.BASE_COLOR);
		}
	}
	
	public void checkButtonPressed(int screenX, int screenY, int button) {
		mouseRect.x = screenX;
		mouseRect.y = screenY;
		if (saveButton.collidesWith(mouseRect)) { // Save-Button
			// TODO: Advance save function for multiple levels
			this.saveLevel();
		} else if (loadButton.collidesWith(mouseRect)) { // Load-Button
			// TODO: Advance load function for multiple levels
			this.loadLevel();
		} else if (backButton.collidesWith(mouseRect)) { // Back-Button
			this.backToMenuScreen();
		} else if (editableField.overlaps(mouseRect)) { // Is the mouse inside the editable field?
			this.editBrick(button);
		} else {
			this.toolbarSelect();
		}
	}
	
	private void saveLevel() {
		//TODO: needs more testing
		this.levelSaved = true;
		this.levelLoaded = false;
		BrickDummy[] dummies = new BrickDummy[brickList.size()];
		for (int i = 0; i < brickList.size(); i++) {
			Brick b = brickList.get(i);
			BrickDummy dummy = new BrickDummy();
			dummy.setPosition(new Vector2(b.getStartPosition().x, b.getStartPosition().y));
			dummy.setItemProperty(b.getItemProperty());
			dummies[i] = dummy;
		}
		// TODO: Edit this for multi-level support
		// Level level = new Level();
		Level level = LevelStorage.getInstance().getCurrentLevel();
		level.setName("Level1");
		level.setBricks(dummies);
		//LevelStorage.getInstance().addLevel(level);
		//LevelStorage.getInstance().setCurrentLevel(level);
	}
	
	private void loadLevel() {
		this.levelLoaded  = true;
		this.levelSaved = false;
		LevelStorage ls = LevelStorage.getInstance();
		// TODO: Implement multi-level support
		brickList.clear();
		for (BrickDummy bd : ls.getCurrentLevel().getBricks()) {
			brickList.add(new Brick(bd.getPosition().x, bd.getPosition().y, bd.getItemProperty(), false));
		}
	}
	
	private void backToMenuScreen() {
		brickList.clear();
		levelLoaded = false;
		levelSaved = false;
		selectedBrick = null;
		game.backToMenu();
	}
	
	private void editBrick(int button) {
		levelSaved = false;
		levelLoaded = false;
		
		Brick brick = null;
		// Iterate over the brick list, if the position is already taken.
		for (Brick b : brickList) {
			Vector2 pos = b.getStartPosition();
			if (pos.x == drawRect.x && pos.y == drawRect.y) {
				brick = b;
				break;
			}
		}
		// If the position is taken
		if (brick != null) {
			// If pressed button is the left mousebutton, do nothing
			if (button == Buttons.LEFT) {
			// If pressed button is the right mouse button, remove the brick form the brickList
			} else if (button == Buttons.RIGHT) {
				brickList.remove(brick);
			}
		} else { // Otherwise add the selected Brick to the editableBrickList, for drawing
			if (selectedBrick != null)
				brickList.add(new Brick(drawRect.x, drawRect.y, selectedBrick.getItemProperty(), false));
		}
	}
	
	private void toolbarSelect() {
		levelSaved = false;
		levelLoaded = false;
		// Toolbar selection process
		for(Brick b : toolbarBrickList) {
			Rectangle rect = new Rectangle(
					b.getStartPosition().x, 
					b.getStartPosition().y, 
					b.getWidth() * Constants.PIXEL_TO_METER, 
					b.getHeight() * Constants.PIXEL_TO_METER);
			if (rect.overlaps(mouseRect)) {
				selectedBrick = b;
				toolbarSelectionRect = rect;
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		//super.resize(width, height);
	}
	
	@Override
	public void hide() {
		// Save the newly created levels to the file system
		LevelStorage.getInstance().saveLevelList();
	}
	
}
