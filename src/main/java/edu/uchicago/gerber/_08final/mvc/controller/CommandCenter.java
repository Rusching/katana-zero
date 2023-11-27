package edu.uchicago.gerber._08final.mvc.controller;



import edu.uchicago.gerber._08final.mvc.model.*;
import edu.uchicago.gerber._08final.mvc.model.Character;
import edu.uchicago.gerber._08final.mvc.model.Zero;
import edu.uchicago.gerber._08final.mvc.view.GamePanel;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
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

	public static int currentScene = 0;
	public static int totalScene = 4;
	/*
	scene 0:
	 */

	public static int currentLevel;
	public static int totalLevels;

	private boolean paused;
	private boolean muted;
	private boolean isSlowMotion = false;

	public boolean isGameOver = true;

//	public boolean isPaused = false;

	//this value is used to count the number of frames (full animation cycles) in the game
	private long frame;

	// values used to scroll map
	public int viewX = 0;
	public int viewY = 0;

	// margins between four bounds
	public int verticalMargin = 150;
	public int leftMargin = 300;
	public int rightMargin = 300;

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
	private List<Movable> movEnemies = new LinkedList<>();
	private List<Movable> movKatanas = new LinkedList<>();
	private final List<Movable> movPunches = new LinkedList<>();
	private final List<Movable> movBullets = new LinkedList<>();
	private List<Character> movCharacters = new LinkedList<>();
	{
		movCharacters.add(zero);
	}
//	protected List<Block> movBlocks = new ArrayList<>();

	private final GameOpsQueue opsQueue = new GameOpsQueue();

	//for sound playing. Limit the number of threads to 5 at a time.
	private final ThreadPoolExecutor soundExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

	//singleton
	private static CommandCenter instance = null;

	// Constructor made private
	private CommandCenter() {
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
		setLevel(0);
		setScore(0);
		setPaused(false);
		//set to one greater than number of falcons lives in your game as initFalconAndDecrementNum() also decrements
		setNumFalcons(4);
		initFalconAndDecrementFalconNum();
		//add the falcon to the movFriends list
//		opsQueue.enqueue(falcon, GameOp.Action.ADD);
		Floor f = new Floor();
		f.loadLevelAndCreateFloors(1, 0);

		opsQueue.enqueue(zero, GameOp.Action.ADD);
//		opsQueue.enqueue(new ShieldCop(new Point(500, 240)), GameOp.Action.ADD);
		opsQueue.enqueue(new Grunt(new Point(600, 240)), GameOp.Action.ADD);
		opsQueue.enqueue(new Grunt(new Point(700, 240)), GameOp.Action.ADD);
//		opsQueue.enqueue(new Grunt(new Point(800, 240)), GameOp.Action.ADD);
		opsQueue.enqueue(new Grunt(new Point(900, 240)), GameOp.Action.ADD);


	}

	public void initFalconAndDecrementFalconNum(){
		numFalcons--;
		if (isGameOver()) return;
//		Sound.playSound("shipspawn.wav");
		falcon.setShield(Falcon.INITIAL_SPAWN_TIME);
		falcon.setInvisible(Falcon.INITIAL_SPAWN_TIME/4);
		//put falcon in the middle of the game-space
		falcon.setCenter(new Point(Game.DIM.width / 2, Game.DIM.height / 2));
		zero.setCenter(new Point(656, 390));
		//random number between 0-360 in steps of TURN_STEP
		falcon.setOrientation(Game.R.nextInt(360 / Falcon.TURN_STEP) * Falcon.TURN_STEP);
		falcon.setDeltaX(0);
		falcon.setDeltaY(0);
		falcon.setRadius(Falcon.MIN_RADIUS);
		falcon.setMaxSpeedAttained(false);
		falcon.setNukeMeter(0);
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
		movCharacters.clear();
		movBloods.clear();
		movEnemies.clear();
		movBullets.clear();
		movKatanas.clear();
		movPunches.clear();
	}







}
