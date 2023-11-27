package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.view.LevelSwitchPanel;
import lombok.Data;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@Data
public class LevelSwitchPanelListener implements KeyListener {
    private int runSoundIdx = 1;

    private static LevelSwitchPanelListener instance = null;

    public LevelSwitchPanelListener() {

    }

    public static LevelSwitchPanelListener getInstance() {
        if (instance == null) {
            instance = new LevelSwitchPanelListener();
        }
        return instance;
    }

    private static final int
            ENTER = 10,
            UP_ARROW = 38,
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
        int keyCode = e.getKeyCode();
        System.out.println(keyCode);
        switch (keyCode) {
            case LEFT: case LEFT_ARROW:
                LevelSwitchPanel.moveSelection(-1);
                break;
            case RIGHT: case RIGHT_ARROW:
                LevelSwitchPanel.moveSelection(1);
                break;
            case UP: case UP_ARROW:
                if (LevelSwitchPanel.currentSelection > 3 && LevelSwitchPanel.currentSelection < 8) {
                    LevelSwitchPanel.moveSelection(-4);
                } else if (LevelSwitchPanel.currentSelection >= 0 && LevelSwitchPanel.currentSelection < 4) {
                    LevelSwitchPanel.currentSelection = 8;
                    LevelSwitchPanel.updateButtonSelection();
                } else {
                    LevelSwitchPanel.currentSelection = 4;
                    LevelSwitchPanel.updateButtonSelection();
                }
                break;
            case DOWN: case DOWN_ARROW:
                if (LevelSwitchPanel.currentSelection >= 0 && LevelSwitchPanel.currentSelection < 5) {
                    LevelSwitchPanel.moveSelection(4);
                } else if (LevelSwitchPanel.currentSelection >= 5 && LevelSwitchPanel.currentSelection < 8) {
                    LevelSwitchPanel.currentSelection = 8;
                    LevelSwitchPanel.updateButtonSelection();
                } else {
                    LevelSwitchPanel.currentSelection = 0;
                    LevelSwitchPanel.updateButtonSelection();
                }
                break;
            case ENTER:
                LevelSwitchPanel.selectLevel();
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
