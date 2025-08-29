package com.thexfactor117.levels.bukkit;

import com.thexfactor117.levels.bukkit.events.EventAttack;
import com.thexfactor117.levels.bukkit.events.EventCreateWeapon;
import com.thexfactor117.levels.bukkit.events.attributes.EventBarrage;
import com.thexfactor117.levels.bukkit.events.attributes.EventSoulBound;
import com.thexfactor117.levels.bukkit.ux.cli.Commands;
import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.resources.Localization;
import com.thexfactor117.levels.common.utils.ResourceManipulator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class LevelsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        File data = getDataFolder();
        data.mkdirs();
        ItemType.init();
        Configs.init(data);

        ResourceManipulator.saveAll(data, "assets", LevelsPlugin.class);
        Localization.init(data);

        //add localization stuff
        Configs.getInstance().main
                .process(Localization.getLocalization())
                .initFile();

        PluginManager pm = this.getServer().getPluginManager();

        pm.registerEvents(new EventAttack(), this);
        pm.registerEvents(new EventSoulBound(), this);
        pm.registerEvents(new EventBarrage(), this);

        this.getServer().getScheduler().runTaskTimer(
                this,
                new EventCreateWeapon(),
                1L,
                10L //every half a second
        );

        this.getServer().getPluginCommand("levels").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
