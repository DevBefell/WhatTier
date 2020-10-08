package me.befell.whattier.Gui;

import me.befell.whattier.WhatTier;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.List;
import java.util.*;


public class GuiColorSelector extends GuiScreen {
    private GuiColorPicker colorPicker;
    private GuiCustomButton isGlobal;
    private GuiCustomButton isOn;
    private Configuration cfg;
    private String name;
    private String catagory;
    private WhatTier mod;
    private Logger logger;
    private Map<Integer, String> texts = new HashMap<>();

    public GuiColorSelector(WhatTier mod, Configuration cfg, String category, String name) {
        this.mod = mod;
        logger = mod.getLogger();
        catagory = category;
        this.name = name;
        this.cfg = cfg;
    }

    @Override
    public void onGuiClosed() {

        if (!cfg.getCategory(catagory).get(name).getString().equals("off") && !cfg.getCategory(catagory).get(name).getString().equals("global")) {
            cfg.getCategory(catagory).get(name).set("" + colorPicker.getPickedColor());
        }

        cfg.save();
    }

    @Override
    public void initGui() {
        mod.getConfig().syncFromFile();
        buttonList.add(new GuiCustomButton(26, getCenter() - 153, getRowPos(8) + 4, 150, 20, "Back"));
        buttonList.add(new GuiCustomButton(25, getCenter() - 2, getRowPos(8) + 4, 150, 20, "Reset"));
        if (cfg.getCategory(catagory).get(name).getName().equals("globalColor")) {
            buttonList.add(colorPicker = new GuiColorPicker(22, 0, 0, "", getCenter() - 59, getRowPos(3), Integer.parseInt(returnAction("getColorValue"))));
        } else {
            buttonList.add(isOn = new GuiCustomButton(28, getCenter() - 2, getRowPos(2), 150, 20, "On: " + returnAction("getOn")));
            buttonList.add(isGlobal = new GuiCustomButton(21, getCenter() - 153, getRowPos(2), 150, 20, "Global Color: " + returnAction("getColorGlobal")));
            buttonList.add(colorPicker = new GuiColorPicker(22, 0, 0, "", getCenter() - 59, getRowPos(3), toColorInt(returnAction("getColorValue"))));
            updateScreen();
        }
        texts.put(21, "If this should use the global color.");
        texts.put(22, "");
        texts.put(25, "");
        texts.put(26, "");
        texts.put(28, "If this will show.");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (GuiButton guiButton : buttonList) {
            if (guiButton.isMouseOver() && !texts.get(guiButton.id).equals("")) {
                GuiToolTip.drawToolTip(toList(texts.get(guiButton.id)), mouseX - 4, mouseY + 3, width, height, -1, mc.fontRendererObj);
            }
        }
    }

    public java.util.List<String> toList(String text) {
        if (text.contains("next line")) {
            return Arrays.asList(text.split("next line"));
        }
        List<String> al = new ArrayList<>();
        al.add(text);
        return al;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 21: {
                cfg.getCategory(catagory).get(name).set(returnAction("setGlobal"));
                isGlobal.displayString = "Global Color: " + (returnAction("getColorGlobal"));
                break;
            }
            case 26: {
                mc.displayGuiScreen(new GuiMain(mod));
                break;
            }
            case 28: {
                cfg.getCategory(catagory).get(name).set(returnAction("setOn"));
                isOn.displayString = "On: " + (returnAction("getOn"));
                break;
            }
            case 25: {
                cfg.getCategory(catagory).get(name).setToDefault();
                if (name.equals("globalColor")) {

                    colorPicker.setPickerColor(Integer.parseInt(cfg.getCategory("color").get("globalColor").getDefault()));
                } else {
                    if (cfg.getCategory(catagory).get(name).getDefault().equals("off")) {
                        isOn.displayString = "On: " + (returnAction("getOn"));
                        isGlobal.displayString = "Global Color:\u00a7a disabled";
                    } else if (cfg.getCategory(catagory).get(name).getDefault().equals("global")) {
                        isOn.displayString = "On:\u00a7c disabled";
                        isGlobal.displayString = "Global Color:\u00a7a enabled";
                    } else {
                        try {
                            colorPicker.setPickerColor(Integer.parseInt(cfg.getCategory(catagory).get(name).getDefault()));
                            isOn.displayString = "On:\u00a7a enabled";
                            isGlobal.displayString = "Global Color:\u00a7c disabled";
                        } catch (Exception ignored) {
                            colorPicker.setPickerColor(-1);
                        }
                    }
                }
                break;
            }
        }
        updateScreen();
    }

    public void updateScreen() {

        if (name.equals("globalColor")) {
            return;
        }
        colorPicker.setPickerVisible(!cfg.getCategory(catagory).get(name).getString().equals("off"));
        isGlobal.enabled = !cfg.getCategory(catagory).get(name).getString().equals("off");
        if (cfg.getCategory(catagory).get(name).getString().equals("off")) {
            isGlobal.displayString = "Global Color: \u00a7c disabled";
        }

    }

    public int getRowPos(int rowNumber) {
        return height / 4 + (24 * rowNumber - 24) - 16;
    }

    public int getCenter() {
        return width / 2;
    }

    private String returnAction(String action) {
        switch (action) {
            case "getColorGlobal": {
                return cfg.getCategory(catagory).get(name).getString().equals("global") ? "\u00a7aenabled" : "\u00a7cdisabled";
            }
            case "getColorValue": {
                return (cfg.getCategory(catagory).get(name).getString().equals("global") || cfg.getCategory(catagory).get(name).getString().toLowerCase().equals("off")) ? ("" + new Color(255, 255, 255).getRGB()) : ("" + cfg.getCategory(catagory).get(name).getString());
            }
            case "setGlobal": {
                if (cfg.getCategory(catagory).get(name).getString().equals("global")) {
                    return "" + colorPicker.getPickedColor();
                } else {
                    return "global";
                }
            }
            case "getOn": {
                return !cfg.getCategory(catagory).get(name).getString().equals("off") ? "\u00a7aenabled" : "\u00a7cdisabled";
            }
            case "setOn": {
                if ((cfg.getCategory(catagory).get(name).getString().equals("off"))) {
                    return "" + colorPicker.getPickedColor();
                } else {

                    return "off";
                }
            }
            default: {
                return "Error";
            }
        }
    }

    private int toColorInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception ignored) {
            cfg.getCategory(catagory).get(name).set(cfg.getCategory("client").get("globalColor").getInt());
            return cfg.getCategory("client").get("globalColor").getInt();
        }
    }
}
