package com.thexfactor117.levels.forge.network;

import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeRarity;
import com.thexfactor117.levels.common.networking.AttributeData;
import com.thexfactor117.levels.forge.client.gui.GuiTypeSelection;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuiPackets {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request implements IMessage {
        private ItemType type;

        @Override
        public void fromBytes(ByteBuf buf) {
            type = ItemType.values()[buf.readInt()];
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(type.ordinal());
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response implements IMessage {
        private ItemType type;
        private List<AttributeData> data;

        @Override
        public void fromBytes(ByteBuf buf) {
            type = ItemType.values()[buf.readInt()];
            int size = buf.readInt();
            data = new ArrayList<>(size);

            for (int i = 0; i < size; i++) {
                String key = readString(buf);
                String baseName = readString(buf);
                String translationKey = readString(buf);

                AttributeRarity rarity = AttributeRarity.values()[buf.readInt()];
                boolean enabled = buf.readBoolean();
                int maxTier = buf.readInt();
                double baseValue = buf.readDouble();
                double multiplier = buf.readDouble();
                int hexColor = buf.readInt();
                String color = readString(buf);

                data.add(new AttributeData(
                    key, baseName, translationKey, rarity, enabled,
                    maxTier, baseValue, multiplier,
                    hexColor, color
                ));
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(type.ordinal());
            buf.writeInt(data.size());

            for (AttributeData entry : data) {
                writeString(buf, entry.getKey());
                writeString(buf, entry.getBaseName());
                writeString(buf, entry.getTranslationKey());
                buf.writeInt(entry.getRarity().ordinal());
                buf.writeBoolean(entry.isEnabled());
                buf.writeInt(entry.getMaxTier());
                buf.writeDouble(entry.getBaseValue());
                buf.writeDouble(entry.getMultiplier());
                buf.writeInt(entry.getHexColor());
                writeString(buf, entry.getColor());
            }
        }

        private static void writeString(ByteBuf buf, String str) {
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        }

        private static String readString(ByteBuf buf) {
            int len = buf.readInt();
            if (len < 0 || len > 32767) throw new IllegalArgumentException("Invalid string length: " + len);
            byte[] bytes = new byte[len];
            buf.readBytes(bytes);
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }


    public static class HandlerServer implements IMessageHandler<Request, Response> {

        @Override
        public Response onMessage(Request message, MessageContext ctx) {
            List<AttributeBase> attributeBaseList = message.type.enabledAttributes();
            List<AttributeData> dataToSend = attributeBaseList.stream().map(AttributeBase::netWrapper).collect(Collectors.toList());
            return new Response(message.type, dataToSend);
        }
    }

    public static class HandlerClient implements IMessageHandler<GuiPackets.Response, IMessage> {

        @Override
        public IMessage onMessage(Response message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft mc = Minecraft.getMinecraft();

                // Replace this with your actual GUI class
                mc.displayGuiScreen(new GuiTypeSelection(message.type, message.getData()));
            });

            return null;
        }
    }

}
