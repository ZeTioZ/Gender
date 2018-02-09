package net.poweredbyhate.gender.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
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
import static net.poweredbyhate.gender.utilities.Messenger.m;

@CommandAlias("gender|g")
public class CommandGender extends BaseCommand {

    private GenderPlugin plugin;

    public CommandGender(GenderPlugin plugin) {
        this.plugin = plugin;
    }

    @HelpCommand
    public void onHelp(Player sender) {
        sender.sendMessage(m("helpMessage"));
    }

    @Subcommand("info|i")
    public void onInfo(Player sender, String gender) {
        Gender g = plugin.goMental().getGender(gender);
        if (g != null) {
            sender.sendMessage(m("genderDesc",  StringUtils.capitalize(gender), plugin.goMental().getGender(gender).getDescription()));
        } else {
            sender.sendMessage(m("noGender"));
        }

    }

    @Subcommand("check|show")
    public void onCheck(Player sender, @Optional Player target) {
        sender.sendMessage(m("checkGender", target.getName(), plugin.goMental().getSnowflake(target).getGender().getName()));
    }

    @Subcommand("set")
    public void onSet(Player sender, String gender) {
        if (plugin.goMental().getGender(gender) == null) {
            sender.sendMessage(m("noGender", gender));
        }
        if (sender.hasPermission("gender.set."+gender) || sender.hasPermission("gender.set.all")) {
            plugin.goMental().setPlayerGender(sender, gender);
            sender.sendMessage(m("genderSetSuccess", StringUtils.capitalize(gender)));
        } else {
            sender.sendMessage(m("noSetGenderPerm", gender));
        }
    }

    @Subcommand("gui|list") @CommandPermission("gender.guilist|gender.list")
    public void onList(Player sender) {
        PaginatedGUI menu = new PaginatedGUI(m("guiName").replace("{SIZE}", String.valueOf(plugin.goMental().getDatabase().keySet().size())));
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
        GUIButton genderInfo = new GUIButton(ItemBuilder.start(Material.BOOK).name(m("guiGenderInfo", g.getName())).lore(Arrays.asList(WordUtils.wrap(ChatColor.translateAlternateColorCodes('&',g.getDescription()), 50).split(System.lineSeparator()))).build());
        genderInfo.setListener(event ->  event.setCancelled(true));
        menu.setToolbarItem(0, genderInfo);
        sender.openInventory(menu.getInventory());
    }

    public int getNotRandomInt() {
        return ThreadLocalRandom.current().nextInt(0,15);
    }
}