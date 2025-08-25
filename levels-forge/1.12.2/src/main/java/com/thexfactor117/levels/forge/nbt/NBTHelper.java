package com.thexfactor117.levels.forge.nbt;

import com.thexfactor117.levels.common.nbt.INBT;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 *
 * @author TheXFactor117
 *
 */
public class NBTHelper {
    public static NBTTagCompound fromCommon(INBT nbt) {
        if (nbt instanceof NBTImpl) {
            return ((NBTImpl) nbt).getNbt();
        }

        throw new RuntimeException("NBTImpl not found");
    }

    public static INBT toCommon(NBTTagCompound nbt) {
        return new NBTImpl(nbt);
    }

    public static NBTTagCompound loadStackNBT(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
    }

    public static void saveStackNBT(ItemStack stack, NBTTagCompound nbt) {
        if (!stack.hasTagCompound() && !nbt.isEmpty()) {
            stack.setTagCompound(nbt);
        }
    }
}
