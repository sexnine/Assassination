package xyz.lotai.assassination.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.lotai.assassination.Assassination;
import xyz.lotai.assassination.Game.GameState;

public class StartCommand extends BaseCommand {

    public StartCommand(CommandHandler commandHandlerInstance) {
        super(commandHandlerInstance, "start", true, true, null);
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args, int index) {
        if (Assassination.getGame().getState() != GameState.WAITING) {
            sender.sendMessage("§cA game is currently in progress.");
            return;
        }
        //TODO: Account for spectators
        if (Bukkit.getOnlinePlayers().size() < 3) {
            sender.sendMessage("§cYou must have at least 3 players to start a game.");
            return;
        }
        Bukkit.broadcastMessage("§eThe game will start soon!");
        Assassination.getGame().startGame();
    }
}
