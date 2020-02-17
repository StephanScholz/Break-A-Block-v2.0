package com.sirkarpfen.entities.eventhandler;

import com.sirkarpfen.entities.Ball;
import com.sirkarpfen.entities.Brick;
import com.sirkarpfen.entities.Edge;
import com.sirkarpfen.entities.Entity;
import com.sirkarpfen.entities.Item;
import com.sirkarpfen.entities.Paddle;
import com.sirkarpfen.main.BlockGame;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/** Responsible for handling all contacts with all game-objects. */
public class EntityContactEventHandler implements ContactListener {
	
	private BlockGame game;

	public EntityContactEventHandler(BlockGame game) {
		this.game = game;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Object fixtureDataA = contact.getFixtureA().getBody().getUserData();
		Object fixtureDataB = contact.getFixtureB().getBody().getUserData();
		
		if(fixtureDataA instanceof Item) {
			if(fixtureDataB instanceof Paddle) {
				Item item = (Item)fixtureDataA;
				item.rewardBonus();
			}
		} else if(fixtureDataB instanceof Item) {
			if(fixtureDataA instanceof Paddle) {
				Item item = (Item)fixtureDataB;
				item.rewardBonus();
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		Object fixtureDataA = contact.getFixtureA().getBody().getUserData();
		Object fixtureDataB = contact.getFixtureB().getBody().getUserData();
		
		if(fixtureDataA instanceof Ball) {
			if(fixtureDataB instanceof Brick) {
				Entity brick = (Entity)fixtureDataB;
				brick.animateDestruction = true;
				brick.flagForDestruction();
			}
		} else if(fixtureDataB instanceof Ball) {
			if(fixtureDataA instanceof Brick) {
				Entity brick = (Entity)fixtureDataA;
				brick.animateDestruction = true;
				brick.flagForDestruction();
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Object fixtureDataA = contact.getFixtureA().getBody().getUserData();
		Object fixtureDataB = contact.getFixtureB().getBody().getUserData();
		
		if(fixtureDataA instanceof Paddle) {
			Paddle paddle = (Paddle)fixtureDataA;
			if(fixtureDataB instanceof Ball) {
				Ball ball = (Ball)fixtureDataB;
				if(ball.hasStarted()) {
					Body body = paddle.getBody();
					body.setLinearVelocity(0,0);
				}
			}
		} else if(fixtureDataB instanceof Paddle) {
			Paddle paddle = (Paddle)fixtureDataB;
			if (fixtureDataA instanceof Ball) {
				Ball ball = (Ball)fixtureDataA;
				if(ball.hasStarted()) {
					Body body = paddle.getBody();
					body.setLinearVelocity(0,0);
				}
			}
		}
		if(fixtureDataA instanceof Ball) {
			if(fixtureDataB instanceof Ball) {
				contact.setEnabled(false);
			}
		} else if(fixtureDataB instanceof Ball) {
			if(fixtureDataA instanceof Ball) {
				contact.setEnabled(false);
			}
		}
		if(!game.isMenuScreen()) {
			if(fixtureDataA instanceof Edge) {
				Edge edge = (Edge)fixtureDataA;
				if(edge.isGroundEdge()) {
					if(fixtureDataB instanceof Ball) {
						Ball ball = (Ball) fixtureDataB;
						if(!game.isDemoGame()) {
							if(!game.gameOver) {
								game.gameOver = true;
								ball.lockMovement(true);
							}
						}
					}
				}
				
				
			} else if(fixtureDataB instanceof Edge) {
				Edge edge = (Edge)fixtureDataB;
				if(edge.isGroundEdge()) {
					if(fixtureDataA instanceof Ball) {
						Ball ball = (Ball) fixtureDataA;
						if(!game.isDemoGame()) {
							if(!game.gameOver) {
								game.gameOver = true;
								ball.lockMovement(true);
							}
						}
						
					}
				}
			}
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
