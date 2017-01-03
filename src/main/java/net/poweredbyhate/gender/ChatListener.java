package net.poweredbyhate.gender;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Lax on 1/3/2017.
 */
public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent ev) {
        ev.setFormat(ev.getFormat().replace("{gender_gender}", Gender.instance.gurlz.getGender(ev.getPlayer()))); //why do I even bother
    }
}
