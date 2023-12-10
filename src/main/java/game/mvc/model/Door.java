package game.mvc.model;

import game.mvc.controller.CommandCenter;
import game.mvc.controller.Game;
import game.mvc.controller.GameOp;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Data
public class Door extends Sprite {

	private final int BLOCK_SIZE = 72;
	private int blockRadius = 36;

	// indicates if this door would spawn enemies infinitely
	private boolean isInfinite = false;

	// enemy num record if this door is finite
	private int enemyNumToSpawn;
	private int enemySpawned;

	private static Map<?, BufferedImage> rasterPicMap;

	static {
		Map<Integer, BufferedImage> rasterMap = new HashMap<>();
		rasterMap.put(0, loadGraphic("/imgs/door.png") );
		rasterPicMap = rasterMap;
	}
	public static boolean loadResources() {
		return true;
	}
	public Door(Point upperLeftCorner, boolean isInfinite) {
		setTeam(Team.BACKGROUND);
		setRadius(blockRadius);

		// the door is 1 block width but 2 blocks height, so we need to calculate its center
		setCenter(new Point(upperLeftCorner.x + BLOCK_SIZE / 2, upperLeftCorner.y + BLOCK_SIZE));

		// if it is finite, then calculate the enemies should be spawned
		if (!isInfinite) {
			setInfinite(false);
			setEnemyNumToSpawn(Game.R.nextInt(5));
			setEnemySpawned(0);
		} else {
			setInfinite(true);
		}
		setBoundingType(BoundingType.RECTANGLE);
		setBoundingBox(new Rectangle(upperLeftCorner.x, upperLeftCorner.y, BLOCK_SIZE, BLOCK_SIZE * 3 / 2));
		setRadius(blockRadius);
	}


	private void spawnEnemies() {
		if (isInfinite || (!isInfinite && enemySpawned < enemyNumToSpawn)) {
			if (CommandCenter.getInstance().getFrame() % 150 == 0) {
				if (Game.R.nextInt(2) == 1) {

					// spawn enemies. generate a random integer between 0-14, and
					// 0 - 4: Grunt
					// 5 - 9: Pomp
					// 11 - 12: Gangster
					// 13 - 14: ShieldCop
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
