package com.sirkarpfen.entities;

import java.util.Random;

import com.sirkarpfen.entities.bodies.BodyFactory;
import com.sirkarpfen.entities.enums.ItemProperty;
import com.sirkarpfen.main.BlockGame;
import com.sirkarpfen.storage.TextureStorage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Item extends Entity{
	
	private ItemProperty property;
	public ItemProperty getProperty() { return property; }
	private Texture texture;
	private Texture scoreTexture;
	private Animation<TextureRegion> animation;
	private Body body;
	private float stateTime;
	private float width;
	private float height;
	private boolean animateScore = false;

	public Item(float x, float y, ItemProperty prop) {
		super(x, y);
		this.setProperties(prop);
		this.setVisible(false);
	}
	
	@Override
	public void create() {
		this.createBody();
	}
	
	public void setProperties(ItemProperty prop) { 
		this.property = prop; 
		TextureStorage ts = TextureStorage.getInstance();
		TextureRegion[][] temp = TextureRegion.split(ts.getTexture("item_destruction"),50,50);
		switch(property) {
		
		case NORMAL:
		
		case HEALTH:
			texture = ts.getTexture("item_health");
			break;
		case PADDLE_LENGTH:
			//texture = ts.getTexture("item_paddle");
		case SCORE:
			texture = null;
			temp = TextureRegion.split(ts.getTexture("item_score"), 24, 24);
			TextureRegion[] tr = new TextureRegion[16];
			for(int i = 0; i < tr.length; i++) {
				tr[i] = temp[0][i];
			}
			animation = new Animation<TextureRegion>(0.05F, tr);
			break;
		case BURNING_BALL:
			//texture = ts.getTexture("item_bball");
		}
		if(texture != null) {
			this.width = texture.getWidth()/pixel_to_meter;
			this.height = texture.getHeight()/pixel_to_meter;
		} else if(animation != null) {
			this.width = animation.getKeyFrame(stateTime).getRegionWidth()/pixel_to_meter;
			this.height = animation.getKeyFrame(stateTime).getRegionHeight()/pixel_to_meter;
		}
	}
	
	@Override
	protected void createBody() {
		body = BodyFactory.createBody(
				new Vector2(startX,startY), 
				BodyType.DynamicBody);
		BodyFactory.createFixture(
				body, 
				BodyFactory.createBoxShape(width/2, height/2), 
				new float[]{0,0,0}, 
				true, 
				(short)0);
		body.setUserData(this);
	}

	@Override
	public void render(SpriteBatch spriteBatch, float delta) {
		if(animateScore) {
			if(body.getPosition().y > 150/pixel_to_meter) {
				this.flagForDestruction();
			}
			body.setLinearVelocity(0,10f);
			spriteBatch.draw(scoreTexture, 
					(body.getPosition().x-width)*pixel_to_meter-delta, 
					(body.getPosition().y-height)*pixel_to_meter-delta);
		} else if(animation != null) {
			stateTime += delta;
			spriteBatch.draw(animation.getKeyFrame(stateTime, true), 
					(body.getPosition().x-width/2)*pixel_to_meter-delta,
					(body.getPosition().y-height/2)*pixel_to_meter-delta);
		} else {
			spriteBatch.draw(texture, body.getPosition().x*pixel_to_meter, body.getPosition().y*pixel_to_meter);
		}
	}
	
	public void rewardBonus() {
		if(animateScore) return;
		switch(property) {
		
		case NORMAL:
		
		case HEALTH:
			scoreTexture = TextureStorage.getInstance().getTexture("score_health");
			
			BlockGame.getInstance().addLife(1);
			break;
		case PADDLE_LENGTH:
			
		case SCORE:
			Random gen = new Random();
			int randNum = gen.nextInt(4);
			scoreTexture = TextureStorage.getInstance().getTexture("score_"+(randNum+1)*100);
			BlockGame.addScore(randNum*100);
			break;
		case BURNING_BALL:
			
		}
		this.animateScore = true;
	}
	
	@Override
	public void lockMovement(boolean lock) {
		super.lockMovement(lock);
		if(lock)
			body.setLinearVelocity(0,0);
	}

	@Override
	public void move(float delta) {
		if(!isMovementLocked()) {
			body.setLinearVelocity(0,-5f);
		} else {
			body.setLinearVelocity(0,0);
			return;
		}
	}
	
	@Override
	public void destroy() {
		if(!animateDestruction) {
			BlockGame.getWorld().destroyBody(body);
		}
	}

}
