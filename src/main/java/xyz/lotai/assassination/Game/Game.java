package xyz.lotai.assassination.Game;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import xyz.lotai.assassination.Assassination;
import xyz.lotai.assassination.GameBossBar.GameBossBar;
import xyz.lotai.assassination.Util.Countdown;
import xyz.lotai.assassination.Util.Util;

import java.util.*;

public class Game {
    private final HashMap<UUID, GamePlayer> players = new HashMap<>();
    private GameState state = GameState.WAITING;
    private final ArrayList<Player> spectators = new ArrayList<>();
    private MultiverseWorld MVWorld;
    private World CBWorld;
    private GameBossBar bossBar;
    private Countdown countdown;
    private final GameEventsManager events = new GameEventsManager();

    //TODO: Check how end and nether works with MV - needs MVNetherPortals - disable nether and end instead perhaps?

    public Game() {
        events.getWaitingEvents().start();
    }

    public void addSpectator(Player player) {
        if (!this.spectators.contains(player)) {
            this.spectators.add(player);
        }
    }

    public void startGame() {
        this.state = GameState.STARTING;
        this.bossBar = new GameBossBar();

        MultiverseCore MVCore = Assassination.getMV();
        MVWorldManager worldManager = MVCore.getMVWorldManager();
        Bukkit.broadcastMessage("§a1/4 §eCreating world...");
        String worldName = "ass_" + Util.shortUUID();  // ass hehe im funny
        worldManager.addWorld(worldName, World.Environment.NORMAL, null, WorldType.NORMAL, true, null);

        Bukkit.broadcastMessage("§a2/4 §eLoading world...");
        worldManager.loadWorld(worldName);
        this.MVWorld = worldManager.getMVWorld(worldName);
        this.MVWorld.setGameMode(GameMode.SURVIVAL);
        this.MVWorld.setDifficulty(Difficulty.EASY);

        this.events.getStartingEvents().start();

        Bukkit.broadcastMessage("§a3/4 §eChoosing targets...");
        // TODO: Implement spectators
        Bukkit.getOnlinePlayers().forEach((player) -> {
            if (!spectators.contains(player)) {
                players.put(player.getUniqueId(), new GamePlayer(player));
            }
        });  // TODO: Check spectators, skip specs
        List<GamePlayer> gamePlayersList = new ArrayList<>(this.getPlayers());
        Collections.shuffle(gamePlayersList);
        // Set targets
        for (int i = 0; i < gamePlayersList.size(); i++) {
            if (i == 0) {
                gamePlayersList.get(gamePlayersList.size() - 1).setTarget(gamePlayersList.get(i));
            } else {
                gamePlayersList.get(i - 1).setTarget(gamePlayersList.get(i));
            }
        }

        Bukkit.broadcastMessage("§a4/4 §eTeleporting players...");
        this.events.getWaitingEvents().stop();
        this.CBWorld = this.MVWorld.getCBWorld();
        Location teleportLocation = this.CBWorld.getSpawnLocation();
        gamePlayersList.forEach((player) -> {
            Player CBPlayer = player.getPlayer();
            Assassination.getInstance().getServer().dispatchCommand(Bukkit.getConsoleSender(),
                    "advancement revoke " + CBPlayer.getName() + " everything");  // cause fuck doing it myself
            CBPlayer.getActivePotionEffects().forEach((potionEffect -> {
                player.getPlayer().removePotionEffect(potionEffect.getType());
            }));
            CBPlayer.setGameMode(GameMode.SURVIVAL);
            CBPlayer.setTotalExperience(0);
            CBPlayer.setHealth(20);
            CBPlayer.setFoodLevel(20);
            CBPlayer.getInventory().clear();
            CBPlayer.addPotionEffects(Arrays.asList(
                    new PotionEffect(PotionEffectType.BLINDNESS, 36000, 10),
                    new PotionEffect(PotionEffectType.REGENERATION, 36000, 10),
                    new PotionEffect(PotionEffectType.SLOW, 36000, 10),
                    new PotionEffect(PotionEffectType.SLOW_DIGGING, 36000, 10)));
            CBPlayer.teleport(teleportLocation);
        });
        Assassination.getInstance().getServer().dispatchCommand(Bukkit.getConsoleSender(),  // Only way I could find to do this, and im not gonna implement this shit myself
                "execute in minecraft:" + worldName + " run spreadplayers 0 0 150 400 false @a[gamemode=survival]");

        WorldBorder worldBorder = this.CBWorld.getWorldBorder();
        worldBorder.setCenter(0, 0);
        worldBorder.setSize(1000);
        this.setCountdown(new Countdown(15) { //a
            @Override
            public void onEverySecond() {
                Bukkit.broadcastMessage("§eStarting in: " + this.getTimeFormatted());
                bossBar.setTimer(this.getTimeLeft(), this.getInitialTime(), "§a§lStarting in %time%");
                playSoundToPlayers(Sound.BLOCK_NOTE_BLOCK_HAT, 1.0F);
            }

            @Override
            public void onDone() {
                gamePlayersList.forEach((player) -> {
                    player.getPlayer().getActivePotionEffects().forEach((potionEffect -> {
                        player.getPlayer().removePotionEffect(potionEffect.getType());
                    }));
                });
                events.getStartingEvents().stop();
                playSoundToPlayers(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F);
                Bukkit.broadcastMessage("§eStart!");
                startGracePeriod();
            }
        });
    }

    public void playSoundToPlayers(Sound sound, float pitch) {
        // TODO: Prolly better and more efficient way to do this
        this.getPlayers().forEach((player) -> player.playSound(sound, pitch));
    }

    public void startGracePeriod() {
        this.events.getGracePeriodEvents().start();
        this.events.getRunningEvents().start();
        this.state = GameState.GRACEPERIOD;
        Bukkit.broadcastMessage("§eYour target will be revealed in §62 §eminutes!");
        this.setCountdown(new Countdown(120) { //a
            @Override
            public void onEverySecond() {
                bossBar.setTimer(this.getTimeLeft(), this.getInitialTime(), "§a§lTargets revealed in %time%");
                if (this.getTimeLeft() <= 3 || this.getTimeLeft() == 60) {
                    Bukkit.broadcastMessage("§eTargets revealed in " + this.getTimeFormatted() + "!");
                    playSoundToPlayers(Sound.BLOCK_NOTE_BLOCK_HAT, 1.0F);
                }
            }

            @Override
            public void onDone() {
                events.getGracePeriodEvents().stop();
                endGracePeriod();
            }
        });
    }

    public void endGracePeriod() {
        this.state = GameState.MAIN;
        this.events.getMainGameEvents().start();
        this.updateBBPlayerCount();
        this.getPlayers().forEach((player) -> {
            Player CBPlayer = player.getPlayer();
            CBPlayer.sendMessage("§eYour target is §6" + player.getTarget().getPlayer().getName() +
                    "§e. Track them down using your compass, but beware as someone is hunting you down too!\n" +
                    "You can use §6/compass §eto get a new one if you lose yours.");
            player.playSound(Sound.BLOCK_NOTE_BLOCK_SNARE, 1.5F);
            player.startUpdateCompassTask();
            player.giveCompass();
        });
    }

    public void updateBBPlayerCount() {
        this.bossBar.setTitle("§c§l" + this.players.size() + "§a§l players left!");
    }

    public void startDeathmatchGracePeriod() {
        this.events.getMainGameEvents().stop();
        this.events.getGracePeriodEvents().start();
        this.state = GameState.GRACEPERIOD;
        this.setCountdown(new Countdown(120) { //a
            @Override
            public void onEverySecond() {
                bossBar.setTimer(this.getTimeLeft(), this.getInitialTime(), "§a§lDeathmatch starts in %time%");
                if (this.getTimeLeft() <= 5 || this.getTimeLeft() == 15 || this.getTimeLeft() == 60) {
                    Bukkit.broadcastMessage("§eDeathmatch starts in " + this.getTimeFormatted() + "!");
                    playSoundToPlayers(Sound.BLOCK_NOTE_BLOCK_HAT, 1.0F);
                }
                if (this.getTimeLeft() <= 20 && this.getTimeLeft() > 10) {
                    getPlayers().forEach((player) -> player.getPlayer().sendMessage("§eYou will be teleported in §6" + (this.getTimeLeft() - 10) + "§es"));
                    playSoundToPlayers(Sound.BLOCK_NOTE_BLOCK_HAT, 1.0F);
                }
                if (this.getTimeLeft() == 10) {
                    // Teleport players
                    Bukkit.broadcastMessage("§eTeleporting players...");
                    Assassination.getInstance().getServer().dispatchCommand(Bukkit.getConsoleSender(),  // Only way I could find to do this, and im not gonna implement this shit myself
                            "execute in minecraft:" + MVWorld.getName() + " run spreadplayers 0 0 25 50 false @a[gamemode=survival]");
                    CBWorld.getWorldBorder().setSize(100);
                }
            }

            @Override
            public void onDone() {
                Bukkit.broadcastMessage("§eFIGHT!");
                bossBar.setTitle("§a§lDEATHMATCH");
                events.getGracePeriodEvents().stop();
                state = GameState.DEATHMATCH;
            }
        });
    }

    public void eliminatePlayer(@NotNull GamePlayer player) {
        GamePlayer assassin = player.getAssassin();
        assassin.inheritTargetFrom(player);
        this.playSoundToPlayers(Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0F);
        player.beforeDestroy();
        this.players.remove(player.getPlayer().getUniqueId());
        updateBBPlayerCount();
        Bukkit.broadcastMessage("§6" + player.getPlayer().getName() + "§e has been eliminated! §6" + this.players.size() + "§e players left!");
        player.getPlayer().setGameMode(GameMode.SPECTATOR);
        if (this.players.size() == 2) {
            this.startDeathmatchGracePeriod();
        }
        if (this.players.size() == 1) {
            Player winner = this.getPlayers().iterator().next().getPlayer();
            this.endGame();
            this.celebrateWinner(winner);
        }
    }

    public void eliminatePlayer(@NotNull Player player) {
        this.eliminatePlayer(this.getPlayer(player));
    }

    public void eliminatePlayerNotByDeath() {

    }

    public void endGame() {
        this.countdown.stop();
        this.events.stopAll();
        this.events.getWaitingEvents().start();
        this.bossBar.remove();
        this.state = GameState.WAITING;
        MVWorldManager worldManager = Assassination.getMV().getMVWorldManager();
        World defaultWorld = worldManager.getMVWorld("world").getCBWorld();  // TODO: Might not need MV for this lmao
        Location teleportLocation = defaultWorld.getSpawnLocation();
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.teleport(teleportLocation);
            player.setGameMode(GameMode.SURVIVAL);
        });
        new BukkitRunnable() {
            @Override
            public void run() {
                worldManager.deleteWorld(MVWorld.getName());
                MVWorld = null;
                CBWorld = null;
            }
        }.runTaskLater(Assassination.getInstance(), 100L);
    }

    public void celebrateWinner(Player winner) {
        //TODO: Fireworks :) and whatever the fuck else
        Bukkit.broadcastMessage("§6" + winner.getName() + "§e won the game poggers!");
    }

    public void setCountdown(Countdown countdown) {
        if (this.countdown != null) {
            this.countdown.stop();
        }
        this.countdown = countdown;
    }

    public GameState getState() {
        return this.state;
    }

    public GamePlayer getPlayer(@NotNull Player player) {
        return this.players.get(player.getUniqueId());
    }

    public GamePlayer getPlayerByUUID(UUID uuid) {
        return this.players.get(uuid);
    }

    public boolean hasPlayer(Player player) {
        return this.players.containsKey(player.getUniqueId());
    }

    public Collection<GamePlayer> getPlayers() {
        return this.players.values();
    }

    public Collection<UUID> getPlayerUUIDs() {
        return this.players.keySet();
    }

    public void addPlayer(GamePlayer player) {
        this.players.put(player.getPlayer().getUniqueId(), player);
    }

    public World getCBWorld() {
        return this.CBWorld;
    }

    public MultiverseWorld getMVWorld() {
        return this.MVWorld;
    }
}
