package com.thexfactor117.levels.forge.leveling;

import com.thexfactor117.levels.common.leveling.exp.ExperienceEditor;
import com.thexfactor117.levels.common.leveling.exp.LevelUpProcessor;
import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.common.nbt.INBTList;
import com.thexfactor117.levels.common.nbt.NBTType;
import com.thexfactor117.levels.forge.nbt.NBTHelper;
import lombok.AllArgsConstructor;
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

    private final ItemStack stack;
    private final NBTTagCompound nbt;
    private final INBT inbt;

    public Experience(ItemStack stack) {
        this.stack = stack;
        this.nbt = stack.getTagCompound();
        this.inbt = NBTHelper.toCommon(nbt);
    }

    @Override
    public INBT getNBT() {
        return this.inbt;
    }

    @AllArgsConstructor
    public class LevelUp implements LevelUpProcessor {
        private final EntityPlayer player;

        @Override
        public ExperienceEditor getExpEditor() {
            return Experience.this;
        }

        @Override
        public INBT getNBT() {
            return Experience.this.inbt;
        }

        @Override
        public void notifyLevelUp() {
            player.sendMessage(
                    new TextComponentString(
                            stack.getDisplayName() + TextFormatting.GRAY + " has leveled up to level " + TextFormatting.GOLD + Experience.this.getLevel() + TextFormatting.GRAY + "!"
                    )
            );
        }

        @Override
        public boolean isWeapon() {
            return stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemAxe;
        }

        @Override
        public void levelUpWeapon() {
            double multiplier = nbt.getDouble("Multiplier");
            NBTTagList taglist = nbt.getTagList("AttributeModifiers", NBTType.COMPOUND.ordinal()); // retrieves our custom Attribute Modifier implementation
            // update damage and attack speed values
            NBTTagCompound damageNbt = taglist.getCompoundTagAt(0);
            NBTTagCompound speedNbt = taglist.getCompoundTagAt(1);

            double damageAmount = damageNbt.getDouble("Amount");
            double speedAmount = speedNbt.getDouble("Amount");

            double newDamage = damageAmount + ((damageAmount * multiplier) / 2);
            double newSpeed = speedAmount - ((speedAmount * multiplier) / 2);

            damageNbt.setDouble("Amount", newDamage);
            speedNbt.setDouble("Amount", newSpeed);
        }

        @Override
        public boolean isArmor() {
            return stack.getItem() instanceof ItemArmor;
        }

        @Override
        public void levelUpArmor() {
            double multiplier = nbt.getDouble("Multiplier");
            NBTTagList taglist = nbt.getTagList("AttributeModifiers", NBTType.COMPOUND.ordinal()); // retrieves our custom Attribute Modifier implementation

            // update armor and armor toughness values
            NBTTagCompound armorNbt = taglist.getCompoundTagAt(0);
            NBTTagCompound toughnessNbt = taglist.getCompoundTagAt(1);

            double armorAmount = armorNbt.getDouble("Amount");
            double toughnessAmount = toughnessNbt.getDouble("Amount");

            double newArmor = armorAmount + ((armorAmount * multiplier) / 2);
            double newToughness = toughnessAmount - ((toughnessAmount * multiplier) / 2);
            armorNbt.setDouble("Amount", newArmor);
            toughnessNbt.setDouble("Amount", newToughness);
        }

        @Override
        public void saveEdits() {
            NBTHelper.saveStackNBT(stack, nbt);
        }
    }
}
