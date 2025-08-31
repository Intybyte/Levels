package com.thexfactor117.levels.bukkit.util;

import com.cryptomorin.xseries.XAttribute;
import com.google.common.collect.Multimap;
import com.thexfactor117.levels.bukkit.leveling.Experience;
import com.thexfactor117.levels.bukkit.nbt.NBTHelper;
import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.MinecraftAttributes;
import com.thexfactor117.levels.common.leveling.Rarity;
import com.thexfactor117.levels.common.nbt.INBT;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.Random;

/**
 *
 * @author TheXFactor117
 *
 */
public class WeaponHelper {

    public static final NamespacedKey MULTIPLIER = new NamespacedKey("levels", "multiplier");

    private static final Random RANDOM = new Random();

    public static void create(ItemStack stack, Player player) {
        if (stack == null || stack.getItemMeta() == null || stack.getType().isAir()) {
            return;
        }

        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        INBT nbt = NBTHelper.toCommon(pdc);

        Rarity rarity = Rarity.getRarity(nbt);
        if (rarity != Rarity.DEFAULT) {
            return;
        }

        Rarity rarityNew = Rarity.getRandomRarity(RANDOM);
        rarityNew.setRarity(nbt); // sets random rarity

        if (rarityNew == Rarity.MYTHIC) {
            player.playSound(player, Sound.ENTITY_ENDER_DRAGON_DEATH, 1f, 1f);
        }

        if (Configs.getInstance().main.getBoolean("unlimitedDurability")) {
            meta.setUnbreakable(true); // adds Unbreakable tag to item
        }

        Experience exp = new Experience(stack, meta);
        exp.setLevel(1);

        pdc.set(MULTIPLIER, PersistentDataType.DOUBLE, rarityNew.generateWeightedMultiplier()); // adds a randomized multiplier to the item, weighted by rarity

        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE); // hides Attribute Modifier and Unbreakable tags
        setAttributeModifiers(meta, stack); // sets up Attribute Modifiers
        stack.setItemMeta(meta);
    }

    /**
     * Creates a new Attribute Modifier tag list and adds it to the NBTTagCompound. Overrides default vanilla implementation.
     * @param meta
     * @param stack
     */
    private static void setAttributeModifiers(ItemMeta meta, ItemStack stack) {
        INBT inbt = NBTHelper.toCommon(meta.getPersistentDataContainer());

        Rarity rarity = Rarity.getRarity(inbt);
        Material type = stack.getType();

        boolean isWeapon = ItemUtil.isWeapon(type);

        if (isWeapon) {
            // retrieves the default attributes, like damage and attack speed.
            Multimap<Attribute, AttributeModifier> map = type.getDefaultAttributeModifiers(EquipmentSlot.HAND);
            Collection<AttributeModifier> damageCollection = map.get(XAttribute.ATTACK_DAMAGE.get());
            Collection<AttributeModifier> speedCollection = map.get(XAttribute.ATTACK_SPEED.get());
            AttributeModifier damageModifier = (AttributeModifier) damageCollection.toArray()[0];
            AttributeModifier speedModifier = (AttributeModifier) speedCollection.toArray()[0];


            double baseDamage = damageModifier.getAmount() + 1; // add one to base damage for player strength
            double baseSpeed = speedModifier.getAmount();
            double damage = rarity.generateWeightedDamage(baseDamage);
            double speed = rarity.generateWeightedAttackSpeed(baseSpeed);

            // Creates new AttributeModifier's and applies them to the stack's NBT tag compound.
            AttributeModifier attackDamage = new AttributeModifier(MinecraftAttributes.ATTACK_DAMAGE_UUID, "attackDamage", damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            AttributeModifier attackSpeed = new AttributeModifier(MinecraftAttributes.ATTACK_SPEED_UUID, "attackSpeed", speed, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);

            meta.addAttributeModifier(XAttribute.ATTACK_DAMAGE.get(), attackDamage);
            meta.addAttributeModifier(XAttribute.ATTACK_SPEED.get(), attackSpeed);
        }

        EquipmentSlot slot = getSlotOfArmor(type);
        if (slot != null) {
            applyArmorAttributeChange(meta, rarity, type, slot);
        }
    }

    public static EquipmentSlot getSlotOfArmor(Material type) {
        if (ItemUtil.isHelmet(type)) return EquipmentSlot.HEAD;
        if (ItemUtil.isChestplate(type)) return EquipmentSlot.CHEST;
        if (ItemUtil.isLeggings(type)) return EquipmentSlot.LEGS;
        if (ItemUtil.isBoots(type)) return EquipmentSlot.FEET;

        return null;
    }

    private static void applyArmorAttributeChange(ItemMeta meta, Rarity rarity, Material type, EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> map = type.getDefaultAttributeModifiers(slot);
        Collection<AttributeModifier> armorCollection = map.get(XAttribute.ARMOR.get());
        Collection<AttributeModifier> toughnessCollection = map.get(XAttribute.ARMOR_TOUGHNESS.get());

        AttributeModifier armorModifier = (AttributeModifier) armorCollection.toArray()[0];
        AttributeModifier toughnessModifier = (AttributeModifier) toughnessCollection.toArray()[0];

        double baseArmor = armorModifier.getAmount();
        double baseToughness = toughnessModifier.getAmount();
        double newArmor = rarity.getWeightedArmor(baseArmor);
        double newToughness = rarity.generateWeightedArmorToughness(baseToughness);

        // Creates new AttributeModifier's and applies them to the stack's NBT tag compound.
        AttributeModifier armor = new AttributeModifier(MinecraftAttributes.ARMOR_UUID, "armor", newArmor, AttributeModifier.Operation.ADD_NUMBER, slot);
        AttributeModifier toughness = new AttributeModifier(MinecraftAttributes.ARMOR_TOUGHNESS_UUID, "armorToughness", newToughness, AttributeModifier.Operation.ADD_NUMBER, slot);

        meta.addAttributeModifier(XAttribute.ARMOR.get(), armor);
        meta.addAttributeModifier(XAttribute.ARMOR_TOUGHNESS.get(), toughness);
    }
}
