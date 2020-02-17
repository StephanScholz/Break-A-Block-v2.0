package com.sirkarpfen.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sirkarpfen.main.BlockGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Break-A-Block";
		config.width = 832;
		config.height = 640;
		config.y = 0;
		// make the window static
		config.resizable = false;
		//Create Instance of the Game. Automatic call to BlockGame.create()
		new LwjglApplication(BlockGame.getInstance(), config);
	}
}
