package com.sirkarpfen.levels;

import com.sirkarpfen.levels.levelEditor.BrickDummy;

public class Level{

	// List of bricks in this level
	private BrickDummy[] bricks;
	/**
	 * @param bricks the bricks to set
	 */
	public void setBricks(BrickDummy[] bricks) {
		this.bricks = bricks;
	}

	// Name of this level
	private String name;
	
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the bricks
	 */
	public BrickDummy[] getBricks() {
		return bricks;
	}
	
}
