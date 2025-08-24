package com.thexfactor117.levels.forge.network;

import com.thexfactor117.levels.forge.leveling.Experience;
import com.thexfactor117.levels.forge.leveling.ItemType;
import com.thexfactor117.levels.forge.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.forge.util.NBTHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

/**
 *
 * @author TheXFactor117
 *
 */
public class PacketAttributeSelection implements IMessage {
    private int index;

    public PacketAttributeSelection() {
    }

    public PacketAttributeSelection(int index) {
        this.index = index;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        index = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(index);
    }

    public static class Handler implements IMessageHandler<PacketAttributeSelection, IMessage> {
        @Override
        public IMessage onMessage(final PacketAttributeSelection message, final MessageContext ctx) {

            IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.getEntityWorld();
            mainThread.addScheduledTask(getTask(ctx, message));

            return null;
        }

        private Runnable getTask(final MessageContext ctx, final PacketAttributeSelection message) {
            return () -> {
                EntityPlayer player = ctx.getServerHandler().player;
                ItemStack stack = player.inventory.getCurrentItem();
                NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

                if (player == null || stack == null || nbt == null) {
                    return;
                }

                ItemType type = ItemType.of(stack.getItem());
                if (type == null) {
                    return;
                }

                List<? extends AttributeBase> attributeList = type.list();
                AttributeBase attribute = attributeList.get(message.index);
                if (attribute.hasAttribute(nbt)) {
                    attribute.setAttributeTier(nbt, attribute.getAttributeTier(nbt) + 1);
                    Experience.setAttributeTokens(nbt, Experience.getAttributeTokens(nbt) - 1);
                } else {
                    int cost = attribute.getRarity().getCost();

                    attribute.addAttribute(nbt);
                    Experience.setAttributeTokens(nbt, Experience.getAttributeTokens(nbt) - cost);

                    if (attribute.getAttributeKey().contains("Unbreakable"))
                        nbt.setInteger("Unbreakable", 1);
                }
            };
        }
    }
}
