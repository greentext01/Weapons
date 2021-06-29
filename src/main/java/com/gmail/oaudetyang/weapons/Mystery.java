package com.gmail.oaudetyang.weapons;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mystery extends Item implements Listener {
    public Mystery() {
        super(Material.SUGAR);

        setModelData(1078273);

        setDisplayName("???");
        List<String> lore = new ArrayList<>();
        lore.add("Makes you feel like a god");
        setLore(lore);
        setMeta();

        NamespacedKey key = new NamespacedKey(getPlugin(), "mystery");
        ShapedRecipe recipe = new ShapedRecipe(key, getItemStack());
        recipe.shape("   ",
                     "CCC",
                     "   ");
        recipe.setIngredient('C', Material.COCOA_BEANS);

        setRecipe(recipe, key);

        getPlugin().getServer().getPluginManager().
                registerEvents(this, getPlugin());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(event.hasItem()) {
                if(event.getItem().hasItemMeta()) {
                    if(event.getItem().getItemMeta().hasCustomModelData()) {
                        if(event.getItem().getItemMeta().getCustomModelData() == 1078273) {
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 1));
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 1));
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 1));
                            event.getItem().setAmount(event.getItem().getAmount() - 1);
                        }
                    }
                }
            }
        }
    }
}
