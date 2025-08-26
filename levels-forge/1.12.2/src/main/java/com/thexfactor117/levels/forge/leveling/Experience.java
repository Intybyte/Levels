package com.thexfactor117.levels.forge.leveling;

import com.thexfactor117.levels.common.leveling.exp.ExperienceEditor;
import com.thexfactor117.levels.common.leveling.exp.LevelUpProcessor;
import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.forge.nbt.NBTHelper;
import lombok.AllArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
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
        public boolean isArmor() {
            return stack.getItem() instanceof ItemArmor;
        }

        @Override
        public void saveEdits() {
            NBTHelper.saveStackNBT(stack, nbt);
        }
    }
}
