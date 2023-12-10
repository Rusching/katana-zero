package game.mvc.controller;

import game.mvc.view.LevelSwitchPanel;
import lombok.Data;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@Data
public class LevelSwitchPanelListener implements KeyListener {

    // singleton
    private static LevelSwitchPanelListener instance = null;

    // used to play sound
    private int runSoundIdx = 1;


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
            QUIT = 81, // Q
            LEFT = 65, //  A
            RIGHT = 68, // D
            UP = 87, //  W
            DOWN = 83; //S


    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println(keyCode);
        switch (keyCode) {
            case LEFT: case LEFT_ARROW:
                LevelSwitchPanel.moveSelection(-1);
                Sound.playSound("Menu/pause.wav");
                break;
            case RIGHT: case RIGHT_ARROW:
                LevelSwitchPanel.moveSelection(1);
                Sound.playSound("Menu/pause.wav");
                break;

            // update the level selection due to our panel is not a regular shape,
            // so we need to perform extra calculations
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
                Sound.playSound("Menu/pause.wav");
                break;

            // update the level selection due to our panel is not a regular shape,
            // so we need to perform extra calculations
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
                Sound.playSound("Menu/pause.wav");
                break;

            // select current level and enter the game play panel
            case ENTER:
                LevelSwitchPanel.selectLevel();
                Sound.playSound("Menu/rewind.wav");
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
