package edu.uchicago.gerber._08final.mvc.view;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.model.Movable;
import edu.uchicago.gerber._08final.mvc.model.Sprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;


public class LevelSwitchPanel extends Panel {

    // ==============================================================
    // FIELDS
    // ==============================================================
    private final Font fontNormal = new Font("Visitor TT1 BRK", Font.BOLD, 25);
    private final Font fontBig = new Font("SansSerif", Font.BOLD + Font.ITALIC, 36);
    private FontMetrics fontMetrics;
    private int fontWidth;
    private int fontHeight;

    //used for double-buffering
    private Image imgOff;
    private Graphics grpOff;

    BufferedImage levelIcon0 = loadGraphic("/imgs/Levels/0.png");
    BufferedImage levelIcon1 = loadGraphic("/imgs/Levels/1.png");
    BufferedImage levelIcon2 = loadGraphic("/imgs/Levels/2.png");
    BufferedImage levelIcon3 = loadGraphic("/imgs/Levels/3.png");
    BufferedImage levelIcon4 = loadGraphic("/imgs/Levels/4.png");



    ArrayList<BufferedImage> plantPics = new ArrayList<>();
    // ==============================================================
    // CONSTRUCTOR
    // ==============================================================

    public static int currentSelection = 0;

    private static final List<JButton> levelButtons = new ArrayList<>();

    public LevelSwitchPanel() {
        setLayout(new GridLayout(3, 1)); // 3 rows
        setBackground(Color.BLACK);
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

        ImageIcon icon = null;
        switch (level / 2) {
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

        }
        button.setIcon(icon);

        // Create the label
        JLabel label = new JLabel("Level " + (level + 1), SwingConstants.CENTER);
        label.setForeground(Color.WHITE); // Set label text color
        label.setFont(fontNormal); // Set label font
        label.setBounds(0, 0, 200, 150);  // Same bounds as button

        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
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
        // Logic to start the game at the selected level
        System.out.println("Selected Level: " + (currentSelection + 1));
        Game.gameState = Game.GameState.GAME_PLAY;
        CommandCenter.getInstance().currentLevel = currentSelection;
        // Change game state to GAME_PLAY and load the selected level
    }


    public void update(Graphics g) {

        // The following "off" vars are used for the off-screen double-buffered image.
        imgOff = createImage(Game.DIM.width, Game.DIM.height);
        //get its graphics context
        grpOff = imgOff.getGraphics();

        //Fill the off-screen image background with black.
        grpOff.setColor(Color.BLACK);
        grpOff.fillRect(0, 0, Game.DIM.width, Game.DIM.height);


        //after drawing all the movables or text on the offscreen-image, copy it in one fell-swoop to graphics context
        // of the game panel, and show it for ~40ms. If you attempt to draw sprites directly on the gamePanel, e.g.
        // without the use of a double-buffered off-screen image, you will see flickering.
        updateButtonSelection();
    }


    //this method causes all sprites to move and draw themselves
    @SafeVarargs
    private final void moveDrawMovables(final Graphics g, List<Movable>... teams) {

        BiConsumer<Movable, Graphics> moveDraw = (mov, grp) -> {
            mov.move();
            mov.draw(grp);
        };


        Arrays.stream(teams) //Stream<List<Movable>>
                //we use flatMap to flatten the teams (List<Movable>[]) passed-in above into a single stream of Movables
                .flatMap(Collection::stream) //Stream<Movable>
                .forEach(m -> moveDraw.accept(m, g));


    }

    protected BufferedImage loadGraphic(String imagePath) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(Objects.requireNonNull(Sprite.class.getResourceAsStream(imagePath)));
        }
        catch (IOException e) {
            e.printStackTrace();
            bufferedImage = null;
        }
        return bufferedImage;
    }
    private void initFontInfo() {
        Graphics g = getGraphics();            // get the graphics context for the panel
        g.setFont(fontNormal);                        // take care of some simple font stuff
        fontMetrics = g.getFontMetrics();
        fontWidth = fontMetrics.getMaxAdvance();
        fontHeight = fontMetrics.getHeight();
        g.setFont(fontBig);                    // set font info
    }


    // This method draws some text to the middle of the screen
    private void displayTextOnScreen(final Graphics graphics, String... lines) {

        //AtomicInteger is safe to pass into a stream
        final AtomicInteger spacer = new AtomicInteger(0);
        Arrays.stream(lines)
                .forEach(str ->
                            graphics.drawString(str, (Game.DIM.width - fontMetrics.stringWidth(str)) / 2,
                                    Game.DIM.height / 4 + fontHeight + spacer.getAndAdd(40))

                );


    }


}
