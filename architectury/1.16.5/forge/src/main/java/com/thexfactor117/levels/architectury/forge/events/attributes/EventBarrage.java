package com.thexfactor117.levels.architectury.forge.events.attributes;

import com.thexfactor117.levels.architectury.nbt.NBTHelper;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.nbt.INBT;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventBarrage {
    @SubscribeEvent
    public static void onBowFire(ArrowLooseEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = event.getBow();

        if (player == null || player.level.isClientSide()) {
            return;
        }

        INBT nbt = NBTHelper.toCommon(stack.getOrCreateTag());
        if (!BowAttribute.BARRAGE.hasAttribute(nbt)) {
            return;
        }

        for (int i = 0; i < (int) BowAttribute.BARRAGE.getCalculatedValue(nbt); i++) {
            Arrow arrow = new Arrow(player.level, player);

            float velocity = BowItem.getPowerForTime(event.getCharge()) * 3.0F;

            Vec2 rot = player.getRotationVector();
            arrow.shootFromRotation(player, rot.x, rot.y, 0.0F, velocity, 1.0F);

            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            player.level.addFreshEntity(arrow);
        }
    }
}
