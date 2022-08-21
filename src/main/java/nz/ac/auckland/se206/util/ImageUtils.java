package nz.ac.auckland.se206.util;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Class contains utility methods for image transformations. You should not change the existing
 * methods, otherwise the prediction might not work properly
 */
public class ImageUtils {
  /**
   * Generates a black image of a specified width and height.
   *
   * @param width The width of the image.
   * @param height The height of the image.
   * @return The black image.
   * @author
   *     https://www.java2s.com/example/java/2d-graphics/invert-black-and-white-bufferedimage.html
   */
  public static BufferedImage getBlackImage(final int width, final int height) {
    final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    for (int i = 0; i < width - 1; i++) {
      for (int j = 0; j < height - 1; j++) {
        image.setRGB(i, j, Color.black.getRGB());
      }
    }

    return image;
  }

  /**
   * Inverts the black and white pixels of an image. Other colours remain unchanged.
   *
   * @param image The image to invert the black and white pixels of.
   * @return The image with inverted black and white pixels.
   * @author
   *     https://www.java2s.com/example/java/2d-graphics/invert-black-and-white-bufferedimage.html
   */
  public static BufferedImage invertBlackAndWhite(final BufferedImage image) {
    final BufferedImage imageOut = getBlackImage(image.getWidth(), image.getHeight());

    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        final Color c = new Color(image.getRGB(i, j));

        if (c.equals(Color.white)) {
          imageOut.setRGB(i, j, Color.black.getRGB());
        } else if (c.equals(Color.black)) {
          imageOut.setRGB(i, j, Color.white.getRGB());
        } else {
          imageOut.setRGB(i, j, image.getRGB(i, j));
        }
      }
    }

    return imageOut;
  }
}
