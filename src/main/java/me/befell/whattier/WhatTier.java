package me.befell.whattier;

import me.befell.whattier.Main.Main;
import me.befell.whattier.Main.UpdateChecker;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;


@Mod(name = "WhatTier",modid = WhatTier.MODID, version = WhatTier.VERSION, clientSideOnly = true)
public class WhatTier {
    public static final String MODID = "whattier";
    public static final String VERSION = "1.0";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new Main());
        MinecraftForge.EVENT_BUS.register(new UpdateChecker());
    }
}
