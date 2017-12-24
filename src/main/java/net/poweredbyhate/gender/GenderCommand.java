package net.poweredbyhate.gender;

import com.cloutteam.samjakob.gui.ItemBuilder;
import com.cloutteam.samjakob.gui.buttons.GUIButton;
import com.cloutteam.samjakob.gui.types.PaginatedGUI;
import net.poweredbyhate.gender.special.Gender;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Lax on 1/5/2017.
 */
public class GenderCommand implements CommandExecutor { //todo Make pretty

    GenderPlugin plugin;

    public GenderCommand(GenderPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("gender.gender")) {
            sender.sendMessage(ChatColor.RED + "You do not have enough privilege! (1)");
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
            if ((args[0].equalsIgnoreCase("gui") || args[0].equalsIgnoreCase("list")) && sender.hasPermission("gender.guilist")) {
                Player p = (Player) sender; //inb4 cast exception from console
                PaginatedGUI menu = new PaginatedGUI("&5Genders &9(&4{SIZE}&9)".replace("{SIZE}", String.valueOf(plugin.goMental().getDatabase().keySet().size())));
                for (Gender g : plugin.goMental().getGenders()) {
                    GUIButton button = new GUIButton(ItemBuilder.start(Material.WOOL).data((short) getNotRandomInt()).name("&a"+g.getName()).lore(Arrays.asList(WordUtils.wrap(g.getDescription(), 50).split(System.lineSeparator()))).build());
                    button.setListener(event -> {
                        event.setCancelled(true);
                        if (event.getCurrentItem().hasItemMeta()) {
                            p.performCommand("gender set " + ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                        }
                        event.getWhoClicked().closeInventory();
                    });
                    menu.addButton(button);
                }
                Gender g = plugin.goMental().getSnowflake(p).getGender();
                GUIButton genderInfo = new GUIButton(ItemBuilder.start(Material.BOOK).name("&aYour gender: &b"+g.getName()).lore(Arrays.asList(WordUtils.wrap(g.getDescription(), 50).split(System.lineSeparator()))).build());
                genderInfo.setListener(event ->  event.setCancelled(true));
                menu.setToolbarItem(0, genderInfo);
                p.openInventory(menu.getInventory());
            }
        }

        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("info") && plugin.goMental().getDatabase().containsKey(args[1].toLowerCase())) {
                plugin.goMental().sendNonGenderNeutralMessage(sender, "&a" + StringUtils.capitalize(args[1]) + ":&b " + plugin.goMental().getGender(args[1]).getDescription());
            }

            if (args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("show")) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                if (player == null) {
                    plugin.goMental().sendNonGenderNeutralMessage(sender, "&cPlayer is not online");
                    return false;
                }
                plugin.goMental().sendNonGenderNeutralMessage(sender, "&a" + args[1] + " identify as&b " + plugin.goMental().getSnowflake((Player) player).getGender());
            }

            if (sender instanceof Player) {
                Player p = (Player) sender;
                String legender = args[1].toLowerCase();
                if (args[0].equalsIgnoreCase("set") && plugin.goMental().getGender(legender) != null) {
                    if (p.hasPermission("gender.set."+legender) || p.hasPermission("gender.set.all")) {
                        plugin.goMental().setPlayerGender(p, legender);
                        plugin.goMental().sendNonGenderNeutralMessage(sender, "&aYou now identify as &b" + StringUtils.capitalize(legender));
                    } else {
                        plugin.goMental().sendNonGenderNeutralMessage(p, "&cYou do not have enough privilege! (2)");
                    }

                }
            }
        }
        return false;
    }

    public int getNotRandomInt() {
        return ThreadLocalRandom.current().nextInt(0,15);
    }
}