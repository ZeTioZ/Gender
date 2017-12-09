package net.poweredbyhate.gender;

import mkremins.fanciful.FancyMessage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lax on 1/5/2017.
 */
public class GenderCommand implements CommandExecutor {

    GenderPlugin plugin;

    public GenderCommand(GenderPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("gender.gender")) {
            sender.sendMessage(ChatColor.RED + "You do not have enough privilege");
            return false;
        }

        if (args.length == 0) {
            plugin.goMental().sendNonGenderNeutralMessage(sender, "&4-&6-&e-&2-&1-&9-&5- &c&lGender &4-&6-&e-&2-&1-&9-&5-");
            plugin.goMental().sendNonGenderNeutralMessage(sender, "&c/gender list &7List all the genders");
            plugin.goMental().sendNonGenderNeutralMessage(sender, "&c/gender info GENDER &7Shows info about the gender");
            plugin.goMental().sendNonGenderNeutralMessage(sender, "&c/gender set GENDER &7Sets your own gender");
            plugin.goMental().sendNonGenderNeutralMessage(sender, "&c/gender check PLAYER &7Checks the players gender");
            plugin.goMental().sendNonGenderNeutralMessage(sender, "&4-&6-&e-&2-&1-&9-&5-&4-&6-&e-&2-&1-&9-&5-&4-&6-&e-&2-&1-&9-&5-");
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                FancyMessage bean = new FancyMessage(ChatColor.RED + "(" + ChatColor.GREEN + plugin.goMental().getDatabase().keySet().size() + ChatColor.RED + ") ");
                for (Gender g : plugin.goMental().getGenders()) {
                    bean.then(ChatColor.AQUA + g.getName()).tooltip(ChatColor.RED + g.getName() + ":", ChatColor.GREEN + g.getDiscription()).then(ChatColor.WHITE + ", ");
                }
                bean.send(sender);
            }
        }

        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("info") && plugin.goMental().getDatabase().containsKey(args[1].toLowerCase())) {
                plugin.goMental().sendNonGenderNeutralMessage(sender, "&a" + StringUtils.capitalize(args[1]) + ":&b " + plugin.goMental().getGender(args[1]).getDiscription());
            }

            if (args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("show")) {
                plugin.goMental().sendNonGenderNeutralMessage(sender, "&a" + args[1] + " identify as&b " + plugin.goMental().getPlayerGender(args[1]));
            }

            if (sender instanceof Player) {
                Player p = (Player) sender;
                String legender = args[1].toLowerCase();
                if (args[0].equalsIgnoreCase("set") && plugin.goMental().getGender(legender) != null) {
                    if (!p.hasPermission("gender.set."+legender) || !p.hasPermission("gender.set.all")) {
                        plugin.goMental().sendNonGenderNeutralMessage(p, "&cYou do not have enough privilege!");
                        return false;
                    }
                    plugin.goMental().setPlayerGender(p, legender);
                    plugin.goMental().sendNonGenderNeutralMessage(sender, "&aYou now identify as &b" + StringUtils.capitalize(legender));
                }
            }
        }
        return false;
    }
}