package com.github.l3nnartt.labyaddons;

import com.github.l3nnartt.labyaddons.utils.AddonInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.labymod.addon.online.AddonInfoManager;
import net.labymod.addon.online.info.OnlineAddonInfo;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class AddonFetcher extends Thread {
    public void run() {
        fetchAddons();
    }

    // Fetch Addon JSON
    private void fetchAddons() {
        String content;
        try {
            content = IOUtils.toString(new URL("http://" + LabyAddons.getInstance().getDlServer() + "/labyaddons/8/addons.json"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        JsonObject json = new JsonParser().parse(content).getAsJsonObject();
        addAddons(json.get("addons").getAsJsonArray());
        LabyAddons.getLogger("Checking for addons...");
    }

    // Add addons to addon store
    private void addAddons(JsonArray addonElements) {
        AddonInfoManager addonInfoManager = AddonInfoManager.getInstance();
        if (addonInfoManager.getAddonInfoList().size() > 0) {
            int[] sorting = new int[addonInfoManager.getAddonInfoList().size()];
            for (JsonElement element : addonElements) {
                // Get addon JSON Object
                JsonObject addonObject = element.getAsJsonObject();

                // Get addon information
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

                // Add addons to addon store
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