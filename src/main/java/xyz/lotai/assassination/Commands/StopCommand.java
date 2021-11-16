package xyz.lotai.assassination.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.lotai.assassination.Assassination;
import xyz.lotai.assassination.Game.Game;
import xyz.lotai.assassination.Game.GameState;
import xyz.lotai.assassination.Util.Util;

import java.util.HashMap;

public class StopCommand extends BaseCommand {

    public StopCommand(CommandHandler commandHandlerInstance) {
        super(commandHandlerInstance, "stop", true, true, null);
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        Game game = Assassination.getGame();
        if (game.getState() == GameState.WAITING) {
            sender.sendMessage("§cThere isn't a game currently running.");
            return;
        }
        if (game.getState() == GameState.STARTING) {
            sender.sendMessage("§cPlease wait until the game has started before stopping.");
            return;
        }

        game.endGame();
        Bukkit.broadcastMessage("§eThe game has been ended.");
    }
}
