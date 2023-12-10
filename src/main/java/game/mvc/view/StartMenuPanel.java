package game.mvc.view;

import game.mvc.controller.CommandCenter;
import game.mvc.controller.Game;
import game.mvc.controller.Utils;
import game.mvc.model.Movable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;


public class StartMenuPanel extends Panel {

    //used for double-buffering
    private Image imgOff;
    private Graphics grpOff;

    // images used to display an animation of plants and flashing neon lights
    private static BufferedImage titleImage = Utils.loadGraphic("/imgs/Menu/title.jpg");
    private static BufferedImage grassImage = Utils.loadGraphic("/imgs/Menu/title_grass.png");
    private static BufferedImage titleKatanaImage = Utils.loadGraphic("/imgs/Menu/title_katana.png");
    private static BufferedImage lightImage0 = Utils.loadGraphic("/imgs/Menu/light_0.png");
    private static BufferedImage lightImage1 = Utils.loadGraphic("/imgs/Menu/light_1.png");
    private static BufferedImage lightImage2 = Utils.loadGraphic("/imgs/Menu/light_2.png");
    private static BufferedImage fontImage = Utils.loadGraphic("/imgs/Menu/title_font.png");

    private static ArrayList<BufferedImage> plantPics = new ArrayList<>();
    static {
        for (int i = 0; i < 12; i++) {
            plantPics.add(Utils.loadGraphic(String.format("/imgs/Menu/plant_%d.png", i)));
        }
    }
    private int currentPicIdx = 0;

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

        // draw title images
        g.drawImage(titleImage, 0, 0, this);

        // light base image
        g.drawImage(lightImage0, 0, 0, this);

        // light image for the first 3 letters, small probability of flickering
        if (CommandCenter.getInstance().getFrame() % 6 != 0) {
            g.drawImage(lightImage1, 0, 0, this);
        }

        // light image for the last letter, big probability of flickering
        if (CommandCenter.getInstance().getFrame() % 10 < 5) {
            g.drawImage(lightImage2, 0, 0, this);
        }

        g.drawImage(titleKatanaImage, 0, 0, this);
        g.drawImage(grassImage, 0, 0, this);

        // determine the plant image to display
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
}
