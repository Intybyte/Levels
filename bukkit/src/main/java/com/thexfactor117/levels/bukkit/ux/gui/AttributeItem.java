package com.thexfactor117.levels.bukkit.ux.gui;

import com.thexfactor117.levels.bukkit.leveling.Experience;
import com.thexfactor117.levels.bukkit.nbt.NBTHelper;
import com.thexfactor117.levels.common.leveling.attributes.AnyAttributes;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.components.config.LevelConfigAttribute;
import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.common.resources.Localization;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.xenondevs.invui.item.Click;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.function.Consumer;

public class AttributeItem extends SimpleItem {
    public AttributeItem(ItemStack main, AttributeBase attributeBase) {
        super(
                getProvider(main, attributeBase),
                consumer(main, attributeBase)
        );
    }

    public static ItemProvider getProvider(ItemStack main, AttributeBase attributeBase) {
        Material material = switch (attributeBase.getRarity()) {
            case UNCOMMON -> Material.GREEN_WOOL;
            case RARE -> Material.BLUE_WOOL;
            case LEGENDARY -> Material.YELLOW_WOOL;
        };

        int maxLevel = LevelConfigAttribute.getMaxLevel(attributeBase);

        INBT nbt = NBTHelper.toCommon(main.getItemMeta().getPersistentDataContainer());
        String display;
        int tier = attributeBase.getAttributeTier(nbt);
        if (tier == maxLevel) {
            display = attributeBase.getBaseName() + " MAX";
        } else {
            display = attributeBase.getCompleteName(tier + 1);
        }

        return new ItemBuilder(material)
                .setDisplayName(display)
                .setLegacyLore(
                        attributeBase.getUpgradeSummary(nbt, Localization.getLocalization())
                );
    }

    private static Consumer<Click> consumer(ItemStack stack, AttributeBase attribute) {
        return (click) -> {

            Experience exp = new Experience(stack);

            int tokens = exp.getAttributeTokens();
            if (tokens <= 0) {
                return;
            }

            INBT nbt = exp.getNBT();
            int currentLevel = attribute.getAttributeTier(nbt);
            boolean hasAttribute = attribute.hasAttribute(nbt);
            boolean isMaxLevel = currentLevel >= LevelConfigAttribute.getMaxLevel(attribute);
            int cost = attribute.getRarity().getCost();

            boolean shouldUpgrade;

            if (isMaxLevel) {
                // Never enable max level attributes
                shouldUpgrade = false;
            } else if (hasAttribute) {
                // Always enable existing attribute unless at tier max
                cost = 1;
                shouldUpgrade = true;
            } else {
                // Only allow adding attributes if tokens >= cost
                shouldUpgrade = tokens >= cost;
            }

            if (!shouldUpgrade) return;

            int newTier = currentLevel + 1;
            attribute.setAttributeTier(nbt, newTier);
            exp.addAttributeTokens(-cost);

            ItemMeta meta = exp.getMeta();
            if (!attribute.hasAttribute(nbt) && AnyAttributes.UNBREAKABLE.equals(attribute)) {
                meta.setUnbreakable(true);
            }

            stack.setItemMeta(meta);
        };
    }
}
