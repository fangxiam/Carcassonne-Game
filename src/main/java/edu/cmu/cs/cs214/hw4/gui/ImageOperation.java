package edu.cmu.cs.cs214.hw4.gui;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * image operations including getting an image, rotating an image and draw circle on the image
 */
public interface ImageOperation {
    /**
     * get an image by a code
     * @param code image code
     * @return the image got
     * @throws IOException throw any exception
     */
    BufferedImage getImage(Character code) throws IOException;

    /**
     * rotate the image clockwisely
     * @param src source image
     * @return the image rotated
     */
    BufferedImage rotateClockWise(BufferedImage src);

    /**
     * draw a circle with certain color on an image
     * @param src source image
     * @param color color
     * @param segmentIdx position index
     * @return the image with an circle
     */
    BufferedImage withCircle(BufferedImage src, Color color, int segmentIdx);
}
