package xyz.lotai.assassination;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lotai.assassination.Commands.CommandHandler;
import xyz.lotai.assassination.Game.Game;

public final class Assassination extends JavaPlugin {
    private static Assassination instance;
    private static Game game;
    private static MultiverseCore MVCoreInstance;

    @Override
    public void onEnable() {
        instance = this;
        new CommandHandler(this);
        game = new Game();
        MVCoreInstance = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
    }

    @Override
    public void onDisable() {
    }

    public static Assassination getInstance() {
        return instance;
    }

    public static Game getGame() {
        return game;
    }

    public static MultiverseCore getMV() {
        return MVCoreInstance;
    }
}
