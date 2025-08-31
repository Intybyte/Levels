package com.thexfactor117.levels.forge.events;

import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.Rarity;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.exp.ExperienceEditor;
import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.forge.leveling.Experience;
import com.thexfactor117.levels.forge.nbt.NBTHelper;
import com.thexfactor117.levels.forge.util.I18nUtil;
import com.thexfactor117.levels.forge.util.ItemUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TheXFactor117
 *
 */
public class EventTooltip {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTooltip(ItemTooltipEvent event) {
        ArrayList<String> tooltip = (ArrayList<String>) event.getToolTip();
        ItemStack stack = event.getItemStack();
        NBTTagCompound baseNbt = NBTHelper.loadStackNBT(stack);

        if (stack == null || baseNbt == null) {
            return;
        }

        ItemType type = ItemUtil.type(stack.getItem());
        if (type == null && !(stack.getItem() instanceof ItemTool)) {
            return;
        }

        INBT nbt = NBTHelper.toCommon(baseNbt);
        Rarity rarity = Rarity.getRarity(nbt);
        if (rarity != Rarity.DEFAULT) {
            addTooltips(tooltip, stack, baseNbt);
        }
    }

    private void addTooltips(ArrayList<String> tooltip, ItemStack stack, NBTTagCompound baseNbt) {
        INBT nbt = NBTHelper.toCommon(baseNbt);
        Rarity rarity = Rarity.getRarity(nbt);

        NBTTagList taglist = baseNbt.getTagList("AttributeModifiers", 10);
        NBTTagCompound damageNbt = taglist.getCompoundTagAt(0);
        NBTTagCompound speedNbt = taglist.getCompoundTagAt(1);
        DecimalFormat format = new DecimalFormat("#.##");

        tooltip.add("");

        if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemAxe) {
            // damage and attack speed
            tooltip.add(TextFormatting.BLUE + "+" + format.format(damageNbt.getDouble("Amount")) + " Damage");
            tooltip.add(TextFormatting.BLUE + "+" + format.format(speedNbt.getDouble("Amount") + 4) + " Attack Speed");
        } else if (stack.getItem() instanceof ItemArmor) {
            // armor and armor toughness
            tooltip.add(TextFormatting.BLUE + "+" + format.format(damageNbt.getDouble("Amount")) + " Armor");
            tooltip.add(TextFormatting.BLUE + "+" + format.format(speedNbt.getDouble("Amount")) + " Armor Toughness");
        }

        if (!(stack.getItem() instanceof ItemShield || stack.getItem() instanceof ItemBow)) {
            tooltip.add("");
        }

        // rarity
        tooltip.add(rarity.getDisplay(I18n::format)); // rarity

        Experience exp = new Experience(stack);
        tooltip.addAll(exp.displayExp(I18n::format));

        // durability
        if (baseNbt.getInteger("Unbreakable") == 1)
            tooltip.add(TextFormatting.GRAY + I18n.format("levels.misc.durability") + ": " + I18n.format("levels.misc.durability.unlimited"));
        else
            tooltip.add(TextFormatting.GRAY + I18n.format("levels.misc.durability") + ": " + (stack.getMaxDamage() - stack.getItemDamage()) + " / " + stack.getMaxDamage());

        tooltip.add("");

        // attributes
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            tooltip.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC + I18n.format("levels.misc.attributes.shift"));
            tooltip.add("");
            return;
        }

        // Process Shift additional info

        tooltip.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC + I18n.format("levels.misc.attributes"));

        ItemType type = ItemUtil.type(stack.getItem());
        if (type == null) {
            tooltip.add("");
            return;
        }

        tooltip.addAll(type.displayAttributes(nbt));
    }
}
