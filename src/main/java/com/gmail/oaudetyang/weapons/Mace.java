package com.gmail.oaudetyang.weapons;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Mace extends Item implements Listener {
    public Mace() {
        super(Material.IRON_SWORD);
        setModelData(1310418);
        setAttribute(WAttr.ATTACKSPEED, 1);
        setAttribute(WAttr.ATTACKDAMAGE, 4);
        setDisplayName("Mace");
        hideAttributes();
        List<String> lore = new ArrayList<>();
        lore.add("When in main hand:");
        lore.add("1 Attack Speed");
        lore.add("4 Attack Damage");
        lore.add("Stuns and enemy for 1 second");
        setLore(lore);
        setMeta();

        NamespacedKey key = new NamespacedKey(getPlugin(), "mace");
        ShapedRecipe recipe = new ShapedRecipe(key, getItemStack());
        recipe.shape(" I ",
                     " S ",
                     " S ");
        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('S', Material.STICK);
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());

        setRecipe(recipe, key);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            if(event.getEntity() instanceof LivingEntity) {
                Player damager = (Player)event.getDamager();
                if(getItemStack().equals(damager.getInventory().getItemInMainHand())) {
                    LivingEntity victim = (LivingEntity) event.getEntity();
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 5));
                }
            }
        }
    }
}
