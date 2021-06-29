package com.gmail.oaudetyang.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

import static org.bukkit.Material.BELL;

public class Catapult extends Item implements Listener {
    private HashMap<UUID, Long> timeMap = new HashMap<>();

    public Catapult() {
        super(BELL);
        setModelData(7529729);
        setDisplayName("Catapult");
        List<String> lore = new ArrayList<>();
        lore.add("Catapults are the best siege engine change my mind");
        setLore(lore);
        setMeta();

        NamespacedKey key = new NamespacedKey(getPlugin(), "catapult");
        ShapedRecipe recipe = new ShapedRecipe(key, getItemStack());
        recipe.shape(" I ",
                     "ISS",
                     "PPP");
        recipe.setIngredient('P', Material.OAK_PLANKS);
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('I', Material.IRON_BLOCK);

        setRecipe(recipe, key);

        getPlugin().getServer().getPluginManager().
                registerEvents(this, getPlugin());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getBlock().getType().equals(BELL)) {
            if(event.getItemInHand().hasItemMeta()) {
                if(event.getItemInHand().getItemMeta().getCustomModelData() == 7529729) {

                    event.setCancelled(true);
                    Location blockLoc = event.getBlock().getLocation();
                    Location loc = new Location(blockLoc.getWorld(), blockLoc.getBlockX() + 0.5,
                            blockLoc.getBlockY(), blockLoc.getBlockZ() + 0.5);

                    Directional direction = (Directional) event.getBlock().getBlockData();

                    int rotation = 0;
                    switch (direction.getFacing()) {
                        case NORTH:
                            rotation = 180;
                            break;

                        case EAST:
                            rotation = 270;
                            break;

                        case SOUTH:
                            rotation = 0;
                            break;

                        case WEST:
                            rotation = 90;
                            break;
                    }

                    ArmorStand as = (ArmorStand) event.getBlock().getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                    as.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);

                    as.setRotation(rotation, 0);
                    as.getEquipment().setHelmet(getItemStack());

                    as.setInvisible(true);
                    as.setSmall(true);

                    as.setMetadata("catapult", new FixedMetadataValue(getPlugin(), "catapult"));

                    event.getItemInHand().setAmount(event.getItemInHand().getAmount() - 1);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
            ArmorStand as = (ArmorStand) event.getEntity();
            if(as.hasMetadata("catapult")) {
                event.getDrops().clear();
                as.getWorld().dropItem(as.getLocation(), getItemStack());
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
            ArmorStand as = (ArmorStand) event.getRightClicked();
            if(as.getEquipment().getHelmet().getType().equals(Material.BELL)) {
                if(as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == 7529729) {
                    if(event.getPlayer().isSneaking()) {
                        as.remove();
                        as.getWorld().dropItem(as.getLocation(), getItemStack());
                    } else {
                        event.getRightClicked().addPassenger(event.getPlayer());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(event.getItem() == null)
                return;
            if(event.getItem().getType().equals(Material.STICK)) {
                if(!event.getItem().hasItemMeta())
                    return;
                if(event.getPlayer().getVehicle() instanceof ArmorStand) {
                    if(!((ArmorStand) event.getPlayer().getVehicle()).getEquipment().getHelmet().hasItemMeta())
                        return;


                    if(timeMap.get(event.getPlayer().getUniqueId()) != null) {
                        if(System.currentTimeMillis() - timeMap.get(event.getPlayer().getUniqueId()) >= 5000) {
                            timeMap.replace(event.getPlayer().getUniqueId(), System.currentTimeMillis());
                        } else {
                            return;
                        }
                    } else {
                        timeMap.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
                    }

                    if(((ArmorStand) event.getPlayer().getVehicle()).getEquipment().getHelmet().getItemMeta().getCustomModelData() == 7529729) { // Big boi
                        if (event.getItem().getItemMeta().getCustomModelData() == 9280324) {
                            event.setCancelled(true);
                            event.setUseItemInHand(Event.Result.DENY);
                            event.getItem().setAmount(event.getItem().getAmount() - 1);
                            Arrow arrow = event.getPlayer().launchProjectile(Arrow.class, event.getPlayer().getLocation().getDirection());
                            arrow.setVelocity(arrow.getVelocity().multiply(2));
                            arrow.setMetadata("catapultproj", new FixedMetadataValue(getPlugin(), "catapultproj"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if(event.getEntity().hasMetadata("catapultproj")) {
            Objects.requireNonNull(event.getEntity().getLocation().getWorld()).
                    createExplosion(event.getEntity().getLocation(), 3, false);
            event.getEntity().remove();
        }
    }
}
