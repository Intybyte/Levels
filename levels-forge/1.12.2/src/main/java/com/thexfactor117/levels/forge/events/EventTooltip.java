package com.thexfactor117.levels.forge.events;

import com.thexfactor117.levels.common.leveling.exp.ExperienceEditor;
import com.thexfactor117.levels.forge.leveling.Experience;
import com.thexfactor117.levels.forge.leveling.ItemType;
import com.thexfactor117.levels.forge.leveling.Rarity;
import com.thexfactor117.levels.forge.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.forge.util.NBTHelper;
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
        NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

        if (stack != null && nbt != null) {
            if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemBow || stack.getItem() instanceof ItemShield || stack.getItem() instanceof ItemTool) {
                Rarity rarity = Rarity.getRarity(nbt);

                if (rarity != Rarity.DEFAULT) {
                    addTooltips(tooltip, stack, nbt);
                }
            }
        }
    }

    private void addTooltips(ArrayList<String> tooltip, ItemStack stack, NBTTagCompound nbt) {
        Rarity rarity = Rarity.getRarity(nbt);

        NBTTagList taglist = nbt.getTagList("AttributeModifiers", 10);
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
        tooltip.add(rarity.getColor() + TextFormatting.ITALIC + rarity.getName()); // rarity

        Experience exp = new Experience(null, stack);
        int level = exp.getLevel();
        // level
        if (exp.isMaxLevel())
            tooltip.add(TextFormatting.GRAY + I18n.format("levels.misc.level") + ": " + I18n.format("levels.misc.max")); // max level
        else
            tooltip.add(TextFormatting.GRAY + I18n.format("levels.misc.level") + ": " + level); // level

        // experience
        if (exp.isMaxLevel())
            tooltip.add(TextFormatting.GRAY + I18n.format("levels.misc.experience") + ": " + I18n.format("levels.misc.max"));
        else
            tooltip.add(TextFormatting.GRAY + I18n.format("levels.misc.experience") + ": " + exp.getExperience() + " / " + ExperienceEditor.getNextLevelExperience(level));

        // durability
        if (nbt.getInteger("Unbreakable") == 1)
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

        ItemType type = ItemType.of(stack.getItem());
        if (type == null) {
            tooltip.add("");
            return;
        }

        List<? extends AttributeBase> attributes = type.attributes();
        for (AttributeBase attribute : attributes) {
            if (attribute.hasAttribute(nbt))
                tooltip.add(" " + attribute.getColor() + attribute.getName(nbt));
        }

        tooltip.add("");
    }
}
