package com.thexfactor117.levels.architectury.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thexfactor117.levels.architectury.leveling.Experience;
import com.thexfactor117.levels.architectury.nbt.NBTHelper;
import com.thexfactor117.levels.architectury.util.ItemUtil;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.Rarity;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.exp.ExperienceEditor;
import com.thexfactor117.levels.common.nbt.INBT;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
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
        super(new TextComponent("Item Information"));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void init() {
        selection = new Button(this.width / 2 - 166, 125, 110, 20, new TextComponent("Attribute Selection"), this::actionPerformed);
        this.addButton(selection);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);

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
        drawStrings(poseStack, stack, nbt);
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

        Minecraft.getInstance().setScreen(
            new GuiTypeSelection(currentType)
        );
    }

    /**
     * Draws the strings for the ability selection screen.
     *
     * @param poseStack
     * @param stack
     * @param nbt
     */
    private void drawStrings(PoseStack poseStack, ItemStack stack, CompoundTag nbt) {
        INBT inbt = NBTHelper.toCommon(nbt);
        Rarity rarity = Rarity.getRarity(inbt);
        Experience exp = new Experience(stack);

        drawCenteredString(poseStack, this.font, stack.getDisplayName(), width / 2, 20, rarity.getHex());
        drawString(poseStack, this.font, I18n.get("levels.misc.rarity") + ": " + I18n.get("levels.rarities." + rarity.ordinal()), width / 2 - 50, 40, rarity.getHex());
        drawCenteredString(poseStack, this.font, I18n.get("levels.misc.attributes"), width / 2, 80, 0xFFFFFF);
        drawCenteredString(poseStack, this.font, I18n.get("levels.misc.attributes.tokens") + ": " + exp.getAttributeTokens(), width / 2 - 112, 100, 0xFFFFFF);
        drawCenteredString(poseStack, this.font, I18n.get("levels.misc.attributes.current"), width / 2 + 112, 100, 0xFFFFFF);


        if (exp.isMaxLevel()) {
            drawString(poseStack, this.font, I18n.get("levels.misc.level") + ": " + I18n.get("levels.misc.max"), width / 2 - 50, 50, 0xFFFFFF);
            drawString(poseStack, this.font, I18n.get("levels.misc.experience") + ": " + I18n.get("levels.misc.max"), width / 2 - 50, 60, 0xFFFFFF);
        } else {
            drawString(poseStack, this.font, I18n.get("levels.misc.level") + ": " + exp.getLevel(), width / 2 - 50, 50, 0xFFFFFF);
            drawString(poseStack, this.font, I18n.get("levels.misc.experience") + ": " + exp.getExperience() + " / " + ExperienceEditor.getNextLevelExperience(exp.getLevel()), width / 2 - 50, 60, 0xFFFFFF);
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
                drawCenteredString(poseStack, this.font, attribute.getName(inbt), width / 2 + 112, 115 + (10 * k), attribute.getHexColor());
            }
        }
    }
}
