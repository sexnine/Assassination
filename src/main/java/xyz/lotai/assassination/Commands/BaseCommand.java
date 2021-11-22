package xyz.lotai.assassination.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotai.assassination.Util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseCommand {
    public final boolean playerOnly;
    public final String label;
    public final HashMap<String, BaseCommand> subcommands = new HashMap<>();
    public final boolean requiresOp;
    public final int index;

    public BaseCommand(CommandHandler commandHandlerInstance, String label, boolean playerOnly, boolean requiresOp, HashMap<String, BaseCommand> subcommands) {
        this.playerOnly = playerOnly;
        this.label = label;
        this.requiresOp = requiresOp;
        this.index = 1;
        commandHandlerInstance.addCommand(this);
    }

    public BaseCommand(int index) {
        this.playerOnly = false;
        this.label = "";
        this.requiresOp = false;
        this.index = index;
    }

    public BaseCommand(boolean playerOnly, boolean requiresOp, int index) {
        this.playerOnly = playerOnly;
        this.label = "";
        this.requiresOp = requiresOp;
        this.index = index;
    }

    public BaseCommand(CommandHandler commandHandlerInstance, String label, boolean playerOnly, boolean requiresOp) {
        this.playerOnly = playerOnly;
        this.label = label;
        this.requiresOp = requiresOp;
        this.index = 1;
        commandHandlerInstance.addCommand(this);
    }

    public final void run(CommandSender sender, Command command, String label, String[] args) {
        if (this.playerOnly && !(sender instanceof Player)) {
            sender.sendMessage("§cOnly players may execute this command!");
            return;
        }
        if (this.requiresOp && !Util.canRunOpCommand(sender)) {
            sender.sendMessage("§cYou must be OP to run this command.  Try running §4op " + sender.getName() + " §cin the console first.");
            return;
        }
        //TODO: Implement subcommands
        if (this.subcommands.size() > 0) {
            if (!Util.isEnoughArgs(args, this.index) || !this.subcommands.containsKey(args[index])) {
                this.sendInfo(sender);
                return;
            }
            this.subcommands.get(args[index]).run(sender, command, label, args);
            return;
        }
        onCommand(sender, command, label, args, this.index);
    }

    public final void sendInfo(CommandSender sender) {
        sender.sendMessage("§eAvailable subcommands: §6" + Util.convertListToString(new ArrayList<>(this.subcommands.keySet())));
    }

    public void onCommand(CommandSender sender, Command command, String label, String[] args, int index) {
        sender.sendMessage("§cNo command handler has been implemented for this command.");
    }
}
