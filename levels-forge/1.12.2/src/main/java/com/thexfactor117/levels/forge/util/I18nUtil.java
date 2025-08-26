package com.thexfactor117.levels.forge.util;

import com.thexfactor117.levels.common.leveling.Rarity;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class I18nUtil {
    
    public static String getRarity(Rarity rarity) {
        return I18n.format("levels.rarities." + rarity.ordinal());
    }
}
