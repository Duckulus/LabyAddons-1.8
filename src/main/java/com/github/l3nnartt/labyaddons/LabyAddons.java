package com.github.l3nnartt.labyaddons;

import com.github.l3nnartt.labyaddons.updater.Authenticator;
import com.github.l3nnartt.labyaddons.updater.UpdateChecker;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.labymod.api.LabyModAddon;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LabyAddons extends LabyModAddon {
    private final ExecutorService exService = Executors.newSingleThreadExecutor();
    private final AddonFetcher fetcher = new AddonFetcher();

    private String dlServer;
    ArrayList<String> availableServers = new ArrayList<>();

    public static LabyAddons instance;

    @Override
    public void onEnable() {
        instance = this;
        exService.execute(new Authenticator());
        exService.execute(new UpdateChecker());
        exService.execute(fetcher::start);

        getLogger("Addon successfully enabled.");
    }

    @Override
    public void loadConfig() {
        this.dlServer = getConfig().has("dlServer") ? getConfig().get("dlServer").getAsString() : "dl.lennartloesche.de";
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
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
            availableServers.add(jsonElm.getAsJsonObject().get("address").getAsString());
        }

        final DropDownMenu<String> dropDownMenu = new DropDownMenu<String>("Choose Download Server", 0, 0, 0, 0).fill(availableServers.toArray(new String[0]));
        DropDownElement<String> dropDown = new DropDownElement<>("Choose Download Server", dropDownMenu);
        dropDownMenu.setSelected(dlServer);
        dropDown.setChangeListener(alignment -> {
            getLogger("Switched DL Server to: " + alignment);
            getConfig().addProperty("dlServer", alignment);
            saveConfig();
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

    public String getDlServer() {
        return dlServer;
    }

    public static LabyAddons getInstance() {
        return instance;
    }

    public static void getLogger(String log) {
        System.out.println("[LabyAddons] " + log);
    }
}