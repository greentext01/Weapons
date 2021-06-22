package com.gmail.oaudetyang.weapons;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class Item {
    private ItemStack m_item;
    private ItemMeta m_meta;
    private NamespacedKey m_key;
    private ShapedRecipe m_recipe;

    enum WAttr {
        ATTACKSPEED,
        ATTACKDAMAGE
    }

    protected Item(Material material) {
        m_item = new ItemStack(material);
        m_meta = m_item.getItemMeta();
    }

    protected void setMeta() {
        m_item.setItemMeta(m_meta);
    }

    protected void setDisplayName(String name) {
        m_meta = m_item.getItemMeta();
        m_meta.setDisplayName(name);
        m_item.setItemMeta(m_meta);
    }

    protected void setModelData(int data) {
        m_meta = m_item.getItemMeta();
        m_meta.setCustomModelData(data);
        m_item.setItemMeta(m_meta);
    }

    protected void setAttribute(WAttr attr, double amount) {
        String name;
        double trueAmount;
        Attribute spigotAttr;

        if(attr == WAttr.ATTACKDAMAGE) {
            name = "generic.attackDamage";
            trueAmount = amount - 2;
            spigotAttr = Attribute.GENERIC_ATTACK_DAMAGE;
        } else { // Assumes it's ATTACKSPEED
            name = "generic.attackSpeed";
            trueAmount = amount - 4;
            spigotAttr = Attribute.GENERIC_ATTACK_SPEED;
        }

        m_meta.addAttributeModifier(spigotAttr,
                new AttributeModifier(UUID.randomUUID(),
                        name, trueAmount,
                        AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        setMeta();
    }

    protected void hideAttributes() {
        m_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    protected void setLore(List<String> lore) {
        m_meta.setLore(lore);
    }

    protected void setRecipe(ShapedRecipe recipe, NamespacedKey key) {
        m_key = key;
        m_recipe = recipe;
        Bukkit.addRecipe(recipe);
    }

    protected Weapons getPlugin() {
        return Weapons.getPlugin(Weapons.class);
    }

    protected ItemStack getItemStack() {
        return m_item;
    }
}
