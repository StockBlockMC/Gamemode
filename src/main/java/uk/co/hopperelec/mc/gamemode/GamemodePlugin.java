package uk.co.hopperelec.mc.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public final class GamemodePlugin extends JavaPlugin {
    @NotNull
    final List<String> gamemodes = Arrays.stream(GameMode.values()).map(Enum::name).toList();

    @NotNull
    private String setOwnGamemode(@NotNull CommandSender author, @NotNull GameMode gamemode) {
        if (author instanceof Player) {
            if (author.hasPermission("gamemode.self."+gamemode)) {
                ((Player) author).setGameMode(gamemode);
                return "Set own gamemode to "+gamemode+" Mode";
            }
            return "You don't have permission to change your own gamemode to "+gamemode+" Mode";
        }
        return "You can't change the console's gamemode";
    }

    @NotNull
    private String setOthersGamemode(@NotNull CommandSender author, @NotNull Player player, @NotNull GameMode gamemode) {
        if (author.hasPermission("gamemode.others."+gamemode)) {
            player.setGameMode(gamemode);
            return "Set "+player.getDisplayName()+"'s gamemode to "+gamemode+" Mode";
        }
        return "You don't have permission to set other players' gamemode to "+gamemode+" Mode";
    }

    @NotNull
    private String viewOwnGamemode(@NotNull CommandSender author) {
        if (author instanceof Player) {
            return "You are in "+((Player) author).getGameMode()+" Mode";
        }
        return "You are in CONSOLE Mode";
    }

    @NotNull
    private String viewOthersGamemode(@NotNull CommandSender author, @NotNull String playername) {
        if (!author.hasPermission("gamemode.view")) {
            return "You don't have permission to view other players' gamemode";
        }
        final Player player = Bukkit.getPlayer(playername);
        if (player != null) {
            return player.getDisplayName()+" is in "+player.getGameMode()+" Mode";
        }
        return "Unknown player '"+playername+"'";
    }

    @Nullable
    private GameMode parseGamemode(@NotNull String potentialGamemode) {
        if (gamemodes.contains(potentialGamemode.toUpperCase())) {
            return GameMode.valueOf(potentialGamemode.toUpperCase());
        }
        final int gamemodeId;
        try {
            gamemodeId = Integer.parseInt(potentialGamemode);
        } catch (NumberFormatException e) {
            return null;
        }
        if (gamemodeId < gamemodes.size()) {
            return GameMode.values()[gamemodeId];
        }
        return null;
    }
    
    @NotNull
    private String messageForCommand(@NotNull CommandSender author, @NotNull String[] args) {
        if (args.length == 0) return viewOwnGamemode(author);
        if (args.length == 1) {
            final GameMode gamemode = parseGamemode(args[0]);
            if (gamemode == null) return viewOthersGamemode(author, args[0]);
            return setOwnGamemode(author, gamemode);
        }
        if (args.length == 2) {
            GameMode gamemode = parseGamemode(args[0]);
            final String playername;
            if (gamemode == null) {
                gamemode = parseGamemode(args[1]);
                if (gamemode == null) return "Unknown gamemode";
                playername = args[1];
            } else {
                playername = args[0];
            }
            final Player player = Bukkit.getPlayer(playername);
            if (player == null) return "Unknown player '"+playername+"'";
            if (player == author) return setOwnGamemode(author, gamemode);
            return setOthersGamemode(author, player, gamemode);
        }
        return "Too many arguments, up to 2 expected!";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(messageForCommand(sender, args));
        return true;
    }
}
