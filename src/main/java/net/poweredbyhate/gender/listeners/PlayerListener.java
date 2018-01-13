package net.poweredbyhate.gender.listeners;

import net.poweredbyhate.gender.GenderPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Lax on 12/11/2017.
 */
public class PlayerListener implements Listener { //this should break on /reload

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent ev) {
        GenderPlugin.instance.goMental().registerFlake(ev.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent ev) {
        GenderPlugin.instance.goMental().removeFlake(ev.getPlayer());
    }
}
