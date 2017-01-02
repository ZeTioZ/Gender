package net.poweredbyhate.gender;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Lax on 12/15/2016.
 */
public class Gender extends JavaPlugin {

    private HashMap<String, String> genderList = new HashMap<String, String>();
    public Gurlz gurlz = new Gurlz();

    public void onEnable() {
        saveDefaultConfig();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderListener(this, "gender").hook();
        }
    }

    public InputStream getGenderFile() {
        return getClassLoader().getResourceAsStream("genders.yml");
    }

    public FileConfiguration getGenderConfig() {
        try {
            FileConfiguration genderConfig = new YamlConfiguration();
            genderConfig.load(getGenderFile());
            return genderConfig;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void populateList() { //I'll do this "optimization" stuff later.
        for (String s : getGenderConfig().getKeys(false)) {
            genderList.put(s, getGenderConfig().getString(s));
        }
    }

    public Set<String> getGenders() {
        return getGenderConfig().getKeys(false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("gender.gender")) {
            sender.sendMessage(ChatColor.RED + "You are not allowed to have a gender.");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(ChatColor.AQUA+"("+getGenders().size()+") " + java.util.Arrays.toString(getGenders().toArray()));
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info")) {
                if (getGenderConfig().getString(args[1]+".description") != null) {
                    sender.sendMessage(ChatColor.GREEN + args[1] + ": " + ChatColor.AQUA + getGenderConfig().getString(args[1]+".description"));
                } else {
                    sender.sendMessage("Such gender does not exist yet.");
                }
            }
            if (args[0].equalsIgnoreCase("set") && getGenderConfig().getString(args[1]+".description") != null && sender instanceof Player) {
                getConfig().set(((Player) sender).getUniqueId().toString(), args[1]);
                saveConfig();
                sender.sendMessage(ChatColor.GREEN + "You now identify as " + ChatColor.AQUA + args[1]);
            }
            if (args[0].equalsIgnoreCase("check")) {
                if (getConfig().getString(Bukkit.getOfflinePlayer(args[1]).getUniqueId().toString()) == null || !Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
                    sender.sendMessage("The player you have dialed is not yet in service");
                } else {
                    sender.sendMessage(ChatColor.GREEN + args[1]+" identify as " + ChatColor.AQUA + gurlz.getGender(Bukkit.getOfflinePlayer(args[1])));
                }
            }
        }
        return false;
    }

}
