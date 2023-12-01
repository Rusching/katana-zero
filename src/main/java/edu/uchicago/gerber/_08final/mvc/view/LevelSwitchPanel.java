package edu.uchicago.gerber._08final.mvc.view;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.Utils;
import edu.uchicago.gerber._08final.mvc.model.Movable;
import edu.uchicago.gerber._08final.mvc.model.Sprite;
import lombok.Data;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Data
public class LevelSwitchPanel extends Panel {
    private final Font fontNormal = new Font("Visitor TT1 BRK", Font.BOLD, 25);
    private final Font fontBig = new Font("SansSerif", Font.BOLD + Font.ITALIC, 36);
    private FontMetrics fontMetrics;
    private int fontWidth;
    private int fontHeight;

    //used for double-buffering
    private Image imgOff;
    private Graphics grpOff;

    // cover images of levels
    BufferedImage levelIcon0 = Utils.loadGraphic("/imgs/Levels/covers/0.png");
    BufferedImage levelIcon1 = Utils.loadGraphic("/imgs/Levels/covers/1.png");
    BufferedImage levelIcon2 = Utils.loadGraphic("/imgs/Levels/covers/2.png");
    BufferedImage levelIcon3 = Utils.loadGraphic("/imgs/Levels/covers/3.png");
    BufferedImage levelIcon4 = Utils.loadGraphic("/imgs/Levels/covers/4.png");
    BufferedImage levelIcon5 = Utils.loadGraphic("/imgs/Levels/covers/5.png");
    BufferedImage levelIcon6 = Utils.loadGraphic("/imgs/Levels/covers/6.png");
    BufferedImage levelIcon7 = Utils.loadGraphic("/imgs/Levels/covers/7.png");
    BufferedImage levelIcon8 = Utils.loadGraphic("/imgs/Levels/covers/8.png");

    ArrayList<String> levelNames = new ArrayList<>();

    public static int currentSelection = 0;
    private static final List<JButton> levelButtons = new ArrayList<>();

    public LevelSwitchPanel() {
        setLayout(new GridLayout(3, 1)); // 3 rows
        setBackground(Color.BLACK);

        // update the level names
        levelNames.add("Factory 1");
        levelNames.add("Factory 2");
        levelNames.add("Studio 1");
        levelNames.add("Studio 2");
        levelNames.add("Chinatown 1");
        levelNames.add("Chinatown 2");
        levelNames.add("Motel 1");
        levelNames.add("Motel 2");
        levelNames.add("Infinite");

        // First two rows with 4 columns each
        for (int i = 0; i < 2; i++) {
            JPanel rowPanel = new JPanel(new GridLayout(1, 4));
            rowPanel.setBackground(Color.BLACK);
            for (int j = 0; j < 4; j++) {
                addButton(rowPanel, i * 4 + j);
            }
            add(rowPanel);
        }

        // Last row with 1 column
        JPanel lastRowPanel = new JPanel(new GridLayout(1, 1));
        lastRowPanel.setBackground(Color.BLACK);
        addButton(lastRowPanel, 8);
        add(lastRowPanel);

        updateButtonSelection();
    }

    private void addButton(JPanel panel, int level) {

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setPreferredSize(new Dimension(200, 150));
        buttonPanel.setBackground(Color.BLACK);

        JButton button = new JButton();
        button.setPreferredSize(new Dimension(200, 150));

        // add level icon
        ImageIcon icon = null;
        switch (level) {
            case 0:
                icon = new ImageIcon(levelIcon0);
                break;
            case 1:
                icon = new ImageIcon(levelIcon1);
                break;
            case 2:
                icon = new ImageIcon(levelIcon2);
                break;
            case 3:
                icon = new ImageIcon(levelIcon3);
                break;
            case 4:
                icon = new ImageIcon(levelIcon4);
                break;
            case 5:
                icon = new ImageIcon(levelIcon5);
                break;
            case 6:
                icon = new ImageIcon(levelIcon6);
                break;
            case 7:
                icon = new ImageIcon(levelIcon7);
                break;
            case 8:
                icon = new ImageIcon(levelIcon8);
                break;

        }
        button.setIcon(icon);

        // Create the label
        JLabel label = new JLabel(levelNames.get(level), SwingConstants.CENTER);
        label.setForeground(Color.WHITE); // Set label text color
        label.setFont(fontNormal); // Set label font
        label.setBounds(0, 0, 200, 150);  // Same bounds as button

        // set border and fill
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);

        // add button to panel
        buttonPanel.add(button);
        buttonPanel.add(label);
        panel.add(buttonPanel);
        levelButtons.add(button);
    }



    public static void moveSelection(int delta) {
        currentSelection = (currentSelection + delta + levelButtons.size()) % levelButtons.size();
        updateButtonSelection();
    }

    public static void updateButtonSelection() {
        for (int i = 0; i < levelButtons.size(); i++) {
            levelButtons.get(i).setBorder(i == currentSelection ?
                    BorderFactory.createLineBorder(Color.WHITE, 3) :
                    BorderFactory.createEmptyBorder());
        }
    }

    public static void selectLevel() {

        // logic to start the game at the selected level
        // change game state to GAME_PLAY and load the selected level
        System.out.println("Selected Level: " + (currentSelection + 1));
        Game.gameState = Game.GameState.GAME_PLAY;
        CommandCenter.getInstance().currentLevel = currentSelection;
    }


    public void update(Graphics g) {

        // The following "off" vars are used for the off-screen double-buffered image.
        imgOff = createImage(Game.DIM.width, Game.DIM.height);
        //get its graphics context
        grpOff = imgOff.getGraphics();

        //Fill the off-screen image background with black.
        grpOff.setColor(Color.BLACK);
        grpOff.fillRect(0, 0, Game.DIM.width, Game.DIM.height);
        if (fontMetrics == null) {
            initFontInfo();
            grpOff.setColor(Color.white);
            grpOff.setFont(fontNormal);
        }

        updateButtonSelection();
    }

    private void initFontInfo() {
        Graphics g = getGraphics();
        g.setFont(fontNormal);
        fontMetrics = g.getFontMetrics();
        fontWidth = fontMetrics.getMaxAdvance();
        fontHeight = fontMetrics.getHeight();
    }
}
