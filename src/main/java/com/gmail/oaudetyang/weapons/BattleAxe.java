package com.gmail.oaudetyang.weapons;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class BattleAxe extends Item {
    public BattleAxe() {
        super(Material.IRON_AXE);
        setModelData(4940216);
        setAttribute(WAttr.ATTACKDAMAGE, 14);
        setAttribute(WAttr.ATTACKSPEED, 0.5);
        setDisplayName("Battle Axe");
        hideAttributes();
        List<String> lore = new ArrayList<>();
        lore.add("When in main hand:");
        lore.add("0.5 Attack Speed");
        lore.add("14 Attack Damage");
        setLore(lore);
        setMeta();

        NamespacedKey key = new NamespacedKey(getPlugin(), "battleaxe");
        ShapedRecipe recipe = new ShapedRecipe(key, getItemStack());
        recipe.shape("III",
                     "ISI",
                     " S ");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('S', Material.STICK);

        setRecipe(recipe, key);
    }
}
