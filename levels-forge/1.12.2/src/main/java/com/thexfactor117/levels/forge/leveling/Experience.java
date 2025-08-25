package com.thexfactor117.levels.forge.leveling;

import com.thexfactor117.levels.common.leveling.exp.ExperienceEditor;
import com.thexfactor117.levels.forge.nbt.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

/**
 *
 * @author TheXFactor117
 *
 */
public class Experience implements ExperienceEditor {

    private final EntityPlayer player;
    private final ItemStack stack;
    private final NBTTagCompound nbt;

    public Experience(EntityPlayer player, ItemStack stack) {
        this.player = player;
        this.stack = stack;
        this.nbt = stack.getTagCompound();
    }

    /**
     * Levels up the current weapon/armor to the next level, assuming it is not at max level.
     */
    @Override
    public void levelUp() {
        if (player == null) {
            throw new RuntimeException("levelUp can't be called with null player, if you see this message open a bug report");
        }

        NBTTagCompound nbt = stack.getTagCompound();
        //JsonToNBT

        if (nbt == null) {
            return;
        }

        while (!this.isMaxLevel() && this.getExperience() >= ExperienceEditor.getNextLevelExperience(this.getLevel())) {
            this.setLevel(this.getLevel() + 1); // increase level by one
            this.addAttributeTokens(1);

            player.sendMessage(new TextComponentString(stack.getDisplayName() + TextFormatting.GRAY + " has leveled up to level " + TextFormatting.GOLD + this.getLevel() + TextFormatting.GRAY + "!"));

            if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemAxe) {
                // update damage and attack speed values
                NBTTagList taglist = nbt.getTagList("AttributeModifiers", 10); // retrieves our custom Attribute Modifier implementation
                NBTTagCompound damageNbt = taglist.getCompoundTagAt(0);
                NBTTagCompound speedNbt = taglist.getCompoundTagAt(1);
                double newDamage = damageNbt.getDouble("Amount") + ((damageNbt.getDouble("Amount") * nbt.getDouble("Multiplier")) / 2);
                double newSpeed = speedNbt.getDouble("Amount") - ((speedNbt.getDouble("Amount") * nbt.getDouble("Multiplier")) / 2);
                damageNbt.setDouble("Amount", newDamage);
                speedNbt.setDouble("Amount", newSpeed);
            } else if (stack.getItem() instanceof ItemArmor) {
                // update armor and armor toughness values
                NBTTagList taglist = nbt.getTagList("AttributeModifiers", 10); // retrieves our custom Attribute Modifier implementation
                NBTTagCompound armorNbt = taglist.getCompoundTagAt(0);
                NBTTagCompound toughnessNbt = taglist.getCompoundTagAt(1);
                double newArmor = armorNbt.getDouble("Amount") + ((armorNbt.getDouble("Amount") * nbt.getDouble("Multiplier")) / 2);
                double newToughness = toughnessNbt.getDouble("Amount") - ((toughnessNbt.getDouble("Amount") * nbt.getDouble("Multiplier")) / 2);
                armorNbt.setDouble("Amount", newArmor);
                toughnessNbt.setDouble("Amount", newToughness);
            }

            NBTHelper.saveStackNBT(stack, nbt);
        }
    }

    /**
     * Returns the level of the current weapon/armor.
     * @return level of the item
     */
    @Override
    public int getLevel() {
        return nbt != null ? nbt.getInteger("LEVEL") : 1;
    }

    /**
     * Sets the level of the current weapon/armor.
     * @param level new level
     */
    @Override
    public void setLevel(int level) {
        if (nbt == null) {
            return;
        }

        if (level > 0)
            nbt.setInteger("LEVEL", level);
        else
            nbt.removeTag("LEVEL");
    }

    /**
     * Returns the experience of the current weapon/armor.
     * @return experience
     */
    @Override
    public int getExperience() {
        return nbt != null ? nbt.getInteger("EXPERIENCE") : 1;
    }

    /**
     * Sets the experience of the current weapon/armor.
     */
    @Override
    public void setExperience(int experience) {
        if (nbt == null) {
            return;
        }

        if (experience > 0)
            nbt.setInteger("EXPERIENCE", experience);
        else
            nbt.removeTag("EXPERIENCE");
    }

    /**
     * Sets the amount of Attribute Tokens the specific NBT tag has.
     * @param tokens
     */
    @Override
    public void setAttributeTokens(int tokens) {
        if (nbt == null) {
            return;
        }

        if (tokens > 0)
            nbt.setInteger("TOKENS", tokens);
        else
            nbt.removeTag("TOKENS");
    }

    /**
     * Returns how many Attribute Tokens the specific NBT tag has.
     * @return
     */
    @Override
    public int getAttributeTokens() {
        return nbt != null ? nbt.getInteger("TOKENS") : 0;
    }
}
