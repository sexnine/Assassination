package xyz.lotai.assassination.Util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.PlayerInventory;
import xyz.lotai.assassination.Assassination;

import java.nio.ByteBuffer;
import java.util.*;

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

    public static double calculateDistanceBetween(double x1, double y1, double x2, double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    public static boolean canRunOpCommand(CommandSender sender) {
        return sender.isOp() || !Assassination.getInstance().getConfig().getBoolean("needs-op");
    }

    public static String convertListToString(List<String> list, String separator) {
        int i = 0;
        StringBuilder res = new StringBuilder();
        while (i < list.size() - 1) {
            res.append(list.get(i)).append(separator);
            i++;
        }
        // Just a simple fix when testing with 1 player, should always be true if plugin ran properly.
        if (list.size() > 1) {
            res.append(list.get(i));
        }
        return res.toString();
    }

    public static String convertListToString(List<String> list) {
        return convertListToString(list, "§e, §6");
    }

    public static boolean isEnoughArgs(String[] args, int index, int argsExpected) {
        return args.length >= index + argsExpected;
    }

    public static boolean isEnoughArgs(String[] args, int index) {
        return isEnoughArgs(args, index, 1);
    }
}
