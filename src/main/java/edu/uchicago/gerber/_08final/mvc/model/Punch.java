package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import lombok.Data;

import java.awt.*;

@Data
public class Punch extends Sprite {

    public static int punchRadius = 36;

    public double theta;
    public Punch(Point punchCenter) {

        setTeam(Team.PUNCH);

        setCenter(punchCenter);
        setRadius(punchRadius);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawOval(getCenter().x - getRadius() - CommandCenter.getInstance().viewX, getCenter().y - getRadius() - CommandCenter.getInstance().viewY, getRadius() *2, getRadius() *2);
    }
}
