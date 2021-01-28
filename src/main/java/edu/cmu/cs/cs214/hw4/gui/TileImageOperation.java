package edu.cmu.cs.cs214.hw4.gui;
import edu.cmu.cs.cs214.hw4.core.Tile;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * this class includes tile images transformation, image rotation and image drawing
 */
public class TileImageOperation implements ImageOperation {
    private static final String TILE_PICTURES = "src/main/resources/Carcassonne.png";
    private static final int MEEPLE_RADIUS = 10;
    private static final int ROTATE_ONCE = 1;
    private static final int X_START = 45;
    private static final int Y_START = 10;
    private static final int WIDTH = 90;
    private static final int COLUMN = 6;
    private static final int ASCII_START = 65;

    /**
     * get each tile image
     * @param code the letter code of each tile
     * @return return the image of specific code
     * @throws IOException throw any exception
     */
    @Override
    public BufferedImage getImage(Character code) throws IOException {
        BufferedImage image = ImageIO.read(new File(TILE_PICTURES));
        int tileNum = code;
        int col = (tileNum + 1) % COLUMN;
        int row = (tileNum - ASCII_START) / COLUMN;
        return image.getSubimage(col * WIDTH,row * WIDTH, WIDTH, WIDTH);
    }

    /**
     * get rotated image by clockwise
     * @param src source image to be rotated
     * @return the image finished rotating
     */
    @Override
    public BufferedImage rotateClockWise(BufferedImage src) {
        int weight = src.getWidth();
        int height = src.getHeight();
        AffineTransform at = AffineTransform.getQuadrantRotateInstance(ROTATE_ONCE, weight / 2.0, height / 2.0);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage dest = new BufferedImage(weight, height, src.getType());
        op.filter(src, dest);
        return dest;
    }

    /**
     * draw a meeple on the tile on four edges and center
     * @param src source image
     * @param color color of meeple
     * @param segmentIdx the index of position to draw
     * @return the tile image with a meeple
     */
    @Override
    public BufferedImage withCircle(BufferedImage src, Color color, int segmentIdx) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        Graphics2D g = (Graphics2D) dest.getGraphics();
        g.drawImage(src, 0, 0, null);
        g.setColor(color);
        int x;
        int y;
        switch (segmentIdx) {
            case (Tile.TOP_INDEX): x = X_START; y = Y_START; break;
            case (Tile.RIGHT_INDEX): x = WIDTH - Y_START; y = X_START; break;
            case (Tile.DOWN_INDEX): x = X_START; y = WIDTH - Y_START; break;
            case (Tile.LEFT_INDEX): x = Y_START; y = X_START; break;
            case (Tile.MONASTERY_INDEX): x = X_START; y = X_START; break;
            default: x = 0; y = 0; break;
        }
        g.fillOval(x - MEEPLE_RADIUS, y - MEEPLE_RADIUS, 2 * MEEPLE_RADIUS, 2 * MEEPLE_RADIUS);
        g.dispose();
        return dest;
    }
}
