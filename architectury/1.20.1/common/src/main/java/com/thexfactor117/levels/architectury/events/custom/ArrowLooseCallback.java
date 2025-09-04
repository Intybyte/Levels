package com.thexfactor117.levels.architectury.events.custom;


import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ArrowLooseCallback {
    Event<ArrowLooseCallback> EVENT = EventFactory.createLoop();

    void onLoose(Player player, ItemStack bow, int charge);
}
