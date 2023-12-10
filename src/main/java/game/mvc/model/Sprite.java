package game.mvc.model;

import game.mvc.controller.CommandCenter;
import game.mvc.controller.Game;

import java.awt.*;
import java.io.IOException;
import java.util.*;

import game.mvc.controller.GameOp;
import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

@Data
public abstract class Sprite implements Movable {

    //the center-point of this sprite
    protected Point center;

    //this causes movement; change-in-x and change-in-y
    private double deltaX, deltaY;

    public final int BLOCK_SIZE = 72;

    //every sprite has a team: friend, foe, floater, or debris.
    private Team team;

    //the radius of circumscribing/inscribing circle
    private int radius;

    // alternative bounding box for collision detection
    enum BoundingType {CIRCLE, RECTANGLE};
    protected BoundingType boundingType;
    protected Rectangle boundingBox = null;

    // orientation from 0-359 in degrees
    private int orientation;

    // record some orientation but in radius
    public double theta;

    // natural mortality (short-lived sprites only)
    private int expiry;

    // some sprites spin, such as floaters and asteroids
    private int spin;
    public static String imgPathPrefix = "/imgs/";

    // constructor
    public Sprite() {

        //place the sprite at some random location in the game-space at instantiation
        setCenter(new Point(Game.R.nextInt(Game.DIM.width),
                Game.R.nextInt(Game.DIM.height)));
    }

    /**
     * when setting center we should also set the bounding box if exists
     * @param center
     */
    public void setCenter(Point center) {
        this.center = center;
        if (boundingBox != null) {
            boundingBox.x = center.x - boundingBox.width / 2;
            boundingBox.y = center.y - boundingBox.height / 2;
        }
    }

    /**
     * when setting center we should also set the bounding box if exists
     * @param x
     */
    public void setCenterX(int x) {
        this.center.x = x;
        if (this.boundingBox != null) {boundingBox.x = x - boundingBox.width / 2;}
    }

    /**
     * when setting center we should also set the bounding box if exists
     * @param y
     */
    public void setCenterY(int y) {
        this.center.y = y;
        if (this.boundingBox != null) {boundingBox.y = y - boundingBox.height / 2;}
    }

    @Override
    public void move() {
        double newXPos = center.x + getDeltaX();
        double newYPos = center.y + getDeltaY();
        setCenter(new Point((int) newXPos, (int) newYPos));

        //expire (decrement expiry) on short-lived objects only
        //the default value of expiry is zero, so this block will only apply to expiring sprites
        if (expiry > 0) expire();

        //if a sprite spins, adjust its orientation
        //the default value of spin is zero, therefore non-spinning objects will not call this block.
        if (spin != 0) orientation += spin;

        if (boundingBox != null) {
            boundingBox.x = center.x - boundingBox.width / 2;
            boundingBox.y = center.y - boundingBox.height / 2;
        }
    }

    private void expire() {

        //if a short-lived sprite has an expiry of one, it commits suicide by enqueuing itself (this) onto the
        //opsList with an operation of REMOVE
        if (expiry == 1) {
            CommandCenter.getInstance().getOpsQueue().enqueue(this, GameOp.Action.REMOVE);
        }

        //and then decrements in all cases
        expiry--;

    }

    //A protected sprite will not be destroyed upon collision
    @Override
    public boolean isProtected() {
        //by default, sprites are not protected
        return false;
    }


    //used to load raster graphics
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

    private void applyFilter(Graphics2D g2d) {

        // set the color to dark with some transparency
        Color darkFilter = new Color(10, 10, 10, 15);
        g2d.setColor(darkFilter);

        // Draw the rectangle over the entire screen
        g2d.fillRect(0, 0, Game.dimensionWidth, Game.dimensionHeight);
    }


    protected void renderRaster(Graphics2D g2d, BufferedImage bufferedImage) {

        if (bufferedImage ==  null) return;

        int centerX = getCenter().x;
        int centerY = getCenter().y;
        double angleRadians = Math.toRadians(getOrientation());

        AffineTransform oldTransform = g2d.getTransform();
        try {
            if (CommandCenter.getInstance().isSlowMotion()) {
                applyFilter(g2d);
            }
            AffineTransform affineTransform = new AffineTransform( oldTransform );
            if ( centerX != 0 || centerY != 0 ) {
                affineTransform.translate( centerX - CommandCenter.getInstance().getViewX(), centerY - CommandCenter.getInstance().getViewY());
            }
            affineTransform.scale( 2, 2 );
            if ( angleRadians != 0 ) {
                affineTransform.rotate( angleRadians );
            }
            affineTransform.translate( -bufferedImage.getWidth() / 2.0, -bufferedImage.getHeight() / 2.0 );

            g2d.setTransform( affineTransform );

            g2d.drawImage( bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null );
        } finally {
            g2d.setTransform( oldTransform );

        }
    }

    protected void renderRasterScale(Graphics2D g2d, BufferedImage bufferedImage, int scaleX, int scaleY) {

        if (bufferedImage ==  null) return;

        int centerX = getCenter().x;
        int centerY = getCenter().y;
        double angleRadians = Math.toRadians(getOrientation());

        AffineTransform oldTransform = g2d.getTransform();
        try {
            if (CommandCenter.getInstance().isSlowMotion()) {
                applyFilter(g2d);
            }
            AffineTransform affineTransform = new AffineTransform( oldTransform );
            if ( centerX != 0 || centerY != 0 ) {
                affineTransform.translate( centerX - CommandCenter.getInstance().getViewX(), centerY - CommandCenter.getInstance().getViewY());
            }
            affineTransform.scale( scaleX, scaleY );
            if ( angleRadians != 0 ) {
                affineTransform.rotate( angleRadians );
            }
            affineTransform.translate( -bufferedImage.getWidth() / 2.0, -bufferedImage.getHeight() / 2.0 );

            g2d.setTransform( affineTransform );

            g2d.drawImage( bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null );
        } finally {
            g2d.setTransform( oldTransform );

        }
    }
    protected void renderRasterFlip(Graphics2D g2d, BufferedImage bufferedImage) {

        if (bufferedImage ==  null) return;

        int centerX = getCenter().x;
        int centerY = getCenter().y;
        double angleRadians = Math.toRadians(getOrientation());

        AffineTransform oldTransform = g2d.getTransform();
        try {

            AffineTransform affineTransform = new AffineTransform( oldTransform );
            if ( centerX != 0 || centerY != 0 ) {
                affineTransform.translate( centerX - CommandCenter.getInstance().getViewX(), centerY - CommandCenter.getInstance().getViewY());
            }
            affineTransform.scale( -1, 1 );
            if ( angleRadians != 0 ) {
                affineTransform.rotate( angleRadians );
            }
            affineTransform.translate( -bufferedImage.getWidth() / 2.0, -bufferedImage.getHeight() / 2.0 );

            g2d.setTransform( affineTransform );

            g2d.drawImage( bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null );
        } finally {
            g2d.setTransform( oldTransform );

        }
    }

    protected void renderRasterFromRect(Graphics2D g2d, BufferedImage bufferedImage, int offsetX, int offsetY) {

        if (bufferedImage ==  null) return;
        Rectangle rect = getBoundingBox();
        int centerX = rect.x + rect.width / 2;
        int centerY = rect.y + rect.height / 2;
        double angleRadians = Math.toRadians(getOrientation());

        AffineTransform oldTransform = g2d.getTransform();
        try {
            AffineTransform affineTransform = new AffineTransform( oldTransform );
            if ( centerX != 0 || centerY != 0 ) {
                affineTransform.translate( centerX - CommandCenter.getInstance().getViewX() + offsetX, centerY - CommandCenter.getInstance().getViewY() + offsetY);
            }
            affineTransform.scale( 2, 2 );
            if ( angleRadians != 0 ) {
                affineTransform.rotate( angleRadians );
            }
            affineTransform.translate( -bufferedImage.getWidth() / 2.0, -bufferedImage.getHeight() / 2.0 );

            g2d.setTransform( affineTransform );

            g2d.drawImage( bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null );
        } finally {
            g2d.setTransform( oldTransform );

        }
    }

    protected void renderRasterFlipFromRect(Graphics2D g2d, BufferedImage bufferedImage, int offsetX, int offsetY) {

        if (bufferedImage ==  null) return;

        Rectangle rect = getBoundingBox();

        int centerX = rect.x + rect.width / 2;
        int centerY = rect.y + rect.height / 2;
        double angleRadians = Math.toRadians(getOrientation());

        AffineTransform oldTransform = g2d.getTransform();
        try {
            AffineTransform affineTransform = new AffineTransform( oldTransform );
            if ( centerX != 0 || centerY != 0 ) {
                affineTransform.translate( centerX - CommandCenter.getInstance().getViewX() - offsetX, centerY - CommandCenter.getInstance().getViewY() + offsetY);
            }
            affineTransform.scale( -2, 2 );
            if ( angleRadians != 0 ) {
                affineTransform.rotate( angleRadians );
            }
            affineTransform.translate( -bufferedImage.getWidth() / 2.0, -bufferedImage.getHeight() / 2.0 );

            g2d.setTransform( affineTransform );

            g2d.drawImage( bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null );
        } finally {
            g2d.setTransform( oldTransform );
        }
    }
}
