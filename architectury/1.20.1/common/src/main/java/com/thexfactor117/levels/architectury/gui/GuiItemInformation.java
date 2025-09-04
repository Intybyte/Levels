package com.thexfactor117.levels.architectury.gui;

import com.thexfactor117.levels.architectury.leveling.Experience;
import com.thexfactor117.levels.architectury.nbt.NBTHelper;
import com.thexfactor117.levels.architectury.network.GuiTypeRequest;
import com.thexfactor117.levels.architectury.util.ItemUtil;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.Rarity;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.exp.ExperienceEditor;
import com.thexfactor117.levels.common.nbt.INBT;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 *
 * @author TheXFactor117
 *
 */
public class GuiItemInformation extends Screen {
    private Button selection;

    public GuiItemInformation() {
        super(Component.literal("Item Information"));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void init() {
        selection = Button.builder(Component.literal("Attribute Selection"), this::actionPerformed)
            .bounds(this.width / 2 - 166, 125, 110, 20)
            .build();

        this.addRenderableWidget(selection);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (stack == null) {
            return;
        }

        ItemType currentType = ItemUtil.type(stack.getItem());

        if (currentType == null) {
            return;
        }

        CompoundTag nbt = stack.getOrCreateTag();
        drawStrings(guiGraphics, stack, nbt);
    }

    @Environment(EnvType.CLIENT)
    protected void actionPerformed(Button button) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (stack == null) {
            return;
        }

        ItemType currentType = ItemUtil.type(stack.getItem());
        if (currentType == null) {
            return;
        }

        if (button != selection) {
            return;
        }

        new GuiTypeRequest(currentType).send();
    }

    /**
     * Draws the strings for the ability selection screen.
     *
     * @param guiGraphics
     * @param stack
     * @param nbt
     */
    private void drawStrings(GuiGraphics guiGraphics, ItemStack stack, CompoundTag nbt) {
        INBT inbt = NBTHelper.toCommon(nbt);
        Rarity rarity = Rarity.getRarity(inbt);
        Experience exp = new Experience(stack);


        guiGraphics.drawCenteredString(this.font, stack.getDisplayName(), width / 2, 20, rarity.getHex());
        guiGraphics.drawString(this.font, I18n.get("levels.misc.rarity") + ": " + I18n.get("levels.rarities." + rarity.ordinal()), width / 2 - 50, 40, rarity.getHex());
        guiGraphics.drawCenteredString(this.font, I18n.get("levels.misc.attributes"), width / 2, 80, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, I18n.get("levels.misc.attributes.tokens") + ": " + exp.getAttributeTokens(), width / 2 - 112, 100, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, I18n.get("levels.misc.attributes.current"), width / 2 + 112, 100, 0xFFFFFF);


        if (exp.isMaxLevel()) {
            guiGraphics.drawString(this.font, I18n.get("levels.misc.level") + ": " + I18n.get("levels.misc.max"), width / 2 - 50, 50, 0xFFFFFF);
            guiGraphics.drawString(this.font, I18n.get("levels.misc.experience") + ": " + I18n.get("levels.misc.max"), width / 2 - 50, 60, 0xFFFFFF);
        } else {
            guiGraphics.drawString(this.font, I18n.get("levels.misc.level") + ": " + exp.getLevel(), width / 2 - 50, 50, 0xFFFFFF);
            guiGraphics.drawString(this.font, I18n.get("levels.misc.experience") + ": " + exp.getExperience() + " / " + ExperienceEditor.getNextLevelExperience(exp.getLevel()), width / 2 - 50, 60, 0xFFFFFF);
        }

        int k = -1;
        ItemType type = ItemUtil.type(stack.getItem());
        if (type == null) {
            return;
        }

        List<? extends AttributeBase> attributes = type.enabledAttributes();

        for (AttributeBase attribute : attributes) {
            if (attribute.hasAttribute(inbt)) {
                k++;
                guiGraphics.drawCenteredString(this.font, attribute.getName(inbt), width / 2 + 112, 115 + (10 * k), attribute.getHexColor());
            }
        }
    }
}
