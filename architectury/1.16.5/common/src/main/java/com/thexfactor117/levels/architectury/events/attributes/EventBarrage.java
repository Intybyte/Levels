package com.thexfactor117.levels.architectury.events.attributes;

import com.thexfactor117.levels.architectury.events.custom.ArrowLooseCallback;
import com.thexfactor117.levels.architectury.nbt.NBTHelper;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.nbt.INBT;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.phys.Vec2;

public class EventBarrage {
    public static void register() {
        ArrowLooseCallback.EVENT.register(((player, bow, charge) -> {
            if (player == null || player.level.isClientSide()) {
                return;
            }

            INBT nbt = NBTHelper.toCommon(bow.getOrCreateTag());
            if (!BowAttribute.BARRAGE.hasAttribute(nbt)) {
                return;
            }

            for (int i = 0; i < (int) BowAttribute.BARRAGE.getCalculatedValue(nbt); i++) {
                Arrow arrow = new Arrow(player.level, player);

                float velocity = BowItem.getPowerForTime(charge) * 3.0F;

                Vec2 rot = player.getRotationVector();
                arrow.shootFromRotation(player, rot.x, rot.y, 0.0F, velocity, 1.0F);

                arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
                player.level.addFreshEntity(arrow);
            }
        }));
    }
}
