package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.GameOp;
import lombok.Data;

import java.awt.*;
import java.io.*;

@Data
public class Level {
    // The bricks are all 72 x 72

    private final int BLOCK_SIZE = 72;

    private String levelMapFileName = "/maps/level_%d.csv";

    public void buildFloors(int level) {


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

    public void loadLevelAndCreateFloors(int level) {
        String path = String.format(levelMapFileName, level);
        String line;

        CommandCenter.getInstance().setEnemyNums(0);

        try (InputStream is = Level.class.getResourceAsStream(path)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            int rowN = 0, colN;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                colN = 0;
                for (String value: values) {
                    switch (Integer.parseInt(value)) {
                        case -1:
                            // air
                            break;
                        case 0:
//                            // zero, our player
//
                            CommandCenter.getInstance().getZero().setCenter(new Point(
                                    colN * BLOCK_SIZE + BLOCK_SIZE / 2,
                                    rowN * BLOCK_SIZE + BLOCK_SIZE / 2
                                    ));
                            System.out.println("Zero position: " + (int) (colN + 0.5) * BLOCK_SIZE + (int) (rowN + 0.5) * BLOCK_SIZE);
                            System.out.println(CommandCenter.getInstance().getZero().getCenter());
                            System.out.println("Zero position set");
                            break;
                        case 1:
                            // grunt, enemy

                            CommandCenter.getInstance().getOpsQueue().enqueue(
                                    new Grunt(new Point(
                                            colN * BLOCK_SIZE + BLOCK_SIZE / 2,
                                            rowN * BLOCK_SIZE + BLOCK_SIZE / 2
                                    )),
                                    GameOp.Action.ADD
                            );
                            CommandCenter.getInstance().setEnemyNums(CommandCenter.getInstance().getEnemyNums() + 1);
                            break;
                        case 2:
                            // pomp, enemy

                            CommandCenter.getInstance().getOpsQueue().enqueue(
                                    new Pomp(new Point(
                                            colN * BLOCK_SIZE + BLOCK_SIZE / 2,
                                            rowN * BLOCK_SIZE + BLOCK_SIZE / 2
                                    )),
                                    GameOp.Action.ADD
                            );
                            CommandCenter.getInstance().setEnemyNums(CommandCenter.getInstance().getEnemyNums() + 1);
                            break;
                        case 3:
                            // gangster, enemy

                            CommandCenter.getInstance().getOpsQueue().enqueue(
                                    new Ganster(new Point(
                                            colN * BLOCK_SIZE + BLOCK_SIZE / 2,
                                            rowN * BLOCK_SIZE + BLOCK_SIZE / 2
                                    )),
                                    GameOp.Action.ADD
                            );
                            CommandCenter.getInstance().setEnemyNums(CommandCenter.getInstance().getEnemyNums() + 1);
                            break;
                        case 4:
                            // shield cop, enemy

                            CommandCenter.getInstance().getOpsQueue().enqueue(
                                    new ShieldCop(new Point(
                                            colN * BLOCK_SIZE + BLOCK_SIZE / 2,
                                            rowN * BLOCK_SIZE + BLOCK_SIZE / 2
                                    )),
                                    GameOp.Action.ADD
                            );
                            CommandCenter.getInstance().setEnemyNums(CommandCenter.getInstance().getEnemyNums() + 1);
                            break;
                        case 5:
                            CommandCenter.getInstance().getOpsQueue().enqueue(
                                    new Block(new Point(
                                        colN * BLOCK_SIZE,
                                        rowN * BLOCK_SIZE
                                    ),
                                    BLOCK_SIZE),
                                    GameOp.Action.ADD
                            );
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
