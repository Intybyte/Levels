package com.thexfactor117.levels.architectury.network;

import com.thexfactor117.levels.architectury.gui.GuiItemInformation;
import com.thexfactor117.levels.architectury.gui.GuiTypeSelection;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeRarity;
import com.thexfactor117.levels.common.networking.AttributeData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

import static com.thexfactor117.levels.architectury.util.NetworkUtil.readString;
import static com.thexfactor117.levels.architectury.util.NetworkUtil.writeString;

@Getter
@AllArgsConstructor
public class GuiTypeResponse {
    private final ItemType type;
    private final List<AttributeData> list;

    public void send(ServerPlayer player) {
        FriendlyByteBuf response = new FriendlyByteBuf(Unpooled.buffer());

        response.writeInt(type.ordinal());
        response.writeInt(list.size());

        for (AttributeData entry : list) {
            writeString(response, entry.getKey());
            writeString(response, entry.getBaseName());
            writeString(response, entry.getTranslationKey());
            response.writeInt(entry.getRarity().ordinal());
            response.writeBoolean(entry.isEnabled());
            response.writeInt(entry.getMaxTier());
            response.writeDouble(entry.getBaseValue());
            response.writeDouble(entry.getMultiplier());
            response.writeInt(entry.getHexColor());
            writeString(response, entry.getColor());
        }

        NetworkManager.sendToPlayer(player, PacketIdentifiers.SEND_ATTRIBUTE_DATA, response);
    }

    public static GuiTypeResponse parse(ByteBuf byteBuf) {
        ItemType type = ItemType.values()[byteBuf.readInt()];
        int size = byteBuf.readInt();
        List<AttributeData> data = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            String key = readString(byteBuf);
            String baseName = readString(byteBuf);
            String translationKey = readString(byteBuf);

            AttributeRarity rarity = AttributeRarity.values()[byteBuf.readInt()];
            boolean enabled = byteBuf.readBoolean();
            int maxTier = byteBuf.readInt();
            double baseValue = byteBuf.readDouble();
            double multiplier = byteBuf.readDouble();
            int hexColor = byteBuf.readInt();
            String color = readString(byteBuf);

            data.add(new AttributeData(
                key, baseName, translationKey, rarity, enabled,
                maxTier, baseValue, multiplier,
                hexColor, color
            ));
        }

        return new GuiTypeResponse(type, data);
    }

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, PacketIdentifiers.SEND_ATTRIBUTE_DATA, (buf, context) -> {
            Minecraft mc = Minecraft.getInstance();
            GuiTypeResponse response = parse(buf);
            mc.execute(() -> mc.setScreen(
                new GuiTypeSelection(response.type, response.list)
            ));

        });
    }
}
