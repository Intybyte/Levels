package com.thexfactor117.levels.forge.client.gui;

import com.thexfactor117.levels.forge.Levels;
import com.thexfactor117.levels.forge.config.Config;
import com.thexfactor117.levels.forge.leveling.Experience;
import com.thexfactor117.levels.forge.leveling.Rarity;
import com.thexfactor117.levels.forge.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.forge.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.forge.leveling.attributes.ShieldAttribute;
import com.thexfactor117.levels.forge.leveling.attributes.WeaponAttribute;
import com.thexfactor117.levels.forge.util.GuiHandler;
import com.thexfactor117.levels.forge.util.NBTHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

/**
 *
 * @author TheXFactor117
 *
 */
public class GuiItemInformation extends GuiScreen {
    private GuiButton selection;

    @SideOnly(Side.CLIENT)
    @Override
    public void initGui() {
        selection = new GuiButton(0, this.width / 2 - 166, 125, 110, 20, "Attribute Selection");

        this.buttonList.add(selection);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        EntityPlayer player = this.mc.player;

        if (player == null) {
            return;
        }

        ItemStack stack = player.inventory.getCurrentItem();
        if (stack == null) {
            return;
        }

        NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);
        if (nbt != null) {
            drawStrings(stack, nbt);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    @ParametersAreNonnullByDefault
    protected void actionPerformed(GuiButton button) throws IOException {
        EntityPlayerSP player = mc.player;

        if (player == null) {
            return;
        }

        ItemStack stack = player.inventory.getCurrentItem();
        if (stack == null) {
            return;
        }

        if (button != selection) {
            return;
        }

        if (stack.getItem() instanceof ItemSword) {
            player.openGui(Levels.instance, GuiHandler.WEAPON_ATTRIBUTES, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
        } else if (stack.getItem() instanceof ItemArmor) {
            player.openGui(Levels.instance, GuiHandler.ARMOR_ATTRIBUTES, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
        } else if (stack.getItem() instanceof ItemBow) {
            player.openGui(Levels.instance, GuiHandler.BOW_ATTRIBUTES, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
        } else if (stack.getItem() instanceof ItemShield) {
            player.openGui(Levels.instance, GuiHandler.SHIELD_ATTRIBUTES, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
        } else if (stack.getItem() instanceof ItemTool) {
            //player.openGui(Levels.instance, GuiHandler., player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
        }
    }

    /**
     * Draws the strings for the ability selection screen.
     * @param stack
     * @param nbt
     */
    private void drawStrings(ItemStack stack, NBTTagCompound nbt) {
        Rarity rarity = Rarity.getRarity(nbt);
        drawCenteredString(fontRenderer, stack.getDisplayName(), width / 2, 20, rarity.getHex());
        drawString(fontRenderer, I18n.format("levels.misc.rarity") + ": " + rarity.getName(), width / 2 - 50, 40, rarity.getHex());
        drawCenteredString(fontRenderer, I18n.format("levels.misc.attributes"), width / 2, 80, 0xFFFFFF);
        drawCenteredString(fontRenderer, I18n.format("levels.misc.attributes.tokens") + ": " + Experience.getAttributeTokens(nbt), width / 2 - 112, 100, 0xFFFFFF);
        drawCenteredString(fontRenderer, I18n.format("levels.misc.attributes.current"), width / 2 + 112, 100, 0xFFFFFF);

        if (Experience.getLevel(nbt) == Config.maxLevel) {
            drawString(fontRenderer, I18n.format("levels.misc.level") + ": " + I18n.format("levels.misc.max"), width / 2 - 50, 50, 0xFFFFFF);
            drawString(fontRenderer, I18n.format("levels.misc.experience") + ": " + I18n.format("levels.misc.max"), width / 2 - 50, 60, 0xFFFFFF);
        } else {
            drawString(fontRenderer, I18n.format("levels.misc.level") + ": " + Experience.getLevel(nbt), width / 2 - 50, 50, 0xFFFFFF);
            drawString(fontRenderer, I18n.format("levels.misc.experience") + ": " + Experience.getExperience(nbt) + " / " + Experience.getNextLevelExperience(Experience.getLevel(nbt)), width / 2 - 50, 60, 0xFFFFFF);
        }

        int k = -1;

        if (stack.getItem() instanceof ItemSword) {
            for (int i = 0; i < WeaponAttribute.WEAPON_ATTRIBUTES.size(); i++) {
                if (WeaponAttribute.WEAPON_ATTRIBUTES.get(i).hasAttribute(nbt)) {
                    k++;
                    drawCenteredString(fontRenderer, WeaponAttribute.WEAPON_ATTRIBUTES.get(i).getName(nbt), width / 2 + 112, 115 + (10 * k), WeaponAttribute.WEAPON_ATTRIBUTES.get(i).getHexColor());
                }
            }
        } else if (stack.getItem() instanceof ItemTool) {

        } else if (stack.getItem() instanceof ItemBow) {
            for (int i = 0; i < BowAttribute.BOW_ATTRIBUTES.size(); i++) {
                if (BowAttribute.BOW_ATTRIBUTES.get(i).hasAttribute(nbt)) {
                    k++;
                    drawCenteredString(fontRenderer, BowAttribute.BOW_ATTRIBUTES.get(i).getName(nbt), width / 2 + 112, 115 + (10 * k), BowAttribute.BOW_ATTRIBUTES.get(i).getHexColor());
                }
            }
        } else if (stack.getItem() instanceof ItemArmor) {
            for (int i = 0; i < ArmorAttribute.ARMOR_ATTRIBUTES.size(); i++) {
                if (ArmorAttribute.ARMOR_ATTRIBUTES.get(i).hasAttribute(nbt)) {
                    k++;
                    drawCenteredString(fontRenderer, ArmorAttribute.ARMOR_ATTRIBUTES.get(i).getName(nbt), width / 2 + 112, 115 + (10 * k), ArmorAttribute.ARMOR_ATTRIBUTES.get(i).getHexColor());
                }
            }
        } else if (stack.getItem() instanceof ItemShield) {
            for (int i = 0; i < ShieldAttribute.SHIELD_ATTRIBUTES.size(); i++) {
                if (ShieldAttribute.SHIELD_ATTRIBUTES.get(i).hasAttribute(nbt)) {
                    k++;
                    drawCenteredString(fontRenderer, ShieldAttribute.SHIELD_ATTRIBUTES.get(i).getName(nbt), width / 2 + 112, 115 + (10 * k), ShieldAttribute.SHIELD_ATTRIBUTES.get(i).getHexColor());
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
