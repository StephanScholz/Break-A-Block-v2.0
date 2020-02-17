package com.sirkarpfen.storage;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureStorage {
	private HashMap<String,Texture> textureMap;
	/** 
	 * @param key to be used retrieval
	 * @return A Texture
	 */
	public Texture getTexture(String key) { return textureMap.get(key); }

	private static TextureStorage instance;
	public static TextureStorage getInstance() {
		if(instance == null) {
			instance = new TextureStorage();
		}
		return instance;
	}
	
	private TextureStorage() {
		textureMap = new HashMap<String,Texture>();
		createTextures();
	}

	/**
	 * Loads all needed textures for the game and stores them in a HashMap for further use.
	 */
	private void createTextures() {
		textureMap.put("background", new Texture(Gdx.files.internal("maps/breakout_background.png")));
		textureMap.put("demo_background", new Texture(Gdx.files.internal("maps/breakout_demo.png")));
		textureMap.put("toolbar", new Texture(Gdx.files.internal("maps/toolbar.png")));
		textureMap.put("game_over", new Texture(Gdx.files.internal("sprites/gameOverScreen/GameOverSheet.png")));
		textureMap.put("main_blue", new Texture(Gdx.files.internal("sprites/mainMenu/main_blue.png")));
		textureMap.put("main_green", new Texture(Gdx.files.internal("sprites/mainMenu/main_green.png")));
		textureMap.put("main_grey", new Texture(Gdx.files.internal("sprites/mainMenu/main_grey.png")));
		textureMap.put("main_purple", new Texture(Gdx.files.internal("sprites/mainMenu/main_purple.png")));
		textureMap.put("main_red", new Texture(Gdx.files.internal("sprites/mainMenu/main_red.png")));
		textureMap.put("main_yellow", new Texture(Gdx.files.internal("sprites/mainMenu/main_yellow.png")));
		textureMap.put("ballGrey", new Texture(Gdx.files.internal("sprites/ballGrey.png")));
		textureMap.put("brick_blue", new Texture(Gdx.files.internal("sprites/brick_blue.png")));
		textureMap.put("brick_grey", new Texture(Gdx.files.internal("sprites/brick_grey.png")));
		textureMap.put("brick_green", new Texture(Gdx.files.internal("sprites/brick_green.png")));
		textureMap.put("brick_purple", new Texture(Gdx.files.internal("sprites/brick_purple.png")));
		textureMap.put("brick_ball", new Texture(Gdx.files.internal("sprites/brick_ball.png")));
		textureMap.put("brick_score", new Texture(Gdx.files.internal("sprites/brick_score.png")));
		textureMap.put("paddleBlu", new Texture(Gdx.files.internal("sprites/paddleBlu.png")));
		textureMap.put("paddleRed", new Texture(Gdx.files.internal("sprites/paddleRed.png")));
		textureMap.put("youwon", new Texture(Gdx.files.internal("sprites/youwon.gif")));
		textureMap.put("item_score", new Texture(Gdx.files.internal("sprites/items/item_score.png")));
		textureMap.put("item_destruction", new Texture(Gdx.files.internal("sprites/items/item_destruction.png")));
		textureMap.put("score_100", new Texture(Gdx.files.internal("sprites/score/score_100.png")));
		textureMap.put("score_200", new Texture(Gdx.files.internal("sprites/score/score_200.png")));
		textureMap.put("score_300", new Texture(Gdx.files.internal("sprites/score/score_300.png")));
		textureMap.put("score_400", new Texture(Gdx.files.internal("sprites/score/score_400.png")));
		textureMap.put("score_500", new Texture(Gdx.files.internal("sprites/score/score_500.png")));
		textureMap.put("item_health", new Texture(Gdx.files.internal("sprites/items/item_health.png")));
		textureMap.put("score_health", new Texture(Gdx.files.internal("sprites/score/score_health.png")));
		textureMap.put("fog", new Texture(Gdx.files.internal("tilesets/fog.png")));
	}
}