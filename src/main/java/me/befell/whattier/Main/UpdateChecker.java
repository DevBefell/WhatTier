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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateChecker{
    static URL GithubVersion;
    static URL GithubChangelog;


    static {
        try {
            GithubVersion = new URL("https://raw.githubusercontent.com/PyICoder/WhatTier/master/version.txt");
            GithubChangelog = new URL("https://raw.githubusercontent.com/PyICoder/WhatTier/master/changelog.txt");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    final static Minecraft mc = Minecraft.getMinecraft();
    static String version = WhatTier.VERSION;;
    static String changelog = "No Changelog";
    @SubscribeEvent
    public void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent ignored) {
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                version = GetInfo(GithubVersion);}
                catch (Exception ignored1){}
                try {
                    changelog = GetInfo(GithubChangelog);
                } catch (Exception ignored){}
                if (!version.equals(WhatTier.VERSION)) {
                    breakline();
                    mc.thePlayer.addChatMessage(new ChatComponentText("There is an update for WhatTier!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                    mc.thePlayer.addChatMessage(new ChatComponentText("Your Current version is " + WhatTier.VERSION + " The newest one is " + version).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                    mc.thePlayer.addChatMessage(new ChatComponentText("Changelog: " + changelog).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                    mc.thePlayer.addChatMessage(new ChatComponentText("Click here to download")
                                    .setChatStyle(new ChatStyle()
                                            .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("https://github.com/PyICoder/WhatTier/releases/latest")))
                                            .setBold(true).setColor(EnumChatFormatting.GOLD)
                                            .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/PyICoder/WhatTier/releases/latest"))));

                    breakline();
                }
            }
        };
        Timer timer = new Timer("Timer");
        timer.schedule(task, 3000);
    }

    public static String GetInfo(URL url) throws IOException {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
            connection.setDoOutput(true);
            BufferedReader serverResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = serverResponse.readLine();
            serverResponse.close();
            if (response == null){
                return "Cannot get Info";
            }
            return response;

    }
    public void breakline(){
        int dashnum =  (int) Math.floor((280 * mc.gameSettings.chatWidth + 40) / 320 * (1 / mc.gameSettings.chatScale) * 53) - 10;
        String dashes = new String(new char[dashnum]).replace("\0", "-");
        mc.thePlayer.addChatMessage(new ChatComponentText(dashes).setChatStyle(new ChatStyle().setBold(true).setStrikethrough(true).setColor(EnumChatFormatting.AQUA)));
    }
}