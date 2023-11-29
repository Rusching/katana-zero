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

	private int numFalcons;
	private int level;
	private long score;


	public static int currentLevel;
	public static int totalLevels = 9;

	public boolean levelInited = false;
	public int enemyNums;
	public boolean levelCleared = false;

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

	//the falcon is located in the movFriends list, but since we use this reference a lot, we keep track of it in a
	//separate reference. Use final to ensure that the falcon ref always points to the single falcon object on heap.
	//Lombok will not provide setter methods on final members
	private final Falcon falcon  = new Falcon();
	private final Zero zero = new Zero(new Point(300, 240));
	//lists containing our movables subdivided by team
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

    //this class maintains game state - make this a singleton.
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
	}
}
