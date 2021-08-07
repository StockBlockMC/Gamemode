package uk.co.hopperelec.mc.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class Main extends JavaPlugin {
    final List<String> gamemodes = Arrays.asList("SURVIVAL","CREATIVE","SPECTATOR","ADVENTURE");

    @Override
    public boolean onCommand(CommandSender author, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("gamemode") || label.equalsIgnoreCase("gm")) {
            if (args.length == 0) {
                if (author instanceof Player) author.sendMessage("You are in "+((Player) author).getGameMode()+" Mode");
                else author.sendMessage("You are in console Mode");
            } else if (args.length == 1) {
                if (gamemodes.contains(args[0].toUpperCase())) {
                    if (author instanceof Player) {
                        final GameMode gamemode = GameMode.valueOf(args[0].toUpperCase());
                        if (author.hasPermission("gamemode.self."+gamemode)) {
                            ((Player) author).setGameMode(gamemode);
                            author.sendMessage("Set own gamemode to "+gamemode+" Mode");
                        } else author.sendMessage("You don't have permission to change your own gamemode to "+gamemode+" Mode");
                    } else author.sendMessage("You can't change the console's gamemode");
                } else {
                    if (author.hasPermission("gamemode.view")) {
                        final Player player = Bukkit.getPlayer(args[0]);
                        if (player != null) author.sendMessage(args[0]+" is in "+player.getGameMode()+" Mode");
                        else author.sendMessage("Unknown player");
                    } else author.sendMessage("You don't have permission to view other players' gamemode");
                }
            } else if (args.length == 2) {
                final String gamemode;
                final String playername;
                if (gamemodes.contains(args[0].toUpperCase())) {
                    gamemode = args[0].toUpperCase();
                    playername = args[1];
                } else if (gamemodes.contains(args[1].toUpperCase())) {
                    gamemode = args[1].toUpperCase();
                    playername = args[0];
                } else {
                    author.sendMessage("Unknown gamemode");
                    return false;
                }
                if (author.hasPermission("gamemode.others."+gamemode)) {
                    final Player player = Bukkit.getPlayer(playername);
                    if (player != null) {
                        player.setGameMode(GameMode.valueOf(gamemode));
                        author.sendMessage("Set "+playername+"'s gamemode to "+gamemode+" Mode");
                    } else author.sendMessage("Unknown player");
                } else author.sendMessage("You don't have permission to set other players' gamemode to "+gamemode+" Mode");
            } else {
                author.sendMessage("Unknown command");
                return false;
            }
        } else return false;
        return true;
    }
}
