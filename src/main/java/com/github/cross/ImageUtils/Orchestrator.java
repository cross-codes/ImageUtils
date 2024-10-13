package com.github.cross.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.drew.metadata.exif.ExifSubIFDDirectory;

public class Orchestrator {
  private final Options options;
  private final CommandLineParser parser;
  private CommandLine cmd;

  public Orchestrator() {
    this.options = new Options();
    this.parser = new DefaultParser();
  }

  public static void main(String[] args) throws ParseException {

    final Orchestrator orchestrator = new Orchestrator();
    orchestrator.createOptions();

    try {
      orchestrator.cmd = orchestrator.parser.parse(orchestrator.options, args);

      if (orchestrator.cmd.hasOption("help")) {
        CLIUtils.printHelp(orchestrator.options);
        System.exit(0);
      }

      if (orchestrator.hasMissingRequiredArguments()) {
        CLIUtils.stdOutWarn("ImageUtils: Missing source or destination file name or action");
        CLIUtils.printHelp(orchestrator.options);
        System.exit(0);
      }

      File inputFile = new File(orchestrator.cmd.getOptionValue("source"));
      String fileFormat = orchestrator.cmd.getOptionValue("ft");
      String action = orchestrator.cmd.getOptionValue("action");
      String destFileName = orchestrator.cmd.getOptionValue("dest");

      if (action.equals(Actions.compress.getAction())) {

        if (orchestrator.cmd.hasOption("maxSizeMB")) {
          Double fileSize = Double.parseDouble(orchestrator.cmd.getOptionValue("maxSizeMB"));
          orchestrator.compressImage(inputFile, fileFormat, destFileName, fileSize);
        } else {
          CLIUtils.stdOutWarn("ImageUtils: Missing option: z");
          CLIUtils.printHelp(orchestrator.options);
          System.exit(-1);
        }

      } else if (action.equals(Actions.rotate.getAction())) {

        if (orchestrator.cmd.hasOption("angle")) {
          double angle = Double.parseDouble(orchestrator.cmd.getOptionValue("angle"));
          orchestrator.rotateImage(inputFile, angle, fileFormat, destFileName);
        } else {
          CLIUtils.stdOutWarn("ImageUtils: Missing option: r");
          CLIUtils.printHelp(orchestrator.options);
          System.exit(-1);
        }

      } else {
        CLIUtils.stdOutWarn("ImageUtils: Invalid action specified");
        CLIUtils.printHelp(orchestrator.options);
        System.exit(-1);
      }

    } catch (ParseException ex) {
      CLIUtils.stdErrWarn("ImageUtils: Parsing failed. Reason: " + ex.getMessage());
      CLIUtils.printHelp(orchestrator.options);
      System.exit(-1);
    }
  }

  private boolean hasMissingRequiredArguments() {
    return !(this.cmd.hasOption("source")
        && this.cmd.hasOption("action")
        && this.cmd.hasOption("ft")
        && this.cmd.hasOption("dest"));
  }

  private void compressImage(
      File inputFile, String fileFormat, String destFileName, double fileSize) {
    try {
      File outputFile = new File(destFileName + "." + fileFormat);
      int[] dimensions =
          ImageCompressor.iterativeSizeCompromise(inputFile, fileSize, outputFile, fileFormat);

      if (!Objects.nonNull(dimensions)) CLIUtils.stdOutWarn("ImageUtils: Compression failed");
      else {
        CLIUtils.stdOutMsg("ImageUtils: Compression complete\n");
        if (this.cmd.hasOption("ignore")
            && !Boolean.parseBoolean(this.cmd.getOptionValue("ignore"))) {
          rotateImageIfExifIsPresent(inputFile, dimensions, outputFile, fileFormat, destFileName);
        } else {
          CLIUtils.stdOutMsg("Skipping Exif check");
        }
      }
    } catch (Throwable t) {
      t.printStackTrace(System.err);
    }
  }

  private void rotateImageIfExifIsPresent(
      File inputFile, int[] dimensions, File outputFile, String fileFormat, String destFileName)
      throws IOException {
    ExifSubIFDDirectory dir = ImageRotator.getExifSubIFDirectory(inputFile);
    if (Objects.nonNull(dir)) {
      CLIUtils.stdOutProc(
          "Exif metadata detected. Attempting affine transformation and rotation to retain"
              + " original orientation");
      double angle = ImageRotator.determineRotationAngleExif(dir);
      if (angle == -1D) {
        CLIUtils.stdOutWarn("Orientation angle unspecified. Consider manually rotating the image");
      } else {
        boolean res =
            ImageRotator.rotateImage(dimensions, outputFile, angle, fileFormat, destFileName);
        if (res) CLIUtils.stdOutMsg("ImageUtils: Rotation complete");
      }
    } else CLIUtils.stdOutMsg("No EXIF directory detected. Skipping affine transform");
  }

  private void rotateImage(File inputFile, double angle, String fileFormat, String destFileName) {
    try {
      BufferedImage inputImage = ImageIO.read(inputFile);
      int[] dimensions = new int[] {inputImage.getWidth(), inputImage.getHeight()};
      boolean res =
          ImageRotator.rotateImage(
              dimensions, inputFile, Math.toRadians(angle), fileFormat, destFileName);
      if (res) CLIUtils.stdOutMsg("ImageUtils: Affine transformation and rotation complete");
      else CLIUtils.stdOutWarn("Error encountered while rotating image");
    } catch (IOException ex) {
      CLIUtils.stdOutWarn("Error obtaining image dimensions: " + ex.getMessage());
    }
  }

  private void createOptions() {
    Option sourceFileOption =
        CLIUtils.createOption("s", "source", "SOURCE", "Path to image source file", true);
    Option fileTypeOption =
        CLIUtils.createOption("t", "ft", "FILETYPE", "Input image format", true);
    Option destFileOption =
        CLIUtils.createOption(
            "d",
            "dest",
            "DESTINATION",
            "Name of the output file name, without extensions (compresss / convert)",
            true);
    Option actionOption = CLIUtils.createOption("a", "action", "ACTION", "Action to execute", true);

    Option maxSizeOption =
        CLIUtils.createOption(
            "z",
            "maxSizeMB",
            "MAX_SIZE_MB",
            "Maximum size of the compressed image (compression only)",
            false);
    Option ignoreExifCheckOption =
        CLIUtils.createOption(
            "i",
            "ignore",
            "IGNORE_EXIF_CHECK",
            "To ignore Exif check stage after compression (compression only)",
            false);
    Option angleOption =
        CLIUtils.createOption(
            "r", "angle", "ANGLE", "Output image rotation (rotation only)", false);

    Option helpOption =
        CLIUtils.createOption("h", "help", "HELP", "Display help information", false);

    CLIUtils.addOptions(
        this.options,
        sourceFileOption,
        fileTypeOption,
        destFileOption,
        actionOption,
        maxSizeOption,
        ignoreExifCheckOption,
        angleOption,
        helpOption);
  }
}
