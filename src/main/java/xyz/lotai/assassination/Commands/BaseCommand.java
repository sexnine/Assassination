package xyz.lotai.assassination.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class BaseCommand {
    public final boolean playerOnly;
    public final String label;
    public final HashMap<String, BaseCommand> subcommands;

    public BaseCommand(CommandHandler commandHandlerInstance, String label, boolean playerOnly, HashMap<String, BaseCommand> subcommands) {
        this.playerOnly = playerOnly;
        this.label = label;
        this.subcommands = subcommands;
        commandHandlerInstance.addCommand(this);
    }

    public void run(CommandSender sender, Command command, String label, String[] args) {
        if (this.playerOnly && !(sender instanceof Player)) {
            sender.sendMessage("§cOnly players may execute this command!");
            return;
        }
        if (this.subcommands != null && args.length < 2) {
            sender.sendMessage("§cMissing subcommand.");
            return;
        }
        //TODO: Implement subcommands
        onCommand(sender, command, label, args);
    }

    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("§cNo command handler has been implemented for this command.");
    }
}
