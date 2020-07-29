package me.befell.whattier.Main;

import me.befell.whattier.WhatTier;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateChecker {
    @SubscribeEvent
    public void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        TimerTask task = new TimerTask() {
            public void run() {
                String Version = CheckUpdate();

                System.out.println(Version);
                if (!Version.equals(WhatTier.VERSION)) {

                    Minecraft.getMinecraft().thePlayer
                            .addChatMessage(new ChatComponentText("There is an update for WhatTier!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Your Current version is " + WhatTier.VERSION + " The newest one is " + Version).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                    Minecraft.getMinecraft().thePlayer
                            .addChatMessage(new ChatComponentText("Click here to download")
                                    .setChatStyle(new ChatStyle()
                                    .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("https://github.com/PyICoder/WhatLevel/releases/latest")))
                                    .setBold(true).setColor(EnumChatFormatting.GOLD)
                                    .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/PyICoder/WhatLevel/releases/latest"))));


                }
            }
        };
        Timer timer = new Timer("Timer");
        timer.schedule(task, 3000);
    }

    public static String CheckUpdate() {
        try {
            URL url = new URL("https://raw.githubusercontent.com/PyICoder/WhatLevel/master/version.txt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
            connection.setDoOutput(true);
            BufferedReader serverResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = serverResponse.readLine();
            serverResponse.close();
            System.out.println(response);
            return response;
        } catch (Exception e) {
            System.out.println(e);
            return WhatTier.VERSION;
        }
    }
}
