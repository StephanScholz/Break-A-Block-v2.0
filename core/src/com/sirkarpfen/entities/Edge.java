package com.sirkarpfen.entities;

import com.sirkarpfen.entities.bodies.BodyFactory;
import com.sirkarpfen.main.BlockGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Edge extends Entity {
	
	private boolean isGroundEdge = false;
	public boolean isGroundEdge() { return isGroundEdge; }
	public void setGroundEdge(boolean isGroundEdge) { this.isGroundEdge = isGroundEdge; }

	public Edge(float x, float y, float width, float height) {
		super(x, y);
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void create() {
		this.createBody();
	}
	
	@Override
	public void render(SpriteBatch spriteBatch, float delta) {
		
	}

	@Override
	public void move(float delta) {
		
	}

	@Override
	protected void createBody() {
		body = BodyFactory.createBody(new Vector2(startX,startY), BodyType.StaticBody);
		BodyFactory.createFixture(
				body, BodyFactory.createBoxShape(width, height), new float[] {10f,0f,0f}, false, (short)1);
		body.setUserData(this);
	}

	@Override
	public void destroy() {
		BlockGame.getWorld().destroyBody(body);
	}

}
