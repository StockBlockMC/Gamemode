package uk.co.hopperelec.mc.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class GamemodePlugin extends JavaPlugin {
    @NotNull final List<String> gamemodes = Arrays.stream(GameMode.values()).map(Enum::name).toList();
    
    @NotNull private String messageForCommand(@NotNull CommandSender author, @NotNull String[] args) {
        if (args.length == 0) {
            if (author instanceof Player) {
                return "You are in "+((Player) author).getGameMode()+" Mode";
            } else {
                return "You are in CONSOLE Mode";
            }
        } else if (args.length == 1) {
            if (gamemodes.contains(args[0].toUpperCase())) {
                if (author instanceof Player) {
                    final String gamemode = args[0].toUpperCase();
                    if (author.hasPermission("gamemode.self."+gamemode)) {
                        ((Player) author).setGameMode(GameMode.valueOf(gamemode));
                        return "Set own gamemode to "+gamemode+" Mode";
                    } else {
                        return "You don't have permission to change your own gamemode to "+gamemode+" Mode";
                    }
                } else {
                    return "You can't change the console's gamemode";
                }
            } else {
                if (author.hasPermission("gamemode.view")) {
                    final Player player = Bukkit.getPlayer(args[0]);
                    if (player != null) {
                        return player.getDisplayName()+" is in "+player.getGameMode()+" Mode";
                    } else {
                        return "Unknown player";
                    }
                } else {
                    return "You don't have permission to view other players' gamemode";
                }
            }
        } else if (args.length == 2) {
            final String gamemode, playername;
            if (gamemodes.contains(args[0].toUpperCase())) {
                gamemode = args[0].toUpperCase();
                playername = args[1];
            } else if (gamemodes.contains(args[1].toUpperCase())) {
                gamemode = args[1].toUpperCase();
                playername = args[0];
            } else {
                return "Unknown gamemode";
            }
            if (author.hasPermission("gamemode.others."+gamemode)) {
                final Player player = Bukkit.getPlayer(playername);
                if (player != null) {
                    player.setGameMode(GameMode.valueOf(gamemode));
                    return "Set "+playername+"'s gamemode to "+gamemode+" Mode";
                } else {
                    return "Unknown player";
                }
            } else {
                return "You don't have permission to set other players' gamemode to "+gamemode+" Mode";
            }
        } else {
            return "Unknown command";
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        sender.sendMessage(messageForCommand(sender,args));
        return true;
    }
}
