package com.github.cross.ImageUtils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageRotator {
  private ImageRotator() {}

  public static ExifSubIFDDirectory getExifSubIFDirectory(File image) throws IOException {
    try {
      Metadata metadata = ImageMetadataReader.readMetadata(image);
      ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
      return directory;
    } catch (ImageProcessingException ex) {
      CLIUtils.stdErrWarn("Image processing failed: " + ex.getMessage());
      return null;
    }
  }

  public static double determineRotationAngleExif(ExifSubIFDDirectory dir) throws IOException {
    try {
      if (dir != null && dir.containsTag(ExifSubIFDDirectory.TAG_ORIENTATION)) {
        int orientation = dir.getInt(ExifSubIFDDirectory.TAG_ORIENTATION);

        switch (orientation) {
          case 1:
            return Math.toRadians(0);
          case 3:
            return Math.toRadians(180);
          case 6:
            return Math.toRadians(90);
          case 8:
            return Math.toRadians(270);
          default:
            return -1D;
        }

      } else return -1D;

    } catch (MetadataException ex) {
      System.out.println("Metadata parsing failed: " + ex.getMessage());
      return -1D;
    }
  }

  public static boolean rotateImage(
      int[] dimensions, File image, double angleRadians, String imageFormat, String destFileName) {
    try {

      BufferedImage inputImage = ImageIO.read(image);

      int currentWidth = dimensions[0], currentHeight = dimensions[1];

      int newWidth =
          (int) Math.abs(currentWidth * Math.cos(angleRadians))
              + (int) Math.abs(currentHeight * Math.sin(angleRadians));
      int newHeight =
          (int) Math.abs(currentWidth * Math.cos(angleRadians))
              + (int) Math.abs(currentHeight * Math.sin(angleRadians));

      BufferedImage outputImage = new BufferedImage(newWidth, newHeight, inputImage.getType());

      AffineTransform transform = new AffineTransform();
      transform.rotate(angleRadians, newWidth / 2, newHeight / 2);

      transform.translate((newWidth - currentWidth) / 2, (newHeight - currentHeight) / 2);

      Graphics2D g2d = outputImage.createGraphics();
      g2d.setTransform(transform);
      g2d.drawImage(inputImage, 0, 0, null);
      g2d.dispose();

      ImageIO.write(outputImage, imageFormat, new File(destFileName + "." + imageFormat));
      return true;
    } catch (IOException ex) {
      CLIUtils.stdErrWarn("Failed to rotate image: " + ex.getMessage());
      return false;
    }
  }
}
