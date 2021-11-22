package xyz.lotai.assassination.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotai.assassination.Assassination;
import xyz.lotai.assassination.Game.Game;
import xyz.lotai.assassination.Game.GamePlayer;
import xyz.lotai.assassination.Util.Util;

import java.util.Collection;
import java.util.stream.Collectors;

public class DebugCommand extends BaseCommand {

    public DebugCommand(CommandHandler commandHandlerInstance) {
        super(commandHandlerInstance, "debug", true, true);
        this.subcommands.put("eliminateOffline", new BaseCommand(2) {
            @Override
            public void onCommand(CommandSender sender, Command command, String label, String[] args, int index) {
                Game game = Assassination.getGame();
                game.getPlayers().forEach((player) -> {
                    if (!player.getPlayer().isOnline()) {
                        game.eliminatePlayer(player);
                    }
                });
            }
        });
        this.subcommands.put("players", new BaseCommand(2) {
            @Override
            public void onCommand(CommandSender sender, Command command, String label, String[] args, int index) {
                Game game = Assassination.getGame();
                Collection<GamePlayer> players = game.getPlayers();
                sender.sendMessage("§eThere are currently §6" + players.size() + "§e players: §6"
                        + Util.convertListToString(players.stream().map(player -> player.getPlayer().getName()).collect(Collectors.toList())));
            }
        });
        this.subcommands.put("eliminate", new BaseCommand(2) {
            @Override
            public void onCommand(CommandSender sender, Command command, String label, String[] args, int index) {
//                if (args.length <= index) {
                if (!Util.isEnoughArgs(args, index)) {
                    sender.sendMessage("§cYou didn't mention a player to eliminate");
                    return;
                }
                Game game = Assassination.getGame();
                String playerName = args[index];
                Player player = Bukkit.getPlayer(playerName);
                if (player != null && game.hasPlayer(player)) {
                    game.eliminatePlayer(player);
                    sender.sendMessage("§eSuccessfully eliminated §6" + playerName);
                    return;
                }  // TODO: Might not work for offline players :\
                sender.sendMessage("§cCould not eliminate §4" + playerName + "§c because they probably were not a player in the game");
            }
        });
    }
}
