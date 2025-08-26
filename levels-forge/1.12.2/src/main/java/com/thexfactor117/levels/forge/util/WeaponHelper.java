package com.thexfactor117.levels.forge.util;

import com.google.common.collect.Multimap;
import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.forge.Levels;
import com.thexfactor117.levels.forge.leveling.Experience;
import com.thexfactor117.levels.common.leveling.Rarity;
import com.thexfactor117.levels.forge.nbt.NBTHelper;
import com.thexfactor117.levels.forge.network.PacketMythicSound;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author TheXFactor117
 *
 */
public class WeaponHelper {
    private static final UUID ATTACK_DAMAGE = UUID.fromString("38d403d3-3e25-4638-957f-71cd25273933");
    private static final UUID ATTACK_SPEED = UUID.fromString("106410b5-3fa8-4fcf-8252-ca4292dc0391");
    private static final UUID ARMOR = UUID.fromString("6ff9f9f0-0498-4623-aeca-a1afa64188e7");
    private static final UUID ARMOR_TOUGHNESS = UUID.fromString("245507c2-cb9d-4274-81ee-ecced32dafe4");

    public static void create(ItemStack stack, EntityPlayer player) {
        NBTTagCompound baseNbt = NBTHelper.loadStackNBT(stack);

        if (baseNbt == null) {
            return;
        }

        INBT nbt = NBTHelper.toCommon(baseNbt);

        Rarity rarity = Rarity.getRarity(nbt);
        Random rand = player.getEntityWorld().rand;

        if (rarity != Rarity.DEFAULT) {
            return;
        }

        Rarity rarityNew = Rarity.getRandomRarity(rand);
        rarityNew.setRarity(nbt); // sets random rarity
        if (rarityNew == Rarity.MYTHIC) {
            SPacketTitle packet = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentString(TextFormatting.GOLD + "MYTHIC"), -1, 20, -1);
            EntityPlayerMP playermp = (EntityPlayerMP) player;
            playermp.connection.sendPacket(packet);
            Levels.network.sendTo(new PacketMythicSound(), (EntityPlayerMP) player);
        }

        if (Configs.getInstance().main.getBoolean("unlimitedDurability")) {
            baseNbt.setInteger("Unbreakable", 1); // adds Unbreakable tag to item
        }

        new Experience(null, stack).setLevel(1);
        baseNbt.setDouble("Multiplier", rarityNew.generateWeightedMultiplier()); // adds a randomized multiplier to the item, weighted by rarity
        baseNbt.setInteger("HideFlags", 6); // hides Attribute Modifier and Unbreakable tags
        setAttributeModifiers(baseNbt, stack); // sets up Attribute Modifiers
        NBTHelper.saveStackNBT(stack, baseNbt);
    }

    /**
     * Creates a new Attribute Modifier tag list and adds it to the NBTTagCompound. Overrides default vanilla implementation.
     * @param nbt
     * @param stack
     */
    private static void setAttributeModifiers(NBTTagCompound nbt, ItemStack stack) {
        Item item = stack.getItem();
        INBT inbt = NBTHelper.toCommon(nbt);

        Rarity rarity = Rarity.getRarity(inbt);
        if (item instanceof ItemSword || item instanceof ItemAxe) {
            // retrieves the default attributes, like damage and attack speed.
            @SuppressWarnings("deprecation")
            Multimap<String, AttributeModifier> map = item.getItemAttributeModifiers(EntityEquipmentSlot.MAINHAND);
            Collection<AttributeModifier> damageCollection = map.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
            Collection<AttributeModifier> speedCollection = map.get(SharedMonsterAttributes.ATTACK_SPEED.getName());
            AttributeModifier damageModifier = (AttributeModifier) damageCollection.toArray()[0];
            AttributeModifier speedModifier = (AttributeModifier) speedCollection.toArray()[0];

            double baseDamage = damageModifier.getAmount() + 1; // add one to base damage for player strength
            double baseSpeed = speedModifier.getAmount();
            double damage = rarity.generateWeightedDamage(baseDamage);
            double speed = rarity.generateWeightedAttackSpeed(baseSpeed);

            // Creates new AttributeModifier's and applies them to the stack's NBT tag compound.
            AttributeModifier attackDamage = new AttributeModifier(ATTACK_DAMAGE, "attackDamage", damage, 0);
            AttributeModifier attackSpeed = new AttributeModifier(ATTACK_SPEED, "attackSpeed", speed, 0);
            NBTTagCompound damageNbt = writeAttributeModifierToNBT(SharedMonsterAttributes.ATTACK_DAMAGE, attackDamage, EntityEquipmentSlot.MAINHAND);
            NBTTagCompound speedNbt = writeAttributeModifierToNBT(SharedMonsterAttributes.ATTACK_SPEED, attackSpeed, EntityEquipmentSlot.MAINHAND);
            NBTTagList list = new NBTTagList();
            list.appendTag(damageNbt);
            list.appendTag(speedNbt);
            nbt.setTag("AttributeModifiers", list);
        } else if (item instanceof ItemArmor) {
            Multimap<String, AttributeModifier> map = item.getAttributeModifiers(((ItemArmor) item).armorType, stack);
            Collection<AttributeModifier> armorCollection = map.get(SharedMonsterAttributes.ARMOR.getName());
            Collection<AttributeModifier> toughnessCollection = map.get(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName());
            AttributeModifier armorModifier = (AttributeModifier) armorCollection.toArray()[0];
            AttributeModifier toughnessModifier = (AttributeModifier) toughnessCollection.toArray()[0];

            double baseArmor = armorModifier.getAmount();
            double baseToughness = toughnessModifier.getAmount();
            double newArmor = rarity.getWeightedArmor(baseArmor);
            double newToughness = rarity.generateWeightedArmorToughness(baseToughness);

            // Creates new AttributeModifier's and applies them to the stack's NBT tag compound.
            AttributeModifier armor = new AttributeModifier(ARMOR, "armor", newArmor, 0);
            AttributeModifier toughness = new AttributeModifier(ARMOR_TOUGHNESS, "armorToughness", newToughness, 0);
            NBTTagCompound armorNbt = writeAttributeModifierToNBT(SharedMonsterAttributes.ARMOR, armor, ((ItemArmor) item).armorType);
            NBTTagCompound toughnessNbt = writeAttributeModifierToNBT(SharedMonsterAttributes.ARMOR_TOUGHNESS, toughness, ((ItemArmor) item).armorType);
            NBTTagList list = new NBTTagList();
            list.appendTag(armorNbt);
            list.appendTag(toughnessNbt);
            nbt.setTag("AttributeModifiers", list);
        }
    }

    private static NBTTagCompound writeAttributeModifierToNBT(IAttribute attribute, AttributeModifier modifier, EntityEquipmentSlot slot) {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("AttributeName", attribute.getName());
        nbt.setString("Name", modifier.getName());
        nbt.setString("Slot", slot.getName());
        nbt.setDouble("Amount", modifier.getAmount());
        nbt.setInteger("Operation", modifier.getOperation());
        nbt.setLong("UUIDMost", modifier.getID().getMostSignificantBits());
        nbt.setLong("UUIDLeast", modifier.getID().getLeastSignificantBits());

        return nbt;
    }
}
