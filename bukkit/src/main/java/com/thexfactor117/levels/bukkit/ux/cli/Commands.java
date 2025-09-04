package com.thexfactor117.levels.bukkit.ux.cli;

import com.thexfactor117.levels.bukkit.util.ItemUtil;
import com.thexfactor117.levels.bukkit.util.StackUtil;
import com.thexfactor117.levels.bukkit.ux.gui.LevelsGui;
import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.leveling.ItemType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }


        Player player = (Player) sender;
        ItemStack itemHeld = player.getInventory().getItemInMainHand();
        ItemType type = ItemUtil.type(itemHeld.getType());

        if (type == null) {
            player.sendMessage(LegacyTextColor.RED + "Invalid item.");
            return true;
        }


        Gui gui = LevelsGui.get(itemHeld, type);
        Window window = Window.single()
                .setViewer(player)
                .setTitle(StackUtil.getName(itemHeld) + " Stats")
                .setGui(gui)
                .build();

        window.open();
        return true;
    }
}
