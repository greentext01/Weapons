package com.gmail.oaudetyang.weapons;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Mace implements Listener {
    ItemStack m_mace;
    public Mace(ItemStack mace) { m_mace = mace; }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            if(event.getEntity() instanceof LivingEntity) {
                Player damager = (Player)event.getDamager();
                if(m_mace.equals(damager.getInventory().getItemInMainHand())) {
                    LivingEntity victim = (LivingEntity) event.getEntity();
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 5));
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 1));
                }
            }
        }
    }
}
