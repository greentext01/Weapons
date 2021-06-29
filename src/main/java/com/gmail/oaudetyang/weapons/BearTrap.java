package com.gmail.oaudetyang.weapons;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class BearTrap extends Item implements Listener {
    public BearTrap() {
        super(Material.BELL);

        setModelData(9472934);

        setDisplayName("Bear Trap");
        List<String> lore = new ArrayList<>();
        lore.add("Traps you for 10 seconds and does 10 damage");
        setLore(lore);
        setMeta();

        NamespacedKey key = new NamespacedKey(getPlugin(), "beartrap");
        ShapedRecipe recipe = new ShapedRecipe(key, getItemStack());
        recipe.shape("   ",
                     "BIB",
                     "   ");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.IRON_BARS);

        setRecipe(recipe, key);

        getPlugin().getServer().getPluginManager().
                registerEvents(this, getPlugin());
    }

    public void primeBeartrap(ArmorStand as) {
        ItemStack item = Objects.requireNonNull(as.getEquipment()).getHelmet();
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(9472934);
        item.setItemMeta(meta);
        as.getEquipment().setHelmet(item);


        as.setMetadata("primed", new FixedMetadataValue(getPlugin(), "primed"));
    }

    public void unPrimeBearTrap(ArmorStand as) {
        ItemStack item = Objects.requireNonNull(as.getEquipment()).getHelmet();
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(9598001);
        item.setItemMeta(meta);
        as.getEquipment().setHelmet(item);

        as.removeMetadata("primed", getPlugin());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getItemInHand().hasItemMeta()) {
            if(event.getItemInHand().getItemMeta().getCustomModelData() == 9472934) {
                event.setCancelled(true);

                Location blockLoc = event.getBlockPlaced().getLocation();
                Location asLoc = new Location(blockLoc.getWorld(), blockLoc.getBlockX() + 0.5, blockLoc.getBlockY(), blockLoc.getBlockZ() + 0.5);

                ArmorStand as = (ArmorStand) asLoc.getWorld().
                        spawnEntity(asLoc, EntityType.ARMOR_STAND);

                as.setInvisible(true);
                as.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
                as.getEquipment().setHelmet(getItemStack());
                as.setSmall(true);
                as.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
                as.setMetadata("beartrap", new FixedMetadataValue(getPlugin(), "beartrap"));

                primeBeartrap(as);

                event.getItemInHand().setAmount(event.getItemInHand().getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
            ArmorStand as = (ArmorStand) event.getRightClicked();
            if (as.getEquipment().getHelmet().getType().equals(Material.BELL)) {
                if(event.getPlayer().isSneaking()) {
                    int modelData = as.getEquipment().getHelmet().getItemMeta().getCustomModelData();
                    if(modelData == 9472934 || modelData == 9598001) {
                        as.remove();
                        as.getWorld().dropItem(as.getLocation(), getItemStack());
                    }
                } else {
                    if (as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == 9598001) {
                        primeBeartrap(as);
                        as.getWorld().playEffect(as.getLocation(), Effect.ANVIL_LAND, null);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        List<Entity> entities = event.getPlayer().getNearbyEntities(0.5, 0.5, 0.5);
        for(Entity entity : entities) {
            if(entity.getType().equals(EntityType.ARMOR_STAND)) {
                ArmorStand as = (ArmorStand) entity;
                if(as.getEquipment().getHelmet().getType().equals(Material.BELL)) {
                    if(as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == 9472934) {
                        if(as.hasMetadata("primed")) {
                            event.getPlayer().addPotionEffect
                                    (new PotionEffect(PotionEffectType.SLOW, 200, 100));
                            as.getWorld().playEffect(as.getLocation(), Effect.ANVIL_LAND, null);
                            unPrimeBearTrap(as);
                        }
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
            ArmorStand as = (ArmorStand) event.getEntity();
            if(as.hasMetadata("beartrap")) {
                event.getDrops().clear();
                as.getWorld().dropItem(as.getLocation(), getItemStack());
            }
        }
    }
}
