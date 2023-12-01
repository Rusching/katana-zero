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

    /**
     * load levels from level csv files under "/resources/map/". Each csv
     * file contains corresponding map info. For each value in the grid,
     *      0: zero, player's position
     *      1: enemy type Grunt's position
     *      2: enemy type Pomp's position
     *      3: enemy type Gangster's position
     *      4: enemy type ShieldCop's position
     *      5: wall
     *      6: door with finite enemies
     *      7: door with infinite enemies
     * @param level level number to load
     */
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
                    if (!value.isEmpty()) {
                        switch (Integer.parseInt(value)) {
                            case 0:

                                // zero, our player
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

                                // wall
                                CommandCenter.getInstance().getOpsQueue().enqueue(
                                        new Block(new Point(
                                                colN * BLOCK_SIZE,
                                                rowN * BLOCK_SIZE
                                        ),
                                                BLOCK_SIZE),
                                        GameOp.Action.ADD
                                );
                                break;
                            case 6:

                                // door with limited enemy
                                CommandCenter.getInstance().getOpsQueue().enqueue(
                                        new Door(
                                            new Point(
                                                    colN * BLOCK_SIZE,
                                                    rowN * BLOCK_SIZE),
                                            false
                                        ),
                                        GameOp.Action.ADD
                                );
                                break;
                            case 7:

                                // door with infinite enemy
                                CommandCenter.getInstance().getOpsQueue().enqueue(
                                        new Door(
                                                new Point(
                                                        colN * BLOCK_SIZE,
                                                        rowN * BLOCK_SIZE),
                                                true
                                        ),
                                        GameOp.Action.ADD
                                );
                                break;
                        }
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
