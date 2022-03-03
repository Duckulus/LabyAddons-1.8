package com.github.l3nnartt.labyaddons.updater;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;

public class FileDownloader implements Runnable {
  private final String url;
  private final File file;

  public void run() {
    if (this.file != null && this.url != null && this.url.startsWith("http"))
      try {
        FileUtils.copyURLToFile(new URL(this.url), this.file);
      } catch (IOException e) {
        e.printStackTrace();
      }
  }
  
  public FileDownloader(String url, File file) {
    this.url = url;
    this.file = file;
  }
}
