package com.thexfactor117.levels.architectury.events;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.InputConstants;
import com.thexfactor117.levels.architectury.leveling.Experience;
import com.thexfactor117.levels.architectury.nbt.NBTHelper;
import com.thexfactor117.levels.architectury.util.ItemUtil;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.Rarity;
import com.thexfactor117.levels.common.nbt.INBT;
import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventTooltip {
    public static void register() {
        ClientTooltipEvent.ITEM.register((stack, lines, tooltipFlag) -> {
            CompoundTag baseNbt = stack.getOrCreateTag();

            ItemType type = ItemUtil.type(stack.getItem());
            if (type == null) {
                return;
            }

            INBT nbt = NBTHelper.toCommon(baseNbt);
            Rarity rarity = Rarity.getRarity(nbt);
            if (rarity != Rarity.DEFAULT) {
                addTooltips(lines, stack, baseNbt);
            }
        });
    }

    private static void addTooltips(List<Component> componentTooltip, ItemStack stack, CompoundTag baseNbt) {
        INBT nbt = NBTHelper.toCommon(baseNbt);
        Rarity rarity = Rarity.getRarity(nbt);


        DecimalFormat format = new DecimalFormat("#.##");

        List<String> tooltip = new ArrayList<>();
        tooltip.add("");

        if (stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem) {
            Multimap<Attribute, AttributeModifier> attributesWeapon = stack.getAttributeModifiers(EquipmentSlot.MAINHAND);

            // damage and attack speed
            double damage = attributesWeapon.get(Attributes.ATTACK_DAMAGE).stream().mapToDouble(AttributeModifier::getAmount).sum();
            double speed = attributesWeapon.get(Attributes.ATTACK_SPEED).stream().mapToDouble(AttributeModifier::getAmount).sum();

            tooltip.add(ChatFormatting.BLUE + "+" + format.format(damage) + " Damage");
            tooltip.add(ChatFormatting.BLUE + "+" + format.format(speed + 4.0) + " Attack Speed");
        } else if (stack.getItem() instanceof ArmorItem) {
            ArmorItem armorItem = (ArmorItem) stack.getItem();
            EquipmentSlot slot = armorItem.getEquipmentSlot();

            Multimap<Attribute, AttributeModifier> attributesArmor = stack.getAttributeModifiers(slot);

            double armor = attributesArmor.get(Attributes.ARMOR).stream().mapToDouble(AttributeModifier::getAmount).sum();
            double armorToughness = attributesArmor.get(Attributes.ARMOR_TOUGHNESS).stream().mapToDouble(AttributeModifier::getAmount).sum();
            //attributesWeapon.get(Attributes.ARMOR).stream().mapToDouble(AttributeModifier::getAmount).sum();

            // armor and armor toughness
            tooltip.add(ChatFormatting.BLUE + "+" + format.format(armor) + " Armor");
            tooltip.add(ChatFormatting.BLUE + "+" + format.format(armorToughness) + " Armor Toughness");
        }

        if (!(stack.getItem() instanceof ShieldItem || stack.getItem() instanceof BowItem)) {
            tooltip.add("");
        }

        // rarity
        tooltip.add(
            rarity.getDisplay(I18n::get)
        );

        Experience exp = new Experience(stack);
        tooltip.addAll(exp.displayExp(I18n::get));

        // durability
        if (baseNbt.getInt("Unbreakable") == 1)
            tooltip.add(ChatFormatting.GRAY + I18n.get("levels.misc.durability") + ": " + I18n.get("levels.misc.durability.unlimited"));
        else
            tooltip.add(ChatFormatting.GRAY + I18n.get("levels.misc.durability") + ": " + (stack.getMaxDamage() - stack.getDamageValue()) + " / " + stack.getMaxDamage());

        tooltip.add("");

        long windowHandle = Minecraft.getInstance().getWindow().getWindow();

        boolean leftShift = InputConstants.isKeyDown(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT);
        boolean rightShift = InputConstants.isKeyDown(windowHandle, GLFW.GLFW_KEY_RIGHT_SHIFT);

        // attributesWeapon
        if (!leftShift && !rightShift) {
            tooltip.add(ChatFormatting.GRAY + "" + ChatFormatting.ITALIC + I18n.get("levels.misc.attributes.shift"));
            tooltip.add("");
            componentTooltip.addAll(
                tooltip.stream().map(Component::literal).collect(Collectors.toList())
            );
            return;
        }

        // Process Shift additional info

        tooltip.add(ChatFormatting.GRAY + "" + ChatFormatting.ITALIC + I18n.get("levels.misc.attributes"));

        ItemType type = ItemUtil.type(stack.getItem());
        if (type == null) {
            tooltip.add("");
            componentTooltip.addAll(
                tooltip.stream().map(Component::literal).collect(Collectors.toList())
            );
            return;
        }

        tooltip.addAll(type.displayAttributes(nbt));

        componentTooltip.addAll(
            tooltip.stream().map(Component::literal).collect(Collectors.toList())
        );
    }
}
