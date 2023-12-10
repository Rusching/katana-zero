package game.mvc.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Block extends Sprite {

	private final int BLOCK_SIZE = 72;

	private int blockRadius = 36;

	// the size of this brick is square
	// we use upperLeftCorner because that is the origin when drawing graphics in Java

	private static Map<?, BufferedImage> rasterPicMap;

	static {
		Map<Integer, BufferedImage> rasterMap = new HashMap<>();
		rasterMap.put(0, loadGraphic("/imgs/Bricks/floor/102.png") );
		rasterPicMap = rasterMap;
	}
	public static boolean loadResources() {
		return true;
	}
	public Block(Point upperLeftCorner, int size) {

		setTeam(Team.FLOOR);
		setRadius(blockRadius);
		setCenter(new Point(upperLeftCorner.x + size/2, upperLeftCorner.y + size/2));

		setBoundingType(BoundingType.RECTANGLE);
		setBoundingBox(new Rectangle(upperLeftCorner.x, upperLeftCorner.y, size, size));
		setRadius(size/2);
	}

	@Override
	public void draw(Graphics g) {
		renderRaster((Graphics2D) g, rasterPicMap.get(0));
	}

	@Override
	public void move(){
	}

}
