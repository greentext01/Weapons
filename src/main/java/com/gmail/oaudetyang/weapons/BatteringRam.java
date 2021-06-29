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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.*;

import static org.bukkit.Material.AIR;
import static org.bukkit.Material.BELL;

public class BatteringRam extends Item implements Listener {
    private HashMap<Location, Integer> breakMap = new HashMap<>();
    private HashMap<UUID, Long> timeMap = new HashMap<>();

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
        setLore(lore);
        setMeta();

        NamespacedKey key = new NamespacedKey(getPlugin(), "batteringram");
        ShapedRecipe recipe = new ShapedRecipe(key, getItemStack());
        recipe.shape("PPP",
                     "LLI",
                     "PPP");
        recipe.setIngredient('P', Material.OAK_PLANKS);
        recipe.setIngredient('L', Material.OAK_LOG);
        recipe.setIngredient('I', Material.IRON_BLOCK);

        setRecipe(recipe, key);

        getPlugin().getServer().getPluginManager().
                registerEvents(this, getPlugin());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
            ArmorStand as = (ArmorStand) event.getEntity();
            if(as.hasMetadata("batteringram")) {
                event.getDrops().clear();
                as.getWorld().dropItem(as.getLocation(), getItemStack());
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

                    as.setInvisible(true);

                    as.setMetadata("batteringram", new FixedMetadataValue(getPlugin(), "batteringram"));
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
                    if(event.getPlayer().isSneaking()) {
                        as.remove();
                        as.getWorld().dropItem(as.getLocation(), getItemStack());
                    } else {
                        if (timeMap.get(event.getPlayer().getUniqueId()) != null) {
                            if (System.currentTimeMillis() - timeMap.get(event.getPlayer().getUniqueId()) >= 1000) {
                                timeMap.replace(event.getPlayer().getUniqueId(), System.currentTimeMillis());
                            } else {
                                return;
                            }
                        } else {
                            timeMap.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
                        }

                        Vector direction = as.getLocation().getDirection();
                        Vector tempDir = direction.clone();
                        double temp = direction.getZ();
                        tempDir.setZ(direction.getX());
                        tempDir.setX(temp);

                        for (int i = 2; i < 5; i++) {
                            Vector[] blocks = {tempDir, new Vector(0, 0, 0), tempDir.clone().multiply(-1)};

                            for(Vector currBlock : blocks) {
                                for(int j = 0; j < 3; j++) {
                                    Block block = as.getLocation().add(currBlock).add(direction.clone().multiply(i)).add(new Vector(0, j, 0)).getBlock();
                                    Integer damage;
                                    if ((damage = breakMap.get(block.getLocation())) != null) {
                                        if (damage - 1 == 0) {
                                            breakMap.remove(block.getLocation());
                                            block.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
                                        } else {
                                            breakMap.replace(block.getLocation(), damage - 1);
                                        }
                                    } else {
                                        int durability;
                                        float blastResistance = block.getType().getBlastResistance();
                                        if (blastResistance <= 1) {
                                            block.setType(AIR);
                                            continue;
                                        } else if (blastResistance > 1200) {
                                            return;
                                        } else if (blastResistance >= 10) {
                                            durability = 20;
                                        } else {
                                            durability = Math.round(blastResistance / 2);
                                        }

                                        breakMap.put(block.getLocation(), durability);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
