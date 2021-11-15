package xyz.lotai.assassination.Game.Events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.lotai.assassination.Assassination;
import xyz.lotai.assassination.Util.Events;

public class GracePeriodEvents extends Events {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player damager) {
            e.setCancelled(true);
            damager.sendMessage("§cYou can only hit your target, further attacks against other players may result in elimination.");
            damager.playSound(damager.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 10, 1.5F);
        }
    }
}
