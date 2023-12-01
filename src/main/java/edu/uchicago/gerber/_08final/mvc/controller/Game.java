package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.model.*;
import edu.uchicago.gerber._08final.mvc.model.Character;
import edu.uchicago.gerber._08final.mvc.model.Zero;
import edu.uchicago.gerber._08final.mvc.view.GameFrame;
import edu.uchicago.gerber._08final.mvc.view.GamePanel;
import edu.uchicago.gerber._08final.mvc.view.LevelSwitchPanel;
import edu.uchicago.gerber._08final.mvc.view.StartMenuPanel;


import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;

public class Game implements Runnable {


    // each block is 72 * 72, so 1080 is 15 blocks, 684 = 9.5 blocks as the bar count 36 height.
    // This way the canvas is 15 * 9 blocks.
    public static int dimensionWidth = 1080;
    public static int dimensionHeight = 684;
    public static final Dimension DIM = new Dimension(dimensionWidth, dimensionHeight); //the dimension of the game.

    // three display panels would be used
    private final GamePanel gamePanel;
    private final StartMenuPanel startMenuPanel;
    private final LevelSwitchPanel levelSwitchPanel;

    // a public random number generator
    public static final Random R = new Random();
    public static int animationDelay = 30; // milliseconds between frames

    // the animation thread used to display actions
    private final Thread animationThread;

    // finite game state
    public enum GameState {
        GAME_PLAY,
        START_MENU,
        LEVEL_SWITCH
    }

    // record previous game state to determine if there is a
    // panel switch to change the background music and refresh
    public static GameState gameState = GameState.START_MENU;
    public static GameState preGameState = GameState.START_MENU;

    // all panels are displayed on the single game frame
    private GameFrame gameFrame;

    // map the songs to be played in different panels and levels
    private HashMap<Integer, String> songMap;
    {
        songMap = new HashMap<>();
        songMap.put(0, "Song/song_sneaky_driver.wav");
        songMap.put(1, "Song/song_slaughterhouse.wav");
        songMap.put(2, "Song/song_chinatown.wav");
        songMap.put(3, "Song/song_thirddistrict.wav");
        songMap.put(4, "Song/song_dragon.wav");
        songMap.put(5, "Song/song_rainonbrick.wav");
    }
    private static Clip soundBackground = null;

    public Game() {

        // game play panel
        gamePanel = new GamePanel();
        gamePanel.addKeyListener(GamePanelListener.getInstance()); //Game object implements KeyListener
        gamePanel.addMouseListener(GamePanelListener.getInstance());

        // start menu panel
        startMenuPanel = new StartMenuPanel();
        startMenuPanel.addKeyListener(StartMenuPanelListener.getInstance());

        // level switch panel
        levelSwitchPanel = new LevelSwitchPanel();
        levelSwitchPanel.addKeyListener(LevelSwitchPanelListener.getInstance());

        // at the beginning we start with the start menu
        gameFrame = new GameFrame();
        gameFrame.getContentPane().add(startMenuPanel);
        startMenuPanel.setFocusable(true);

        gameFrame.pack();
        gameFrame.setSize(DIM);
        gameFrame.setTitle("Game Base");
        gameFrame.setResizable(false);
        gameFrame.setVisible(true);

        // set custom cursor
        Cursor customCursor = null;
        BufferedImage cursorImage = Utils.loadGraphic("/imgs/Cursor/0.png");
        if (cursorImage != null) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Point hotSpot = new Point(0, 0);
            customCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "Custom Cursor");
        }
        gameFrame.setCursor(customCursor);

        // load all static resources of sprites to save latter loading time
        loadAllResources();

        // load custom font so we can use it latter. The font name is "Visitor TT1 BRK"
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/visitor1.ttf");
            Font visitorFont = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(visitorFont);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // play background music
        soundBackground = Sound.clipForLoopFactory(songMap.get(5));
        soundBackground.loop(50);

        // start the animation thread
        animationThread = new Thread(this);
        animationThread.start();
    }

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

            // display different panels according to the game state
            switch (gameState) {
                case START_MENU:

                    // if current state is different to the previous state,
                    // we need to repaint and set focus
                    if (gameState != preGameState) {
                        startMenuPanel.repaint();
                        startMenuPanel.setFocusable(true);
                    }
                    preGameState = gameState;
                    startMenuPanel.update(startMenuPanel.getGraphics());
                    CommandCenter.getInstance().incrementFrame();
                    try {
                        startTime += animationDelay;
                        Thread.sleep(Math.max(0,
                                startTime - System.currentTimeMillis()));
                    } catch (InterruptedException e) {}
                    break;
                case GAME_PLAY:

                    // repaint and set focus
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

                        // change background music
                        soundBackground.stop();
                        soundBackground = Sound.clipForLoopFactory(songMap.get(CommandCenter.getInstance().currentLevel / 2));
                        soundBackground.loop(50);
                    }
                    preGameState = gameState;

                    // inside "update" we perform move and draw of each sprite
                    gamePanel.update(gamePanel.getGraphics());

                    // perform collision detection, process gameOpsQueue,
                    // check new levels and increment frames
                    if (!CommandCenter.getInstance().isPaused()) {
                        CollisionDetection.checkAllCollisions();
                        processGameOpsQueue();
                        checkNewLevel();
                        CommandCenter.getInstance().incrementFrame();
                    }
                    try {
                        startTime += animationDelay;
                        Thread.sleep(Math.max(0,
                                startTime - System.currentTimeMillis()));
                    } catch (InterruptedException e) {
                    }
                    break;
                case LEVEL_SWITCH:

                    // repaint and set focus
                    if (gameState != preGameState) {
                        System.out.println("Enter repaint");
                        levelSwitchPanel.repaint();
                        levelSwitchPanel.setFocusable(true);
                        gameFrame.getContentPane().removeAll();
                        gameFrame.getContentPane().add(levelSwitchPanel);
                        levelSwitchPanel.requestFocusInWindow();
                        gameFrame.revalidate();
                        gameFrame.repaint();

                        // change background music
                        if (preGameState != GameState.START_MENU) {
                            soundBackground.stop();
                            soundBackground = Sound.clipForLoopFactory(songMap.get(5));
                            soundBackground.loop(50);
                        }
                    }
                    preGameState = gameState;
                    levelSwitchPanel.update(levelSwitchPanel.getGraphics());
                    CommandCenter.getInstance().incrementFrame();
                    try {
                        startTime += animationDelay;
                        Thread.sleep(Math.max(0,
                                startTime - System.currentTimeMillis()));
                    } catch (InterruptedException e) {}
                    break;
            }
        }
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
                    } else {
                        CommandCenter.getInstance().getMovFriends().remove(mov);
                    }
                    break;
                case ENEMY:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovEnemies().add(mov);
                    } else {
                        CommandCenter.getInstance().getMovEnemies().remove(mov);
                    }
                    break;
                case BACKGROUND:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovBackground().add(mov);
                    } else {
                        CommandCenter.getInstance().getMovBackground().remove(mov);
                    }
                case BLOOD:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovBloods().add(mov);
                    } else {
                        CommandCenter.getInstance().getMovBloods().remove(mov);
                    }
                    break;
                case KATANA:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovKatanas().add(mov);
                    } else {
                        CommandCenter.getInstance().getMovKatanas().remove(mov);
                    }
                    break;
                case PUNCH:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovPunches().add(mov);
                    } else {
                        CommandCenter.getInstance().getMovPunches().remove(mov);
                    }
                    break;
                case BULLET:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovBullets().add(mov);
                    } else {
                        CommandCenter.getInstance().getMovBullets().remove(mov);
                    }
                    break;
                case DEBRIS:
                    if (action == GameOp.Action.ADD) {
                        CommandCenter.getInstance().getMovDebris().add(mov);
                    } else {
                        CommandCenter.getInstance().getMovDebris().remove(mov);
                    }
                    break;
            }
        }
    }

    /**
     * every 150 frames we perform a level clear check. If all enemies are finished
     * and current level is not infinite, then we set levelClear flag to true
     */
    private void checkNewLevel() {
        if (CommandCenter.getInstance().getFrame() % 150 == 0) {
            if (CommandCenter.getInstance().isLevelInited()
                    && CommandCenter.getInstance().getEnemyNums() == 0
                    && CommandCenter.getInstance().currentLevel != 8) {

                // level 8 is infinite, so we do not check if it is cleared
                System.out.println("Cleared");
                CommandCenter.getInstance().setLevelCleared(true);
            }
        }
    }

    /**
     * load all static raster images in each sprite to save loading time
     */
    private static void loadAllResources() {
        BloodDebris.loadResources();
        Block.loadResources();
        Bullet.loadResources();
        BulletReflectionDebris.loadResources();
        Door.loadResources();
        Ganster.loadResources();
        Grunt.loadResources();
        HitSlashDebris.loadResources();
        JumpDebris.loadResources();
        NormalSlashDebris.loadResources();
        Pomp.loadResources();
        ShieldCop.loadResources();
        Zero.loadResources();
    }
}


