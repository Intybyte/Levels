package com.thexfactor117.levels.forge.client.gui;

import com.thexfactor117.levels.common.leveling.attributes.components.config.LevelConfigAttribute;
import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.forge.Levels;
import com.thexfactor117.levels.forge.leveling.Experience;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.forge.network.PacketAttributeSelection;
import com.thexfactor117.levels.forge.nbt.NBTHelper;
import com.thexfactor117.levels.forge.util.ItemUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;

public class GuiTypeSelection extends GuiScreen {
    private GuiButton[] attributeButtons;
    private final ItemType type;
    private final List<? extends AttributeBase> attributes;

    public GuiTypeSelection(ItemType type) {
        this.type = type;
        this.attributes = type.enabledAttributes();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initGui() {
        EntityPlayer player = this.mc.player;
        ItemStack stack = player.inventory.getCurrentItem();
        NBTTagCompound baseNbt = NBTHelper.loadStackNBT(stack);

        ItemType currentType = ItemUtil.type(stack.getItem());

        if (player == null || stack == null || baseNbt == null || currentType != type) {
            return;
        }

        INBT nbt = NBTHelper.toCommon(baseNbt);
        attributeButtons = new GuiButton[attributes.size()];

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

            attributeButtons[i] = new GuiButton(i, width / 2 - 147, 60 + (i * 20), 75, 20, display);
            this.buttonList.add(attributeButtons[i]);
            attributeButtons[i].enabled = false;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        EntityPlayer player = this.mc.player;
        ItemStack stack = player.inventory.getCurrentItem();
        NBTTagCompound baseNbt = NBTHelper.loadStackNBT(stack);

        ItemType currentType = ItemUtil.type(stack.getItem());

        if (player == null || stack == null || baseNbt == null || currentType != type) {
            return;
        }

        Experience exp = new Experience(stack);
        drawCenteredString(fontRenderer, I18n.format("levels.misc.attributes"), width / 2, 20, 0xFFFFFF);
        drawCenteredString(fontRenderer, I18n.format("levels.misc.attributes.tokens") + ": " + exp.getAttributeTokens(), width / 2 - 112, 40, 0xFFFFFF);
        drawCenteredString(fontRenderer, I18n.format("levels.misc.attributes.current"), width / 2 + 112, 40, 0xFFFFFF);

        int k = -1;

        INBT nbt = NBTHelper.toCommon(baseNbt);
        for (AttributeBase attribute : attributes) {
            if (attribute.hasAttribute(nbt)) {
                k++;
                drawCenteredString(fontRenderer, attribute.getName(nbt), width / 2 + 112, 60 + (10 * k), attribute.getHexColor());
            }
        }

        displayButtons(stack);
        drawTooltips(baseNbt, mouseX, mouseY);
    }

    @SideOnly(Side.CLIENT)
    @Override
    @ParametersAreNonnullByDefault
    protected void actionPerformed(GuiButton button) throws IOException {
        EntityPlayerSP player = mc.player;
        ItemStack stack = player.inventory.getCurrentItem();
        NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

        ItemType currentType = ItemUtil.type(stack.getItem());

        if (player == null || stack == null || nbt == null) {
            return;
        }

        Experience exp = new Experience(stack);
        if (exp.getAttributeTokens() <= 0) {
            return;
        }

        if (currentType != type) {
            return;
        }

        for (int i = 0; i < attributeButtons.length; i++) {
            if (button == attributeButtons[i])
                Levels.network.sendToServer(new PacketAttributeSelection(i));
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
            for (GuiButton attributeButton : attributeButtons) {
                attributeButton.enabled = false;
            }
            return;
        }

        NBTTagCompound nbtBase = stack.getTagCompound();
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

            attributeButtons[i].enabled = shouldEnable;
        }
    }

    private void drawTooltips(NBTTagCompound nbtBase, int mouseX, int mouseY) {
        INBT nbt = NBTHelper.toCommon(nbtBase);
        for (int i = 0; i < attributeButtons.length; i++) {
            HoverChecker checker = new HoverChecker(attributeButtons[i], 0);

            if (!checker.checkHover(mouseX, mouseY)) {
                continue;
            }

            AttributeBase attr = attributes.get(i);
            List<String> list = attr.getUpgradeSummary(nbt, I18n::format);

            drawHoveringText(list, mouseX + 3, mouseY + 3);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
