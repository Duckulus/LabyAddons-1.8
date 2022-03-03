package com.github.l3nnartt.labyaddons;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.DropDownElement;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.ModColor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Settings {
    private final LabyAddons addon;

    public Settings(LabyAddons addon) {
        this.addon = addon;
    }

    public void createSettings(List<SettingsElement> list) {
        list.add(new HeaderElement(ModColor.cl('c') + "Restart the game after changing the Download Server for it to apply."));
        String content;
        try {
            content = IOUtils.toString(new URL("http://dl.lennartloesche.de/labyaddons/8/servers.json"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        JsonObject json = new JsonParser().parse(content).getAsJsonObject();
        for (JsonElement jsonElm : json.get("servers").getAsJsonArray()) {
            addon.getAvailableServers().add(jsonElm.getAsJsonObject().get("address").getAsString());
        }

        final DropDownMenu<String> dropDownMenu = new DropDownMenu<String>("Choose Download Server", 0, 0, 0, 0).fill(addon.getAvailableServers().toArray(new String[0]));
        DropDownElement<String> dropDown = new DropDownElement<>("Choose Download Server", dropDownMenu);
        dropDownMenu.setSelected(LabyAddons.getInstance().getDlServer());
        dropDown.setChangeListener(alignment -> {
            LabyAddons.getLogger("Switched DL Server to: " + alignment);
            addon.getConfig().addProperty("dlServer", alignment);
            addon.saveConfig();
        });

        dropDownMenu.setEntryDrawer((object, x, y, trimmedEntry) -> {
            String entry = object.toString().toLowerCase();
            LabyMod.getInstance().getDrawUtils().drawString(LanguageManager.translate(entry), x, y);
        });

        list.add(dropDown);
        list.add(new HeaderElement(ModColor.cl('r') + ""));
        list.add(new HeaderElement(ModColor.cl('b') + "Special thanks to following contributors:"));
        list.add(new HeaderElement(ModColor.cl('e') + "Northernside"));
    }
}
