package edu.uchicago.gerber._08final.mvc.model;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class WhiteCloudDebris extends Sprite{

   private int index = 0;

    public WhiteCloudDebris(Sprite explodingSprite) {

        //DEBRIS means that this sprite is inert, and does not interact with other teams.
        setTeam(Team.DEBRIS);

        Map<Integer, BufferedImage> rasterMap = new HashMap<>();
        //see readme.txt file in the resources/imgs/exp directory for how I created these assets
//        rasterMap.put(0, loadGraphic("/imgs/exp/row-1-column-1.png") );
//        rasterMap.put(1, loadGraphic("/imgs/exp/row-1-column-2.png") );
//        rasterMap.put(2, loadGraphic("/imgs/exp/row-1-column-3.png") );
//        rasterMap.put(3, loadGraphic("/imgs/exp/row-2-column-1.png") );
//        rasterMap.put(4, loadGraphic("/imgs/exp/row-2-column-2.png") );
//        rasterMap.put(5, loadGraphic("/imgs/exp/row-2-column-3.png") );
//        rasterMap.put(6, loadGraphic("/imgs/exp/row-3-column-1.png") );
//        rasterMap.put(7, loadGraphic("/imgs/exp/row-3-column-2.png") );
//        rasterMap.put(8, loadGraphic("/imgs/exp/row-3-column-3.png") );
        rasterMap.put(0, loadGraphic("/imgs/ZeroSprites/idle/spr_casual_idle_0.png") );
        rasterMap.put(1, loadGraphic("/imgs/ZeroSprites/idle/spr_casual_idle_1.png") );
        rasterMap.put(2, loadGraphic("/imgs/ZeroSprites/idle/spr_casual_idle_2.png") );
        rasterMap.put(3, loadGraphic("/imgs/ZeroSprites/idle/spr_casual_idle_3.png") );
        rasterMap.put(4, loadGraphic("/imgs/ZeroSprites/idle/spr_casual_idle_4.png") );
        rasterMap.put(5, loadGraphic("/imgs/ZeroSprites/idle/spr_casual_idle_5.png") );
        rasterMap.put(6, loadGraphic("/imgs/ZeroSprites/idle/spr_casual_idle_6.png") );
        rasterMap.put(7, loadGraphic("/imgs/ZeroSprites/idle/spr_casual_idle_7.png") );
        rasterMap.put(8, loadGraphic("/imgs/ZeroSprites/idle/spr_casual_idle_8.png") );
        rasterMap.put(9, loadGraphic("/imgs/ZeroSprites/idle/spr_casual_idle_9.png") );
        rasterMap.put(10, loadGraphic("/imgs/ZeroSprites/idle/spr_casual_idle_10.png") );

//        setRasterMap(rasterMap);

        //expire it out after it has done its animation. Multiply by 2 to slow down the animation
        setExpiry(rasterMap.size() * 2);

        //everything is relative to the exploding sprite
        setSpin(explodingSprite.getSpin());
        setCenter(explodingSprite.getCenter());
        setDeltaX(explodingSprite.getDeltaX());
        setDeltaY(explodingSprite.getDeltaY());
        setRadius((int) (explodingSprite.getRadius() * 1.3));

    }

    //In this example, we are simply in-order traversing the rasterMap once.
    //However, we could also create a looping animation; think bird flapping over and over.
    //We can also create a hybrid of looping and image-state; think Mario
    //walking (looping), standing (suspended loop), jumping (one state), crouching (another state).
    //See Falcon class for example of image-state.
    @Override
    public void draw(Graphics g) {


//        renderRaster((Graphics2D) g, getRasterMap().get(index));
//        //hold the image for two frames to slow down the dust cloud animation
//        //we already have a simple decrement-to-zero counter with expiry; see move() method of Sprite.
//        if (getExpiry() % 2 == 0) index++;


    }
}
