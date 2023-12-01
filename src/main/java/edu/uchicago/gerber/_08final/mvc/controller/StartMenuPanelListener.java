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

    // singleton
    private static StartMenuPanelListener instance = null;

    // used to play sound
    private int runSoundIdx = 1;

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
        QUIT = 81; // Q key

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {

            // enter the level switch panel
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
