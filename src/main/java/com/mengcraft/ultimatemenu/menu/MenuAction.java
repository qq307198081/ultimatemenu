package com.mengcraft.ultimatemenu.menu;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mengcraft.ultimatemenu.Main;
import com.mengcraft.ultimatemenu.SendTitle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MenuAction {
    public static Plugin pl;

    static {
        pl = Main.main;
    }

    public static boolean process(Player p, int slot) {
        MenuInfo menu = MenuManager.MANAGER.getOpened(p);
        boolean b = menu != null && menu.getItemMap().containsKey(slot);
        if (b) {
            ArrayList<String> commandList = menu.getItemMap().get(slot).commandList;
            portal(commandList, p);
            executeBy(p, commandList);
            execute(commandList, p);
            loadTitle(commandList, p);
            sendMessage(p, commandList);
        }
        return b;
    }

    public static void sendMessage(Player var0, List var1) {
        Iterator var3 = var1.iterator();

        while (var3.hasNext()) {
            String var2 = (String) var3.next();
            if (var2.contains("msg:")) {
                var0.sendMessage(var2.replace("msg:", "").replaceAll("&", "§"));
            }
        }

    }

    public static void executeBy(Player var0, List var1) {
        Iterator var3 = var1.iterator();

        while (var3.hasNext()) {
            String var2 = (String) var3.next();
            if (var2.contains("asOP:")) {
                if (var0.isOp()) {
                    var0.performCommand(var2.replace("asOP:", ""));
                } else {
                    var0.setOp(true);
                    var0.performCommand(var2.replace("asOP:", ""));
                    var0.setOp(false);
                }
            }
        }

    }

    public static void execute(List var0, Player var1) {
        Iterator var3 = var0.iterator();

        while (var3.hasNext()) {
            String var2 = (String) var3.next();
            if (var2.contains("asConsole:")) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), var2.replace("%PlayerName%", var1.getName()).replace("asConsole:", ""));
            }
        }

    }

    public static void portal(List var0, Player var1) {
        Iterator var3 = var0.iterator();

        while (var3.hasNext()) {
            String var2 = (String) var3.next();
            if (var2.contains("send:")) {
                ByteArrayDataOutput var4 = ByteStreams.newDataOutput();
                var4.writeUTF("Connect");
                var4.writeUTF(var2.replace("send:", ""));
                var1.sendPluginMessage(pl, "BungeeCord", var4.toByteArray());
            }
        }

    }

    public static void loadTitle(List var0, Player var1) {
        Iterator var3 = var0.iterator();

        while (var3.hasNext()) {
            String var2 = (String) var3.next();
            if (var2.contains("title:")) {
                if (pl.getServer().getVersion().contains("1.8")) {
                    String var4 = var2.replace("title:", "").replaceAll("&", "§");
                    String[] var5 = var4.split("##");
                    if (var5.clone().length == 5) {
                        SendTitle.sendTitle(var1, var5[0], var5[1], Integer.parseInt(var5[2]), Integer.parseInt(var5[3]), Integer.parseInt(var5[4]));
                    } else {
                        System.out.print("[UltimateMenu] Title must have 5 Parts, Title, Subtitle,FadeIn,Stay,FadeOut");
                    }
                } else {
                    System.out.print("[UltimateMenu] Titles don't work on 1.7!");
                }
            }
        }

    }
}
