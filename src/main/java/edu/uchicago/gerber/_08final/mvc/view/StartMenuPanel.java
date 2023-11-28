package edu.uchicago.gerber._08final.mvc.view;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.Utils;
import edu.uchicago.gerber._08final.mvc.model.Movable;
import edu.uchicago.gerber._08final.mvc.model.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;


public class StartMenuPanel extends Panel {
    //used for double-buffering
    private Image imgOff;
    private Graphics grpOff;

    private static BufferedImage titleImage = loadGraphic("/imgs/Menu/title.jpg");
    private static BufferedImage grassImage = loadGraphic("/imgs/Menu/title_grass.png");
    private static BufferedImage titleKatanaImage = loadGraphic("/imgs/Menu/title_katana.png");
    private static BufferedImage lightImage0 = loadGraphic("/imgs/Menu/light_0.png");
    private static BufferedImage lightImage1 = loadGraphic("/imgs/Menu/light_1.png");
    private static BufferedImage lightImage2 = loadGraphic("/imgs/Menu/light_2.png");
    private static BufferedImage fontImage = loadGraphic("/imgs/Menu/title_font.png");


    private static ArrayList<BufferedImage> plantPics = new ArrayList<>();
    static {
        for (int i = 0; i < 12; i++) {
            plantPics.add(loadGraphic(String.format("/imgs/Menu/plant_%d.png", i)));
        }
    }
    private int currentPicIdx = 0;
    // ==============================================================
    // CONSTRUCTOR
    // ==============================================================

    public StartMenuPanel() {

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
        g.drawImage(fontImage, 0, 0, this);
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

    static protected BufferedImage loadGraphic(String imagePath) {
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
