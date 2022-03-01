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
    private static LabyAddons instance;

    @Override
    public void onEnable() {
        instance = this;
        exService.execute(new Authenticator());
        exService.execute(new UpdateChecker());
        System.out.println("[LabyAddons] Addon successful activated");
    }

    @Override
    public void loadConfig() {

    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {

    }
}