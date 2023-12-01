package edu.uchicago.gerber._08final.mvc.controller;



import edu.uchicago.gerber._08final.mvc.model.*;
import edu.uchicago.gerber._08final.mvc.model.Zero;
import lombok.Data;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

//The CommandCenter is a singleton that manages the state of the game.
//the lombok @Data gives us automatic getters and setters on all members
@Data
public class CommandCenter {

	private long score;
	public static int currentLevel;

	// the enemy numbers in one level
	private int enemyNums;

	// flags indicating the level status
	private boolean levelInited = false;
	private boolean levelCleared = false;

	// flags indicating the game playing state
	private boolean paused;
	private boolean muted;
	private boolean isSlowMotion = false;
	private boolean isGameOver = true;

	//this value is used to count the number of frames (full animation cycles) in the game
	private long frame;

	// values used to scroll map
	private int viewX = 0;
	private int viewY = 0;

	// margins between four bounds
	private int verticalMargin = 150;
	private int leftMargin = 300;
	private int rightMargin = 300;

	// zero is our player.
	private final Zero zero = new Zero(new Point(300, 240));

	// lists containing our movables subdivided by team, used
	// to:
	// 		1. move and draw them by each queue
	//		2. detect collisions
	private final List<Movable> movDebris = new LinkedList<>();
	private final List<Movable> movBloods = new LinkedList<>();
	private final List<Movable> movFriends = new LinkedList<>();
	private final List<Movable> movFoes = new LinkedList<>();
	private final List<Movable> movFloaters = new LinkedList<>();
	private final List<Movable> movFloors = new LinkedList<>();
	private final List<Movable> movEnemies = new LinkedList<>();
	private final List<Movable> movKatanas = new LinkedList<>();
	private final List<Movable> movPunches = new LinkedList<>();
	private final List<Movable> movBullets = new LinkedList<>();
	private final List<Movable> movBackground = new LinkedList<>();

	// levelLoader is to load new level bricks and enemies
	private Level levelLoader = new Level();

	private final GameOpsQueue opsQueue = new GameOpsQueue();

	//for sound playing. Limit the number of threads to 5 at a time.
	private final ThreadPoolExecutor soundExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

	//singleton
	private static CommandCenter instance = null;

	// Constructor made private
	private CommandCenter() {
		movFriends.add(zero);
	}

	public static CommandCenter getInstance(){
		if (instance == null){
			instance = new CommandCenter();
		}
		return instance;
	}

	public void initGame(){
		clearAll();
		setScore(0);
		setPaused(false);
		levelLoader.loadLevelAndCreateFloors(currentLevel);
		movFriends.add(zero);
		levelInited = true;

	}

	public void incrementFrame(){
		//use of ternary expression to simplify the logic to one line
		frame = frame < Long.MAX_VALUE ? frame + 1 : 0;
	}

	/**
	 * clear all the movable lists at the beginning of each level
	 */
	private void clearAll(){
		movDebris.clear();
		movFriends.clear();
		movFoes.clear();
		movFloaters.clear();
		movFloors.clear();
		movBloods.clear();
		movEnemies.clear();
		movBullets.clear();
		movKatanas.clear();
		movPunches.clear();
		movBackground.clear();
	}
}
