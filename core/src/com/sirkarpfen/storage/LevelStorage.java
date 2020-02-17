package com.sirkarpfen.storage;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.sirkarpfen.levels.DemoLevel;
import com.sirkarpfen.levels.Level;

public class LevelStorage {

	private String standardPath = "levels/";
	
	private static LevelStorage instance = null;
	private List<Level> levelList;
	public void addLevel(Level level) {
		if (!levelList.contains(level)) {
			levelList.add(level);
		} else {
			Level l = levelList.get(levelList.indexOf(level));
			l.setName(level.getName());
			l.setBricks(level.getBricks());
		}
	}
	public void removeLevel(Level level) {
		levelList.remove(level);
	}
	public void removeLevel(int index) {
		levelList.remove(index);
	}
	public Level getLevel(int index) {
		return levelList.get(index);
	}
	public Level getLevel(String name) {
		Level level = null;
		for (Level l : levelList) {
			if (l.getName().equals(name)) {
				level = l;
				break;
			}
		}
		return level;
	}
	public List<Level> getLevelList() {
		return levelList;
	}
	public boolean containsLevel(Level level) {
		return levelList.contains(level);
	}
	
	private DemoLevel demoLevel;
	public DemoLevel getDemoLevel() {return demoLevel;}
	
	private Level currentLevel;
	public void setCurrentLevel(int index) { this.currentLevel = levelList.get(index); }
	public Level getCurrentLevel() { return currentLevel; }
	
	public Level debugLevel;

	private LevelStorage() {
		this.levelList = new ArrayList<Level>();
		this.demoLevel = new DemoLevel();
		this.loadLevelList();
	}
	
	public static LevelStorage getInstance() {
		if(instance == null) {
			instance = new LevelStorage();
		}
		return instance;
	}
	
	/*
	 * Load all existing levels from the file system, then populate the levelList with it
	 */
	private void loadLevelList() {
		// clear the levelList
		levelList.clear();
		Json json = new Json();
		// Reading the levels file
		FileHandle dirHandle = Gdx.files.internal(standardPath);
		for (FileHandle entry : dirHandle.list()) {
			Level level = json.fromJson(Level.class, entry.readString());
			levelList.add(level);
		}
		// Set the current level to the first one in the list
		currentLevel = levelList.get(0);
	}
	
	public void nextLevel() {
		int index = levelList.indexOf(currentLevel);
		if(index+1 > levelList.size()) {
			return;
		}
		currentLevel = this.getLevel(index+1);
	}
	
	public void saveLevelList() {
		Json json = new Json();
		for (Level level : levelList) {
			FileHandle fh = Gdx.files.local(standardPath + level.getName() + ".json");
			fh.writeString(json.prettyPrint(level), false);
		}
	}
}
