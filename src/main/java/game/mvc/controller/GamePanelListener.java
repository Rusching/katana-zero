package game.mvc.controller;

import game.mvc.model.JumpDebris;
import game.mvc.model.Katana;
import game.mvc.model.NormalSlashDebris;
import game.mvc.model.Zero;
import lombok.Data;

import javax.sound.sampled.Clip;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TimerTask;

@Data
public class GamePanelListener implements KeyListener, MouseListener {

    // singleton
    private static GamePanelListener instance = null;

    // index to play run sound of the player
    private int runSoundIdx = 1;

    public GamePanelListener() {
    }

    public static GamePanelListener getInstance() {
        if (instance == null) {
            instance = new GamePanelListener();
        }
        return instance;
    }

    private static final int
            ENTER = 10,
            UP_ARROW = 30,
            DOWN_ARROW = 40,
            LEFT_ARROW = 37,
            RIGHT_ARROW = 39,
            PAUSE = 80, // p key
            QUIT = 81, // q key
            LEFT = 65, //  A
            RIGHT = 68, // D
            UP = 87, //  W
            DOWN = 83, //S
            LEVEL_SWITCH = 76, // L
            SHIFT = 16; // shift

    @Override
    public void keyPressed(KeyEvent e) {
        Zero zero = CommandCenter.getInstance().getZero();
        int keyCode = e.getKeyCode();
        switch (keyCode) {

            // set pause
            case PAUSE:
                CommandCenter.getInstance().setPaused(!CommandCenter.getInstance().isPaused());
                break;

            // exit immediately
            case QUIT:
                System.exit(0);
                break;

            // go to the level switch panel by "L" key
            //      IF player is dead, or
            //      IF current level is paused, or
            //      IF current level is cleared
            case LEVEL_SWITCH:
                if (CommandCenter.getInstance().getZero().isDeathSoundPlayed() || CommandCenter.getInstance().isPaused() || CommandCenter.getInstance().isLevelCleared()) {

                    // clear all flags relate to game playing
                    CommandCenter.getInstance().getZero().setProtected(false);
                    CommandCenter.getInstance().getZero().setHurtGround(false);
                    CommandCenter.getInstance().getZero().setCurrentHurtGroundIdx(0);
                    CommandCenter.getInstance().getZero().setDeathSoundPlayed(false);
                    CommandCenter.getInstance().setLevelInited(false);
                    CommandCenter.getInstance().setLevelCleared(false);
                    CommandCenter.getInstance().setGameOver(true);
                    Game.gameState = Game.GameState.LEVEL_SWITCH;
                }
                break;

            // enter one level
            case ENTER:
                if (CommandCenter.getInstance().isLevelCleared() && CommandCenter.getInstance().currentLevel < 8) {
                    CommandCenter.getInstance().currentLevel += 1;
                    CommandCenter.getInstance().getZero().setProtected(false);
                    CommandCenter.getInstance().getZero().setHurtGround(false);
                    CommandCenter.getInstance().getZero().setCurrentHurtGroundIdx(0);
                    CommandCenter.getInstance().getZero().setDeathSoundPlayed(false);
                    CommandCenter.getInstance().setLevelInited(false);
                    CommandCenter.getInstance().setLevelCleared(false);
                    CommandCenter.getInstance().initGame();
                    CommandCenter.getInstance().setGameOver(false);
                }
                break;

            // check for jumping and wall climbing
            case UP: case UP_ARROW:
                if (zero.isOnPlatform() && !zero.isRolling()) {
                    if (zero.isRunning() && (zero.isOnLeftWall() || zero.isOnRightWall())) {

                        // player is on a platform and against a wall, and add a small movement
                        Sound.playSound("Zero/player_jump.wav");
                        zero.setYVelocity(-zero.getMaxYVelocity() / 2);
                    } else {

                        // player is on a platform and away from walls so simply jumps
                        Sound.playSound("Zero/player_jump.wav");
                        CommandCenter.getInstance().getOpsQueue().enqueue(new JumpDebris(zero.getCenter()), GameOp.Action.ADD);
                        zero.setYVelocity(zero.getInitialYVelocity());
                        zero.setFalling(false);
                    }
                } else if (zero.isRunning()) {

                    // wall jumping to flip
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

            // restart a level by "S" key
            //      IF player is dead, or
            //      IF current level is paused, or
            //      IF current level is cleared
            //
            // otherwise, if player is on platform then perform rolling.
            // during rolling player is protected
            case DOWN: case DOWN_ARROW:
                if (CommandCenter.getInstance().getZero().isDeathSoundPlayed() || CommandCenter.getInstance().isPaused() || CommandCenter.getInstance().isLevelCleared()) {
                    CommandCenter.getInstance().getZero().setProtected(false);
                    CommandCenter.getInstance().getZero().setHurtGround(false);
                    CommandCenter.getInstance().getZero().setCurrentHurtGroundIdx(0);
                    CommandCenter.getInstance().getZero().setDeathSoundPlayed(false);
                    CommandCenter.getInstance().setLevelInited(false);
                    CommandCenter.getInstance().setLevelCleared(false);
                    CommandCenter.getInstance().initGame();
                    CommandCenter.getInstance().setGameOver(false);
                } else if (zero.isOnPlatform()) {
                    if (!zero.isRolling()) {

                        // player is ready to roll, set action and x-axis velocity
                        zero.setRolling(true);
                        Sound.playSound("Zero/player_roll.wav");
                        if (zero.isFacingLeft()) {
                            zero.setXVelocity(-zero.getMaxXVelocity() * 3);
                        } else {
                            zero.setXVelocity(zero.getMaxXVelocity() * 3);
                        }
                    }
                } else {

                    // player is in the air, then adds the speed to fall
                    zero.setYVelocity(zero.getYVelocity() + 10);
                    zero.setDeltaY(zero.getDeltaY() + 14);
                }
                break;

            // movement towards left or right
            case LEFT: case LEFT_ARROW:
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
            case RIGHT: case RIGHT_ARROW:
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

            // if in game playing without player dead, paused, level cleared
            // we can start the slow motion by "SHIFT" key
            case SHIFT:
                if (!(CommandCenter.getInstance().isPaused() || CommandCenter.getInstance().getZero().isDeathSoundPlayed() || CommandCenter.getInstance().isLevelCleared())) {
                    startSlowMotion();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        Zero zero = CommandCenter.getInstance().getZero();
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case LEFT:
            case RIGHT:
                zero.setRunning(false);
                zero.setRun2IdleFlag(4);
                break;
            case SHIFT:
                endSlowMotion();
                break;
            default:
                break;
        }

    }

    /**
     * this method starts the slow motion. Our slow motion is
     * implemented by modifying the animation delay to change
     * the frame rate to make everything looks like "slow"
     */
    public void startSlowMotion() {
        Zero zero = CommandCenter.getInstance().getZero();
        if (!CommandCenter.getInstance().isSlowMotion()) {
            Sound.playSlowMotion();
            Game.animationDelay = 150;
            CommandCenter.getInstance().setSlowMotion(true);
            zero.getSlowMotionTimer().schedule(new TimerTask() {
               @Override
               public void run() {
                   endSlowMotion();
               }
           }, zero.getMaxSlowMotionDuration());
        }
    }

    /**
     * end slow motion. this method is safe as it firstly
     * check if is in slow motion then stop it
     */
    public void endSlowMotion() {
        if (CommandCenter.getInstance().isSlowMotion()) {
            CommandCenter.getInstance().setSlowMotion(false);
            Clip slowMotionClip = Sound.slowMotionClip;
            if (slowMotionClip != null && slowMotionClip.isRunning()) {
                slowMotionClip.stop();
                slowMotionClip.close();
            }
            Sound.playSound("Zero/Slowmo_Exit.wav");
            Game.animationDelay = 30;
        }
    }

    @Override
    // does nothing, but we need it b/c of KeyListener contract
    public void keyTyped(KeyEvent e) {
    }

    /**
     * This method is used to perform attack of the player. If player is not
     * attacking, then it would enter attack action and create an invisible
     * katana object used to determine collision between enemies. After attack
     * this katana object is destroyed
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Zero zero = CommandCenter.getInstance().getZero();
        if (!zero.isAttack()) {
            Sound.playSound(String.format("Zero/slash_%d.wav", Game.R.nextInt(3) + 1));
            int attackX = e.getX();
            int attackY = e.getY();

            // add attack slash
            CommandCenter.getInstance().getOpsQueue().enqueue(new NormalSlashDebris(
                            attackX, attackY, zero.getCenter()),
                    GameOp.Action.ADD
            );

            // add katana
            Katana currentKatana = new Katana(attackX, attackY, zero.getCenter(), zero.getRadius());
            CommandCenter.getInstance().getOpsQueue().enqueue(currentKatana, GameOp.Action.ADD);
            zero.setKatana(currentKatana);
            zero.setAttack(true);

            // set attack impact
            if (attackY < zero.getCenter().y - CommandCenter.getInstance().getViewY()) {
                zero.setYVelocity(-zero.getMaxYVelocity() / 2);
                zero.setDeltaY(zero.getDeltaY() - 7);
            }
            if (attackX < zero.getCenter().x - CommandCenter.getInstance().getViewX()) {
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
