package com.thexfactor117.levels.architectury.network;

import com.thexfactor117.levels.architectury.leveling.Experience;
import com.thexfactor117.levels.architectury.nbt.NBTHelper;
import com.thexfactor117.levels.architectury.util.ItemUtil;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.AnyAttributes;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.components.config.LevelConfigAttribute;
import com.thexfactor117.levels.common.nbt.INBT;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PacketAttributeSelection {
    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, PacketIdentifiers.ATTRIBUTE_SELECTION, (buffer, ctx) -> {
            Player player = ctx.getPlayer();
            int index = buffer.readInt();

            if (player == null) return;

            ItemStack stack = player.getMainHandItem();
            if (stack == null) return;

            ItemType type = ItemUtil.type(stack.getItem());
            if (type == null) return;

            CompoundTag baseNbt = stack.getOrCreateTag();

            Experience exp = new Experience(stack);

            List<? extends AttributeBase> attributeList = type.enabledAttributes();
            if (index < 0 || attributeList.size() <= index) return;

            AttributeBase attribute = attributeList.get(index);

            INBT nbt = NBTHelper.toCommon(baseNbt);

            int maxLevel = LevelConfigAttribute.getMaxLevel(attribute);
            int newTier = attribute.getAttributeTier(nbt) + 1;
            int cost = attribute.hasAttribute(nbt) ? 1 : attribute.getRarity().getCost();

            boolean isEnough = cost <= exp.getAttributeTokens();
            if (!isEnough || newTier > maxLevel) {
                return; //maybe notify something weird is going on?
            }

            attribute.setAttributeTier(nbt, newTier);
            exp.addAttributeTokens(-cost);

            if (!attribute.hasAttribute(nbt) && AnyAttributes.UNBREAKABLE.equals(attribute)) {
                nbt.setInt("Unbreakable", 1);
            }
        });
    }
}
