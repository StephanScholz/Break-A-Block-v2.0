package com.sirkarpfen.levels.levelEditor;

import com.badlogic.gdx.math.Vector2;

import com.sirkarpfen.entities.enums.ItemProperty;

public class BrickDummy {
	private Vector2 position;
	private ItemProperty itemProperty;

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public ItemProperty getItemProperty() {
		return itemProperty;
	}

	public void setItemProperty(ItemProperty itemProperty) {
		this.itemProperty = itemProperty;
	}
}
