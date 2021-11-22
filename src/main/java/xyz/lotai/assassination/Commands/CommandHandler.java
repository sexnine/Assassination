package xyz.lotai.assassination.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.lotai.assassination.Assassination;

import java.util.HashMap;

public class CommandHandler implements CommandExecutor {
    private final HashMap<String, BaseCommand> commandsList = new HashMap<String, BaseCommand>();

    public CommandHandler(Assassination plugin) {
        new StartCommand(this);
        new StopCommand(this);
        new DebugCommand(this);
        plugin.getCommand("assassination").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            infoCommand(sender);
            return true;
        }
        // TODO: Polish, error prone rn lmao
        commandsList.get(args[0]).run(sender, command, label, args);
        return true;
    }

    public void infoCommand(CommandSender sender) {
        sender.sendMessage("§eAssassination - developed by §6sexnine");
        // TODO: Dynamic arguments - fuck it, just make this whole command handler extend BaseCommand
        sender.sendMessage("§eAvailable subcommands: §6start§e, §6info§e, §6debug§e, §6stop§e.");
    }

    public void addCommand(BaseCommand command) {
        this.commandsList.put(command.label, command);
    }
}
