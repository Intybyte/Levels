package com.thexfactor117.levels.bukkit.ux.gui;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class BackItem extends PageItem {
    public BackItem() {
        super(false);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> pagedGui) {
        PagedGui<?> gui = this.getGui();
        String message;
        if (!gui.hasPreviousPage()) {
            message = "Previous Page";
        } else {
            int curr = gui.getCurrentPage();
            int next = curr - 1;
            message = next + " <- " + curr;
        }

        return new ItemBuilder(Material.ARROW)
                .setDisplayName("Previous Page")
                .addLoreLines(message);
    }
}