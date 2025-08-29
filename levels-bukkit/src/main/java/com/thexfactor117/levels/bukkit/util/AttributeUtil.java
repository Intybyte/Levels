package com.thexfactor117.levels.bukkit.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class AttributeUtil {
    public static void editAttribute(
            ItemMeta meta,
            Attribute attribute,
            UUID targetUUID,
            UnaryOperator<AttributeModifier> modifierFunction
    ) {
        Collection<AttributeModifier> modifiers = meta.getAttributeModifiers(attribute);
        if (modifiers == null) return;

        AttributeModifier oldModifier = null;

        for (AttributeModifier modif : modifiers) {
            if (modif.getUniqueId().equals(targetUUID)) {
                oldModifier = modif;
                break;
            }
        }

        if (oldModifier == null) return;

        AttributeModifier newModifier = modifierFunction.apply(oldModifier);
        if (newModifier != null) {
            meta.removeAttributeModifier(attribute, oldModifier);
            meta.addAttributeModifier(attribute, newModifier);
        }

    }
}
