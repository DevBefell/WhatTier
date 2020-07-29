package me.befell.whattier.Main;

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
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.TreeMap;

public class Main {
    public static boolean ShouldRun() {
        String title;
        try {
            title = EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName());
        }catch (Exception ignored){return true;}
        // Can add games to exclude.
        if (title.toLowerCase().equals("skyblock")) {
            return false;
        }
        return true;
    }

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

    private final static TreeMap<Integer, String> map = new TreeMap<>();

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

    // Thanks to Cow https://github.com/cow-mc
    private static IntPair size(GuiScreen gui, IInventory inventory) {
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
        return new IntPair(guiLeft, guiTop);
    }

    @SubscribeEvent
    public void onRenderGuiBackground(GuiScreenEvent.DrawScreenEvent.Pre e) {
        if (ShouldRun()) {
            if (e.gui instanceof GuiContainer) {
                GuiContainer guiContainer = (GuiContainer) e.gui;
                Container inventorySlots = guiContainer.inventorySlots;
                IInventory inventory = inventorySlots.getSlot(0).inventory;
                // For Skywars kit selector gui, can add others.
                if (inventory.getName().toLowerCase().equals("kit selector")){
                    return;
                }
                IntPair xy = size(guiContainer, inventory);
                int guiLeft = xy.getFirst();
                int guiTop = xy.getSecond();
                GlStateManager.pushMatrix();
                float scaleFactor = 0.7f;
                GlStateManager.translate(0, 0, 1);
                GlStateManager.scale(scaleFactor, scaleFactor, 0);
                for (Slot inventorySlot : inventorySlots.inventorySlots) {
                    if (inventorySlot.getHasStack()) {
                        renderEnchant(inventorySlot.getStack(), guiLeft, guiTop, inventorySlot);
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }

    private void renderEnchant(ItemStack item, int guiLeft, int guiTop, Slot inventorySlot) {
        float scaleFactor = 0.7f;
        String status = "";
        Color color = new Color(255, 255, 255, 255);
        if (item.getItem() instanceof ItemPotion) {
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

            status = toRoman(num);

        } else {
            NBTTagList itemEnchant = item.getEnchantmentTagList();
            if (itemEnchant == null || itemEnchant.toString().equals("[]")) {
                return;
            } else {
                for (int j = 0; j < itemEnchant.tagCount(); ++j) {
                    short id = itemEnchant.getCompoundTagAt(j).getShort("id");
                    short num = itemEnchant.getCompoundTagAt(j).getShort("lvl");
                    // sharpness, power, protection, efficiency
                    if (id == 16 || id == 48 || id == 0 || id == 32) {
                        status = toRoman(num);
                    }
                    // fire aspect, flame
                    if (id == 20 || id == 50) {
                        if (status.equals("")){
                            status = "N";
                        }
                        // fire aspect 2 is hotter
                        if (id == 20 && num > 1) {
                            color = new Color(255, 7, 57, 255);
                        } else {
                            color = new Color(255, 157, 0, 255);
                        }
                    }

                }
            }
        }
        if (status.equals("")){
            return;
        }

        int x = (int) ((guiLeft + inventorySlot.xDisplayPosition) / scaleFactor + 1);
        int y = (int) ((guiTop + inventorySlot.yDisplayPosition) / scaleFactor) + 1;

        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(status, x, y, color.getRGB());
    }
}