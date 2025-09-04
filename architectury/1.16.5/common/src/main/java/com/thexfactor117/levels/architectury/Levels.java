package com.thexfactor117.levels.architectury;

import com.thexfactor117.levels.architectury.init.ModEvents;
import com.thexfactor117.levels.architectury.network.PacketIdentifiers;
import com.thexfactor117.levels.common.leveling.ItemType;

public final class Levels {
    public static final String MOD_ID = "levels";

    public static void init() {
        ItemType.init();
        ModEvents.init();

        PacketIdentifiers.init();
    }
}
