package com.thexfactor117.levels.forge.nbt;

import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.common.nbt.INBTList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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

    public static NBTImpl toCommon(NBTTagCompound nbt) {
        if (nbt == null) return null;

        return new NBTImpl(nbt);
    }

    public static NBTListImpl toCommon(NBTTagList nbt) {
        if (nbt == null) return null;

        return new NBTListImpl(nbt);
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
