package com.gmail.oaudetyang.weapons;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ArmorPiercing implements Listener {
    ItemStack m_armorP;
    public ArmorPiercing(ItemStack armorP) {
        m_armorP = armorP;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if(event.getEntity() instanceof Arrow) {
            Arrow proj = (Arrow) event.getEntity();
            if(event.getHitEntity() instanceof Player) {
                if(m_armorP.equals(proj)) {
                    Player player = (Player) event.getEntity();
                    event.setCancelled(true);
                    player.damage(6, proj);
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(!(event.getEntity() instanceof Arrow))
            return;

        Arrow arrow = (Arrow) event.getEntity();
        Player shooter = (Player) arrow.getShooter();
        PlayerInventory inventory = shooter.getInventory();
        int arrowSlot = isShotFromMainHand(shooter) ? inventory.first(Material.ARROW) : inventory.getHeldItemSlot();
        ItemStack arrowItem = inventory.getItem(arrowSlot);

        if(arrowItem.getItemMeta().getCustomModelData() == 1) {

        }
    }

    private boolean isShotFromMainHand(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        return ((mainHand != null) && (mainHand.getType().equals(Material.BOW)));
    }
}
