package xyz.lotai.assassination.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SpectatorsCommand extends BaseCommand {

    public SpectatorsCommand(CommandHandler commandHandlerInstance) {
//        super(commandHandlerInstance, "spectators", true, new String[]{"join", "list", "add", "remove"});
        super(commandHandlerInstance, "spectators", true, null);
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args[1]) {
            case "join":

        }
        sender.sendMessage("§cNo command handler has been implemented for this command. test");
    }
}
