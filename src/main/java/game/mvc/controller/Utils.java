package game.mvc.controller;

import game.mvc.model.Sprite;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


public class Utils {
    public static BufferedImage loadGraphic(String imagePath) {
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
