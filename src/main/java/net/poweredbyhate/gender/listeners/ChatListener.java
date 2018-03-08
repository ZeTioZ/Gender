package net.poweredbyhate.gender.listeners;

import net.poweredbyhate.gender.GenderPlugin;
import net.poweredbyhate.gender.special.Gender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Lax on 1/3/2017.
 */
public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent ev) {
        Gender gender = GenderPlugin.instance.goMental().getSnowflake(ev.getPlayer()).getGender();
        String g = gender.getName().replace("_", " ");
        ev.setFormat(ev.getFormat().replace("{gender_gender}", g).replace("%gender_gender%", g)
                .replace("{gender_pronoun}", gender.getPronoun()).replace("%gender_pronoun%", gender.getPronoun())); //why do I even bother
    }
}
