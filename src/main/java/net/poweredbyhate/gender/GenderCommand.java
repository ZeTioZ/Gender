package net.poweredbyhate.gender;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Lax on 1/5/2017.
 */
public class GenderCommand implements CommandExecutor {

    MentalIllness mentalIllness = new MentalIllness();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("gender.gender")) {
            sender.sendMessage(ChatColor.RED + "You do not have enough privilege");
            return false;
        }

        if (args.length == 0) {
            mentalIllness.sendNonGenderNeutralMessage(sender, "&4-&6-&e-&2-&1-&9-&5- &c&lGender &4-&6-&e-&2-&1-&9-&5-");
            mentalIllness.sendNonGenderNeutralMessage(sender, "&c/list &7List all the genders");
            mentalIllness.sendNonGenderNeutralMessage(sender, "&c/info GENDER &7Shows info about the gender");
            mentalIllness.sendNonGenderNeutralMessage(sender, "&c/set GENDER &7Sets your own gender");
            mentalIllness.sendNonGenderNeutralMessage(sender, "&c/check PLAYER &7Checks the players gender");
            mentalIllness.sendNonGenderNeutralMessage(sender, "&4-&6-&e-&2-&1-&9-&5-&4-&6-&e-&2-&1-&9-&5-&4-&6-&e-&2-&1-&9-&5-");
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                mentalIllness.sendNonGenderNeutralMessage(sender,"&a(&b"+ mentalIllness.getDatabaseSize()+"&a) &b" + mentalIllness.getDatabase().keySet());
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info") && mentalIllness.getDatabase().containsKey(args[1].toLowerCase())) {
                mentalIllness.sendNonGenderNeutralMessage(sender, "&a"+ StringUtils.capitalize(args[1])+":&b " + mentalIllness.getGenderInfo(args[1]));
            }

            if (args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("show")) {
                mentalIllness.sendNonGenderNeutralMessage(sender,"&a"+args[1]+" identify as&b " + mentalIllness.getGender(args[1]));
            }

            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args[0].equalsIgnoreCase("set") && mentalIllness.getDatabase().containsKey(args[1].toLowerCase())) {
                    mentalIllness.setGender(p, args[1]);
                    mentalIllness.sendNonGenderNeutralMessage(sender, "&aYou now identify as &b" + mentalIllness.getGender(p));
                }
            }
        }
        return false;
    }
}
