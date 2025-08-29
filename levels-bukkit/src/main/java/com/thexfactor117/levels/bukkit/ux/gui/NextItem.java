package com.thexfactor117.levels.bukkit.ux.gui;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class NextItem extends PageItem {
    public NextItem() {
        super(true);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> pagedGui) {
        PagedGui<?> gui = this.getGui();
        String message;
        if (!gui.hasPreviousPage()) {
            message = "No more pages";
        } else {
            int curr = gui.getCurrentPage();
            int next = curr + 1;
            message = curr + " -> " + next;
        }

        return new ItemBuilder(Material.ARROW)
                .setDisplayName("Next Page")
                .addLoreLines(message);
    }
}