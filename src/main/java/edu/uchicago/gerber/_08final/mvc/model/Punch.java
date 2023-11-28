package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import lombok.Data;

import java.awt.*;

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
//        g.setColor(Color.BLUE);
//        g.drawOval(getCenter().x - getRadius() - CommandCenter.getInstance().getViewX(), getCenter().y - getRadius() - CommandCenter.getInstance().getViewY(), getRadius() *2, getRadius() *2);
    }
}
