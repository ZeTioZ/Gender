package net.poweredbyhate.gender.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.contexts.OnlinePlayer;
import com.cloutteam.samjakob.gui.ItemBuilder;
import com.cloutteam.samjakob.gui.buttons.GUIButton;
import com.cloutteam.samjakob.gui.types.PaginatedGUI;
import net.poweredbyhate.gender.GenderPlugin;
import net.poweredbyhate.gender.special.Gender;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@CommandAlias("gender")
public class CommandGender extends BaseCommand {

    private GenderPlugin plugin;

    public CommandGender(GenderPlugin plugin) {
        this.plugin = plugin;
    }

    @HelpCommand
    public void onHelp(Player sender) {
        plugin.goMental().sendNonGenderNeutralMessage(sender, "&4-&6-&e-&2-&1-&9-&5- &c&lGender &4-&6-&e-&2-&1-&9-&5-");
        plugin.goMental().sendNonGenderNeutralMessage(sender, "&c/gender <list/gui> &7Displays all the genders");
        plugin.goMental().sendNonGenderNeutralMessage(sender, "&c/gender info <GENDER> &7Shows info about the gender");
        plugin.goMental().sendNonGenderNeutralMessage(sender, "&c/gender set <GENDER> &7Sets your own gender");
        plugin.goMental().sendNonGenderNeutralMessage(sender, "&c/gender check <PLAYER> &7Checks the players gender");
        plugin.goMental().sendNonGenderNeutralMessage(sender, "&4-&6-&e-&2-&1-&9-&5-&4-&6-&e-&2-&1-&9-&5-&4-&6-&e-&2-&1-&9-&5-");
    }

    @Subcommand("info|i")
    public void onInfo(Player sender, String gender) {
        Gender g = plugin.goMental().getGender(gender);
        if (g != null) {
            plugin.goMental().sendNonGenderNeutralMessage(sender, "&a" + StringUtils.capitalize(gender) + ":&b " + plugin.goMental().getGender(gender).getDescription());
        } else {
            plugin.goMental().sendNonGenderNeutralMessage(sender, "&cGender does not exist!");
        }

    }

    @Subcommand("check|show")
    public void onCheck(Player sender, @Optional Player target) {
        plugin.goMental().sendNonGenderNeutralMessage(sender, "&6" + target.getName() + " &aidentify as&b " + plugin.goMental().getSnowflake(target).getGender().getName());
    }

    @Subcommand("set")
    public void onSet(Player sender, String gender) {
        if (plugin.goMental().getGender(gender) == null) {
            plugin.goMental().sendNonGenderNeutralMessage(sender, "&cGender does not exist!");
        }
        if (sender.hasPermission("gender.set."+gender) || sender.hasPermission("gender.set.all")) {
            plugin.goMental().setPlayerGender(sender, gender);
            plugin.goMental().sendNonGenderNeutralMessage(sender, "&aYou now identify as &b" + StringUtils.capitalize(gender));
        } else {
            plugin.goMental().sendNonGenderNeutralMessage(sender, "You don't have permission to set gender!");
        }
    }

    @Subcommand("gui|list") @CommandPermission("gender.guilist|gender.list")
    public void onList(Player sender) {
        PaginatedGUI menu = new PaginatedGUI("&5Genders &9(&4{SIZE}&9)".replace("{SIZE}", String.valueOf(plugin.goMental().getDatabase().keySet().size())));
        for (Gender g : plugin.goMental().getGenders()) {
            GUIButton button = new GUIButton(ItemBuilder.start(Material.WOOL).data((short) getNotRandomInt()).name("&a"+g.getName()).lore(Arrays.asList(WordUtils.wrap(ChatColor.translateAlternateColorCodes('&',g.getDescription()), 50).split(System.lineSeparator()))).build());
            button.setListener(event -> {
                event.setCancelled(true);
                if (event.getCurrentItem().hasItemMeta()) {
                    sender.performCommand("gender set " + ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                }
                event.getWhoClicked().closeInventory();
            });
            menu.addButton(button);
        }
        Gender g = plugin.goMental().getSnowflake(sender).getGender();
        GUIButton genderInfo = new GUIButton(ItemBuilder.start(Material.BOOK).name("&aYour gender: &b"+g.getName()).lore(Arrays.asList(WordUtils.wrap(ChatColor.translateAlternateColorCodes('&',g.getDescription()), 50).split(System.lineSeparator()))).build());
        genderInfo.setListener(event ->  event.setCancelled(true));
        menu.setToolbarItem(0, genderInfo);
        sender.openInventory(menu.getInventory());
    }

    public int getNotRandomInt() {
        return ThreadLocalRandom.current().nextInt(0,15);
    }
}