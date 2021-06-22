package com.gmail.oaudetyang.weapons;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class JoinEvent implements Listener {
    public JoinEvent() {
        Weapons.getPlugin(Weapons.class).getServer().getPluginManager().
                registerEvents(this, Weapons.getPlugin(Weapons.class));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setResourcePack("https://www.dropbox.com/s/0sckvffae90b42x/BelleriumMCWeapons.zip?dl=1");
    }
}
