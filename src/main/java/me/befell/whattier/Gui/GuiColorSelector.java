package me.befell.whattier.Gui;

import me.befell.whattier.WhatTier;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
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
            cfg.getCategory(catagory).get(name).set("" +colorPicker.getPickedColor());
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
                drawToolTip(toList(texts.get(guiButton.id)), mouseX - 4, mouseY + 3, width, height, -1, mc.fontRendererObj);
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
                }
                else {
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
    public void drawToolTip(List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int tooltipTextWidth = 0;

            for (String textLine : textLines) {
                int textLineWidth = font.getStringWidth(textLine);

                if (textLineWidth > tooltipTextWidth) {
                    tooltipTextWidth = textLineWidth;
                }
            }

            boolean needsWrap = false;

            int titleLinesCount = 1;
            int tooltipX = mouseX + 14;
            if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
                tooltipX = mouseX - 1 - tooltipTextWidth;
                if (tooltipX < 4) // if the tooltip doesn't fit on the screen
                {
                    if (mouseX > screenWidth / 2) {
                        tooltipTextWidth = mouseX - 12 - 8;
                    } else {
                        tooltipTextWidth = screenWidth - 16 - mouseX;
                    }
                    needsWrap = true;
                }
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
                tooltipTextWidth = maxTextWidth;
                needsWrap = true;
            }

            if (needsWrap) {
                int wrappedTooltipWidth = 0;
                List<String> wrappedTextLines = new ArrayList<>();
                for (int i = 0; i < textLines.size(); i++) {
                    String textLine = textLines.get(i);
                    List<String> wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth);
                    if (i == 0) {
                        titleLinesCount = wrappedLine.size();
                    }

                    for (String line : wrappedLine) {
                        int lineWidth = font.getStringWidth(line);
                        if (lineWidth > wrappedTooltipWidth) {
                            wrappedTooltipWidth = lineWidth;
                        }
                        wrappedTextLines.add(line);
                    }
                }
                tooltipTextWidth = wrappedTooltipWidth;
                textLines = wrappedTextLines;

                if (mouseX > screenWidth / 2) {
                    tooltipX = mouseX - 16 - tooltipTextWidth;
                } else {
                    tooltipX = mouseX + 12;
                }
            }

            int tooltipY = mouseY - 9;
            int tooltipHeight = 8;

            if (textLines.size() > 1) {
                tooltipHeight += (textLines.size() - 1) * 10;
                if (textLines.size() > titleLinesCount) {
                    tooltipHeight += 2; // gap between title lines and next lines
                }
            }

            if (tooltipY + tooltipHeight + 6 > screenHeight) {
                tooltipY = screenHeight - tooltipHeight - 6;
            }
            // draws the box
            final int zLevel = 300;
            final int backgroundColor = new Color(0xDD717171, true).getRGB();
            drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            drawGradientRect(tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            //draws the border on the box
            final int borderColorStart = new Color(0xDD717171, true).getRGB();
            final int borderColorEnd = new Color(0xDD717171, true).getRGB();
            drawGradientRect( tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            drawGradientRect(tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            drawGradientRect( tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            drawGradientRect( tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber) {
                String line = textLines.get(lineNumber);
                font.drawStringWithShadow(line, (float) tooltipX, (float) tooltipY, -1);

                if (lineNumber + 1 == titleLinesCount) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
}
