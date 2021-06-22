package com.gmail.oaudetyang.weapons;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArmorPiercing extends Item implements Listener {
    public ArmorPiercing() {
        super(Material.ARROW);
        setModelData(6146527);
        setDisplayName("Armor piercing arrow");
        hideAttributes();
        List<String> lore = new ArrayList<>();
        lore.add("If shot from a crossbow, has a 25% chance to ignore the armor's damage reduction");
        setLore(lore);
        setMeta();

        NamespacedKey key = new NamespacedKey(getPlugin(), "armorpiercing");
        ShapedRecipe recipe = new ShapedRecipe(key, getItemStack());
        recipe.shape(" I ",
                     " S ",
                     " F ");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('F', Material.FEATHER);
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());

        setRecipe(recipe, key);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if(event.getEntity() instanceof Arrow) {
            Arrow proj = (Arrow) event.getEntity();
            if(proj.getShooter() instanceof Player) {
                if(event.getHitEntity() instanceof LivingEntity) {
                    LivingEntity entity = (LivingEntity) event.getHitEntity();
                    if(proj.hasMetadata("ap")) {
                        Random random = new Random();
                        if(random.nextInt(4) == 0) {
                            event.setCancelled(true);
                            entity.damage(6, proj);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();

            if(arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                PlayerInventory inventory = shooter.getInventory();
                ItemStack weapon = isShotFromMainHand(shooter) ? inventory.getItemInMainHand() : inventory.getItemInOffHand();

                if(weapon.getType().equals(Material.ARROW)) {
                    int arrowSlot = isShotFromMainHand(shooter) ? inventory.first(Material.ARROW) : inventory.getHeldItemSlot();
                    ItemStack arrowItem = inventory.getItem(arrowSlot);

                    if(arrowItem.getItemMeta().getCustomModelData() == 6146527) {
                        arrow.setMetadata("ap", new FixedMetadataValue(Weapons.getPlugin(Weapons.class), "ap"));
                    }
                }
            }
        }
    }

    private boolean isShotFromMainHand(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        return mainHand.getType().equals(Material.CROSSBOW) || mainHand.getType().equals(Material.BOW);
    }
}
