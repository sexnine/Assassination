package xyz.lotai.assassination.Game.Events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.lotai.assassination.Assassination;
import xyz.lotai.assassination.Game.Game;
import xyz.lotai.assassination.Util.Events;

public class WaitingEvents extends Events {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Game game = Assassination.getGame();
        World world = Bukkit.getWorld("world");
        if (world != null && player.getWorld() != world) {
            player.teleport(world.getSpawnLocation());
        }
        player.setGameMode(GameMode.SURVIVAL);
    }
}
