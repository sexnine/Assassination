package xyz.lotai.assassination.Game.Events;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.lotai.assassination.Assassination;
import xyz.lotai.assassination.Game.Game;
import xyz.lotai.assassination.Game.GamePlayer;
import xyz.lotai.assassination.Game.GameState;
import xyz.lotai.assassination.Util.Events;
import xyz.lotai.assassination.Util.Util;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;


public class RunningEvents extends Events {
    private final HashMap<UUID, BukkitTask> awaitJoinTasks = new HashMap<>();

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType() == Material.COMPASS) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent e) {
        if (e.getItem().getItemStack().getType() == Material.COMPASS) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        e.setKeepInventory(true);
        e.getDrops().clear();
        Player player = e.getEntity();
        player.spigot().respawn();
        Game game = Assassination.getGame();
        GamePlayer assassin = game.getPlayer(player).getAssassin();
        if (game.getState() != GameState.GRACEPERIOD) {
            GamePlayer gamePlayer = game.getPlayer(player);
            PlayerInventory inventory = player.getInventory(); //TODO: NEEDS TESTING
            if ((player.getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent && damageEvent.getDamager() instanceof Player killer && killer != assassin.getPlayer())
                    || gamePlayer.getLastAttackTime() > Instant.now().getEpochSecond() - 15) {
                // If it was caused by an entity and there was a killer and the killer wasn't the assassin
                // OR the player was attacking/attacked in the last 15 seconds

                // Util.dropInventoryAt(inventory, e.getEntity().getLocation(), game.getCBWorld());
                gamePlayer.getTarget().giveInventory(inventory);
                gamePlayer.getTarget().getPlayer().giveExp(e.getDroppedExp());
            } else {
                assassin.giveInventory(inventory);
                assassin.getPlayer().giveExp(e.getDroppedExp());
            }
        }
        game.eliminatePlayer(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity() instanceof Player CBPlayer && e.getDamager() instanceof Player CBDamager) {
            Game game = Assassination.getGame();
            GamePlayer damager = game.getPlayer(CBDamager);
            GamePlayer player = game.getPlayer(CBPlayer);
            if (damager.getTarget() == player || damager.getAssassin() == player) {
                GamePlayer assassin = damager.getTarget() == player ? damager : player;
                assassin.setLastAttackTimeToNow();  // So if assassin dies in next 15 seconds, the items will go to the target instead of the assassin.
            } else {
                player.setLastAttackTime(0);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player CBPlayer = e.getPlayer();
        PlayerInventory inventory = CBPlayer.getInventory();
        Game game = Assassination.getGame();
        if (game.hasPlayer(CBPlayer)) {
            GamePlayer player = game.getPlayer(CBPlayer);
            UUID uuid = CBPlayer.getUniqueId();
            if (this.awaitJoinTasks.containsKey(uuid)) {
                this.awaitJoinTasks.get(uuid).cancel();
            }
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!CBPlayer.isOnline()) {
                        GamePlayer playerRewarded = player.getLastAttackTime() > Instant.now().getEpochSecond() - 25 ? player.getTarget() : player.getAssassin();
                        playerRewarded.giveInventory(inventory);
                        playerRewarded.getPlayer().giveExp(Math.min(CBPlayer.getLevel() * 7, 100));
                        game.eliminatePlayer(player);
                    }
                }
            }.runTaskLater(Assassination.getInstance(), 200L);
            this.awaitJoinTasks.put(uuid, task);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Game game = Assassination.getGame();
        if (!game.hasPlayer(player)) {
            if (player.getWorld() != game.getCBWorld()) {
                player.teleport(game.getCBWorld().getSpawnLocation());
            }
            player.setGameMode(GameMode.SPECTATOR);
        } else {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}
