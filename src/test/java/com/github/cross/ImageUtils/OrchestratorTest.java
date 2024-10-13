package com.github.cross.ImageUtils;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class OrchestratorTest extends TestCase {
  public OrchestratorTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(OrchestratorTest.class);
  }

  @org.junit.Test
  public void testImageCompressionPNG() throws Exception {

    double sizeMB = 1.5D;

    String cmd =
        String.format(
            "java -jar target/ImageUtils-1.0-SNAPSHOT.jar --source %s --dest %s --action compress"
                + " --maxSizeMB %.2f --ft png",
            "src/test/resources/img/original/IMG1.png", "IMG1_compressed", sizeMB);

    Process process = Runtime.getRuntime().exec(cmd);
    process.waitFor();

    File compressedImage = new File("IMG1_compressed.png");
    assertTrue("Compressed file should exist", compressedImage.exists());

    double compressedSize = compressedImage.length() / (1024D * 1024D);
    assertTrue("Reduced size", compressedSize - sizeMB < 0.2 * sizeMB);

    compressedImage.delete();
  }

  @org.junit.Test
  public void testImageCompressionJPG() throws Exception {

    double sizeMB = 1.2D;

    String cmd =
        String.format(
            "java -jar target/ImageUtils-1.0-SNAPSHOT.jar --source %s --dest %s --action compress"
                + " --maxSizeMB %.2f --ft jpg",
            "src/test/resources/img/original/IMG2.jpg", "IMG2_compressed", sizeMB);

    Process process = Runtime.getRuntime().exec(cmd);
    process.waitFor();

    File compressedImage = new File("IMG2_compressed.jpg");
    assertTrue("Compressed file should exist", compressedImage.exists());

    double compressedSize = compressedImage.length() / (1024D * 1024D);
    assertTrue("Reduced size", compressedSize - sizeMB < 0.2 * sizeMB);

    compressedImage.delete();
  }

  @org.junit.Test
  public void testArgumentPassing() throws Exception {
    String cmd = "java -jar target/ImageUtils-1.0-SNAPSHOT.jar --source x --action compress";

    Process process = Runtime.getRuntime().exec(cmd);
    process.waitFor();

    assertTrue("Process should exit", process.exitValue() == 255);
  }
}
