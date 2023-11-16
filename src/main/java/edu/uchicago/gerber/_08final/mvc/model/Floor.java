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

    private final int BLOCK_SIZE = 72;


    public void buildFloors() {
        final int ROWS = 5, COLS = 20, X_OFFSET = BLOCK_SIZE * 5, Y_OFFSET = 300;

        for (int nCol = 0; nCol < COLS; nCol++) {
            for (int nRow = 0; nRow < ROWS; nRow++) {
                if (!(nRow == 0 && nCol != 0 && nCol != COLS - 1 && nCol != 10 && nCol != 11 && nCol != 12 && nCol != 13 && nCol != 14 && nCol != 15) && !(nRow == 1) && !(nRow == 2 && nCol != 0 && nCol != COLS - 1)) {

                    CommandCenter.getInstance().getOpsQueue().enqueue(
                            new Block(new Point(nCol * BLOCK_SIZE + X_OFFSET, nRow * BLOCK_SIZE + Y_OFFSET), BLOCK_SIZE),
                            GameOp.Action.ADD);
                }
            }
        }



    }
}
