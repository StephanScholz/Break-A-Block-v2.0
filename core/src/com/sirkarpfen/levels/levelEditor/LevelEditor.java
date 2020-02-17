package com.sirkarpfen.levels.levelEditor;

import com.badlogic.gdx.math.Rectangle;

import com.sirkarpfen.main.Constants;
import com.sirkarpfen.screens.LevelEditorScreen;

public class LevelEditor {
	
	private Rectangle[] brickRectArray = new Rectangle[96];
	public Rectangle[] getBrickArray() { return brickRectArray; }

	public LevelEditor(LevelEditorScreen screen) {
		this.createBrickRectArray();
	}
	
	private void createBrickRectArray() {
		int x = 32;
		int y = 320;
		for(int i = 0; i < brickRectArray.length; i++) {
			if(x > Constants.MAP_WIDTH-64) {
				x = 32;
				y += 32;
			}
			brickRectArray[i] = new Rectangle(x,y,64,32);
			x += 64;
		}
	}
	
}
