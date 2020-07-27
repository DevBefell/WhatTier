package me.befell.whatenchant.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.TreeMap;

public class Main {
    // https://stackoverflow.com/a/2832629/10565413
    static final class IntPair {
        private final int first;
        private final int second;

        public IntPair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        public int getFirst() {
            return first;
        }

        public int getSecond() {
            return second;
        }
    }

    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public static String toRoman(int number) {
        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }

    // Thanks to Cow https://github.com/cow-mc https://discord.com/invite/f6N7RmA
    private static IntPair size(GuiScreen gui, IInventory inventory) {
        int left = 0;
        int top = 0;
        if (gui instanceof GuiInventory
                || gui instanceof GuiFurnace
                || gui instanceof GuiBrewingStand
                || gui instanceof GuiRepair
                || gui instanceof GuiCrafting
                || gui instanceof GuiDispenser
                || gui instanceof GuiScreenHorseInventory
                || gui instanceof GuiMerchant
                || gui instanceof GuiEnchantment) {
            int xSize = 176;
            int guiLeft = (gui.width - xSize) / 2;
            int ySize = 166;
            int guiTop = (gui.height - ySize) / 2;
            left = guiLeft;
            top = guiTop;
        } else if (gui instanceof GuiContainerCreative) {
            int xSize = 195;
            int ySize = 136;
            int guiLeft = (gui.width - xSize) / 2;
            int guiTop = (gui.height - ySize) / 2;
            left = guiLeft;
            top = guiTop;
        } else if (gui instanceof GuiChest) {
            int guiLeft = (gui.width - 176) / 2;
            int inventoryRows = inventory.getSizeInventory() / 9;
            int ySize = 222 - 108 + inventoryRows * 18;
            int guiTop = (gui.height - ySize) / 2;
            left = guiLeft;
            top = guiTop;
        } else if (gui instanceof GuiBeacon) {
            int xSize = 230;
            int ySize = 219;
            int guiLeft = (gui.width - xSize) / 2;
            int guiTop = (gui.height - ySize) / 2;
            left = guiLeft;
            top = guiTop;
        } else if (gui instanceof GuiHopper) {
            int xSize = 176;
            int ySize = 133;
            int guiLeft = (gui.width - xSize) / 2;
            int guiTop = (gui.height - ySize) / 2;
            left = guiLeft;
            top = guiTop;
        }
        return new IntPair(left + 12, top + 10);
    }

    @SubscribeEvent
    public void onRenderGuiBackground(GuiScreenEvent.DrawScreenEvent.Pre e) {
        if (e.gui instanceof GuiContainer ) {
            GuiContainer guiContainer = (GuiContainer) e.gui;

            Container inventorySlots = guiContainer.inventorySlots;
            IInventory inventory = inventorySlots.getSlot(0).inventory;
            IntPair xy = size(guiContainer, inventory);
            int guiLeft = xy.getFirst();
            int guiTop = xy.getSecond();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 1);
            float scaleFactor = 0.8f;
            GlStateManager.scale(scaleFactor, scaleFactor, 0);
            for (Slot inventorySlot : inventorySlots.inventorySlots) {
                if (inventorySlot.getHasStack()) {
                    int slotX = (int) ((guiLeft + inventorySlot.xDisplayPosition) / scaleFactor);
                    int slotY = (int) ((guiTop + inventorySlot.yDisplayPosition) / scaleFactor);
                    renderEnchant(inventorySlot.getStack(), slotX, slotY);
                }
            }
            GlStateManager.popMatrix();
        }
    }

    private void renderEnchant(ItemStack item, int x, int y) {
        String status = "N";
        Color color = new Color(255, 255, 255, 255);
        System.out.println(item.getAttributeModifiers());
        NBTTagList itemEnchant = item.getEnchantmentTagList();
        if (itemEnchant == null || itemEnchant.toString().equals("[]")) {
            return;
        } else {
            for (int j = 0; j < itemEnchant.tagCount(); ++j) {
                short id = itemEnchant.getCompoundTagAt(j).getShort("id");
                short lvl = itemEnchant.getCompoundTagAt(j).getShort("lvl");
                // sharpness, power, protection, efficiency
                if (id == 16 || id == 48 || id == 0 || id == 32) {
                    status = toRoman(lvl);
                }
                // fire aspect, flame
                if (id == 20 || id == 50) {
                    // fire aspect 2 is hotter
                    if (id == 20 && lvl > 1) {
                        color = new Color(255, 7, 57, 255);
                    } else {
                        color = new Color(255, 157, 0, 255);
                    }
                }


            }
        }
        Minecraft.getMinecraft().fontRendererObj.drawString(status, x, y, color.getRGB());
    }
}