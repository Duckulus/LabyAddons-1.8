package com.github.l3nnartt.labyaddons.updater;

import com.github.l3nnartt.labyaddons.LabyAddons;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import java.net.HttpURLConnection;
import java.net.URL;

public class Authenticator implements Runnable {
    public boolean authenticate() {
        Minecraft mc = Minecraft.getMinecraft();
        Session session = mc.getSession();
        if (session == null) {
            return false;
        }
        try {
            mc.getSessionService().joinServer(session.getProfile(), session.getToken(), "26c142208fc4cb3e6ed4ebc598d989b4848786ed");
            return true;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void request() {
        try {
            HttpURLConnection con = (HttpURLConnection) (new URL("http://dl.lennartloesche.de/labyaddons/auth.php?name=" + LabyMod.getInstance().getLabyModAPI().getPlayerUsername() + "&uuid=" + LabyMod.getInstance().getLabyModAPI().getPlayerUUID())).openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            con.connect();
            int code = con.getResponseCode();
            if (code == 200) {
                LabyAddons.getLogger("Request successful");
            } else {
                LabyAddons.getLogger("Request failed. Errorcode: " + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (authenticate()) {
            request();
        }
    }
}