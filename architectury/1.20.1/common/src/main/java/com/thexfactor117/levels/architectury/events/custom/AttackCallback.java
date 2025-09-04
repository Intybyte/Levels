package com.thexfactor117.levels.architectury.events.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface AttackCallback {
    Event<AttackCallback> EVENT = EventFactory.createLoop();

    void attack(AttackInstance event);

    @Setter
    @Getter
    @AllArgsConstructor
    class AttackInstance {
        private final DamageSource source;
        private final LivingEntity victim;
        private float amount;
    }
}
