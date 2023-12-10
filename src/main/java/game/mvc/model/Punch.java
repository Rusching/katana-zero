package game.mvc.model;

import lombok.Data;

import java.awt.*;

/**
 * Punch is an invisible class for collision detection between enemies and player. When
 * conditions satisfied then the enemy would create a punch object and check the collision
 * between punch and character. If there are overlapping then player is hit. After attacking
 * the punch is destroyed. See more details in Controller/CollisionDetection.java
 */
@Data
public class Punch extends Sprite {

    private static int punchRadius = 36;

    private double theta;
    public Punch(Point punchCenter) {
        setTeam(Team.PUNCH);
        setCenter(punchCenter);
        setRadius(punchRadius);
    }

    @Override
    public void draw(Graphics g) {
    }
}
