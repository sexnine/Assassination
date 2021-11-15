package xyz.lotai.assassination.GameBossBar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.lotai.assassination.Assassination;

public class GameBossBar implements Listener {
    private final BossBar bossBar;
    private Boolean isRunning;

    public GameBossBar() {
        this.bossBar = Bukkit.createBossBar(
                "Assassination!",
                BarColor.GREEN,
                BarStyle.SOLID
        );
        Bukkit.getOnlinePlayers().forEach(this.bossBar::addPlayer);
        this.isRunning = true;
        Assassination.getInstance().getServer().getPluginManager().registerEvents(this, Assassination.getInstance());
    }

    public void setTimer(int timeLeft, int maxTime, String template) {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        String minutesString = minutes > 0 ? "§a§l" + minutes + "§c§lm " : "";
        String secondsString = "§a§l" + seconds + "§c§ls";
        String timeString = minutesString + secondsString;
        double progress = (double)timeLeft / (double)maxTime;

        String barTitle = template.replaceAll("%time%", timeString);

        bossBar.setTitle(barTitle);
        bossBar.setProgress(progress);
    }

    public void setTitle(String text) {
        this.bossBar.setTitle(text);
        this.bossBar.setProgress(1.0);
    }

    public void remove() {
        this.isRunning = false;
        HandlerList.unregisterAll(this);
        bossBar.removeAll();
    }

    public BossBar getCBBossBar() {
        return this.bossBar;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (this.isRunning) {
            this.bossBar.addPlayer(e.getPlayer());
        }
    }
}
