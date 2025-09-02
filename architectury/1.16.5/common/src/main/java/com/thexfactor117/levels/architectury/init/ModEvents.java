package com.thexfactor117.levels.architectury.init;

import com.thexfactor117.levels.architectury.events.EventCreateWeapon;
import com.thexfactor117.levels.architectury.events.EventInput;
import com.thexfactor117.levels.architectury.events.EventTooltip;
import com.thexfactor117.levels.architectury.events.attributes.EventSoulBound;

public class ModEvents {
    public static void init() {
        EventCreateWeapon.register();
        EventSoulBound.register();
        EventTooltip.register();
        EventInput.register();
    }
}
