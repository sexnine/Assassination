package xyz.lotai.assassination.Game;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.lotai.assassination.Assassination;
import xyz.lotai.assassination.Util.Util;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;

public class GamePlayer {
    private final Player player;
    private GamePlayer target;
    private GamePlayer assassin;
    private BukkitTask updateCompassTask;
    private int strikesLeft = 2;
    long lastAttackTime = 0;

    public GamePlayer(Player player) {
        this.player = player;
    }

    public void playSound(Sound sound, float pitch) {
        player.playSound(player.getLocation(), sound, 10, pitch);
    }

    public void giveCompass() {
        PlayerInventory inventory = this.player.getInventory();
        ItemStack offHandItem = inventory.getItemInOffHand();
        ItemStack item = new ItemStack(Material.COMPASS, 1);
        if (offHandItem.getType().isAir()) {
            inventory.setItemInOffHand(item);
        } else {
            HashMap<Integer, ItemStack> res = inventory.addItem(item);
            if (res.containsKey(0)) {
                this.player.sendMessage("§cCouldn't add the compass to your inventory probably because you didn't have any free slots.\nRun §6/compass §cto get a compass once you have a free slot in your inventory.");
            }
        }
    }

    public void giveStrike() {
        this.strikesLeft--;
        if (this.strikesLeft <= 0) {
            player.sendMessage("§cBad luck, maybe don't start hitting innocent people next time.");
            this.getAssassin().giveInventory(player.getInventory());
            Assassination.getGame().eliminatePlayer(this);
        } else {
            player.sendMessage("§cYou can only hit your target, further attacks against other players may result in elimination.");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 10, 1.5F);
        }
    }

    public void beforeDestroy() {
        if (this.updateCompassTask != null) {
            this.updateCompassTask.cancel();
        }
    }

    public void updateCompass() {
        player.setCompassTarget(target.getPlayer().getLocation());
    }

    public void startUpdateCompassTask() {
        this.updateCompassTask = new BukkitRunnable() {
            @Override
            public void run() {
                updateCompass();
            }
        }.runTaskTimer(Assassination.getInstance(), 0L, 1L);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setAssassin(GamePlayer playeri) {
        this.assassin = playeri;
    }

    public void setTarget(GamePlayer playeri) {
        this.target = playeri;
        this.lastAttackTime = 0;
        playeri.setAssassin(this);
//        Bukkit.broadcastMessage("Set target of " + this.player.getName() + " to " + playeri.getPlayer().getName());  //DEBUG
        if (!Arrays.asList(GameState.STARTING, GameState.WAITING, GameState.GRACEPERIOD).contains(Assassination.getGame().getState())) {
//            Bukkit.broadcastMessage("sent message on new target");
            this.player.sendMessage("§eYour new target is §6" + this.target.getPlayer().getName());
            this.playSound(Sound.BLOCK_NOTE_BLOCK_SNARE, 1.0F);
        }
    }

    public GamePlayer getAssassin() {
        return this.assassin;
    }

    public void inheritTargetFrom(GamePlayer playeri) {
        this.setTarget(playeri.getTarget());
    }

    public GamePlayer getTarget() {
        return this.target;
    }

    public void giveInventory(PlayerInventory inventory) {
        Location dropLocation = this.getPlayer().getLocation();
        World dropWorld = Assassination.getGame().getCBWorld();
        Util.dropInventoryAt(inventory, dropLocation, dropWorld);
    }

    public void setLastAttackTimeToNow() {
        this.lastAttackTime = Instant.now().getEpochSecond();
    }

    public void setLastAttackTime(long t) {
        this.lastAttackTime = t;
    }

    public long getLastAttackTime() {
        return this.lastAttackTime;
    }
}
