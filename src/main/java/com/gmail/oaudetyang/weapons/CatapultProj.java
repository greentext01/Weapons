package com.gmail.oaudetyang.weapons;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class CatapultProj extends Item {
    public CatapultProj() {
        super(Material.STICK);
        setModelData(9280324);
        setDisplayName("Catapult projectile");
        List<String> lore = new ArrayList<>();
        lore.add("Gets launched from a catapult");
        setLore(lore);
        setMeta();

        NamespacedKey key = new NamespacedKey(getPlugin(), "catapultproj");
        ShapedRecipe recipe = new ShapedRecipe(key, getItemStack());
        recipe.shape("SSS",
                     "SIS",
                     "SSS");
        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('S', Material.COBBLESTONE);

        setRecipe(recipe, key);
    }
}
