package com.thexfactor117.levels.bukkit.ux.gui;


import com.thexfactor117.levels.bukkit.leveling.Experience;
import com.thexfactor117.levels.bukkit.nbt.NBTHelper;
import com.thexfactor117.levels.bukkit.util.WeaponHelper;
import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.Rarity;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.resources.Localization;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Click;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LevelsGui {

    private static final PagedGui.Builder<Item> dietMenuPrefab;

    private static final Consumer<Click> useless;

    private static final SimpleItem background;
    private static final SimpleItem anotherBackground;


    static {
        useless = click -> click.getEvent().setCancelled(true);
        background = new SimpleItem(
                new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE), useless
        );

        anotherBackground = new SimpleItem(
                new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE), useless
        );

        dietMenuPrefab = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# E # w I w # A #",
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # < # # # > # #"
                )
                .addIngredient('#', background)
                .addIngredient('w', anotherBackground)
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('<', new BackItem())
                .addIngredient('>', new NextItem())
                .setContent(new ArrayList<>())
                .setPageChangeHandlers(new ArrayList<>());
    }

    public static Gui get(ItemStack stack, ItemType type) {
        PagedGui.Builder<Item> prepared = dietMenuPrefab
                .clone()
                .addIngredient('I', new SimpleItem(stack));


        Localization loc = Localization.getLocalization();

        Experience exp = new Experience(stack);
        List<String> expDesc = new ArrayList<>();
        expDesc.add("");

        double multiplier = exp.getPdc().get(WeaponHelper.MULTIPLIER, PersistentDataType.DOUBLE);
        expDesc.add(LegacyTextColor.GRAY + "Levelup Multiplier: " + multiplier);
        expDesc.addAll(exp.displayExp(Localization.getLocalization()));

        Rarity rarity = Rarity.getRarity(exp.getNBT());
        expDesc.add(LegacyTextColor.GRAY + loc.format("levels.misc.rarity") + ": " + rarity.getColor() + rarity);

        expDesc.add("");
        SimpleItem xpViewer = new SimpleItem(
                new ItemBuilder(Material.EXPERIENCE_BOTTLE)
                        .setDisplayName("Level Info")
                        .setLegacyLore(expDesc),
                useless);

        prepared.addIngredient('E', xpViewer);

        List<String> attributeDesc = new ArrayList<>();

        attributeDesc.add("");
        attributeDesc.add(loc.format("levels.misc.attributes.tokens") + ": " + exp.getAttributeTokens());
        attributeDesc.add("");
        attributeDesc.addAll(
                type.displayAttributes(
                        NBTHelper.toCommon(stack.getItemMeta().getPersistentDataContainer())
                )
        );


        SimpleItem attributeViewer = new SimpleItem(
                new ItemBuilder(Material.NETHER_STAR)
                        .setDisplayName("Attribute Info")
                        .setLegacyLore(attributeDesc),
                useless
        );
        prepared.addIngredient('A', attributeViewer);

        List<Item> attributes = new ArrayList<>();
        for (AttributeBase base : type.enabledAttributes()) {
            attributes.add(new AttributeItem(stack, base));
        }

        prepared.setContent(attributes);

        return prepared.build();
    }
}
