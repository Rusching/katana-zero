package game.mvc.model;

import game.mvc.controller.CommandCenter;
import lombok.Data;

import java.awt.*;

/**
 * Katana is an invisible class for collision detection between enemies and player. When
 * mouse is clicked and player is not attacking, then the player would create a katana object
 * and check the collision between katana and enemies and bullets. If there are overlapping
 * then object is hit. After attacking the katana object is destroyed.
 * See more details in Controller/CollisionDetection.java
 */
@Data
public class Katana extends Sprite {

    private static int katanaRadius = 36;

    private double theta;

    private HitSlashDebris hitSlashDebris = null;
    private BulletReflectionDebris bulletReflectionDebris = null;
    public Katana(int attackX, int attackY, Point zeroCenter, int zeroRadius) {

        setTeam(Team.KATANA);
        Point katanaCenter = findIntersection(zeroCenter, zeroRadius,
                attackX + CommandCenter.getInstance().getViewX(),
                attackY + CommandCenter.getInstance().getViewY());
        setCenter(katanaCenter);
        setRadius(katanaRadius);
    }

    public Point findIntersection(Point circleCenter, double radius, int x_p, int y_p) {
        int x_c = circleCenter.x;
        int y_c = circleCenter.y;

        if (x_c == x_p && y_c == y_p) {

            // exactly the same point
            return new Point(x_c, y_c);
        } else {

            // Calculate the angle theta
            double dx = x_p - x_c;
            double dy = y_p - y_c;
            double theta = Math.atan2(dy, dx);

            // this is radians;
            setTheta(theta);

            // Calculate intersection points
            double x_intersection = x_c + radius * Math.cos(theta);
            double y_intersection = y_c + radius * Math.sin(theta);
            return new Point((int) x_intersection, (int) y_intersection);
        }
    }

    @Override
    public void draw(Graphics g) {
    }
}
