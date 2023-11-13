package edu.uchicago.gerber._08final.mvc.model;

import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Data
public class Floor extends Sprite {
    // The bricks are all 36 x 36

    private final int brickSize = 36;

    private int x_offset;
    private int y_offset;
    private int x_bricks;
    private int y_bricks;
    private int x_left_upper_brick_idx;
    private int y_left_upper_brick_idx;
    private int x_right_bottom_brick_idx;
    private int y_right_bottom_brick_idx;
    /**
     * Floor is a rectangle that is made of several small base squares, and
     * the minimum unit of the square is 36 pixel x 36 pixel, which is the texture
     * unit. For example, leftUpperCorner is (3, 1), rightBottomCorner is (6, 3),
     * then the floor is like this:
     *
     *              * * *
     *              * * *
     *
      * @param leftUpperCorner
     * @param rightBottomCorner
     */
    public Floor(Point leftUpperCorner, Point rightBottomCorner) {
        setTeam(Team.FLOOR);
        setBoundingType(BoundingType.RECTANGLE);
        setCenter(new Point((leftUpperCorner.x + rightBottomCorner.x) * 18, (leftUpperCorner.y + rightBottomCorner.y) * 18));
        x_left_upper_brick_idx = leftUpperCorner.x;
        y_left_upper_brick_idx = leftUpperCorner.y;
        x_right_bottom_brick_idx = rightBottomCorner.x;
        y_right_bottom_brick_idx = rightBottomCorner.y;
        x_offset = (leftUpperCorner.x - 1) * brickSize;
        y_offset = (leftUpperCorner.y - 1) * brickSize;
        x_bricks = rightBottomCorner.x - leftUpperCorner.x + 1;
        y_bricks = rightBottomCorner.y - leftUpperCorner.y + 1;
        setBoundingBox(new Rectangle(x_offset, y_offset, x_bricks * brickSize, y_bricks * brickSize));
        Map<Integer, BufferedImage> rasterMap = new HashMap<>();
        //brick from Mario Bros
        rasterMap.put(0, loadGraphic(imgPathPrefix + "Bricks/floor/102.png") );
        setRasterMap(rasterMap);
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage floorTexture = getRasterMap().get(0);
        System.out.println(floorTexture.getWidth());
        System.out.println(floorTexture.getHeight());

        for (int i = x_left_upper_brick_idx; i < x_bricks; ++i) {
            for (int j = y_left_upper_brick_idx; j < y_bricks; ++j) {
                g.drawImage( floorTexture, i * brickSize + x_offset, j * brickSize + y_offset, floorTexture.getWidth(), floorTexture.getHeight(), null );
            }
        }
        renderRaster((Graphics2D) g, getRasterMap().get(0));
//        //if you uncomment these, you can see how collision works. Feel free to remove these two lines.
//        g.setColor(Color.LIGHT_GRAY);
//        g.drawOval(getCenter().x - getRadius(), getCenter().y - getRadius(), getRadius() *2, getRadius() *2);

    }
    @Override
    public void move(){
        //do NOT call super.move() and do nothing; a brick does not move.
    }
}
