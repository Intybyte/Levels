package com.thexfactor117.levels.architectury.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thexfactor117.levels.architectury.leveling.Experience;
import com.thexfactor117.levels.architectury.nbt.NBTHelper;
import com.thexfactor117.levels.architectury.network.PacketIdentifiers;
import com.thexfactor117.levels.architectury.util.ItemUtil;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.components.config.LevelConfigAttribute;
import com.thexfactor117.levels.common.nbt.INBT;
import io.netty.buffer.Unpooled;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Environment(EnvType.CLIENT)
public class GuiTypeSelection extends Screen {
    private Button[] attributeButtons;
    private final ItemType type;
    private final List<? extends AttributeBase> attributes;

    public GuiTypeSelection(ItemType type, List<? extends AttributeBase> attributes) {
        super(Component.literal("Attribute Selection"));
        this.type = type;
        this.attributes = attributes;
    }

    @Override
    public void init() {
        //
        ItemStack stack = getItemStack();
        if (stack == null) return;


        CompoundTag baseNbt = stack.getOrCreateTag();
        INBT nbt = NBTHelper.toCommon(baseNbt);
        attributeButtons = new Button[attributes.size()];

        for (int i = 0; i < attributeButtons.length; i++) {
            AttributeBase attr = attributes.get(i);
            int tier = attr.getAttributeTier(nbt);

            int maxLevel = LevelConfigAttribute.getMaxLevel(attr);

            String display;
            if (tier == maxLevel) {
                display = attr.getBaseName() + " MAX";
            } else {
                display = attr.getCompleteName(tier + 1);
            }


            attributeButtons[i] = Button.builder(Component.literal(display), this::actionPerformed)
                .bounds(width / 2 - 147, 60 + (i * 20), 75, 20)
                .tooltip(getTooltipLines(i))
                .build();
            this.addRenderableWidget(attributeButtons[i]);
            attributeButtons[i].active = false;
        }
    }

    private @Nullable ItemStack getItemStack() {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return null;
        }

        ItemStack stack = player.getMainHandItem();
        if (stack == null) {
            return null;
        }

        ItemType currentType = ItemUtil.type(stack.getItem());

        if (currentType != type) {
            return null;
        }
        return stack;
    }

    private void drawButtonTooltip(Button button, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        ItemStack stack = getItemStack();
        if (stack == null) return;

        INBT nbt = NBTHelper.toCommon(stack.getOrCreateTag());

        for (int i = 0; i < attributeButtons.length; i++) {
            if (button.equals(attributeButtons[i])) {
                AttributeBase attr = attributes.get(i);
                MutableComponent cmp = Component.empty();
                attr.getUpgradeSummary(nbt, I18n::get).stream()
                    .map(this::createComponent)
                    .forEach(cmp::append);

                guiGraphics.renderTooltip(this.font, cmp, mouseX + 3, mouseY + 3);
            }
        }
    }

    private Tooltip getTooltipLines(int pos) {
        ItemStack stack = getItemStack();
        if (stack == null) return Tooltip.create(Component.empty());

        INBT nbt = NBTHelper.toCommon(stack.getOrCreateTag());

        AttributeBase attr = attributes.get(pos);
        MutableComponent cmp = Component.literal("");
        attr.getUpgradeSummary(nbt, I18n::get).stream()
            .map(this::createComponent)
            .forEach(cmp::append);

        return Tooltip.create(cmp);
    }

    // renderBackground
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        ItemStack stack = getItemStack();
        if (stack == null) return;

        CompoundTag baseNbt = stack.getOrCreateTag();

        ItemType currentType = ItemUtil.type(stack.getItem());
        if (baseNbt == null || currentType != type) {
            return;
        }

        Experience exp = new Experience(stack);
        guiGraphics.drawCenteredString(this.font, I18n.get("levels.misc.attributes"), width / 2, 20, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, I18n.get("levels.misc.attributes.tokens") + ": " + exp.getAttributeTokens(), width / 2 - 112, 40, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, I18n.get("levels.misc.attributes.current"), width / 2 + 112, 40, 0xFFFFFF);

        int k = -1;

        INBT nbt = NBTHelper.toCommon(baseNbt);
        for (AttributeBase attribute : attributes) {
            if (attribute.hasAttribute(nbt)) {
                k++;
                guiGraphics.drawCenteredString(this.font, attribute.getName(nbt), width / 2 + 112, 60 + (10 * k), attribute.getHexColor());
            }
        }

        displayButtons(stack);
    }


    protected void actionPerformed(Button button) {
        ItemStack stack = getItemStack();
        if (stack == null) return;

        ItemType currentType = ItemUtil.type(stack.getItem());
        Experience exp = new Experience(stack);
        if (exp.getAttributeTokens() <= 0) {
            return;
        }

        if (currentType != type) {
            return;
        }

        for (int i = 0; i < attributeButtons.length; i++) {
            if (button == attributeButtons[i]) {
                FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
                byteBuf.writeInt(i); // write attribute chosen
                NetworkManager.sendToServer(PacketIdentifiers.ATTRIBUTE_SELECTION, byteBuf);
            }
                //Levels.network.sendToServer(new PacketAttributeSelection(i));
        }
    }

    /**
     * Determines which buttons need to be enabled.
     * @param stack
     */
    private void displayButtons(ItemStack stack) {
        Experience exp = new Experience(stack);

        int tokens = exp.getAttributeTokens();
        if (tokens <= 0) {
            for (Button attributeButton : attributeButtons) {
                attributeButton.active = false;
            }
            return;
        }

        CompoundTag nbtBase = stack.getTag();
        INBT nbt = NBTHelper.toCommon(nbtBase);
        for (int i = 0; i < attributeButtons.length; i++) {
            AttributeBase attribute = attributes.get(i);
            boolean hasAttribute = attribute.hasAttribute(nbt);
            boolean isMaxLevel = attribute.getAttributeTier(nbt) >= LevelConfigAttribute.getMaxLevel(attribute);
            int cost = attribute.getRarity().getCost();

            boolean shouldEnable;

            if (isMaxLevel) {
                // Never enable max level attributes
                shouldEnable = false;
            } else if (hasAttribute) {
                // Always enable existing attribute unless at tier 3
                shouldEnable = true;
            } else {
                // Only allow adding attributes if tokens >= cost
                shouldEnable = tokens >= cost;
            }

            attributeButtons[i].active = shouldEnable;
        }
    }

    private Component createComponent(String arg) {
        return Component.literal(arg + "\n");
    }
}
