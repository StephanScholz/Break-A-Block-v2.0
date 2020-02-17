package com.sirkarpfen.screens.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MenuButton {
	
	public static final Color BASE_COLOR = Color.YELLOW;
	/**
	 * Font to be used inside the Button
	 */
	private BitmapFont font;
	/**
	 * Collision rectangle
	 */
	private Rectangle collisionRect;
	/**
	 * Button Position Vector
	 */
	private Vector2 position;
	/**
	 * Width of the Button
	 */
	private float width;
	/**
	 * Height of the Button
	 */
	private float height;
	
	private Color fontColor;
	/**
	 * Text inside the Button
	 */
	private String text;

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public BitmapFont getFont() {
		return font;
	}

	public void setFont(BitmapFont font) {
		this.font = font;
	}
	
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
		this.createCollisionRect();
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public MenuButton(String text) {
		this.text = text;
		createFont(BASE_COLOR, 2);
		GlyphLayout gl = new GlyphLayout();
		gl.setText(font, text);
		this.width = gl.width;
		this.height = gl.height;
	}
	
	private void createFont(Color c, int scale) {
		setFontColor(c);
		font = new BitmapFont();
		font.setColor(fontColor);
		font.getData().setScale(scale);;
		position = new Vector2(10f, 10f);
		this.createCollisionRect();
	}

	private void createCollisionRect() {
		collisionRect = new Rectangle(
				position.x,
				position.y - height,
				width,
				height);
				
	}
	
	public void render(SpriteBatch batch) {
		// TODO Back button gone
		font.setColor(fontColor);
		font.draw(batch, text, position.x, position.y);
	}
	
	public boolean collidesWith(Rectangle rect) {
		return collisionRect.overlaps(rect);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
}
