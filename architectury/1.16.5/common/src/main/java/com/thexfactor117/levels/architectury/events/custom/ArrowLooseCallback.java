package com.thexfactor117.levels.architectury.events.custom;


import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ArrowLooseCallback {
    Event<ArrowLooseCallback> EVENT = EventFactory.createLoop();

    void onLoose(Player player, ItemStack bow, int charge);
}
