package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.GameOp;
import lombok.Data;

import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Data
public class Floor {
    // The bricks are all 36 x 36

    private final int BLOCK_SIZE = 36;


    public void buildFloors() {
        final int ROWS = 2, COLS = 20, X_OFFSET = BLOCK_SIZE * 5, Y_OFFSET = 500;

        for (int nCol = 0; nCol < COLS; nCol++) {
            for (int nRow = 0; nRow < ROWS; nRow++) {
                CommandCenter.getInstance().getOpsQueue().enqueue(
                        new Block(
                                new Point(nCol * BLOCK_SIZE + X_OFFSET, nRow * BLOCK_SIZE + Y_OFFSET),
                                BLOCK_SIZE),
                        GameOp.Action.ADD);

            }
        }
    }
}
