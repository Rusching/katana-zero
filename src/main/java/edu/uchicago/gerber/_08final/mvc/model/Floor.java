package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.GameOp;
import lombok.Data;

import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Data
public class Floor {
    // The bricks are all 36 x 36

    private final int BLOCK_SIZE = 72;

    private String levelMapFileName = "/maps/scene%d/%d.csv";
    private int scene = 1;
    private int level = 0;

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

    public void loadLevelAndCreateFloors(int scene, int level) {
        String path = String.format(levelMapFileName, scene, level);
        String line;

        try (InputStream is = Floor.class.getResourceAsStream(path)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            int rowN = 0, colN = 0;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                colN = 0;
                for (String value: values) {
                    switch (Integer.parseInt(value)) {
                        case 0:
                            break;
                        case 1:
                            CommandCenter.getInstance().getOpsQueue().enqueue(
                                    new Block(new Point(
                                            colN * BLOCK_SIZE,
                                            rowN * BLOCK_SIZE
                                    ),
                                            BLOCK_SIZE),
                                    GameOp.Action.ADD
                            );
                            System.out.println("Build a block");
                            break;
                    }
                    colN += 1;
                }
                rowN += 1;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
