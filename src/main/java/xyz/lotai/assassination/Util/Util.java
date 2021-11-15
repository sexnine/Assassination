package xyz.lotai.assassination.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import xyz.lotai.assassination.Assassination;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.UUID;

public class Util {
    public static String shortUUID() {
        return Long.toString(ByteBuffer.wrap(UUID.randomUUID().toString().getBytes()).getLong(), Character.MAX_RADIX);
    }

    public static Location generateRandomLocation(int maxRad, int min, World world) {
        //Generate a random X and Z based off location on a spiral curve
        int max = maxRad - min;
        int x, z;

        double area = Math.PI * (max - min) * (max + min); //of all the area in this donut
        double subArea = area * new Random().nextDouble(); //pick a random subset of that area

        double r = Math.sqrt(subArea/Math.PI + min*min); //convert area to radius
        double theta = (r - (int) r) * 2 * Math.PI; //use the remainder as an angle

        // polar to cartesian
        x = (int) (r * Math.cos(theta));
        z = (int) (r * Math.sin(theta));
        return new Location(world, x, 80, z);
    }

    public static void dropInventoryAt(PlayerInventory inventory, Location location, World world) {  // TODO: world might be unnecessary as you should be able to do location.getWorld()
        inventory.forEach((item) -> {
            if (item != null && item.getType() != Material.COMPASS) {
                world.dropItemNaturally(location, item);
            }
        });
    }

    public static void refreshPlayerVisibilty(Player player, Player playerToHide) {
        Bukkit.broadcastMessage("Refreshing " + playerToHide.getName() + " for " + player.getName()); // DEBUG
        player.hidePlayer(Assassination.getInstance(), playerToHide);
        player.showPlayer(Assassination.getInstance(), playerToHide);
    }
}
