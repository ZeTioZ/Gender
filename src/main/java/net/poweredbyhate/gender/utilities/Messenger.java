package net.poweredbyhate.gender.utilities;

import org.bukkit.ChatColor;

import java.util.HashMap;

public class Messenger {

    public static HashMap<String, String> messagesCache = new HashMap<>();

    public static String m(String key, String... m) {
        return ChatColor.translateAlternateColorCodes('&', String.format(messagesCache.get(key), m));
    }

}
