package me.befell.whattier.Main;

import com.typesafe.config.ConfigException;
import me.befell.whattier.Utils.Utils;
import me.befell.whattier.WhatTier;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Events {
    static URL GithubVersion;
    static URL GithubChangelog;
    private WhatTier mod;
    private Logger logger;
    private String isenabled;

    final static Minecraft mc = Minecraft.getMinecraft();
    static String version = WhatTier.VERSION;
    ;
    static String changelog = "No Changelog";

    public Events(WhatTier mod) {
        this.mod = mod;
        logger = mod.getLogger();
        isenabled = mod.getConfig().getConfig().getCategory("client").get("isEnabled").getString();
    }

    public boolean ShouldRun() {
        if (isenabled.equals("disabled")) {
            return false;
        }
        String title;
        try {
            title = EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName());
        } catch (NullPointerException e) {
            title = "null";
        }

        // Can add games to exclude.
        if (title.toLowerCase().startsWith("skyblock")) {
            return false;
        }
        return true;
    }

    @SubscribeEvent
    public void onJoinWorld(PlayerSetSpawnEvent e) {
        new Utils().Delay(() -> {
            if (e.entity.getName().equals(mc.thePlayer.getName())) {
                if (ShouldRun()) {
                    logger.info("Registering");
                    MinecraftForge.EVENT_BUS.register(new Main(mod));
                } else {
                    logger.info("Unregistering");
                    MinecraftForge.EVENT_BUS.unregister(new Main(mod));
                }
            }
        }, 2);
    }

    @SubscribeEvent
    public void onLeaveWorld(PlayerEvent.PlayerLoggedOutEvent e) {
        MinecraftForge.EVENT_BUS.unregister(new Main(mod));
        logger.info("Left the world unregistering");
    }

    static {
        try {
            GithubVersion = new URL("https://raw.githubusercontent.com/PyICoder/WhatTier/master/version.txt");
            GithubChangelog = new URL("https://raw.githubusercontent.com/PyICoder/WhatTier/master/changelog.txt");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent ignored) {
        new Utils().Delay(() -> {
            try {
                version = GetInfo(GithubVersion);
            } catch (Exception ignored1) {
            }
            try {
                changelog = GetInfo(GithubChangelog);
            } catch (Exception ignored1) {
            }
            if (!version.equals(WhatTier.VERSION)) {
                breakline();
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00a76There is an update for WhatTier!"));
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00a76Your Current version is  The newest one is " + version));
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00a76Changelog: " + changelog));
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00a76\u00a7n\u00a7lClick here to download")
                        .setChatStyle(new ChatStyle()
                                .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("https://github.com/PyICoder/WhatTier/releases/latest")))
                                .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/PyICoder/WhatTier/releases/latest"))));

                breakline();
            }
        }, 3);
    }

    public static String GetInfo(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setUseCaches(true);
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
        connection.setDoOutput(true);
        BufferedReader serverResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = serverResponse.readLine();
        serverResponse.close();
        if (response == null) {
            return "Cannot get Info";
        }
        return response;

    }

    public void breakline() {
        int dashnum = (int) Math.floor((280 * mc.gameSettings.chatWidth + 40) / 320 * (1 / mc.gameSettings.chatScale) * 53) - 10;
        String dashes = new String(new char[dashnum]).replace("\0", "-");
        mc.thePlayer.addChatMessage(new ChatComponentText(dashes).setChatStyle(new ChatStyle().setBold(true).setStrikethrough(true).setColor(EnumChatFormatting.AQUA)));
    }

}
