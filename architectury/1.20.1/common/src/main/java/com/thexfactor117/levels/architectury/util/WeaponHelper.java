package com.thexfactor117.levels.architectury.util;

import com.google.common.collect.Multimap;
import com.thexfactor117.levels.architectury.leveling.Experience;
import com.thexfactor117.levels.architectury.nbt.NBTHelper;
import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.common.leveling.MinecraftAttributes;
import com.thexfactor117.levels.common.leveling.Rarity;
import com.thexfactor117.levels.common.nbt.INBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

import java.util.Collection;
import java.util.Random;

public class WeaponHelper {
    private static final Random RND = new Random();

    public static void create(ItemStack stack, Player player) {
        CompoundTag baseNbt = stack.getOrCreateTag();

        if (baseNbt == null) {
            return;
        }

        INBT nbt = NBTHelper.toCommon(baseNbt);

        Rarity rarity = Rarity.getRarity(nbt);

        if (rarity != Rarity.DEFAULT) {
            return;
        }

        Rarity rarityNew = Rarity.getRandomRarity(RND);
        rarityNew.setRarity(nbt); // sets random rarity
        if (rarityNew == Rarity.MYTHIC) {
            player.playSound(SoundEvents.ENDER_DRAGON_DEATH, 1f, 1f);
        }

        if (Configs.getInstance().main.getBoolean("unlimitedDurability")) {
            baseNbt.putInt("Unbreakable", 1); // adds Unbreakable tag to item
        }

        new Experience(stack).setLevel(1);
        baseNbt.putDouble("Multiplier", rarityNew.generateWeightedMultiplier()); // adds a randomized multiplier to the item, weighted by rarity
        baseNbt.putInt("HideFlags", 6); // hides Attribute Modifier and Unbreakable tags
        setAttributeModifiers(baseNbt, stack); // sets up Attribute Modifiers
        NBTHelper.saveStackNBT(stack, baseNbt);
    }

    /**
     * Creates a new Attribute Modifier tag list and adds it to the CompoundTag. Overrides default vanilla implementation.
     * @param nbt
     * @param stack
     */
    private static void setAttributeModifiers(CompoundTag nbt, ItemStack stack) {
        Item item = stack.getItem();
        INBT inbt = NBTHelper.toCommon(nbt);

        Rarity rarity = Rarity.getRarity(inbt);
        if (item instanceof SwordItem || item instanceof AxeItem) {
            // retrieves the default attributes, like damage and attack speed.
            Multimap<Attribute, AttributeModifier> map = item.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
            Collection<AttributeModifier> damageCollection = map.get(Attributes.ATTACK_DAMAGE);
            Collection<AttributeModifier> speedCollection = map.get(Attributes.ATTACK_SPEED);
            AttributeModifier damageModifier = (AttributeModifier) damageCollection.toArray()[0];
            AttributeModifier speedModifier = (AttributeModifier) speedCollection.toArray()[0];

            double baseDamage = damageModifier.getAmount() + 1; // add one to base damage for player strength
            double baseSpeed = speedModifier.getAmount();
            double damage = rarity.generateWeightedDamage(baseDamage);
            double speed = rarity.generateWeightedAttackSpeed(baseSpeed);

            // Creates new AttributeModifier's and applies them to the stack's NBT tag compound.
            AttributeModifier attackDamage = new AttributeModifier(MinecraftAttributes.ATTACK_DAMAGE_UUID, "attackDamage", damage, AttributeModifier.Operation.ADDITION);
            AttributeModifier attackSpeed = new AttributeModifier(MinecraftAttributes.ATTACK_SPEED_UUID, "attackSpeed", speed, AttributeModifier.Operation.ADDITION);

            stack.addAttributeModifier(Attributes.ATTACK_DAMAGE, attackDamage, EquipmentSlot.MAINHAND);
            stack.addAttributeModifier(Attributes.ATTACK_SPEED, attackSpeed, EquipmentSlot.MAINHAND);
        } else if (item instanceof ArmorItem) {
            ArmorItem armorItem = (ArmorItem) item;
            EquipmentSlot slot = armorItem.getEquipmentSlot();

            Multimap<Attribute, AttributeModifier> map = item.getDefaultAttributeModifiers(slot);
            Collection<AttributeModifier> armorCollection = map.get(Attributes.ARMOR);
            Collection<AttributeModifier> toughnessCollection = map.get(Attributes.ARMOR_TOUGHNESS);

            AttributeModifier armorModifier = (AttributeModifier) armorCollection.toArray()[0];
            AttributeModifier toughnessModifier = (AttributeModifier) toughnessCollection.toArray()[0];

            double baseArmor = armorModifier.getAmount();
            double baseToughness = toughnessModifier.getAmount();
            double newArmor = rarity.getWeightedArmor(baseArmor);
            double newToughness = rarity.generateWeightedArmorToughness(baseToughness);

            // Creates new AttributeModifier's and applies them to the stack's NBT tag compound.
            AttributeModifier armor = new AttributeModifier(MinecraftAttributes.ARMOR_UUID, "armor", newArmor, AttributeModifier.Operation.ADDITION);
            AttributeModifier toughness = new AttributeModifier(MinecraftAttributes.ARMOR_TOUGHNESS_UUID, "armorToughness", newToughness, AttributeModifier.Operation.ADDITION);

            stack.addAttributeModifier(Attributes.ARMOR, armor, slot);
            stack.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, toughness, slot);
        }
    }
}

