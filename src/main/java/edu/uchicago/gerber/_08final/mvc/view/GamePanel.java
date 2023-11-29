package edu.uchicago.gerber._08final.mvc.view;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.Utils;
import edu.uchicago.gerber._08final.mvc.model.*;

import javax.imageio.ImageIO;
import javax.tools.Tool;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;


public class GamePanel extends Panel {

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
    private static ArrayList<BufferedImage> bgImageList;

    static {
        bgImageList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            bgImageList.add(loadGraphic(String.format("/maps/imgs/%d.jpg", i)));
        }
    }
    // ==============================================================
    // CONSTRUCTOR
    // ==============================================================

    public GamePanel() {

//        GameFrame gameFrame = new GameFrame();
//
//        gameFrame.getContentPane().add(this);
//
//        gameFrame.pack();


//        initFontInfo();

//        gameFrame.setSize(dim);
//        //change the name of the game-frame to your game name
//        gameFrame.setTitle("Game Base");
//        gameFrame.setResizable(false);
//        gameFrame.setVisible(true);
////        gameFram
//        setFocusable(true);

//        Cursor customCursor = null;
//        BufferedImage cursorImage = Utils.loadGraphic("/imgs/Cursor/0.png");
//        if (cursorImage != null) {
//            Toolkit toolkit = Toolkit.getDefaultToolkit();
//            Point hotSpot = new Point(0, 0);
//            customCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "Custom Cursor");
//        }
//        gameFrame.setCursor(customCursor);
    }


    // ==============================================================
    // METHODS
    // ==============================================================

    private void drawFalconStatus(final Graphics graphics){

        graphics.setColor(Color.white);
        graphics.setFont(fontNormal);

        //draw score always
        graphics.drawString("Score :  " + CommandCenter.getInstance().getScore(), fontWidth, fontHeight);

        //draw the level upper-left corner always
        String levelText = "Level: " + CommandCenter.getInstance().getLevel();
        graphics.drawString(levelText, 20, 30); //upper-left corner

        //build the status string array with possible messages in middle of screen
        List<String> statusArray = new ArrayList<>();
        if (CommandCenter.getInstance().getFalcon().getShowLevel() > 0) statusArray.add(levelText);
        if (CommandCenter.getInstance().getFalcon().isMaxSpeedAttained()) statusArray.add("WARNING - SLOW DOWN");
        if (CommandCenter.getInstance().getFalcon().getNukeMeter() > 0) statusArray.add("PRESS N for NUKE");

        //draw the statusArray strings to middle of screen
        if (statusArray.size() > 0)
            displayTextOnScreen(graphics, statusArray.toArray(new String[0]));



    }

    //this is used for development, you can remove it from your final game
    private void drawNumFrame(Graphics g) {
        g.setColor(Color.white);
        g.setFont(fontNormal);
        g.drawString("FRAME :  " + CommandCenter.getInstance().getFrame(), fontWidth,
                Game.DIM.height  - (fontHeight + 22));

    }

    private void drawMeters(Graphics g){

        //will be a number between 0-100 inclusive
        int shieldValue =   CommandCenter.getInstance().getFalcon().getShield() / 2;
        int nukeValue = CommandCenter.getInstance().getFalcon().getNukeMeter() /6;

        drawOneMeter(g, Color.CYAN, 1, shieldValue);
        drawOneMeter(g, Color.YELLOW, 2, nukeValue);


    }

    private void drawOneMeter(Graphics g, Color color, int offSet, int percent) {

        int xVal = Game.DIM.width - (100 + 120 * offSet);
        int yVal = Game.DIM.height - 45;

        //draw meter
        g.setColor(color);
        g.fillRect(xVal, yVal, percent, 10);

        //draw gray box
        g.setColor(Color.DARK_GRAY);
        g.drawRect(xVal, yVal, 100, 10);
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
        }
        //this is used for development, you may remove drawNumFrame() in your final game.
        drawNumFrame(grpOff);

//        if (CommandCenter.getInstance().isGameOver()) {
//            displayTextOnScreen(grpOff,
//                    "GAME OVER",
//                    "use the arrow keys to turn and thrust",
//                    "use the space bar to fire",
//                    "'S' to Start",
//                    "'P' to Pause",
//                    "'Q' to Quit",
//                    "'M' to toggle music"
//
//            );
        if (CommandCenter.getInstance().getZero().isDeathSoundPlayed()) {
            displayTextOnScreen(grpOff,
                    "No... That won't work",
                    "",
                    "",
                    "",
                    "'L' to go back to Level selection panel",
                    "'S' to reStart this level",
                    "'Q' to Quit game"
            );
        } else if (CommandCenter.getInstance().isLevelCleared()) {
            displayTextOnScreen(grpOff,
                    "Yes, that should work.",
                    "",
                    "",
                    "",
                    "'ENTER' to next level",
                    "'L' to go back to Level selection panel",
                    "'S' to reStart this level",
                    "'Q' to Quit game"
            );
        } else if (CommandCenter.getInstance().isPaused()) {

            displayTextOnScreen(grpOff,
                "Game Paused",
                        "",
                        "",
                        "LEFT CLICK TO ATTACK",
                        "'S' TO ROLL",
                        "'W' TO JUMP",
                        "SHIFT TO ENTER SLOW MOTION",
                        "'P' to continue",
                        "'S' to reStart this level",
                        "'L' to go back to Level selection panel",
                        "'Q' to Quit game"
                    );

        } else {

        //playing and not paused!
//        else {

//             can draw images here!
            grpOff.drawImage(bgImageList.get(CommandCenter.getInstance().currentLevel), 0, 0, this);
            moveDrawMovables(grpOff,
                    CommandCenter.getInstance().getMovFloaters(),
                    CommandCenter.getInstance().getMovBloods(),
                    CommandCenter.getInstance().getMovFoes(),
                    CommandCenter.getInstance().getMovFloors(),
                    CommandCenter.getInstance().getMovEnemies(),
                    CommandCenter.getInstance().getMovFriends(),
                    CommandCenter.getInstance().getMovKatanas(),
                    CommandCenter.getInstance().getMovPunches(),
                    CommandCenter.getInstance().getMovBullets(),
                    CommandCenter.getInstance().getMovDebris());


//            drawNumberShipsRemaining(grpOff);
//            drawMeters(grpOff);
//            drawFalconStatus(grpOff);


        }

        //after drawing all the movables or text on the offscreen-image, copy it in one fell-swoop to graphics context
        // of the game panel, and show it for ~40ms. If you attempt to draw sprites directly on the gamePanel, e.g.
        // without the use of a double-buffered off-screen image, you will see flickering.
        g.drawImage(imgOff, 0, 0, this);
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

        graphics.setColor(Color.WHITE);
        //AtomicInteger is safe to pass into a stream
        final AtomicInteger spacer = new AtomicInteger(0);
        Arrays.stream(lines)
                .forEach(str ->
                            graphics.drawString(str, (Game.DIM.width - fontMetrics.stringWidth(str)) / 2,
                                    Game.DIM.height / 4 + fontHeight + spacer.getAndAdd(40))

                );


    }

    protected static BufferedImage loadGraphic(String imagePath) {
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
}
