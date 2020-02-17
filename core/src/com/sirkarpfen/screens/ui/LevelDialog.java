package com.sirkarpfen.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sirkarpfen.main.BlockGame;
import com.sirkarpfen.main.Constants;

public class LevelDialog {
	
	private Texture texture;
	private BitmapFont font;
	private String title = "LEVELS:";
	
	public LevelDialog() {
		texture = new Texture(Gdx.files.internal("ui/levelDialog.png"));
		font = new BitmapFont();
		font.getData().setScale(2F);
		font.setColor(Color.YELLOW);
	}
	
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.setProjectionMatrix(BlockGame.getInstance().getCamera().combined);
		System.out.println("width: " + (Constants.MAP_WIDTH-224) + ", height: " + 224);
		// Draw title
		font.draw(spriteBatch, title, 48, texture.getHeight()+64);
		spriteBatch.draw(texture, 32, 32, texture.getWidth(), texture.getHeight());
	}
	
}
