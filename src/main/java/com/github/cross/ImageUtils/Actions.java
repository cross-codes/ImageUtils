package com.github.cross.ImageUtils;

public enum Actions {
  compress("compress"),
  rotate("rotate"),
  convert("convert");

  private String action;

  private Actions(String action) {
    this.action = action;
  }

  public String getAction() {
    return this.action;
  }
}
