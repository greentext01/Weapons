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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Weapons extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("\n" +
                "██████╗░███████╗██╗░░░░░██╗░░░░░███████╗██████╗░██╗██╗░░░██╗███╗░░░███╗\n" +
                "██╔══██╗██╔════╝██║░░░░░██║░░░░░██╔════╝██╔══██╗██║██║░░░██║████╗░████║\n" +
                "██████╦╝█████╗░░██║░░░░░██║░░░░░█████╗░░██████╔╝██║██║░░░██║██╔████╔██║\n" +
                "██╔══██╗██╔══╝░░██║░░░░░██║░░░░░██╔══╝░░██╔══██╗██║██║░░░██║██║╚██╔╝██║\n" +
                "██████╦╝███████╗███████╗███████╗███████╗██║░░██║██║╚██████╔╝██║░╚═╝░██║\n" +
                "╚═════╝░╚══════╝╚══════╝╚══════╝╚══════╝╚═╝░░╚═╝╚═╝░╚═════╝░╚═╝░░░░░╚═╝\n" +
                "\n" +
                "░██╗░░░░░░░██╗███████╗░█████╗░██████╗░░█████╗░███╗░░██╗░██████╗\n" +
                "░██║░░██╗░░██║██╔════╝██╔══██╗██╔══██╗██╔══██╗████╗░██║██╔════╝\n" +
                "░╚██╗████╗██╔╝█████╗░░███████║██████╔╝██║░░██║██╔██╗██║╚█████╗░\n" +
                "░░████╔═████║░██╔══╝░░██╔══██║██╔═══╝░██║░░██║██║╚████║░╚═══██╗\n" +
                "░░╚██╔╝░╚██╔╝░███████╗██║░░██║██║░░░░░╚█████╔╝██║░╚███║██████╔╝\n" +
                "░░░╚═╝░░░╚═╝░░╚══════╝╚═╝░░╚═╝╚═╝░░░░░░╚════╝░╚═╝░░╚══╝╚═════╝░\n" +
                "\n" +
                "Has started!\n" +
                "Build: 8");

        registerLance();
        registerBattleAxe();
        registerMace();

        // For the texture pack
        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("\n" +
                "██████╗░███████╗██╗░░░░░██╗░░░░░███████╗██████╗░██╗██╗░░░██╗███╗░░░███╗\n" +
                "██╔══██╗██╔════╝██║░░░░░██║░░░░░██╔════╝██╔══██╗██║██║░░░██║████╗░████║\n" +
                "██████╦╝█████╗░░██║░░░░░██║░░░░░█████╗░░██████╔╝██║██║░░░██║██╔████╔██║\n" +
                "██╔══██╗██╔══╝░░██║░░░░░██║░░░░░██╔══╝░░██╔══██╗██║██║░░░██║██║╚██╔╝██║\n" +
                "██████╦╝███████╗███████╗███████╗███████╗██║░░██║██║╚██████╔╝██║░╚═╝░██║\n" +
                "╚═════╝░╚══════╝╚══════╝╚══════╝╚══════╝╚═╝░░╚═╝╚═╝░╚═════╝░╚═╝░░░░░╚═╝\n" +
                "\n" +
                "░██╗░░░░░░░██╗███████╗░█████╗░██████╗░░█████╗░███╗░░██╗░██████╗\n" +
                "░██║░░██╗░░██║██╔════╝██╔══██╗██╔══██╗██╔══██╗████╗░██║██╔════╝\n" +
                "░╚██╗████╗██╔╝█████╗░░███████║██████╔╝██║░░██║██╔██╗██║╚█████╗░\n" +
                "░░████╔═████║░██╔══╝░░██╔══██║██╔═══╝░██║░░██║██║╚████║░╚═══██╗\n" +
                "░░╚██╔╝░╚██╔╝░███████╗██║░░██║██║░░░░░╚█████╔╝██║░╚███║██████╔╝\n" +
                "░░░╚═╝░░░╚═╝░░╚══════╝╚═╝░░╚═╝╚═╝░░░░░░╚════╝░╚═╝░░╚══╝╚═════╝░\n" +
                "\n" +
                "Has stopped!");
    }

    private void registerArmorPiercing() {
        ItemStack armorP = getArmorPiercingIS();
        Bukkit.addRecipe(getArmorPiercingRecipe(armorP));

        this.getServer().getPluginManager().registerEvents(new ArmorPiercing(armorP), this);
    }

    private ShapedRecipe getArmorPiercingRecipe(ItemStack armorP) {
        NamespacedKey key = new NamespacedKey(this, "armorP");
        ShapedRecipe recipe = new ShapedRecipe(key, armorP);
        recipe.shape(" I ",
                     " S ",
                     " F ");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('F', Material.FEATHER);

        return recipe;
    }

    private ItemStack getArmorPiercingIS() {
        ItemStack armorP = new ItemStack(Material.ARROW);
        ItemMeta meta = armorP.getItemMeta();
        meta.setDisplayName("Mace");
        meta.setCustomModelData(1310418);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,
                new AttributeModifier(UUID.randomUUID(),
                        "generic.attackSpeed", -3,
                        AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier(UUID.randomUUID(),
                        "generic.attackDamage", 7,
                        AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        List<String> lore = new ArrayList<>();
        lore.add("When in main hand:");
        lore.add("1 Attack Speed");
        lore.add("8 Attack Damage");
        lore.add("Stuns and gives nausea to enemy for 3 seconds");

        meta.setLore(lore);

        armorP.setItemMeta(meta);
        return armorP;
    }

    private void registerMace() {
        ItemStack mace = getMaceIS();
        Bukkit.addRecipe(getMaceRecipe(mace));

        this.getServer().getPluginManager().registerEvents(new Mace(mace), this);
    }

    private ShapedRecipe getMaceRecipe(ItemStack mace) {
        NamespacedKey key = new NamespacedKey(this, "mace");
        ShapedRecipe recipe = new ShapedRecipe(key, mace);
        recipe.shape("  I",
                     " S ",
                     "S  ");
        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('S', Material.STICK);

        return recipe;
    }

    private ItemStack getMaceIS() {
        ItemStack mace = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = mace.getItemMeta();
        meta.setDisplayName("Mace");
        meta.setCustomModelData(1310418);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,
                new AttributeModifier(UUID.randomUUID(),
                        "generic.attackSpeed", -3,
                        AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier(UUID.randomUUID(),
                        "generic.attackDamage", 7,
                        AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        List<String> lore = new ArrayList<>();
        lore.add("When in main hand:");
        lore.add("1 Attack Speed");
        lore.add("8 Attack Damage");
        lore.add("Stuns and gives nausea to enemy for 3 seconds");

        meta.setLore(lore);

        mace.setItemMeta(meta);
        return mace;
    }


    private void registerBattleAxe() {
        ItemStack battleAxe = getBattleAxeIS();
        Bukkit.addRecipe(getBattleAxeRecipe(battleAxe));
    }

    private ShapedRecipe getBattleAxeRecipe(ItemStack battleAxe) {
        NamespacedKey key = new NamespacedKey(this, "battleaxe");
        ShapedRecipe recipe = new ShapedRecipe(key, battleAxe);
        recipe.shape("III",
                     "ISI",
                     " S ");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('S', Material.STICK);

        return recipe;
    }

    private ItemStack getBattleAxeIS() {
        ItemStack battleAxe = new ItemStack(Material.IRON_AXE);
        ItemMeta meta = battleAxe.getItemMeta();
        meta.setDisplayName("Battle Axe");
        meta.setCustomModelData(4940216);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,
                new AttributeModifier(UUID.randomUUID(),
                        "generic.attackSpeed", -3.5,
                        AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier(UUID.randomUUID(),
                        "generic.attackDamage", 13,
                        AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        List<String> lore = new ArrayList<>();
        lore.add("When in main hand:");
        lore.add("0.5 Attack Speed");
        lore.add("14 Attack Damage");

        meta.setLore(lore);

        battleAxe.setItemMeta(meta);
        return battleAxe;
    }

    private void registerLance() {
        ItemStack lance = getLanceIS();
        Bukkit.addRecipe(getLanceRecipe(lance));

        this.getServer().getPluginManager().registerEvents(new Lance(lance),this);
    }

    private ShapedRecipe getLanceRecipe(ItemStack lance) {

        NamespacedKey key = new NamespacedKey(this, "lance");
        ShapedRecipe recipe = new ShapedRecipe(key, lance);
        recipe.shape("  B",
                     "PS ",
                     "SP ");
        recipe.setIngredient('B', Material.IRON_BLOCK);
        recipe.setIngredient('P', Material.OAK_PLANKS);
        recipe.setIngredient('S', Material.STICK);

        return recipe;
    }

    private ItemStack getLanceIS() {
        ItemStack lance = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = lance.getItemMeta();
        meta.setDisplayName("Lance");
        meta.setCustomModelData(8612592);

        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,
                new AttributeModifier(UUID.randomUUID(),
                        "generic.attackSpeed", -3.1,
                        AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        List<String> lore = new ArrayList<>();
        lore.add("When in main hand:");
        lore.add("0.9 Attack Speed");
        lore.add("2 to 16 Attack Damage");
        lore.add("---------------");
        lore.add("0-16 Attack damage if riding on a horse, depending on its speed");
        lore.add("2 Attack damage if not riding on a horse");
        lore.add("---------------");
        lore.add("2 Attack damage if not riding on a horse");

        meta.setLore(lore);
        lance.setItemMeta(meta);
        return lance;
    }
}