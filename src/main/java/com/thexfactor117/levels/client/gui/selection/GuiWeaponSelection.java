package com.thexfactor117.levels.client.gui.selection;

import com.thexfactor117.levels.Levels;
import com.thexfactor117.levels.leveling.Experience;
import com.thexfactor117.levels.leveling.Rarity;
import com.thexfactor117.levels.leveling.attributes.AttributeRarity;
import com.thexfactor117.levels.leveling.attributes.WeaponAttribute;
import com.thexfactor117.levels.network.PacketAttributeSelection;
import com.thexfactor117.levels.util.NBTHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TheXFactor117
 *
 */
public class GuiWeaponSelection extends GuiScreen {
    private GuiButton[] attributes;

    @SideOnly(Side.CLIENT)
    @Override
    public void initGui() {
        EntityPlayer player = this.mc.player;
        ItemStack stack = player.inventory.getCurrentItem();
        NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

        if (player != null && stack != null && nbt != null && stack.getItem() instanceof ItemSword) {
            attributes = new GuiButton[WeaponAttribute.WEAPON_ATTRIBUTES.size()];

            for (int i = 0; i < attributes.length; i++) {
                attributes[i] = new GuiButton(i, width / 2 - 147, 60 + (i * 20), 75, 20, WeaponAttribute.WEAPON_ATTRIBUTES.get(i).getName(nbt));
                this.buttonList.add(attributes[i]);
                attributes[i].enabled = false;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        EntityPlayer player = this.mc.player;
        ItemStack stack = player.inventory.getCurrentItem();
        NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

        if (player != null && stack != null && nbt != null && stack.getItem() instanceof ItemSword) {
            drawCenteredString(fontRenderer, I18n.format("levels.misc.attributes"), width / 2, 20, 0xFFFFFF);
            drawCenteredString(fontRenderer, I18n.format("levels.misc.attributes.tokens") + ": " + Experience.getAttributeTokens(nbt), width / 2 - 112, 40, 0xFFFFFF);
            drawCenteredString(fontRenderer, I18n.format("levels.misc.attributes.current"), width / 2 + 112, 40, 0xFFFFFF);

            int k = -1;

            for (int i = 0; i < WeaponAttribute.WEAPON_ATTRIBUTES.size(); i++) {
                if (WeaponAttribute.WEAPON_ATTRIBUTES.get(i).hasAttribute(nbt)) {
                    k++;
                    drawCenteredString(fontRenderer, WeaponAttribute.WEAPON_ATTRIBUTES.get(i).getName(nbt), width / 2 + 112, 60 + (10 * k), WeaponAttribute.WEAPON_ATTRIBUTES.get(i).getHex());
                }
            }

            displayButtons(nbt);
            drawTooltips(nbt, mouseX, mouseY);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        EntityPlayerSP player = mc.player;
        ItemStack stack = player.inventory.getCurrentItem();
        NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

        if (player != null && stack != null && nbt != null) {
            if (Experience.getAttributeTokens(nbt) > 0) {
                if (stack.getItem() instanceof ItemSword) {
                    for (int i = 0; i < attributes.length; i++) {
                        if (button == attributes[i]) {
                            Levels.network.sendToServer(new PacketAttributeSelection(i));
                        }
                    }
                }
            }
        }
    }

    /**
     * Determines which buttons need to be enabled.
     * @param nbt
     */
    private void displayButtons(NBTTagCompound nbt) {
        if (Experience.getAttributeTokens(nbt) > 0) {
            for (int i = 0; i < attributes.length; i++) {
                ArrayList<WeaponAttribute> list = WeaponAttribute.WEAPON_ATTRIBUTES;

                /*
                 * Enable Uncommon attributes UNLESS already added to nbt AND are not already tier 3.
                 * Enable ALL attributes that have already been added UNLESS they are at tier 3.
                 */
                if (Experience.getAttributeTokens(nbt) == 1) {
                    if (list.get(i).getRarity() == AttributeRarity.UNCOMMON && list.get(i).getAttributeTier(nbt) != 3)
                        attributes[i].enabled = true;

                    if (list.get(i).hasAttribute(nbt) && list.get(i).getAttributeTier(nbt) != 3)
                        attributes[i].enabled = true;
                    else if (list.get(i).hasAttribute(nbt) && list.get(i).getAttributeTier(nbt) == 3)
                        attributes[i].enabled = false;
                }

                /*
                 * Enable UNCOMMON AND RARE attributes UNLESS already added to nbt AND are not already tier 3.
                 * Enable ALL attributes that have already been added UNLESS they are at tier 3.
                 */
                if (Experience.getAttributeTokens(nbt) == 2) {
                    if ((list.get(i).getRarity() == AttributeRarity.RARE || list.get(i).getRarity() == AttributeRarity.UNCOMMON) && !list.get(i).hasAttribute(nbt))
                        attributes[i].enabled = true;

                    if (list.get(i).hasAttribute(nbt) && list.get(i).getAttributeTier(nbt) != 3)
                        attributes[i].enabled = true;
                    else if (list.get(i).hasAttribute(nbt) && list.get(i).getAttributeTier(nbt) == 3)
                        attributes[i].enabled = false;
                } else {
                    if (list.get(i).getRarity() == AttributeRarity.RARE && !list.get(i).hasAttribute(nbt))
                        attributes[i].enabled = false;

                    if (list.get(i).hasAttribute(nbt) && list.get(i).getAttributeTier(nbt) != 3)
                        attributes[i].enabled = true;
                    else if (list.get(i).hasAttribute(nbt) && list.get(i).getAttributeTier(nbt) == 3)
                        attributes[i].enabled = false;
                }

                /*
                 * Enable ALL attributes UNLESS already added to nbt AND are not already tier 3.
                 * Enable ALL attributes that have already been added.
                 */
                if (Experience.getAttributeTokens(nbt) >= 3) {
                    if (!list.get(i).hasAttribute(nbt))
                        attributes[i].enabled = true;

                    if (list.get(i).hasAttribute(nbt) && list.get(i).getAttributeTier(nbt) != 3)
                        attributes[i].enabled = true;
                    else if (list.get(i).hasAttribute(nbt) && list.get(i).getAttributeTier(nbt) == 3)
                        attributes[i].enabled = false;
                } else {
                    if (list.get(i).getRarity() == AttributeRarity.LEGENDARY && !list.get(i).hasAttribute(nbt))
                        attributes[i].enabled = false;

                    if (list.get(i).hasAttribute(nbt) && list.get(i).getAttributeTier(nbt) != 3)
                        attributes[i].enabled = true;
                    else if (list.get(i).hasAttribute(nbt) && list.get(i).getAttributeTier(nbt) == 3)
                        attributes[i].enabled = false;
                }
            }
        } else {
            for (int i = 0; i < attributes.length; i++) {
                attributes[i].enabled = false;
            }
        }
    }

    private void drawTooltips(NBTTagCompound nbt, int mouseX, int mouseY) {
        for (int i = 0; i < attributes.length; i++) {
            HoverChecker checker = new HoverChecker(attributes[i], 0);

            if (checker.checkHover(mouseX, mouseY)) {
                int cost = 1;

                if (WeaponAttribute.WEAPON_ATTRIBUTES.get(i).hasAttribute(nbt)) {
                    cost = WeaponAttribute.WEAPON_ATTRIBUTES.get(i).getRarity().getCost();
                }

                if (WeaponAttribute.WEAPON_ATTRIBUTES.get(i).getAttributeTier(nbt) == 3) cost = 0;

                List<String> list = new ArrayList<>();
                list.add(WeaponAttribute.WEAPON_ATTRIBUTES.get(i).getColor() + WeaponAttribute.WEAPON_ATTRIBUTES.get(i).getName(nbt));
                list.add(TextFormatting.GRAY + "Cost: " + cost + " token(s)");
                list.add("");
                list.add(I18n.format("levels.attributes.weapons.info." + WeaponAttribute.WEAPON_ATTRIBUTES.get(i).ordinal()));
                list.add("");
                list.add("Tiers:");
                list.add(" I - " + WeaponAttribute.WEAPON_ATTRIBUTES.get(i).getColor() + I18n.format("levels.attributes.weapons.info." + WeaponAttribute.WEAPON_ATTRIBUTES.get(i).ordinal() + ".tier1"));
                list.add(" II - " + WeaponAttribute.WEAPON_ATTRIBUTES.get(i).getColor() + I18n.format("levels.attributes.weapons.info." + WeaponAttribute.WEAPON_ATTRIBUTES.get(i).ordinal() + ".tier2"));
                list.add(" III - " + WeaponAttribute.WEAPON_ATTRIBUTES.get(i).getColor() + I18n.format("levels.attributes.weapons.info." + WeaponAttribute.WEAPON_ATTRIBUTES.get(i).ordinal() + ".tier3"));
                drawHoveringText(list, mouseX + 3, mouseY + 3);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
