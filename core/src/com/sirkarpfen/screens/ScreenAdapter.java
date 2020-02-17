package com.sirkarpfen.screens;

import com.sirkarpfen.entities.manager.EntityManager;
import com.sirkarpfen.main.BlockGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

public class ScreenAdapter implements Screen {
	
	protected SpriteBatch spriteBatch;
	protected BlockGame game;
	private long lastRender;
	protected int screenWidth, screenHeight;
	protected EntityManager entityManager;
	protected World world;

	protected ScreenAdapter(BlockGame game) {
		this.game = game;
		lastRender = System.nanoTime();
		spriteBatch = new SpriteBatch();
		this.screenWidth = game.getScreenSize().width;
		this.screenHeight = game.getScreenSize().height;
		world = BlockGame.getWorld();
		entityManager = EntityManager.getInstance();
	}
	
	@Override
	public void dispose() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void render(float delta) {
		//System.out.println(world.getBodyCount());
		long now = System.nanoTime();
		Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT);
		
		// Limiting FPS. On test-machine made 50-55 FPS.
		now = System.nanoTime();
		if (now - lastRender < 30000000) { // 30 ms, ~33FPS
			try {
				Thread.sleep(30 - (now - lastRender) / 1000000);
			} catch (InterruptedException e) {
			}
		}
		
		lastRender = now;
	}

	@Override
	public void resize(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {

	}

}
