package com.gmail.oaudetyang.weapons;

import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Lance implements Listener {
    private final ItemStack m_lance;
    Map<UUID, Vector> velocities = new HashMap<>();
    public Lance(ItemStack lance) {
        m_lance = lance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(m_lance.equals(event.getPlayer().getInventory().getItemInMainHand())) {
            UUID playerUUID = event.getPlayer().getUniqueId();
            if(velocities.get(playerUUID) != null) {
                velocities.replace(playerUUID,
                        event.getFrom().toVector().subtract(event.getTo().toVector()));
            } else {
                velocities.put(playerUUID,
                        event.getFrom().toVector().subtract(event.getTo().toVector()));
            }
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            if(event.getEntity() instanceof LivingEntity) {
                Player damager = (Player)event.getDamager();
                if(m_lance.equals(damager.getInventory().getItemInMainHand())) {
                    if(damager.getVehicle() instanceof Horse) {
                        Vector damagerVelocity;
                        if((damagerVelocity = velocities.get(damager.getUniqueId())) != null) {
                            double speed =
                                    Math.abs(damagerVelocity.getX()) + Math.abs(damagerVelocity.getZ());
                            event.setDamage(speed * 30);
                        } else {
                            event.setDamage(5);
                        }
                        Random randGen = new Random();
                        if(randGen.nextInt(5) == 0)
                            event.getEntity().leaveVehicle();

                    } else {
                        event.setDamage(2);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        velocities.remove(event.getPlayer().getUniqueId());
    }
}