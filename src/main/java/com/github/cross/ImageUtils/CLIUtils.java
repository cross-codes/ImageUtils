package com.github.cross.ImageUtils;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CLIUtils {
  private CLIUtils() {}

  public static Option createOption(
      String shortName, String longName, String argName, String desc, boolean required) {
    return Option.builder(shortName)
        .longOpt(longName)
        .desc(desc)
        .hasArg()
        .required(required)
        .build();
  }

  public static void addOptions(Options optGroup, Option... opts) {
    try {
      for (Option option : opts) {
        optGroup.addOption(option);
      }
    } catch (Throwable t) {
      t.printStackTrace(System.err);
      System.exit(-1);
    }
  }

  public static void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(
        "ImageUtils -s source -t inFormat -d dest -a action (-z size | -r angle)", options);
  }

  public static void stdOutWarn(String msg) {
    System.out.println(
        ANSISequences.BOLD.getCode()
            + ANSISequences.RED.getCode()
            + msg
            + ANSISequences.RESET.getCode());
  }

  public static void stdOutMsg(String msg) {
    System.out.println(
        ANSISequences.BOLD.getCode()
            + ANSISequences.GREEN.getCode()
            + msg
            + ANSISequences.RESET.getCode());
  }

  public static void stdOutProc(String msg) {
    System.out.println(
        ANSISequences.BOLD.getCode()
            + ANSISequences.BLUE.getCode()
            + msg
            + ANSISequences.RESET.getCode());
  }

  public static void stdOutBold(String msg) {
    System.out.println(ANSISequences.BOLD.getCode() + msg + ANSISequences.RESET.getCode());
  }

  public static void stdErrWarn(String msg) {
    System.err.println(
        ANSISequences.BOLD.getCode()
            + ANSISequences.RED.getCode()
            + msg
            + ANSISequences.RESET.getCode());
  }
}
