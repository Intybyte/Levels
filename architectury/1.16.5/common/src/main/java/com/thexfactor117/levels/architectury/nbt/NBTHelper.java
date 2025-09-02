package com.thexfactor117.levels.architectury.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NBTHelper {
    public static NBTImpl toCommon(CompoundTag nbt) {
        if (nbt == null) return null;

        return new NBTImpl(nbt);
    }

    public static void saveStackNBT(ItemStack stack, CompoundTag nbt) {
        if (!stack.hasTag() && !nbt.isEmpty()) {
            stack.setTag(nbt);
        }
    }
}
