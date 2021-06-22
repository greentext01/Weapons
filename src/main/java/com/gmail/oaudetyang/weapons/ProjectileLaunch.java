package com.gmail.oaudetyang.weapons;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;

public class ProjectileLaunch implements Listener {
    public ProjectileLaunch() {
        Weapons.getPlugin(Weapons.class).getServer().getPluginManager().
                registerEvents(this, Weapons.getPlugin(Weapons.class));
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(event.getEntity() instanceof Arrow) {
            Arrow projectile = (Arrow) event.getEntity();
            if(projectile.getShooter() instanceof Player) {
                Player shooter = (Player) projectile.getShooter();
                PlayerInventory inventory = shooter.getInventory();

                ItemStack weapon = isShotFromMainHand(shooter) ? inventory.getItemInMainHand() : inventory.getItemInOffHand();
                if(weapon.getType().equals(Material.CROSSBOW)) {
                    projectile.setMetadata("crossbow",
                            new FixedMetadataValue(Weapons.getPlugin(Weapons.class), "crossbow"));
                } else if(weapon.getType().equals(Material.BOW)) {
                    projectile.setMetadata("bow",
                            new FixedMetadataValue(Weapons.getPlugin(Weapons.class), "bow"));
                }

                if(shooter.getVehicle() instanceof Horse) {
                    projectile.setMetadata("horse",
                            new FixedMetadataValue(Weapons.getPlugin(Weapons.class), "horse"));
                }
            }
        }
    }

    private boolean isShotFromMainHand(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        return mainHand.getType().equals(Material.CROSSBOW) || mainHand.getType().equals(Material.BOW);
    }
}
