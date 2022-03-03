package com.github.l3nnartt.labyaddons;

import com.github.l3nnartt.labyaddons.updater.Authenticator;
import com.github.l3nnartt.labyaddons.updater.UpdateChecker;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.SettingsElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LabyAddons extends LabyModAddon {
    public static LabyAddons instance;
    private final ExecutorService exService = Executors.newSingleThreadExecutor();
    private final AddonFetcher fetcher = new AddonFetcher();
    private final ArrayList<String> availableServers = new ArrayList<>();
    private Settings settings;
    private String dlServer;

    @Override
    public void onEnable() {
        instance = this;
        this.settings = new Settings(this);
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
        this.settings.createSettings(list);
    }


    public ArrayList<String> getAvailableServers() {
        return availableServers;
    }

    public String getDlServer() {
        return dlServer;
    }

    public static void getLogger(String log) {
        System.out.println("[LabyAddons] " + log);
    }

    public static LabyAddons getInstance() {
        return instance;
    }
}