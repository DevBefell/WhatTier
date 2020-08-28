package me.befell.whattier.Config;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WTConfig {
    private static String isEnabled;
    private static String isRoman;
    private static String location;
    private static String enablePotion;
    private static String powerColor;
    private static String sharpColor;
    private static String fireTwoColor;
    private static String fireOneColor;
    private static String flameColor;
    private static String protColor;
    private static String size;
    private static String itemSpecific;
    private static String infinityColor;
    private static String knockbackColor;
    private static String globalColor;
    private Configuration cfg;

    public WTConfig() {
        cfg = new Configuration(new File(Minecraft.getMinecraft().mcDataDir, "config/WhatTier.cfg"));
        initConfig();
    }

    public Configuration getConfig() {
        return cfg;
    }

    private void initConfig() {
        syncFromFile();
    }

    public void syncFromFile() {
        updateConfig(true, true);
    }

    public void updateConfig(boolean loadConfigFromFile, boolean readFieldsFromConfig) {
        if (loadConfigFromFile) {
            cfg.load();
        }

        // All the configs
        Property pIsEnabled = cfg.get("client",
                "isEnabled", "enabled", "If WhatTier is enabled");
        Property pIsRoman = cfg.get("client",
                "isRoman", "enabled", "If the text is rendered as Roman Numeral");
        Property pLocation = cfg.get("client",
                "location", "top left", "Where the text is rendered (DO NOT CHANGE THIS YOURSELF)", new String[]{
                        "top left", "top center", "top right",
                        "center left", "center", "center right",
                        "bottom left", "bottom center", "bottom right"});
        Property pEnablePotion = cfg.get("client",
                "enablePotion", "enabled", "If Potion tier is rendered");
        Property pSize = cfg.get("client",
                "size", "0.7", "Size for the tier text");
        Property pItemSpecific = cfg.get("client",
                "itemSpecific", "disabled", "If tiers are shown on the specific item, sharpness on swords, power on bows, etc");
        Property pGlobalColor = cfg.get("color",
                "globalColor", new Color(255, 255, 255).getRGB(), "Global color for text, off to not show, global for global color");
        Property pSharpColor = cfg.get("color",
                "sharpColor", "global", "Color for Sharpness, off to not show, global for global color");
        Property pPowerColor = cfg.get("color",
                "powerColor", "global", "Color for Power, off to not show, global for global color");
        Property pProtColor = cfg.get("color",
                "protColor", "global", "Color for Protection, off to not show, global for global color");
        Property pFireOneColor = cfg.get("color",
                "fireOneColor", "" + new Color(255, 157, 0).getRGB(), " Color for fire aspect one, off to not show, global for global color");
        Property pFireTwoColor = cfg.get("color",
                "fireTwoColor", "" + new Color(255, 0, 51).getRGB(), "Color for fire aspect two, off to not show, global for global color");
        Property pFlameColor = cfg.get("color",
                "flameColor", "" + new Color(255, 157, 0).getRGB(), "Color for flame on bows, off to not show, global for global color");
        Property pInfinityColor = cfg.get("color",
                "infinityColor", "" + new Color(149, 206, 255).getRGB(), "Color for Infinity, off to not show, global for global color");
        Property pKnockbackColor = cfg.get("color",
                "knockbackColor", "" + new Color(11, 252, 3).getRGB(), "Color for knockback, off to not show, global for global color");
        if (readFieldsFromConfig) {
            isEnabled = pIsEnabled.getString();
            isRoman = pIsRoman.getString();
            location = pLocation.getString();
            size = pSize.getString();
            itemSpecific = pItemSpecific.getString();
            enablePotion = pEnablePotion.getString();
            globalColor = pGlobalColor.getString();
            sharpColor = pSharpColor.getString();
            powerColor = pPowerColor.getString();
            fireOneColor = pFireOneColor.getString();
            fireTwoColor = pFireTwoColor.getString();
            flameColor = pFlameColor.getString();
            protColor = pProtColor.getString();
            infinityColor = pInfinityColor.getString();
            knockbackColor = pKnockbackColor.getString();
        }
        List<String> propOrderClient = new ArrayList<>();
        propOrderClient.add(pIsEnabled.getName());
        propOrderClient.add(pIsRoman.getName());
        propOrderClient.add(pLocation.getName());
        propOrderClient.add(pEnablePotion.getName());
        propOrderClient.add(pItemSpecific.getName());
        cfg.setCategoryPropertyOrder("client", propOrderClient);
        List<String> propOrderColor = new ArrayList<>();
        propOrderColor.add(pGlobalColor.getName());
        propOrderColor.add(pSharpColor.getName());
        propOrderColor.add(pPowerColor.getName());
        propOrderColor.add(pFireOneColor.getName());
        propOrderColor.add(pFireTwoColor.getName());
        propOrderColor.add(pFlameColor.getName());
        propOrderColor.add(pProtColor.getName());
        propOrderColor.add(pInfinityColor.getName());
        propOrderColor.add(pKnockbackColor.getName());
        cfg.setCategoryPropertyOrder("color", propOrderColor);
        pIsEnabled.set(isEnabled);
        pIsRoman.set(isRoman);
        pSize.set(size);
        pLocation.set(location);
        pEnablePotion.set(enablePotion);
        pSharpColor.set(sharpColor);
        pPowerColor.set(powerColor);
        pProtColor.set(protColor);
        pGlobalColor.set(globalColor);
        pFireOneColor.set(fireOneColor);
        pFireTwoColor.set(fireTwoColor);
        pFlameColor.set(flameColor);
        pInfinityColor.set(infinityColor);
        pKnockbackColor.set(knockbackColor);
        if (cfg.hasChanged()) {
            cfg.save();
        }
    }
}
