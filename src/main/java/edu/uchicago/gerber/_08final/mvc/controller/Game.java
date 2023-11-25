package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.model.*;
import edu.uchicago.gerber._08final.mvc.model.Character;
import edu.uchicago.gerber._08final.mvc.model.Zero;
import edu.uchicago.gerber._08final.mvc.view.GamePanel;


import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


// ===============================================
// == This Game class is the CONTROLLER
// ===============================================

public class Game implements Runnable, KeyListener, MouseListener {

    // ===============================================
    // FIELDS
    // ===============================================

    // Each block is 72 * 72, so 1080 is 15 blocks, 684 = 9.5 blocks as the bar count 36 height.
    // This way the canvas is 15 * 9 blocks.
    public static int dimensionWidth = 1080;
    public static int dimensionHeight = 684;
    public static final Dimension DIM = new Dimension(dimensionWidth, dimensionHeight); //the dimension of the game.
    private final GamePanel gamePanel;

    //this is used throughout many classes.
    public static final Random R = new Random();

    private int runSoundIdx = 1;
    public static int animationDelay = 30; // milliseconds between frames

    public final static int FRAMES_PER_SECOND = 1000 / animationDelay;

    private final Thread animationThread;


    //key-codes
    private static final int
            PAUSE = 80, // p key
            QUIT = 81, // q key
            LEFT = 65, //  A
            RIGHT = 68, // D
            UP = 87, //  W
            DOWN = 83, //s
            SHIFT = 16, // shift
            START = 83, // s key
            FIRE = 32, // space key
            MUTE = 77, // m-key mute

            NUKE = 78; // n-key mute

    // for possible future use
    // HYPER = 68, 					// D key
    //ALIEN = 65;                // A key
    // SPECIAL = 70; 					// fire special weapon;  F key

//    private final Clip soundThrust;
//    private final Clip soundBackground;



    // ===============================================
    // ==CONSTRUCTOR
    // ===============================================

    public Game() {

        gamePanel = new GamePanel(DIM);
        gamePanel.addKeyListener(this); //Game object implements KeyListener
        gamePanel.addMouseListener(this);
//        soundThrust = Sound.clipForLoopFactory("whitenoise.wav");
//        soundBackground = Sound.clipForLoopFactory("music-background.wav");

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


            //this call will cause all movables to move() and draw() themselves every ~40ms
            // see GamePanel class for details
            gamePanel.update(gamePanel.getGraphics());

            checkCollision();
            checkNewLevel();
            //keep track of the frame for development purposes
            CommandCenter.getInstance().incrementFrame();

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
        } // end while
    } // end run

    private void checkFloaters() {
        spawnNewWallFloater();
        spawnShieldFloater();
        spawnNukeFloater();
    }

    private void checkCollision() {
        Zero zero = CommandCenter.getInstance().getZero();
        if (zero.isAttack()) {
            // only in attack action we check the collision between
            // katana and enemies
            Katana currentKatana = zero.getKatana();
            int katanaRadius = currentKatana.getRadius();
            int enemyRadius = Character.MIN_RADIUS;
            for (Movable enemy: CommandCenter.getInstance().getMovEnemies()) {
                if (currentKatana.getCenter().distance(enemy.getCenter()) < (katanaRadius + enemyRadius)) {
                    if (!enemy.isProtected()) {
                        if (enemy instanceof Grunt) {
                            Grunt gruntEnemy = (Grunt) enemy;
                            gruntEnemy.action = Grunt.gruntActions.HURT_GROUND;
                            gruntEnemy.setProtected(true);
                            gruntEnemy.setHurtGround(true);
                        }
                    }
                }
            }

        }
        processGameOpsQueue();
    }

    private void checkCollisions() {

        Point pntFriendCenter, pntFoeCenter;
        int radFriend, radFoe;

        //This has order-of-growth of O(n^2), there is no way around this.
        for (Movable movFriend : CommandCenter.getInstance().getMovFriends()) {
            for (Movable movFoe : CommandCenter.getInstance().getMovFoes()) {

                pntFriendCenter = movFriend.getCenter();
                pntFoeCenter = movFoe.getCenter();
                radFriend = movFriend.getRadius();
                radFoe = movFoe.getRadius();

                //detect collision
                if (pntFriendCenter.distance(pntFoeCenter) < (radFriend + radFoe)) {
                    //remove the friend (so long as he is not protected)
                    if (!movFriend.isProtected()) {
                        CommandCenter.getInstance().getOpsQueue().enqueue(movFriend, GameOp.Action.REMOVE);
                    }

                    //remove the foe
                    CommandCenter.getInstance().getOpsQueue().enqueue(movFoe, GameOp.Action.REMOVE);

                    if (movFoe instanceof Brick) {
                        CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + 1000);
                        Sound.playSound("rock.wav");
                    } else {
                        CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + 10);
                        Sound.playSound("kapow.wav");
                    }
                }

            }//end inner for
        }//end outer for

        //check for collisions between falcon and floaters. Order of growth of O(n) where n is number of floaters
        Point pntFalCenter = CommandCenter.getInstance().getFalcon().getCenter();
        int radFalcon = CommandCenter.getInstance().getFalcon().getRadius();

        Point pntFloaterCenter;
        int radFloater;
        for (Movable movFloater : CommandCenter.getInstance().getMovFloaters()) {
            pntFloaterCenter = movFloater.getCenter();
            radFloater = movFloater.getRadius();

            //detect collision
            if (pntFalCenter.distance(pntFloaterCenter) < (radFalcon + radFloater)) {

                Class<? extends Movable> clazz = movFloater.getClass();
                switch (clazz.getSimpleName()) {
                    case "ShieldFloater":
                        Sound.playSound("shieldup.wav");
                        CommandCenter.getInstance().getFalcon().setShield(Falcon.MAX_SHIELD);
                        break;
                    case "NewWallFloater":
                        Sound.playSound("insect.wav");
                        buildWall();
                        break;
                    case "NukeFloater":
                        Sound.playSound("nuke-up.wav");
                        CommandCenter.getInstance().getFalcon().setNukeMeter(Falcon.MAX_NUKE);
                        break;
                }
                CommandCenter.getInstance().getOpsQueue().enqueue(movFloater, GameOp.Action.REMOVE);


            }//end if
        }//end for

        processGameOpsQueue();

    }//end meth


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

    @Override
    public void keyPressed(KeyEvent e) {
//        Falcon falcon = CommandCenter.getInstance().getFalcon();
        Zero zero = CommandCenter.getInstance().getZero();
        int keyCode = e.getKeyCode();

        if (keyCode == START && CommandCenter.getInstance().isGameOver()) {
            CommandCenter.getInstance().initGame();
            Sound.playSound("Song/song_sneaky_driver.wav");
//            Sound.clipForLoopFactory("Song/song_sneaky_driver.wav");
            return;
        }


        switch (keyCode) {
            case PAUSE:
//                CommandCenter.getInstance().setPaused(!CommandCenter.getInstance().isPaused());
//                if (CommandCenter.getInstance().isPaused()) stopLoopingSounds(soundBackground, soundThrust);
                break;
            case QUIT:
                System.exit(0);
                break;
            case UP:
                if (zero.isOnPlatform() && !zero.isRolling()) {
                    if (zero.isRunning() && (zero.isOnLeftWall() || zero.isOnRightWall())) {
                        Sound.playSound("Zero/player_jump.wav");
                        zero.setYVelocity(-zero.getMaxYVelocity() * 3);
//                        zero.setDeltaY(zero.getDeltaY() + 60);
                    } else {
                        Sound.playSound("Zero/player_jump.wav");
                        CommandCenter.getInstance().getOpsQueue().enqueue(new JumpDebris(zero.getCenter()), GameOp.Action.ADD);
                        zero.setYVelocity(zero.getInitialYVelocity());
                        zero.setFalling(false);
                    }
                } else if (zero.isRunning()) {
                    if (zero.isOnLeftWall()) {
                        zero.setFlipping(true);
                        zero.setFacingLeft(false);
                        Sound.playSound("Zero/player_jump.wav");
                        zero.setXVelocity(zero.getMaxXVelocity());
                        zero.setYVelocity(-zero.getMaxYVelocity() * 2 / 3);
                    } else if (zero.isOnRightWall()) {
                        zero.setFlipping(true);
                        zero.setFacingLeft(true);
                        Sound.playSound("Zero/player_jump.wav");
                        zero.setXVelocity(-zero.getMaxXVelocity());
                        zero.setYVelocity(-zero.getMaxYVelocity() * 2 / 3);
                    }
                }
                break;
            case DOWN:
                if (zero.isOnPlatform()) {
                    if (!zero.isRolling()) {
                        zero.setRolling(true);
                        Sound.playSound("Zero/player_roll.wav");
                        if (zero.isFacingLeft()) {
                            zero.setXVelocity(-zero.getMaxXVelocity() * 3);
                        } else {
                            zero.setXVelocity(zero.getMaxXVelocity() * 3);
                        }
                    }
                } else {
                    zero.setYVelocity(zero.getYVelocity() + 10);
                    zero.setDeltaY(zero.getDeltaY() + 14);
                }

                break;
            case LEFT:
                if (!zero.isAttack() && !zero.isRolling()) {
                    zero.setFacingLeft(true);
                    zero.setRunning(true);
                    if (zero.isOnPlatform() && CommandCenter.getInstance().getFrame() % 6 == 0) {
                        Sound.playSound(String.format("Zero/player_running_%d.wav", runSoundIdx % 4 + 1));
                        runSoundIdx += 1;
                        if (runSoundIdx > 10000) {
                            runSoundIdx = 0;
                        }
                    }
                }
                break;
            case RIGHT:
                if (!zero.isAttack() && !zero.isRolling()) {
                    zero.setFacingLeft(false);
                    zero.setRunning(true);
                    if (CommandCenter.getInstance().getFrame() % 6 == 0) {
                        Sound.playSound(String.format("Zero/player_running_%d.wav", runSoundIdx % 4 + 1));
                        runSoundIdx += 1;
                        if (runSoundIdx > 10000) {
                            runSoundIdx = 0;
                        }
                    }
                }
                break;
            case SHIFT:
                startSlowMotion();
                break;
            // possible future use
            // case KILL:
            // case SHIELD:
            // case NUM_ENTER:

            default:
                break;
        }

    }

    //key events are triggered by the main (Swing) thread which is listening for keystrokes. Notice that some of the
    // cases below touch the GameOpsQueue such as fire bullet and nuke.
    //The animation-thread also has access to the GameOpsQueue via the processGameOpsQueue() method.
    // Therefore, to avoid mutating the GameOpsQueue on the main thread, while we are iterating it on the
    // animation-thread, we synchronize on the same intrinsic lock. processGameOpsQueue() is also synchronized.
    @Override
    public void keyReleased(KeyEvent e) {
//        Falcon falcon = CommandCenter.getInstance().getFalcon();
        Zero zero = CommandCenter.getInstance().getZero();
        int keyCode = e.getKeyCode();
        //show the key-code in the console
        System.out.println(keyCode);

        switch (keyCode) {
            case FIRE:
//                synchronized (this){
//                    CommandCenter.getInstance().getOpsQueue().enqueue(new Bullet(falcon), GameOp.Action.ADD);
//                }
//                Sound.playSound("thump.wav");
                break;
            case NUKE:
//                if (CommandCenter.getInstance().getFalcon().getNukeMeter() > 0){
//                    synchronized (this) {
//                        CommandCenter.getInstance().getOpsQueue().enqueue(new Nuke(falcon), GameOp.Action.ADD);
//                    }
//                    Sound.playSound("nuke.wav");
//                    CommandCenter.getInstance().getFalcon().setNukeMeter(0);
//                }
                break;
            //releasing either the LEFT or RIGHT arrow key will set the TurnState to IDLE
            case LEFT:
            case RIGHT:
//                falcon.setTurnState(Falcon.TurnState.IDLE);
                zero.setRunning(false);
                zero.setRun2IdleFlag(4);
                break;

            case UP:
                break;
            case SHIFT:

                endSlowMotion();
                break;

            case MUTE:
                CommandCenter.getInstance().setMuted(!CommandCenter.getInstance().isMuted());

//                if (!CommandCenter.getInstance().isMuted()) {
//                    stopLoopingSounds(soundBackground);
//                } else {
//                    soundBackground.loop(Clip.LOOP_CONTINUOUSLY);
//                }
                break;

            default:
                break;
        }

    }


    public void startSlowMotion() {
        Zero zero = CommandCenter.getInstance().getZero();
        if (!CommandCenter.getInstance().isSlowMotion()) {
            Sound.playSlowMotion();
            this.animationDelay = 150;
            CommandCenter.getInstance().setSlowMotion(true);
            zero.getSlowMotionTimer().schedule(new TimerTask() {
                   @Override
                   public void run() {
                        endSlowMotion();
                   }
               }, zero.getMaxSlowMotionDuration()
            );
        }
    }

    public void endSlowMotion() {
        if (CommandCenter.getInstance().isSlowMotion()) {
            CommandCenter.getInstance().setSlowMotion(false);
            Clip slowMotionClip = Sound.slowMotionClip;
            if (slowMotionClip != null && slowMotionClip.isRunning()) {
                slowMotionClip.stop();
                slowMotionClip.close();
            }
            Sound.playSound("Zero/Slowmo_Exit.wav");
            this.animationDelay = 30;
        }
    }

    @Override
    // does nothing, but we need it b/c of KeyListener contract
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Zero zero = CommandCenter.getInstance().getZero();
        if (!zero.isAttack()) {
            Sound.playSound(String.format("Zero/slash_%d.wav", R.nextInt(3) + 1));
            int attackX = e.getX();
            int attackY = e.getY();

            // add attack slash
            CommandCenter.getInstance().getOpsQueue().enqueue(new NormalSlashDebris(
                    attackX, attackY, zero.getCenter(), zero.getBoundingBox()),
                    GameOp.Action.ADD
            );

            // add katana
            Katana currentKatana = new Katana(attackX, attackY, zero.getCenter(), zero.getRadius());
            zero.katana = currentKatana;
            zero.setAttack(true);

            // set attack impact
            if (attackY < zero.getCenter().y - CommandCenter.getInstance().viewY) {
                zero.setYVelocity(-zero.getMaxYVelocity() / 2);
                zero.setDeltaY(zero.getDeltaY() - 7);
            }
            if (attackX < zero.getCenter().x - CommandCenter.getInstance().viewX) {
                zero.setFacingLeft(true);
                zero.setXVelocity(-zero.getMaxXVelocity() * 2);
            } else {
                zero.setFacingLeft(false);
                zero.setXVelocity(zero.getMaxXVelocity() * 2);
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}


