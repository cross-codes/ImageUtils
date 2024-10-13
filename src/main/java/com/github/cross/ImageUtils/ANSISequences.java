package com.github.cross.ImageUtils;

public enum ANSISequences {
  RESET("\u001B[0m"),
  RED("\u001B[31m"),
  GREEN("\u001B[32m"),
  YELLOW("\u001B[33m"),
  BLUE("\u001B[34m"),
  MAGENTA("\u001B[35m"),
  CYAN("\u001B[36m"),
  BOLD("\u001B[1m"),
  WHITE("\u001B[37m");

  private final String code;

  private ANSISequences(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
