package com.github.cross.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class ImageCompressor {
  private ImageCompressor() {}

  public static int[] iterativeSizeCompromise(
      File inputFile, double sizeThresholdMB, File outputFile, String imageFormat) {
    int cnt = 0;
    printRunData(true);
    try {
      final BufferedImage inputImage = ImageIO.read(inputFile);
      final int width = inputImage.getWidth(), height = inputImage.getHeight();

      double inverseFactor = 1D;

      while (true) {
        BufferedImage resizedImage =
            Scalr.resize(
                inputImage,
                Scalr.Method.ULTRA_QUALITY,
                (int) (width * inverseFactor),
                (int) (height * inverseFactor));

        ImageIO.write(resizedImage, imageFormat, outputFile);

        CLIUtils.stdOutProc("Iteration " + ++cnt + ": Inverse factor: " + inverseFactor);

        double newFileSize = outputFile.length() / (1024D * 1024D);
        if (newFileSize <= sizeThresholdMB || cnt >= 10_000) {
          printRunData(false);
          CLIUtils.stdOutProc("Number of iterations taken: " + cnt);;

          int currentWidth = (int) (width * inverseFactor);
          int currentHeight = (int) (height * inverseFactor);

          return (cnt < 10_000) ? new int[] {currentWidth, currentHeight} : null;
        } else {
          if (newFileSize - sizeThresholdMB > 0.5D) inverseFactor -= 0.1;
          else inverseFactor -= 0.01;
        }
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  private static void printRunData(boolean begin) {
    if (begin) {
      System.out.println("-------------------");
      CLIUtils.stdOutBold("    BEGIN RUN");
      System.out.println("-------------------");
      CLIUtils.stdOutProc("Mode: ULTRA_QUALITY");
    } else {
      System.out.println("-------------------");
      CLIUtils.stdOutBold("     END RUN");
      System.out.println("-------------------");
    }
  }
}
