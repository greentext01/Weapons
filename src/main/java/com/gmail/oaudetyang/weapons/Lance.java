package com.gmail.oaudetyang.weapons;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.util.Vector;

import java.util.*;

public class Lance extends Item implements Listener {
    Map<UUID, Vector> velocities = new HashMap<>();

    public Lance() {
        super(Material.IRON_SWORD);
        setModelData(8612592);
        setAttribute(WAttr.ATTACKSPEED, 0.9);
        setDisplayName("Lance");
        hideAttributes();
        List<String> lore = new ArrayList<>();
        lore.add("When in main hand:");
        lore.add("0.9 Attack Speed");
        lore.add("2 to 35 Attack Damage");
        lore.add("---------------");
        lore.add("0-35 Attack damage if riding on a horse, depending on its speed");
        lore.add("2 Attack damage if not riding on a horse");
        lore.add("---------------");
        lore.add("2 Attack damage if not riding on a horse");
        setLore(lore);
        setMeta();

        NamespacedKey key = new NamespacedKey(getPlugin(), "lance");
        ShapedRecipe recipe = new ShapedRecipe(key, getItemStack());
        recipe.shape("  B",
                     "PS ",
                     "SP ");
        recipe.setIngredient('B', Material.IRON_BLOCK);
        recipe.setIngredient('P', Material.OAK_PLANKS);
        recipe.setIngredient('S', Material.STICK);
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());

        setRecipe(recipe, key);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(getItemStack().equals(event.getPlayer().getInventory().getItemInMainHand())) {
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
                if(getItemStack().equals(damager.getInventory().getItemInMainHand())) {
                    if(damager.getVehicle() instanceof Horse) {
                        Vector damagerVelocity;
                        if((damagerVelocity = velocities.get(damager.getUniqueId())) != null) {
                            double speed =
                                    Math.abs(damagerVelocity.getX()) + Math.abs(damagerVelocity.getZ());
                            event.setDamage(speed * 50);
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