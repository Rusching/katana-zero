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
public class StartMenuPanelListener implements KeyListener {
    private int runSoundIdx = 1;

    private static StartMenuPanelListener instance = null;

    public StartMenuPanelListener() {

    }

    public static StartMenuPanelListener getInstance() {
        if (instance == null) {
            instance = new StartMenuPanelListener();
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
        START = 83; // s key



    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case ENTER:
                Game.gameState = Game.GameState.LEVEL_SWITCH;
                Sound.playSound("Menu/sound_transition_begin.wav");
                break;
            case QUIT:
                System.exit(0);
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


    @Override
    // does nothing, but we need it b/c of KeyListener contract
    public void keyTyped(KeyEvent e) {
    }
}
