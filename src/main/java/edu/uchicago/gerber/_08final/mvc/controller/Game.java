package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.model.*;
import edu.uchicago.gerber._08final.mvc.model.Character;
import edu.uchicago.gerber._08final.mvc.model.Zero;
import edu.uchicago.gerber._08final.mvc.view.GameFrame;
import edu.uchicago.gerber._08final.mvc.view.GamePanel;
import edu.uchicago.gerber._08final.mvc.view.StartMenuPanel;


import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


// ===============================================
// == This Game class is the CONTROLLER
// ===============================================

public class Game implements Runnable {

    // ===============================================
    // FIELDS
    // ===============================================

    // Each block is 72 * 72, so 1080 is 15 blocks, 684 = 9.5 blocks as the bar count 36 height.
    // This way the canvas is 15 * 9 blocks.
    public static int dimensionWidth = 1080;
    public static int dimensionHeight = 684;
    public static final Dimension DIM = new Dimension(dimensionWidth, dimensionHeight); //the dimension of the game.
    public final GamePanel gamePanel;
    public final StartMenuPanel startMenuPanel;

    //this is used throughout many classes.
    public static final Random R = new Random();
    public static int animationDelay = 30; // milliseconds between frames
    public final static int FRAMES_PER_SECOND = 1000 / animationDelay;

    private final Thread animationThread;

    public static enum GameState {
        GAME_PLAY,
        START_MENU,
        LEVEL_SWITCH,
        GAME_OVER,
        LEVEL_CLEAR,
        GAME_WIN
    }

    public static GameState gameState = GameState.START_MENU;
    public static GameState preGameState = GameState.START_MENU;
    // for possible future use
    // HYPER = 68, 					// D key
    //ALIEN = 65;                // A key
    // SPECIAL = 70; 					// fire special weapon;  F key

//    private final Clip soundThrust;
    public GameFrame gameFrame;
    private final Clip soundBackground;



    // ===============================================
    // ==CONSTRUCTOR
    // ===============================================

    public Game() {

        gamePanel = new GamePanel();
        gamePanel.addKeyListener(GamePanelListener.getInstance()); //Game object implements KeyListener
        gamePanel.addMouseListener(GamePanelListener.getInstance());

        startMenuPanel = new StartMenuPanel();
        startMenuPanel.addKeyListener(StartMenuPanelListener.getInstance());

        gameFrame = new GameFrame();

        gameFrame.getContentPane().add(startMenuPanel);
        startMenuPanel.setFocusable(true);

        gameFrame.pack();
//        initFontInfo();
        gameFrame.setSize(DIM);
        //change the name of the game-frame to your game name
        gameFrame.setTitle("Game Base");
        gameFrame.setResizable(false);
        gameFrame.setVisible(true);
//        gameFram
//        setFocusable(true);

        Cursor customCursor = null;
        BufferedImage cursorImage = Utils.loadGraphic("/imgs/Cursor/0.png");
        if (cursorImage != null) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Point hotSpot = new Point(0, 0);
            customCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "Custom Cursor");
        }
        gameFrame.setCursor(customCursor);

        try {

            InputStream is = getClass().getResourceAsStream("/fonts/visitor1.ttf");
            Font visitorFont = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(visitorFont);

        } catch (Exception e) {
            e.printStackTrace();
        }




//        soundThrust = Sound.clipForLoopFactory("whitenoise.wav");
//        soundBackground = Sound.clipForLoopFactory("music-background.wav");
//        soundBackground = Sound.clipForLoopFactory("Song/song_sneaky_driver.wav");
        soundBackground = Sound.clipForLoopFactory("Song/song_rainonbrick.wav");
        soundBackground.loop(5);
        //fire up the animation thread
        animationThread = new Thread(this); // pass the animation thread a runnable object, the Game object
        animationThread.start();


    }

    // ===============================================
    // ==METHODS
    // ===============================================

    public static void main(String[] args) {
        //typical Swing application start; we pass EventQueue a Runnable object.
        EventQueue.invokeLater(Game::new);
    }

    // Game implements runnable, and must have run method
    @Override
    public void run() {

        // lower animation thread's priority, thereby yielding to the "main" aka 'Event Dispatch'
        // thread which listens to keystrokes
        animationThread.setPriority(Thread.MIN_PRIORITY);

        // and get the current time
        long startTime = System.currentTimeMillis();

        // this thread animates the scene
        while (Thread.currentThread() == animationThread) {


            switch (gameState) {
                case START_MENU:
//                    System.out.println("Current state: " + gameState + " Pre state: " + preGameState);
                    if (gameState != preGameState) {

                        startMenuPanel.repaint();
                        startMenuPanel.setFocusable(true);
                    }
                    preGameState = GameState.START_MENU;

                    startMenuPanel.update(startMenuPanel.getGraphics());
                    CommandCenter.getInstance().incrementFrame();
                    try {
                        // The total amount of time is guaranteed to be at least ANIMATION_DELAY long.  If processing (update)
                        // between frames takes longer than ANIMATION_DELAY, then the difference between startTime -
                        // System.currentTimeMillis() will be negative, then zero will be the sleep time
                        startTime += animationDelay;

                        Thread.sleep(Math.max(0,
                                startTime - System.currentTimeMillis()));
                    } catch (InterruptedException e) {
                        // do nothing (bury the exception), and just continue, e.g. skip this frame -- no big deal
                    }
                    break;
                case GAME_PLAY:
//                    System.out.println("Current state: " + gameState + " Pre state: " + preGameState);

                    if (gameState != preGameState) {
                        gamePanel.repaint();
                        gamePanel.setFocusable(true);
                        gameFrame.getContentPane().removeAll();
                        gameFrame.getContentPane().add(gamePanel);
                        gamePanel.requestFocusInWindow();
                        gameFrame.revalidate();
                        gameFrame.repaint();
                        CommandCenter.getInstance().initGame();
                        CommandCenter.getInstance().setGameOver(false);
                    }
                    preGameState = gameState;
                    gamePanel.update(gamePanel.getGraphics());
                    if (!CommandCenter.getInstance().isPaused()) {
                        CollisionDetection.checkAllCollisions();
                        processGameOpsQueue();
                        checkNewLevel();
                        CommandCenter.getInstance().incrementFrame();
                    }
                    //keep track of the frame for development purposes

                    // surround the sleep() in a try/catch block
                    // this simply controls delay time between
                    // the frames of the animation
                    try {
                        // The total amount of time is guaranteed to be at least ANIMATION_DELAY long.  If processing (update)
                        // between frames takes longer than ANIMATION_DELAY, then the difference between startTime -
                        // System.currentTimeMillis() will be negative, then zero will be the sleep time
                        startTime += animationDelay;

                        Thread.sleep(Math.max(0,
                                startTime - System.currentTimeMillis()));
                    } catch (InterruptedException e) {
                        // do nothing (bury the exception), and just continue, e.g. skip this frame -- no big deal
                    }

                    break;
                case LEVEL_SWITCH:
                    break;
                case LEVEL_CLEAR:
                    break;
                case GAME_OVER:
                    break;
                case GAME_WIN:
                    break;

            }
            //this call will cause all movables to move() and draw() themselves every ~40ms
            // see GamePanel class for details
        } // end while
    } // end run

    private void checkFloaters() {
        spawnNewWallFloater();
        spawnShieldFloater();
        spawnNukeFloater();
    }

    //This method adds and removes movables to/from their respective linked-lists.
    //This method is being called by the animationThread. The entire method is locked on the intrinsic lock of this
    // Game object. The main (Swing) thread also has access to the GameOpsQueue via the
    // key event methods such as keyReleased. Therefore, to avoid mutating the GameOpsQueue while we are iterating
    // it, we also synchronize the critical sections of the keyReleased and keyPressed methods below on the same
    // intrinsic lock.
    private synchronized void processGameOpsQueue() {

        //deferred mutation: these operations are done AFTER we have completed our collision detection to avoid
        // mutating the movable linkedlists while iterating them above.
        while (!CommandCenter.getInstance().getOpsQueue().isEmpty()) {
            GameOp gameOp = CommandCenter.getInstance().getOpsQueue().dequeue();
            Movable mov = gameOp.getMovable();
            GameOp.Action action = gameOp.getAction();

            switch (mov.getTeam()) {
                case FOE:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovFoes().add(mov);
                    } else { //GameOp.Operation.REMOVE
                        CommandCenter.getInstance().getMovFoes().remove(mov);
                        if (mov instanceof Asteroid) spawnSmallerAsteroidsOrDebris((Asteroid) mov);
                    }

                    break;
                case FLOOR:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovFloors().add(mov);
                    } else {
                        CommandCenter.getInstance().getMovFloors().remove(mov);
                    }
                    break;
                case FRIEND:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovFriends().add(mov);
                    } else { //GameOp.Operation.REMOVE
                        if (mov instanceof Falcon) {
                            CommandCenter.getInstance().initFalconAndDecrementFalconNum();
                        } else {
                            CommandCenter.getInstance().getMovFriends().remove(mov);
                        }
                    }
                    break;
                case ENEMY:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovEnemies().add(mov);
                    } else {
                        CommandCenter.getInstance().getMovEnemies().remove(mov);
                    }
                    break;

                case FLOATER:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovFloaters().add(mov);
                    } else { //GameOp.Operation.REMOVE
                        CommandCenter.getInstance().getMovFloaters().remove(mov);
                    }
                    break;
                case BLOOD:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovBloods().add(mov);
                    } else { //GameOp.Operation.REMOVE
                        CommandCenter.getInstance().getMovBloods().remove(mov);
                    }
                    break;
                case KATANA:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovKatanas().add(mov);
                    } else { //GameOp.Operation.REMOVE
                        CommandCenter.getInstance().getMovKatanas().remove(mov);
                    }
                    break;
                case PUNCH:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovPunches().add(mov);
                    } else { //GameOp.Operation.REMOVE
                        CommandCenter.getInstance().getMovPunches().remove(mov);
                    }
                    break;
                case BULLET:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovBullets().add(mov);
                    } else { //GameOp.Operation.REMOVE
                        CommandCenter.getInstance().getMovBullets().remove(mov);
                    }
                    break;
                case DEBRIS:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovDebris().add(mov);
                    } else { //GameOp.Operation.REMOVE
                        CommandCenter.getInstance().getMovDebris().remove(mov);
                    }
                    break;


            }

        }
    }

    //shows how to add walls or rectangular elements one brick at a time
    private void buildWall() {
        final int BRICK_SIZE = Game.DIM.width / 30, ROWS = 2, COLS = 20, X_OFFSET = BRICK_SIZE * 5, Y_OFFSET = 50;

        for (int nCol = 0; nCol < COLS; nCol++) {
            for (int nRow = 0; nRow < ROWS; nRow++) {
                CommandCenter.getInstance().getOpsQueue().enqueue(
                        new Brick(
                                new Point(nCol * BRICK_SIZE + X_OFFSET, nRow * BRICK_SIZE + Y_OFFSET),
                                BRICK_SIZE),
                        GameOp.Action.ADD);

            }
        }
    }


    private void spawnNewWallFloater() {

        if (CommandCenter.getInstance().getFrame() % NewWallFloater.SPAWN_NEW_WALL_FLOATER == 0 && isBrickFree()) {
            CommandCenter.getInstance().getOpsQueue().enqueue(new NewWallFloater(), GameOp.Action.ADD);
        }
    }

    private void spawnShieldFloater() {

        if (CommandCenter.getInstance().getFrame() % ShieldFloater.SPAWN_SHIELD_FLOATER == 0) {
            CommandCenter.getInstance().getOpsQueue().enqueue(new ShieldFloater(), GameOp.Action.ADD);
        }
    }

    private void spawnNukeFloater() {

        if (CommandCenter.getInstance().getFrame() % NukeFloater.SPAWN_NUKE_FLOATER == 0) {
            CommandCenter.getInstance().getOpsQueue().enqueue(new NukeFloater(), GameOp.Action.ADD);
        }
    }


    //this method spawns new Large (0) Asteroids
    private void spawnBigAsteroids(int num) {
        while (num-- > 0) {
            //Asteroids with size of zero are big
            CommandCenter.getInstance().getOpsQueue().enqueue(new Asteroid(0), GameOp.Action.ADD);

        }
    }

    private void spawnSmallerAsteroidsOrDebris(Asteroid originalAsteroid) {

        int size = originalAsteroid.getSize();
        //small asteroids
        if (size > 1) {
            CommandCenter.getInstance().getOpsQueue().enqueue(new WhiteCloudDebris(originalAsteroid), GameOp.Action.ADD);
        }
        //med and large
        else {
            //for large (0) and medium (1) sized Asteroids only, spawn 2 or 3 smaller asteroids respectively
            //We can use the existing variable (size) to do this
            size += 2;
            while (size-- > 0) {
                CommandCenter.getInstance().getOpsQueue().enqueue(new Asteroid(originalAsteroid), GameOp.Action.ADD);
            }
        }

    }

    private boolean isBrickFree() {
        //if there are no more Bricks on the screen
        boolean brickFree = true;
        for (Movable movFoe : CommandCenter.getInstance().getMovFoes()) {
            if (movFoe instanceof Brick) {
                brickFree = false;
                break;
            }
        }
        return brickFree;
    }

    private boolean isLevelClear() {
        //if there are no more Asteroids on the screen
        boolean asteroidFree = true;
        for (Movable movFoe : CommandCenter.getInstance().getMovFoes()) {
            if (movFoe instanceof Asteroid) {
                asteroidFree = false;
                break;
            }
        }
        return asteroidFree;
    }

    private void checkNewLevel() {

        if (isLevelClear()) {
            //currentLevel will be zero at beginning of game
            int level = CommandCenter.getInstance().getLevel();
            //award some points for having cleared the previous level
            CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + (10_000L * level));
            //bump the level up
            level = level + 1;
            CommandCenter.getInstance().setLevel(level);
            //spawn some big new asteroids
//            spawnBigAsteroids(level);
            //make falcon invincible momentarily in case new asteroids spawn on top of him, and give player
            //time to adjust to new asteroids in game space.
            CommandCenter.getInstance().getFalcon().setShield(Falcon.INITIAL_SPAWN_TIME);
            //show "Level X" in middle of screen
            CommandCenter.getInstance().getFalcon().setShowLevel(Falcon.INITIAL_SPAWN_TIME);

        }
    }


    // Varargs for stopping looping-music-clips
    private static void stopLoopingSounds(Clip... clpClips) {
        Arrays.stream(clpClips).forEach(clip -> clip.stop());
    }

    // ===============================================
    // KEYLISTENER METHODS
    // ===============================================

}


