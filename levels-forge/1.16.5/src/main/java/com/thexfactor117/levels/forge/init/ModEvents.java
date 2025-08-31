package com.thexfactor117.levels.forge.init;

import com.thexfactor117.levels.forge.events.EventAttack;
import com.thexfactor117.levels.forge.events.EventCreateWeapon;
import com.thexfactor117.levels.forge.events.EventInput;
import com.thexfactor117.levels.forge.events.EventTooltip;
import com.thexfactor117.levels.forge.events.attributes.EventBarrage;
import com.thexfactor117.levels.forge.events.attributes.EventSoulBound;
import net.minecraftforge.common.MinecraftForge;

/**
 *
 * @author TheXFactor117
 *
 */
public class ModEvents {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new EventInput());
        MinecraftForge.EVENT_BUS.register(new EventCreateWeapon());
        MinecraftForge.EVENT_BUS.register(new EventAttack());
        MinecraftForge.EVENT_BUS.register(new EventTooltip());
        MinecraftForge.EVENT_BUS.register(new EventSoulBound());
        MinecraftForge.EVENT_BUS.register(new EventBarrage());
    }
}
