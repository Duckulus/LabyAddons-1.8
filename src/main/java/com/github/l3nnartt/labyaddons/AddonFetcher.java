package com.github.l3nnartt.labyaddons;

import com.github.l3nnartt.labyaddons.utils.AddonInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import net.labymod.addon.online.AddonInfoManager;
import net.labymod.addon.online.info.OnlineAddonInfo;
import org.apache.commons.io.IOUtils;

public class AddonFetcher extends Thread {

    public void run() {
        fetchAddons();
    }

    //Fetch Addon Json
    private void fetchAddons() {
        String content;
        try {
            content = IOUtils.toString(new URL("http://dl.lennartloesche.de/labyaddons/addons.json"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        JsonObject json = (new JsonParser()).parse(content).getAsJsonObject();
        addAddons(json.get("addons").getAsJsonArray());
        LabyAddons.getLogger("Checking for Addons");
    }

    //add Addons to Store
    private void addAddons(JsonArray addonElements) {
        AddonInfoManager addonInfoManager = AddonInfoManager.getInstance();
        if (addonInfoManager.getAddonInfoList().size() > 0) {
            int[] sorting = new int[addonInfoManager.getAddonInfoList().size()];

            for (JsonElement element : addonElements) {
                // Get Addon Json Object
                JsonObject addonObject = element.getAsJsonObject();

                // Get Addon Infos
                UUID uuid = UUID.fromString(addonObject.get("uuid").getAsString());
                String name = addonObject.get("name").getAsString();
                int version = addonObject.get("version").getAsInt();
                String hash = addonObject.get("hash").getAsString();
                String author = addonObject.get("author").getAsString();
                String description = addonObject.get("description").getAsString();
                int category = addonObject.get("category").getAsInt();
                boolean restart = addonObject.get("restart").getAsBoolean();
                String iconURL = addonObject.get("icon").getAsString();
                String downloadURL = addonObject.get("download").getAsString();

                // Create OnlineAddonInfo
                LabyAddons.getLogger("Addon found " + name);
                OnlineAddonInfo addonInfo = new AddonInfo(uuid, name, version, hash, author, description, category, restart, downloadURL, iconURL, false, sorting);

                // Add addons to addons store
                if (addonInfoManager.getAddonInfoMap().get(addonInfo.getUuid()) == null) {
                    addonInfoManager.getAddonInfoList().add(addonInfo);
                    addonInfoManager.getAddonInfoMap().put(addonInfo.getUuid(), addonInfo);
                }
            }
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            addAddons(addonElements);
        }
    }
}