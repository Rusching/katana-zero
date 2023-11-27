package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.model.JumpDebris;
import edu.uchicago.gerber._08final.mvc.model.Katana;
import edu.uchicago.gerber._08final.mvc.model.NormalSlashDebris;
import edu.uchicago.gerber._08final.mvc.model.Zero;
import lombok.Data;

import javax.sound.sampled.Clip;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TimerTask;

@Data
public class GamePanelListener implements KeyListener, MouseListener {
    private int runSoundIdx = 1;

    private static GamePanelListener instance = null;

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
            DOWN = 83, //s
            SHIFT = 16, // shift
            START = 83, // s key
            FIRE = 32, // space key
            MUTE = 77, // m-key mute

    NUKE = 78; // n-key mute


    @Override
    public void keyPressed(KeyEvent e) {
//        Falcon falcon = CommandCenter.getInstance().getFalcon();
        Zero zero = CommandCenter.getInstance().getZero();
        int keyCode = e.getKeyCode();

        if (keyCode == START && CommandCenter.getInstance().isGameOver()) {
            CommandCenter.getInstance().initGame();
            CommandCenter.getInstance().setGameOver(false);
            return;
        }


        switch (keyCode) {
            case PAUSE:
                CommandCenter.getInstance().setPaused(!CommandCenter.getInstance().isPaused());
//                if (CommandCenter.getInstance().isPaused()) stopLoopingSounds(soundBackground, soundThrust);
                break;
            case QUIT:
                System.exit(0);
                break;
            case UP: case UP_ARROW:
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
            case DOWN: case DOWN_ARROW:
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
            Game.animationDelay = 150;
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
            Game.animationDelay = 30;
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
            Sound.playSound(String.format("Zero/slash_%d.wav", Game.R.nextInt(3) + 1));
            int attackX = e.getX();
            int attackY = e.getY();

            // add attack slash
            CommandCenter.getInstance().getOpsQueue().enqueue(new NormalSlashDebris(
                            attackX, attackY, zero.getCenter(), zero.getBoundingBox()),
                    GameOp.Action.ADD
            );

            // add katana
            Katana currentKatana = new Katana(attackX, attackY, zero.getCenter(), zero.getRadius());
            CommandCenter.getInstance().getOpsQueue().enqueue(currentKatana, GameOp.Action.ADD);
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
