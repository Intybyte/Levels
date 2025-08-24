package com.thexfactor117.levels.util;

import com.thexfactor117.levels.client.gui.GuiItemInformation;
import com.thexfactor117.levels.client.gui.GuiTypeSelection;
import com.thexfactor117.levels.leveling.ItemType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 *
 * @author TheXFactor117
 *
 */
public class GuiHandler implements IGuiHandler {
    public static final int ITEM_INFORMATION = 0;
    public static final int WEAPON_ATTRIBUTES = 1;
    public static final int ARMOR_ATTRIBUTES = 2;
    public static final int BOW_ATTRIBUTES = 3;
    public static final int SHIELD_ATTRIBUTES = 4;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ITEM_INFORMATION)
            return new GuiItemInformation();

        if (ID <= 4) {
            ItemType type = ItemType.values()[ID - 1];
            return new GuiTypeSelection(type);
        }

        return null;
    }
}
