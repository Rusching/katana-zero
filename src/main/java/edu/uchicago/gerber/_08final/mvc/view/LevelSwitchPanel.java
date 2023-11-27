package edu.uchicago.gerber._08final.mvc.view;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.model.Movable;
import edu.uchicago.gerber._08final.mvc.model.Sprite;

import javax.imageio.ImageIO;
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

    BufferedImage titleImage = loadGraphic("/imgs/Menu/title.jpg");
    BufferedImage grassImage = loadGraphic("/imgs/Menu/title_grass.png");
    BufferedImage titleKatanaImage = loadGraphic("/imgs/Menu/title_katana.png");
    BufferedImage lightImage0 = loadGraphic("/imgs/Menu/light_0.png");
    BufferedImage lightImage1 = loadGraphic("/imgs/Menu/light_1.png");
    BufferedImage lightImage2 = loadGraphic("/imgs/Menu/light_2.png");

    ArrayList<BufferedImage> plantPics = new ArrayList<>();
    private int currentPicIdx = 0;
    // ==============================================================
    // CONSTRUCTOR
    // ==============================================================

    public LevelSwitchPanel() {
        for (int i = 0; i < 12; i++) {
            plantPics.add(loadGraphic(String.format("/imgs/Menu/plant_%d.png", i)));
        }

//        initFontInfo();
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
//
//        setFocusable(true);
//
//        Cursor customCursor = null;
//        BufferedImage cursorImage = Utils.loadGraphic("/imgs/Cursor/0.png");
//        if (cursorImage != null) {
//            Toolkit toolkit = Toolkit.getDefaultToolkit();
//            Point hotSpot = new Point(0, 0);
//            customCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "Custom Cursor");
//        }
//        gameFrame.setCursor(customCursor);
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
        g.drawImage(titleImage, 0, 0, this);

        g.drawImage(lightImage0, 0, 0, this);

        if (CommandCenter.getInstance().getFrame() % 6 != 0) {
            g.drawImage(lightImage1, 0, 0, this);
        }

        if (CommandCenter.getInstance().getFrame() % 10 < 5) {
            g.drawImage(lightImage2, 0, 0, this);
        }


        g.drawImage(titleKatanaImage, 0, 0, this);

        g.drawImage(grassImage, 0, 0, this);

        if (CommandCenter.getInstance().getFrame() % 5 == 0) {
            currentPicIdx += 1;
            if (currentPicIdx == plantPics.size()) {
                currentPicIdx = 0;
            }
        }

        g.drawImage(plantPics.get(currentPicIdx), 0, 254, this);


//        ArrayList<BufferedImage> plantPics =
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
