package com.github.l3nnartt.labyaddons;

import com.github.l3nnartt.labyaddons.updater.Authenticator;
import com.github.l3nnartt.labyaddons.updater.UpdateChecker;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.SettingsElement;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LabyAddons extends LabyModAddon {

    private final ExecutorService exService = Executors.newSingleThreadExecutor();
    private final AddonFetcher fetcher = new AddonFetcher();

    @Override
    public void onEnable() {
        LabyAddons instance = this;
        exService.execute(new Authenticator());
        exService.execute(new UpdateChecker());
        fetcher.start();
        getLogger("Addon successful activated");
    }

    @Override
    public void loadConfig() {

    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {

    }

    public static void getLogger(String log) {
        String prefix = "[LabyAddons] ";
        System.out.println(prefix + log);
    }
}