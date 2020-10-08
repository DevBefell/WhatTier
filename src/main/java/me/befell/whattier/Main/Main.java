package me.befell.whattier.Main;

import me.befell.whattier.Utils.Utils;
import me.befell.whattier.WhatTier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

import static me.befell.whattier.Utils.Utils.toRoman;

public class Main {
    private final ConfigCategory cfgclient;
    private final ConfigCategory cfgcolor;
    private final static Map<Integer, String> values = new HashMap<>();

    static {
        values.put(50, "flameColor");
        values.put(20, "fireAspect");
        values.put(0, "protColor");
        values.put(16, "sharpColor");
        values.put(48, "powerColor");
        values.put(51, "infinityColor");
        values.put(19, "knockbackColor");
    }

    public Main(WhatTier mod) {
        Configuration cfg = mod.getConfig().getConfig();
        cfgclient = cfg.getCategory("client");
        cfgcolor = cfg.getCategory("color");

    }

    // Thanks to Cow https://github.com/cow-mc
    private Utils.IntPair size(GuiScreen gui, IInventory inventory) {
        int xSize = 176;
        int ySize = 166;
        if (gui instanceof GuiContainerCreative) {
            xSize = 195;
            ySize = 136;
        } else if (gui instanceof GuiChest) {
            int inventoryRows = inventory.getSizeInventory() / 9;
            ySize = 222 - 108 + inventoryRows * 18;
        } else if (gui instanceof GuiBeacon) {
            xSize = 230;
            ySize = 219;
        } else if (gui instanceof GuiHopper) {
            ySize = 133;
        }
        int guiLeft = (gui.width - xSize) / 2;
        int guiTop = (gui.height - ySize) / 2;
        switch (cfgclient.get("location").getString()) {
            case "top left": {
                break;
            }

            case "top center": {
                guiLeft = guiLeft + 6;
                break;
            }

            case "top right": {
                guiLeft = guiLeft + 12;
                break;
            }

            case "center left": {
                guiTop = guiTop + 5;
                break;
            }

            case "center": {
                guiTop = guiTop + 5;
                guiLeft = guiLeft + 6;
                break;
            }

            case "center right": {
                guiTop = guiTop + 5;
                guiLeft = guiLeft + 12;
                break;
            }

            case "bottom left": {
                guiTop = guiTop + 10;
                break;
            }

            case "bottom center": {
                guiTop = guiTop + 10;
                guiLeft = guiLeft + 6;
                break;
            }

            case "bottom right": {
                guiTop = guiTop + 10;
                guiLeft = guiLeft + 12;
                break;
            }

        }
        return new Utils.IntPair(guiLeft, guiTop);
    }

    @SubscribeEvent
    public void onRenderGuiBackground(GuiScreenEvent.DrawScreenEvent.Pre e) {
        if (e.gui instanceof GuiContainer && cfgclient.get("isEnabled").getString().equals("enabled")) {
            GuiContainer guiContainer = (GuiContainer) e.gui;
            Container inventorySlots = guiContainer.inventorySlots;
            IInventory inventory = inventorySlots.getSlot(0).inventory;
            // For Skywars kit selector gui, can add others.
            if (inventory.getName().toLowerCase().equals("kit selector")) {
                return;
            }
            Utils.IntPair xy = size(guiContainer, inventory);
            int guiLeft = xy.getFirst();
            int guiTop = xy.getSecond();
            float scaleFactor = Float.parseFloat(cfgclient.get("size").getString());
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 260);
            GlStateManager.scale(scaleFactor, scaleFactor, 0);
            for (Slot inventorySlot : inventorySlots.inventorySlots) {
                if (inventorySlot.getHasStack()) {
                    renderEnchant(inventorySlot.getStack(), guiLeft, guiTop, inventorySlot);
                }
            }
            GlStateManager.popMatrix();

        }
    }

    private void renderEnchant(ItemStack item, int guiLeft, int guiTop, Slot inventorySlot) {
        float scaleFactor = Float.parseFloat(cfgclient.get("size").getString());
        String status = "";
        int color = cfgcolor.get("globalColor").getInt();
        if (item.getItem() instanceof ItemPotion && cfgclient.get("enablePotion").getString().equals("enabled")) {
            ItemPotion itempotion = (ItemPotion) item.getItem();
            String[] potionstat;
            try {
                potionstat = itempotion.getEffects(item).toString().split(", ");
            } catch (Exception e) {
                return;
            }
            int num = 1;
            try {
                num = Integer.parseInt(potionstat[0].substring(potionstat[0].length() - 1));
            } catch (NumberFormatException ignored) {
            }

            status = cfgclient.get("isRoman").getString().equals("enabled") ? toRoman(num) : "" + num;

        } else {
            NBTTagList itemEnchant = item.getEnchantmentTagList();
            if (itemEnchant == null || itemEnchant.toString().equals("[]")) {
                return;
            } else {
                for (int j = 0; j < itemEnchant.tagCount(); ++j) {
                    int id = itemEnchant.getCompoundTagAt(j).getInteger("id");
                    int num = itemEnchant.getCompoundTagAt(j).getInteger("lvl");
                    if (values.containsKey(id)) {
                        switch (values.get(id)) {
                            case "protColor": {
                                if (cfgclient.get("itemSpecific").getString().equals("enabled") && !(item.getItem() instanceof ItemArmor)) {
                                    return;
                                }
                                break;
                            }
                            case "knockbackColor":
                            case "sharpColor":
                            case "fireAspect": {
                                if (cfgclient.get("itemSpecific").getString().equals("enabled")) {
                                    if (!(item.getItem() instanceof ItemSword)) {
                                        return;
                                    }
                                }
                                break;
                            }
                            case "powerColor": {
                                if (cfgclient.get("itemSpecific").getString().equals("enabled") && !(item.getItem() instanceof ItemBow)) {
                                    return;
                                }
                                break;
                            }
                        }
                        switch (values.get(id)) {
                            case "fireAspect": {
                                if (itemEnchant.getCompoundTagAt(j).getInteger("lvl") > 1) {
                                    if (!cfgcolor.get("fireTwoColor").getString().equals("off") &&
                                            !cfgcolor.get("fireTwoColor").getString().equals("global")) {
                                        try {
                                            color = cfgcolor.get("fireTwoColor").getInt();
                                        } catch (Exception ignored) {
                                        }
                                    }
                                } else {
                                    if (!cfgcolor.get("fireOneColor").getString().equals("off") &&
                                            !cfgcolor.get("fireOneColor").getString().equals("global")) {
                                        try {
                                            color = cfgcolor.get("fireOneColor").getInt();
                                        } catch (Exception ignored) {
                                        }
                                    }
                                }
                                if (status.equals("")) {
                                    status = "N";
                                }
                                break;
                            }
                            case "flameColor": {
                                if (!cfgcolor.get("flameColor").getString().equals("off") &&
                                        !cfgcolor.get("flameColor").getString().equals("global")) {
                                    try {
                                        color = cfgcolor.get("flameColor").getInt();
                                    } catch (Exception ignored) {
                                    }
                                }
                                if (status.equals("")) {
                                    status = "N";
                                }
                                break;
                            }
                            case "knockbackColor": {
                                if (!cfgcolor.get("knockbackColor").getString().equals("off") &&
                                        !cfgcolor.get("knockbackColor").getString().equals("global")) {
                                    try {
                                        color = cfgcolor.get("knockbackColor").getInt();
                                    } catch (Exception ignored) {
                                    }
                                }
                                if (status.equals("")) {
                                    status = "N";
                                }
                                break;
                            }
                            case "infinityColor": {
                                if (!cfgcolor.get("infinityColor").getString().equals("off") &&
                                        !cfgcolor.get("infinityColor").getString().equals("global")) {
                                    color = cfgcolor.get("infinityColor").getInt();
                                }
                                if (status.equals("")) {
                                    status = "N";
                                }
                                break;
                            }
                            default: {
                                if (cfgcolor.get(values.get(id)).getString().equals("off")) {
                                    break;
                                }
                                if (!cfgcolor.get(values.get(id)).getString().equals("global") &&
                                        !(color == cfgcolor.get("fireTwoColor").getInt()) &&
                                        !(color == cfgcolor.get("fireOneColor").getInt())) {
                                    try {
                                        color = cfgcolor.get(values.get(id)).getInt();
                                    } catch (Exception ignored) {
                                        color = cfgcolor.get("globalColor").getInt();
                                    }
                                }
                                status = cfgclient.get("isRoman").getString().equals("enabled") ? toRoman(num) : "" + num;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (status.equals("")) {
            return;
        }
        if (cfgclient.get("location").getString().contains(" center") || cfgclient.get("location").getString().equals("center")) {
            if (status.length() > 1 || status.contains("V") || status.equals("N")) {
                guiLeft = guiLeft - ((status.length() > 1) ? status.length() - 2 : status.length());
            }
        } else if (cfgclient.get("location").getString().contains("right")) {
            if (status.length() > 1 || status.contains("V") || status.contains("N")) {
                guiLeft = guiLeft - ((status.length() > 2) ? status.length() + 2 : status.length());
            }
        }


        int x = (int) ((guiLeft + inventorySlot.xDisplayPosition) / scaleFactor + 1);
        int y = (int) ((guiTop + inventorySlot.yDisplayPosition) / scaleFactor) + 1;
        System.out.println(color);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(status, x, y, color);
    }
}