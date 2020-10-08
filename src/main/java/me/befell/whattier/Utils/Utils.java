package me.befell.whattier.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class Utils {
    private static final Minecraft mc;

    // https://stackoverflow.com/a/2832629/10565413
    public static final class IntPair {
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

    public static Point getMousePos() {
        Point scaled = getScaledDimensions();
        int width = scaled.getX();
        int height = scaled.getY();
        int mouseX = Mouse.getX() * width / Utils.mc.displayWidth;
        int mouseY = height - Mouse.getY() * height / Utils.mc.displayHeight;
        return new Point(mouseX, mouseY);
    }

    public static Point getScaledDimensions() {
        ScaledResolution sr = new ScaledResolution(Utils.mc);
        int width = sr.getScaledWidth();
        int heigth = sr.getScaledHeight();
        return new Point(width, heigth);
    }

    public static boolean isMouseWithinBounds(int minX, int minY, int width, int height) {
        Point mousePos = getMousePos();
        return mousePos.getX() >= minX && mousePos.getX() <= minX + width && mousePos.getY() >= minY && mousePos.getY() <= minY + height;
    }

    static {
        mc = Minecraft.getMinecraft();
    }

    public void Delay(Runnable task, int secs) {
        TimerTask exe = new TimerTask() {
            public void run() {
                task.run();
            }
        };
        Timer timer = new Timer("WhatTierDelay " + secs);
        timer.schedule(exe, secs * 1000);
    }

}
