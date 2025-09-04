package com.thexfactor117.levels.architectury.network;

import net.minecraft.resources.ResourceLocation;

public class PacketIdentifiers {
    public static final ResourceLocation ATTRIBUTE_SELECTION = new ResourceLocation("levels", "attribute_selection");
    public static final ResourceLocation GET_TYPE_MENU = new ResourceLocation("levels", "type_menu");
    public static final ResourceLocation SEND_ATTRIBUTE_DATA = new ResourceLocation("levels", "attribute_data");

    public static void init() {
        PacketAttributeSelection.register();
        GuiTypeRequest.register();
        GuiTypeResponse.register();
    }
}
