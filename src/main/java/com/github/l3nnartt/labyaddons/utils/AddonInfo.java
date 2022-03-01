package com.github.l3nnartt.labyaddons.utils;

import java.util.UUID;
import net.labymod.addon.online.info.OnlineAddonInfo;

public class AddonInfo extends OnlineAddonInfo {

  private final String downloadURL;
  private final String iconURL;
  
  public AddonInfo(UUID uuid, String name, int version, String hash, String author, String description, int category, boolean restart, String downloadURL, String iconURL, boolean verified, int[] sorting) {
    super(uuid, name, version, hash, author, description, category, true, false, restart, verified, "1.8", sorting);
    this.downloadURL = downloadURL;
    this.iconURL = iconURL;
  }
  
  public String getImageURL() {
    return iconURL;
  }
  
  public String getDownloadURL() {
    return downloadURL;
  }
}