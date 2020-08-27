package me.befell.whattier;

import me.befell.whattier.Command.WTCommand;
import me.befell.whattier.Config.WTConfig;
import me.befell.whattier.Gui.GuiMain;
import me.befell.whattier.Main.Events;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;

@Mod(name = "WhatTier", modid = WhatTier.MODID, version = WhatTier.VERSION, clientSideOnly = true)
public class WhatTier {
    public static final String MODID = "whattier";
    public static final String VERSION = "1.5";
    private final WTConfig config = new WTConfig();
    private boolean openMenu;
    public Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent ignored) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Events(this));
        ClientCommandHandler.instance.registerCommand(new WTCommand(this));
        config.syncFromFile();
    }

    @SubscribeEvent
    public void OnTick(TickEvent.ClientTickEvent e) {
        if (openMenu) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiMain(this));
            openMenu = false;
        }
    }

    public WTConfig getConfig() {
        return this.config;
    }

    public Logger getLogger() {
        return logger;
    }

    public void openMenu() {
        this.openMenu = true;
        logger.info(openMenu);
    }

}
