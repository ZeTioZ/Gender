package net.poweredbyhate.gender.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import net.poweredbyhate.gender.GenderPlugin;
import net.poweredbyhate.gender.hospital.Flat;
import net.poweredbyhate.gender.hospital.Sql;
import net.poweredbyhate.gender.special.Gender;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static net.poweredbyhate.gender.utilities.Messenger.m;

@CommandAlias("%gender")
public class CommandGender extends BaseCommand {

    private GenderPlugin plugin;

    public CommandGender(GenderPlugin plugin) {
        this.plugin = plugin;
    }

    @HelpCommand
    public void onHelp(Player sender) {
        sender.sendMessage(m("helpMessage"));
    }

    @Subcommand("export") @CommandPermission("gender.admin")
    public void onExport(CommandSender sender, String type, String fileName) {
        Flat flat = new Flat(plugin);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aExporting database..."));
        if (flat.dbExport(fileName.split("\\."))) {
            sender.sendMessage(m("exportSuccess", fileName));
        } else {
            sender.sendMessage(m("exportFail"));
        }
    }

    @Subcommand("import") @CommandPermission("gender.admin")
    public void onImport(CommandSender sender, String type, String fileName) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aImporting to database..."));

        if (type.equalsIgnoreCase("pack")) {
            Sql sql = new Sql(plugin);
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (sql.importPack(fileName.split("\\."))) {
                    sender.sendMessage(m("importSuccess"));
                    plugin.getAsylum().loadGenders();
                } else {
                    sender.sendMessage(m("importFail"));
                }
            });
            return;
        }

        if (type.equalsIgnoreCase("database")) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (plugin.getAsylum().dbImport(fileName.split("\\."))) {
                    sender.sendMessage(m("importSuccess"));
                    plugin.getAsylum().loadGenders();
                } else {
                    sender.sendMessage(m("importFail"));
                }
            });
            return;
        }
        sender.sendMessage(m("helpMessage"));
    }

    @Subcommand("info|i")
    public void onInfo(Player sender, String gender) {
        Gender g = plugin.goMental().getGender(gender);
        if (g != null) {
            sender.sendMessage(m("genderDesc",  StringUtils.capitalize(gender), plugin.goMental().getGender(gender).getDescription()));
        } else {
            sender.sendMessage(m("noGender", gender));
        }
    }

    @Subcommand("check|show")
    public void onCheck(Player sender, @Optional Player target) {
        String gender = plugin.goMental().getSnowflake(target).getGender().getName();
        if (gender.isEmpty()) {
            sender.sendMessage(m("notIdentified", target.getName(), gender));
        } else {
            sender.sendMessage(m("checkGender", target.getName(), gender));
        }
    }

    @Subcommand("set")
    public void onSet(Player sender, String gender) {
        if (plugin.goMental().getGender(gender) == null) {
            sender.sendMessage(m("noGender", gender));
            return;
        }
        if (sender.hasPermission("gender.set."+gender) || sender.hasPermission("gender.set.all")) {
            plugin.goMental().setPlayerGender(sender, gender);
            sender.sendMessage(m("genderSetSuccess", StringUtils.capitalize(gender)));
        } else {
            sender.sendMessage(m("noSetGenderPerm", gender));
        }
    }

    @Subcommand("gui|list") @CommandPermission("gender.list")
    public void onList(Player sender) {
        GuiElementGroup group = new GuiElementGroup('x');
        InventoryGui gui = new InventoryGui(plugin, sender, m("guiName").replace("{SIZE}", String.valueOf(plugin.goMental().getDatabase().keySet().size())), buildMatrix(plugin.goMental().getDatabase().size()));

        for (Gender g : plugin.goMental().getGenders()) {
            if (g.isPublic()) {
                List<String> infos = new ArrayList<>();
                infos.add(g.getName());
                infos.addAll(Arrays.asList(WordUtils.wrap(g.getDescription(), 50).split(System.lineSeparator())));
                ItemStack stack = new ItemStack(Material.valueOf(DyeColor.values()[ThreadLocalRandom.current().nextInt(DyeColor.values().length)].name()+"_WOOL"), 1);
                group.addElement(new StaticGuiElement('x',
                        new ItemStack(stack.getType(), 1),
                        click -> {
                            sender.performCommand("gender set " + g.getName());
                            return true;
                        },
                        infos.toArray((new String[0]))
                ));
            }
        }

        gui.addElement(new GuiPageElement('b', new ItemStack(Material.COAL, 1), GuiPageElement.PageAction.PREVIOUS, "&cPREVIOUS"));
        gui.addElement(new GuiPageElement('f', new ItemStack(Material.CHARCOAL, 1), GuiPageElement.PageAction.NEXT, "&aNEXT"));
        gui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
        group.setFiller(gui.getFiller());
        gui.addElement(group);
        gui.show(sender);
    }

    public String[] buildMatrix(int i) {
        List<String> matrix = new ArrayList<>();
        matrix.add("         ");
        matrix.add(" xxxxxxx ");
        if (i >= 8) {
            matrix.add(" xxxxxxx ");
        }
        if (i >= 15) {
            matrix.add(" xxxxxxx ");
        }
        if (i >= 22) {
            matrix.add(" xxxxxxx ");
            matrix.add("b       f");
            return matrix.toArray(new String[0]);
        }
        matrix.add("         ");
        return matrix.toArray(new String[0]);
    }

    @Subcommand("reload") @CommandPermission("gender.admin")
    public void onReload() {
        plugin.reload();
    }
}