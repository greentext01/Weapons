package com.gmail.oaudetyang.weapons;

import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class ProjectileHit implements Listener {
    public ProjectileHit() {
        Weapons.getPlugin(Weapons.class).getServer().getPluginManager().
                registerEvents(this, Weapons.getPlugin(Weapons.class));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            if(event.getDamager() instanceof Arrow) {
                Arrow proj = (Arrow) event.getDamager();
                if(proj.hasMetadata("bow")) {
                    if(proj.hasMetadata("horse")) {
                        event.setDamage(event.getDamage() * 2);
                    }
                } else if(proj.hasMetadata("crossbow")) {
                    if(proj.hasMetadata("horse")) {
                        event.setDamage(event.getDamage() / 2);
                    }
                }
            }
        }
    }
}
