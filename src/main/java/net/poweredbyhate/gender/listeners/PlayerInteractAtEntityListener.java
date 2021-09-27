package net.poweredbyhate.gender.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import net.poweredbyhate.gender.GenderPlugin;

import static net.poweredbyhate.gender.utilities.Messenger.m;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInteractAtEntityListener implements Listener
{
	private final GenderPlugin plugin;
	private final Map<UUID, Long> genderCheckCooldown;
	
	public PlayerInteractAtEntityListener(GenderPlugin plugin)
	{
		this.plugin = plugin;
		genderCheckCooldown = new HashMap<>();
	}
	
	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e)
	{
		if(!(e.getRightClicked() instanceof Player)) return;
		
		final Player player = e.getPlayer();
		final UUID playerUUID = player.getUniqueId();
		
		if(!player.isSneaking()
			|| (genderCheckCooldown.containsKey(playerUUID) && genderCheckCooldown.get(playerUUID) > System.currentTimeMillis())) return;
		
		final Player playerClicked = (Player) e.getRightClicked();
		genderCheckCooldown.put(playerUUID, System.currentTimeMillis() + 3000);
		
		String gender = plugin.goMental().getSnowflake(playerClicked).getGender().getName();
        if (gender.isEmpty())
        {
            player.sendMessage(m("notIdentified", playerClicked.getName(), gender));
        }
        else
        {
        	player.sendMessage(m("checkGender", playerClicked.getName(), gender));
        }
	}
}
