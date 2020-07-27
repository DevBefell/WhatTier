package me.befell.whatenchant;

import me.befell.whatenchant.main.Main;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = WhatEnchant.MODID, version = WhatEnchant.VERSION)
public class WhatEnchant {
    public static final String MODID = "whatenchant";
    public static final String VERSION = "1.0";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new Main());

    }
}
