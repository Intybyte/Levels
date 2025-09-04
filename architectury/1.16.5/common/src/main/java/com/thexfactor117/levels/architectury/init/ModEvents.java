package com.thexfactor117.levels.architectury.init;

import com.thexfactor117.levels.architectury.events.EventAttack;
import com.thexfactor117.levels.architectury.events.EventCreateWeapon;
import com.thexfactor117.levels.architectury.events.EventInput;
import com.thexfactor117.levels.architectury.events.EventTooltip;
import com.thexfactor117.levels.architectury.events.attributes.EventBarrage;
import com.thexfactor117.levels.architectury.events.attributes.EventSoulBound;

public class ModEvents {
    public static void init() {
        EventCreateWeapon.register();
        EventAttack.register();

        EventSoulBound.register();
        EventBarrage.register();

        EventTooltip.register();
        EventInput.register();
    }
}
