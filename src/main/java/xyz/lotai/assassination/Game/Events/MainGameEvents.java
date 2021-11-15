package xyz.lotai.assassination.Game.Events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.lotai.assassination.Assassination;
import xyz.lotai.assassination.Game.GamePlayer;
import xyz.lotai.assassination.Util.Events;

public class MainGameEvents extends Events {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player player && e.getDamager() instanceof Player damager) {
            GamePlayer GPDamager = Assassination.getGame().getPlayer(damager);
            if (GPDamager.getTarget().getPlayer() != player && GPDamager.getAssassin().getPlayer() != player) {
                e.setCancelled(true);
                Assassination.getGame().getPlayer(damager).giveStrike();
            }
        }
    }
}
