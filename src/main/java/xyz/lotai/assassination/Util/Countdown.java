package xyz.lotai.assassination.Util;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.lotai.assassination.Assassination;

public class Countdown {
    private final int initialTime;
    private int timeLeft;
    private final BukkitTask task;

    public Countdown(int time) {
        this.initialTime = time;
        this.timeLeft = time;

        onEverySecond();

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                timeLeft--;
                if (timeLeft <= 0) {
                    stop();
                    onDone();
                } else {
                    onEverySecond();
                }
            }
        }.runTaskTimer(Assassination.getInstance(), 20L, 20L);
    }

    public String getTimeFormatted() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        String minutesString = minutes > 0 ? "§6" + minutes + "§em " : "";
        String secondsString = seconds > 0 ? "§6" + seconds + "§es" : "";

        return minutesString + secondsString;
    }

    public void stop() {
        if (!this.task.isCancelled()) {
            this.task.cancel();
        }
    }

    public int getTimeLeft() {
        return this.timeLeft;
    }

    public int getInitialTime() {
        return this.initialTime;
    }

    public BukkitTask getTask() {
        return this.task;
    }

    public void onEverySecond() {}

    public void onDone() {}
}
