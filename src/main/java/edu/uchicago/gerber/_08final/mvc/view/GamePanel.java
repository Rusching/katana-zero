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
    private final Font fontNormal = new Font("Visitor TT1 BRK", Font.BOLD, 25);
    private FontMetrics fontMetrics;
    private int fontWidth;
    private int fontHeight;

    //used for double-buffering
    private Image imgOff;
    private Graphics grpOff;
    private static ArrayList<BufferedImage> bgImageList;

    // background images for different levels
    static {
        bgImageList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            bgImageList.add(Utils.loadGraphic(String.format("/imgs/Levels/bgs/%d.jpg", i)));
        }
    }

    public GamePanel() {
    }

    private void drawNumFrame(Graphics g) {
        g.setColor(Color.white);
        g.setFont(fontNormal);
        g.drawString("FRAME :  " + CommandCenter.getInstance().getFrame(), fontWidth,
                Game.DIM.height  - (fontHeight + 22));

    }

    public void update(Graphics g) {
        imgOff = createImage(Game.DIM.width, Game.DIM.height);

        // get its graphics context
        grpOff = imgOff.getGraphics();

        // fill the off-screen image background with black.
        grpOff.setColor(Color.BLACK);
        grpOff.fillRect(0, 0, Game.DIM.width, Game.DIM.height);
        if (fontMetrics == null) {
            initFontInfo();
        }
        drawNumFrame(grpOff);


        if (CommandCenter.getInstance().getZero().isDeathSoundPlayed()
                && CommandCenter.getInstance().getZero().getCurrentHurtGroundIdx() == CommandCenter.getInstance().getZero().getHurtGroundFrames() - 1) {

            // if player dead
            if (CommandCenter.getInstance().currentLevel == 8) {

                // if in level 8
                displayTextOnScreen(grpOff,
                        "No... That won't work",
                        "",
                        "Score: " + CommandCenter.getInstance().getScore(),
                        "",
                        "'L' to go back to Level selection panel",
                        "'S' to reStart this level",
                        "'Q' to Quit game"
                );
            } else {

                // levels other than level 8
                displayTextOnScreen(grpOff,
                        "No... That won't work",
                        "",
                        "",
                        "",
                        "'L' to go back to Level selection panel",
                        "'S' to reStart this level",
                        "'Q' to Quit game");
            }
        } else if (CommandCenter.getInstance().isLevelCleared()) {

            // level cleared
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

            // level paused
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

            // move all movable lists
            grpOff.drawImage(bgImageList.get(CommandCenter.getInstance().currentLevel), 0, 0, this);
            moveDrawMovables(grpOff,
                CommandCenter.getInstance().getMovBackground(),
                CommandCenter.getInstance().getMovBloods(),
                CommandCenter.getInstance().getMovFoes(),
                CommandCenter.getInstance().getMovFloors(),
                CommandCenter.getInstance().getMovEnemies(),
                CommandCenter.getInstance().getMovFriends(),
                CommandCenter.getInstance().getMovKatanas(),
                CommandCenter.getInstance().getMovPunches(),
                CommandCenter.getInstance().getMovBullets(),
                CommandCenter.getInstance().getMovDebris());
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
}
