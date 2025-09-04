package com.thexfactor117.levels.forge.util;

import com.thexfactor117.levels.forge.client.gui.GuiItemInformation;
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

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ITEM_INFORMATION)
            return new GuiItemInformation();

        return null;
    }
}
