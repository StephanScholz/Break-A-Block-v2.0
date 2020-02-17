package com.sirkarpfen.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.sirkarpfen.entities.bodies.BodyFactory;
import com.sirkarpfen.main.BlockGame;
import com.sirkarpfen.main.Constants;
import com.sirkarpfen.storage.TextureStorage;

public class Ball extends Entity {
	
	private Sprite ballGrey;
	
	/** Saved for later uses as Upgrade. 
	 * Burning Ball might destroy multiple bricks and possibly be a little faster
	 */
	//private Sprite ballBurning;
	
	private float radius;
	public float getRadius() { return radius; }
	
	private boolean started = false;
	public void setStarted(boolean started) { this.started = started; }
	public boolean hasStarted() { return started; }
	
	/**
	 * Last touch position of ball and paddle
	 */
	private int touchX;
	public void setTouchX(int touchX) { this.touchX = touchX; }
	
	public Ball(float x, float y, float scale) {
		super(x, y);
		this.radius = TextureStorage.getInstance().getTexture("ballGrey").getWidth()/2/pixel_to_meter*scale;
		touchX = (int)(x*pixel_to_meter);
	}
	
	public Ball(float x, float y, float scale, float radius) {
		super(x, y);
		this.radius = radius*scale;
	}
	
	@Override
	public void create() {
		this.createBody();
	}
	
	@Override
	public void createBody() {
		ballGrey = new Sprite(TextureStorage.getInstance().getTexture("ballGrey"));
		body = BodyFactory.createBody(new Vector2(startX, startY), BodyType.DynamicBody);
		BodyFactory.createFixture(body, BodyFactory.createCircleShape(radius), new float[] {0f,0f,1f}, false, (short)1);
		body.setUserData(this);
	}
	
	@Override
	public void render(SpriteBatch spriteBatch, float delta) {
		
		ballGrey.setX((body.getPosition().x-radius)*pixel_to_meter);
		ballGrey.setY((body.getPosition().y-radius)*pixel_to_meter);
		ballGrey.draw(spriteBatch);
		
	}
	
	public void move(float delta) {
		
		if(!started && !BlockGame.getInstance().isDemoGame()) {
			if(touchX+52 >= Constants.MAP_WIDTH-Constants.MAP_EDGE) {
				body.setTransform((Constants.MAP_WIDTH-Constants.MAP_EDGE-53)/pixel_to_meter, body.getPosition().y, 0);
				return;
			} else if(touchX-52 <= Constants.MAP_EDGE){
				body.setTransform((Constants.MAP_EDGE+53)/pixel_to_meter, body.getPosition().y, 0);
				return;
			} else {
				body.setTransform(touchX/pixel_to_meter, body.getPosition().y, 0);
			}
			/*switch (direction) {
			
			case LEFT:
				if((body.getPosition().x-paddle.getWidth()/2)*pixel_to_meter <= Constants.MAP_EDGE) {
					body.setLinearVelocity(0,0);
				} else {
					body.setLinearVelocity(-Constants.PADDLE_VELOCITY,0);
				}
				break;
			case RIGHT:
				if((body.getPosition().x+paddle.getWidth()/2)*pixel_to_meter  >= Gdx.graphics.getWidth()-Constants.MAP_EDGE) {
					body.setLinearVelocity(0,0);
				} else {
					body.setLinearVelocity(Constants.PADDLE_VELOCITY,0);
				}
				break;
			case STOP:
				body.setLinearVelocity(0,0);
				break;
			}*/
		}
	}
	
	@Override
	public void lockMovement(boolean lock) {
		super.lockMovement(lock);
		if(lock)
			body.setLinearVelocity(0,0);
	}
	
	public void start() {
		body.setLinearVelocity(0,0);
		Random generator = new Random();
		if(generator.nextBoolean()) {
			body.applyLinearImpulse(new Vector2(-10F,10F), body.getPosition(), true);
		} else {
			body.applyLinearImpulse(new Vector2(10f,10f), body.getPosition(), true);
		}
		this.setStarted(true);
	}
	
	public void resetToStartPosition() {
		body.setTransform(touchX, startY, 0);
		this.setStarted(false);
		this.lockMovement(false);
	}
	
	public void destroy() {
		World world = BlockGame.getWorld();
		world.destroyBody(body);
		this.setStarted(false);
	}
	
}
