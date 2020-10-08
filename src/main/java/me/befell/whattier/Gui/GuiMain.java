package me.befell.whattier.Gui;

import me.befell.whattier.WhatTier;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class GuiMain extends GuiScreen {
    private final WhatTier mod;
    private GuiCustomButton modState;
    private GuiCustomButton isRoman;
    private GuiCustomButton location;
    private GuiCustomButton potion;
    private GuiCustomButton powerColor;
    private GuiCustomButton sharpColor;
    private GuiCustomButton fireTwoColor;
    private GuiCustomButton fireOneColor;
    private GuiCustomButton flameColor;
    private GuiCustomButton protColor;
    private GuiCustomButton infinityColor;
    private GuiCustomButton itemSpecific;
    private GuiCustomButton globalColor;
    private GuiCustomButton knockbackColor;
    private GuiCustomSlider size;
    private final Configuration cfg;
    private final ConfigCategory cfgclient;
    private final ConfigCategory cfgcolor;
    private final Logger logger;
    private final Map<Integer, String> texts = new HashMap<>();

    public GuiMain(WhatTier mod) {
        this.mod = mod;
        logger = mod.getLogger();
        cfg = mod.getConfig().getConfig();
        cfgclient = cfg.getCategory("client");
        cfgcolor = cfg.getCategory("color");
    }

    public int getRowPos(int rowNumber) {
        return height / 4 + (24 * rowNumber - 24) - 16;
    }

    public int getCenter() {
        return width / 2;
    }

    public List<String> toList(String text) {
        if (text.contains("/n")) {
            return Arrays.asList(text.split("/n"));
        }
        List<String> al = new ArrayList<>();
        al.add(text);
        return al;
    }

    private static String opposite(String state) {
        switch (state) {
            case "disabled": {
                return "enabled";
            }
            case "enabled": {
                return "disabled";
            }
            default: {
                return "null";
            }
        }

    }

    @Override
    public void onGuiClosed() {
        cfgclient.get("size").set("" + size.GetValue());
        // save to the file once closed
        cfg.save();
    }

    private static String getColorString(String state) {
        switch (state) {
            case "disabled": {
                return "\u00a7cdisabled";
            }
            case "enabled": {
                return "\u00a7aenabled";
            }
            default: {
                return "\u00a7cnull";
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        // If hovers draw the custom tooltip
        for (GuiButton guiButton : buttonList) {
            if (guiButton.isMouseOver() && !texts.get(guiButton.id).equals("") && texts.containsKey(guiButton.id)) {
                GuiToolTip.drawToolTip(toList(texts.get(guiButton.id)), mouseX - 4, mouseY + 3, width, height, -1, mc.fontRendererObj);
            }
        }
    }

    private void updateLabels() {
        boolean ifTrue = cfgclient.get("isEnabled").getString().equals("enabled");
        isRoman.enabled = ifTrue;
        location.enabled = ifTrue;
        potion.enabled = ifTrue;
        powerColor.enabled = ifTrue;
        sharpColor.enabled = ifTrue;
        fireTwoColor.enabled = ifTrue;
        fireOneColor.enabled = ifTrue;
        flameColor.enabled = ifTrue;
        protColor.enabled = ifTrue;
        infinityColor.enabled = ifTrue;
        knockbackColor.enabled = ifTrue;
        itemSpecific.enabled = ifTrue;
        globalColor.enabled = ifTrue;
        size.enabled = ifTrue;
    }

    @Override
    public void initGui() {
        // Gets it from the file meaning it is always sync
        mod.getConfig().syncFromFile();
        // Adds the buttons
        buttonList.add(modState = new GuiCustomButton(0, getCenter() - 153, getRowPos(1), 150, 20, "Mod State: " + getColorString(cfgclient.get("isEnabled").getString())));
        buttonList.add(location = new GuiCustomButton(13, getCenter() + 2, getRowPos(1), 150, 20, "Location: " + "\u00a77" + cfgclient.get("location").getString()));
        buttonList.add(potion = new GuiCustomButton(2, getCenter() - 153, getRowPos(2), 150, 20, "Potion: " + getColorString(cfgclient.get("enablePotion").getString())));
        buttonList.add(isRoman = new GuiCustomButton(1, getCenter() + 2, getRowPos(2), 150, 20, "Roman Numeral: " + getColorString(cfgclient.get("isRoman").getString())));
        buttonList.add(globalColor = new GuiCustomButton(3, getCenter() - 153, getRowPos(3), 150, 20, "Global Color: \u00a77" + cfgcolor.get("globalColor").getString()));
        buttonList.add(itemSpecific = new GuiCustomButton(11, getCenter() + 2, getRowPos(3), 150, 20, "Item Specific: " + getColorString(cfgclient.get("itemSpecific").getString())));
        buttonList.add(sharpColor = new GuiCustomButton(4, getCenter() - 153, getRowPos(4), 150, 20, "Sharpness: \u00a77" + cfgcolor.get("sharpColor").getString()));
        buttonList.add(powerColor = new GuiCustomButton(12, getCenter() + 2, getRowPos(4), 150, 20, "Power: \u00a77" + cfgcolor.get("powerColor").getString()));
        buttonList.add(fireOneColor = new GuiCustomButton(5, getCenter() - 153, getRowPos(5), 150, 20, "Fire Aspect 1: \u00a77" + cfgcolor.get("fireOneColor").getString()));
        buttonList.add(fireTwoColor = new GuiCustomButton(6, getCenter() + 2, getRowPos(5), 150, 20, "Fire Aspect 2: \u00a77" + cfgcolor.get("fireTwoColor").getString()));
        buttonList.add(protColor = new GuiCustomButton(8, getCenter() - 153, getRowPos(6), 150, 20, "Protection: \u00a77" + cfgcolor.get("protColor").getString()));
        buttonList.add(flameColor = new GuiCustomButton(7, getCenter() + 2, getRowPos(6), 150, 20, "Flame: \u00a77" + cfgcolor.get("flameColor").getString()));
        buttonList.add(knockbackColor = new GuiCustomButton(10, getCenter() - 153, getRowPos(7), 150, 20, "Knockback: \u00a77" + cfgcolor.get("knockbackColor").getString()));
        buttonList.add(infinityColor = new GuiCustomButton(9, getCenter() + 2, getRowPos(7), 150, 20, "Infinity: \u00a77" + cfgcolor.get("infinityColor").getString()));
        buttonList.add(size = new GuiCustomSlider(14, getCenter() - 150, getRowPos(8), 300, 20, "Tier Scale: ", 0.5F, 1.5F, Float.parseFloat(cfgclient.get("size").getString()), false));
        updateLabels();
        // Puts the tooltip infos
        texts.put(0, "If WhatTier is Enabled");
        texts.put(1, "If the tiers are shown in Roman Numeral");
        texts.put(2, "If the tiers of potions are shown");
        texts.put(3, "The global color for all tiers text ");
        texts.put(4, "The options for Sharpness enchantment /n " +
                "-global: using the global color /n " +
                "-off: it is not shown");
        texts.put(5, "The options for Fire Aspect I (1) enchantment /n " +
                "-global: using the global color /n " +
                "-off: it is not shown");
        texts.put(6, "The options for Fire Aspect II (2) enchantment /n " +
                "-global: using the global color /n " +
                "-off: it is not shown");
        texts.put(7, "The options for Flame enchantmen /n " +
                "-global: using the global color /n " +
                "-off: it is not shown");
        texts.put(8, "The options for Protection enchantment /n " +
                "-global: using the global color /n " +
                "-off: it is not shown");
        texts.put(9, "The options for Infinity enchantment /n " +
                "-global: using the global color /n " +
                "-off: it is not shown");
        texts.put(10, "The options for Knockback enchantment /n " +
                "-global: using the global color /n " +
                "-off: it is not shown");
        texts.put(11, "If the enchantment only apply to the correct item");
        texts.put(12, "The options for Power enchantment /n " +
                "-global: using the global color /n " +
                "-off: it is not shown");
        texts.put(13, "The location of where the tier is rendered");
        texts.put(14, "The size of the tier text");
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            // what to run when which button id is matched.
            case 0: {
                cfgclient.get("isEnabled").set(opposite(cfgclient.get("isEnabled").getString()));
                modState.displayString = "ModState: " + (getColorString(cfgclient.get("isEnabled").getString()));
                break;
            }
            case 13: {
                int index;
                if (Arrays.asList(cfgclient.get("location").getValidValues()).indexOf(cfgclient.get("location").getString()) == cfgclient.get("location").getValidValues().length - 1) {
                    index = Arrays.asList(cfgclient.get("location").getValidValues()).indexOf(cfgclient.get("location").getString()) - 8;
                } else {
                    index = Arrays.asList(cfgclient.get("location").getValidValues()).indexOf(cfgclient.get("location").getString()) + 1;
                }
                cfgclient.get("location").set(Arrays.asList(cfgclient.get("location").getValidValues()).get(index));
                location.displayString = "Location: \u00a77" + cfgclient.get("location").getString();
                break;
            }
            case 1: {
                cfgclient.get("isRoman").set(opposite(cfgclient.get("isRoman").getString()));
                button.displayString = "Roman Numeral: " + (getColorString(cfgclient.get("isRoman").getString()));
                break;
            }
            case 2: {
                cfgclient.get("enablePotion").set(opposite(cfgclient.get("enablePotion").getString()));
                button.displayString = "Potion: " + (getColorString(cfgclient.get("enablePotion").getString()));
                break;
            }
            case 3: {
                mc.displayGuiScreen(new GuiColorSelector(mod, cfg, "color", "globalColor"));
                break;
            }
            case 4: {
                mc.displayGuiScreen(new GuiColorSelector(mod, cfg, "color", "sharpColor"));
                break;
            }
            case 5: {
                mc.displayGuiScreen(new GuiColorSelector(mod, cfg, "color", "fireOneColor"));
                break;
            }
            case 6: {
                mc.displayGuiScreen(new GuiColorSelector(mod, cfg, "color", "fireTwoColor"));
                break;
            }
            case 7: {
                mc.displayGuiScreen(new GuiColorSelector(mod, cfg, "color", "flameColor"));
                break;
            }
            case 8: {
                mc.displayGuiScreen(new GuiColorSelector(mod, cfg, "color", "protColor"));
                break;
            }
            case 9: {
                mc.displayGuiScreen(new GuiColorSelector(mod, cfg, "color", "infinityColor"));
                break;
            }
            case 10: {
                mc.displayGuiScreen(new GuiColorSelector(mod, cfg, "color", "knockbackColor"));
                break;
            }
            case 11: {
                cfgclient.get("itemSpecific").set(opposite(cfgclient.get("itemSpecific").getString()));
                button.displayString = "Item Specific: " + (getColorString(cfgclient.get("itemSpecific").getString()));
                break;
            }
            case 12: {
                mc.displayGuiScreen(new GuiColorSelector(mod, cfg, "color", "powerColor"));
                break;
            }
        }
        updateLabels();
    }
}
