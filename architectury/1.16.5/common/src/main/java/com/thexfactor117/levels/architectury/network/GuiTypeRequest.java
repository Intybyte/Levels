package com.thexfactor117.levels.architectury.network;

import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.networking.AttributeData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class GuiTypeRequest {
    private final ItemType type;

    public void send() {
        FriendlyByteBuf send = new FriendlyByteBuf(Unpooled.buffer());
        send.writeInt(type.ordinal());
        NetworkManager.sendToServer(PacketIdentifiers.GET_TYPE_MENU, send);
    }

    public static GuiTypeRequest parse(ByteBuf byteBuf) {
        int pos = byteBuf.readInt();
        ItemType type = ItemType.values()[pos];
        return new GuiTypeRequest(type);
    }

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, PacketIdentifiers.GET_TYPE_MENU, (input, context) -> {
            GuiTypeRequest typeRequest = parse(input);
            ItemType type = typeRequest.type;

            List<AttributeData> data = type.enabledAttributes().stream().map(AttributeBase::netWrapper).collect(Collectors.toList());

            GuiTypeResponse guiTypeResponse = new GuiTypeResponse(type, data);
            guiTypeResponse.send((ServerPlayer) context.getPlayer());
        });
    }
}
