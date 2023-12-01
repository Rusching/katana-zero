package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.GameOp;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Data
public class Door extends Sprite {

	private final int BLOCK_SIZE = 72;

	private int blockRadius = 36;
	private boolean isInfinite = false;
	private int enemyNumToSpawn;
	private int enemySpawned;
	//The size of this brick is always square!
	//we use upperLeftCorner because that is the origin when drawing graphics in Java

	protected static Map<?, BufferedImage> rasterPicMap;

	static {
		Map<Integer, BufferedImage> rasterMap = new HashMap<>();
		//brick from Mario Bros
		rasterMap.put(0, loadGraphic("/imgs/door.png") );
		rasterPicMap = rasterMap;
	}
	public static boolean loadResources() {
		return true;
	}
	public Door(Point upperLeftCorner, boolean isInfinite) {

		//you can shoot to destroy the wall which yields big points
		setTeam(Team.BACKGROUND);
		setRadius(blockRadius);
		setCenter(new Point(upperLeftCorner.x + BLOCK_SIZE / 2, upperLeftCorner.y + BLOCK_SIZE));
		if (!isInfinite) {
			setInfinite(false);
			setEnemyNumToSpawn(Game.R.nextInt(5));
			setEnemySpawned(0);
		} else {
			setInfinite(true);
		}
		setBoundingType(BoundingType.RECTANGLE);
		setBoundingBox(new Rectangle(upperLeftCorner.x, upperLeftCorner.y, BLOCK_SIZE, BLOCK_SIZE * 3 / 2));
		// why must set a radius?
		setRadius(blockRadius);
	}


	private void spawnEnemies() {
		if (isInfinite || (!isInfinite && enemySpawned < enemyNumToSpawn)) {
			if (CommandCenter.getInstance().getFrame() % 150 == 0) {
				if (Game.R.nextInt(2) == 1) {
					// spawn enemies
					int spawnType = Game.R.nextInt(15);
					CommandCenter.getInstance().setEnemyNums(CommandCenter.getInstance().getEnemyNums() + 1);
					if (!isInfinite) {
						enemySpawned += 1;
					}
					if (spawnType < 5) {
						CommandCenter.getInstance().getOpsQueue().enqueue(
								new Grunt(new Point(center.x,center.y + BLOCK_SIZE / 2)),
								GameOp.Action.ADD
						);
					} else if (spawnType < 10) {
						CommandCenter.getInstance().getOpsQueue().enqueue(
								new Pomp(new Point(center.x,center.y + BLOCK_SIZE / 2)),
								GameOp.Action.ADD
						);
					} else if (spawnType < 13) {
						CommandCenter.getInstance().getOpsQueue().enqueue(
								new Ganster(new Point(center.x,center.y + BLOCK_SIZE / 2)),
								GameOp.Action.ADD
						);
					} else {
						CommandCenter.getInstance().getOpsQueue().enqueue(
								new ShieldCop(new Point(center.x,center.y + BLOCK_SIZE / 2)),
								GameOp.Action.ADD
						);
					}
				}
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		renderRaster((Graphics2D) g, rasterPicMap.get(0));
	}

	@Override
	public void move(){
		spawnEnemies();
	}


} //end class
