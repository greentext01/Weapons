package com.gmail.oaudetyang.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Material.BELL;

public class BatteringRam extends Item implements Listener {
    public BatteringRam() {
        super(BELL);
        setModelData(1263912);
        setDisplayName("Battering Ram");
        List<String> lore = new ArrayList<>();
        lore.add("Makes a 1 x 2 x 3 hole in a wall with enough hits");
        lore.add("Obsidian: 10 hits");
        lore.add("Cobblestone: 6 hits");
        lore.add("Iron door: 5 hits");
        lore.add("Sand: 1 hit");
        lore.add("There is a math formula to calculate the resistance of a block but it's too long to put on the lore -_-");
        setLore(lore);
        setMeta();

        NamespacedKey key = new NamespacedKey(getPlugin(), "batteringram");
        ShapedRecipe recipe = new ShapedRecipe(key, getItemStack());
        recipe.shape("PPP",
                     "LLI",
                     "PPP");
        recipe.setIngredient('P', Material.DARK_OAK_PLANKS);
        recipe.setIngredient('L', Material.BIRCH_LOG);
        recipe.setIngredient('I', Material.IRON_BLOCK);

        setRecipe(recipe, key);

        Weapons.getPlugin(Weapons.class).getServer().getPluginManager().
                registerEvents(this, Weapons.getPlugin(Weapons.class));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
            ArmorStand as = (ArmorStand) event.getEntity();
            if(as.getEquipment().getHelmet().getType().equals(BELL)) {
                if(as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == 1263912) {
                    as.setMarker(true);
                    as.getWorld().dropItem(as.getLocation(), getItemStack());
                    as.setHealth(0);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getBlock().getType().equals(BELL)) {
            if(event.getItemInHand().hasItemMeta()) {
                if(event.getItemInHand().getItemMeta().getCustomModelData() == 1263912) {

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

                    event.getItemInHand().setAmount(event.getItemInHand().getAmount() - 1);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
            ArmorStand as = (ArmorStand) event.getRightClicked();
            if(as.getEquipment().getHelmet().getType().equals(Material.BELL)) {
                if(as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == 1263912) {
                    Vector direction = as.getLocation().getDirection();
                    for(int i = 5; i < 8; i++) {
                        for(int j = 0; j < 2; j++) {
                            Block block = as.getLocation().add(direction.clone().
                                    multiply(i)).add(0, j, 0).getBlock();
                            if(!block.hasMetadata("weaponsBlockBreak")) {
                                int durability;
                                float blastResistance = block.getType().getBlastResistance();
                                if(blastResistance < 1) {
                                    durability = 1;
                                } else if(blastResistance > 1200) {
                                    return;
                                } else if(blastResistance >= 10) {
                                    durability = 10;
                                } else {
                                    durability = Math.round(blastResistance);
                                }

                                // The system uses the length of the array
                                // I feel so f'ing smart

                                for(int k = 0; k < durability; k++) {
                                    block.setMetadata("weaponsBlockBreak",
                                            new FixedMetadataValue(getPlugin(), "weaponsBlockBreak"));
                                }
                            } else if(!block.getMetadata("weaponsBlockBreak").isEmpty()) {
                                int value = block.getMetadata("weaponsBlockBreak").size();
                                block.getMetadata("weaponsBlockBreak").remove(0);
                            } else {
                                block.setType(Material.AIR);
                            }
                        }
                    }
                }
            }
        }
    }
}
