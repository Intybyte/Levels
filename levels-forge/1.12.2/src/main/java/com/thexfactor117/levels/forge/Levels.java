package com.thexfactor117.levels.forge;

import com.thexfactor117.levels.common.config.MainConfig;
import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.forge.init.ModEvents;
import com.thexfactor117.levels.common.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.leveling.attributes.ShieldAttribute;
import com.thexfactor117.levels.common.leveling.attributes.WeaponAttribute;
import com.thexfactor117.levels.forge.network.PacketAttributeSelection;
import com.thexfactor117.levels.forge.network.PacketMythicSound;
import com.thexfactor117.levels.forge.proxies.CommonProxy;
import com.thexfactor117.levels.forge.util.GuiHandler;
import com.thexfactor117.levels.forge.util.Reference;
import lombok.Getter;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 *
 * @author TheXFactor117
 *
 * Levels 3
 *
 */
@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, updateJSON = Reference.UPDATE_URL)
public class Levels {
    @SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
    public static CommonProxy proxy;
    @Instance(Reference.MODID)
    public static Levels instance;
    public static final Logger LOGGER = LogManager.getLogger("Levels");
    public static SimpleNetworkWrapper network;
    @Getter
    private static File configDir;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory() + "/" + Reference.MODID);
        configDir.mkdirs();
        Configs.init(configDir);
        Configs cfg = Configs.getInstance();

        cfg.attributes
                .processEnum(ArmorAttribute.class)
                .processEnum(BowAttribute.class)
                .processEnum(ShieldAttribute.class)
                .processEnum(WeaponAttribute.class)
                .initFile();

        cfg.main
                .process(new MainConfig())
                .initFile();

        //Config.init(configDir);

        ModEvents.register();
        proxy.preInit();

        network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);
        network.registerMessage(PacketAttributeSelection.Handler.class, PacketAttributeSelection.class, 0, Side.SERVER);
        network.registerMessage(PacketMythicSound.Handler.class, PacketMythicSound.class, 1, Side.CLIENT);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

}
