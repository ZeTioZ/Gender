package net.poweredbyhate.gender.listeners;

import static net.poweredbyhate.gender.utilities.Messenger.m;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.poweredbyhate.gender.GenderPlugin;

public class CommandPreProcessListener implements Listener
{
	@EventHandler
	public void onCommandPreProcess(PlayerCommandPreprocessEvent e)
	{
		String[] args = e.getMessage().split(" ");
		if(args.length > 2
			&& args[0].equalsIgnoreCase("gender")
			&& (args[1].equalsIgnoreCase("check")
				|| args[1].equalsIgnoreCase("show")))
		{
			e.setCancelled(true);
			
			final Player sender = e.getPlayer();
			final Player target = Bukkit.getPlayer(args[2]);
			if(target == null) return;
			
	        String gender = GenderPlugin.instance.goMental().getSnowflake(target).getGender().getName();
	        
	        if (gender.isEmpty()) {
	            sender.sendMessage(m("notIdentified", target.getName(), gender));
	        } else {
	            sender.sendMessage(m("checkGender", target.getName(), gender));
	        }
		}
	}
}
