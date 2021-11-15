package xyz.lotai.assassination;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lotai.assassination.Commands.CommandHandler;
import xyz.lotai.assassination.Game.Game;

import java.util.List;

public final class Assassination extends JavaPlugin {
    private static Assassination instance;
    private static Game game;
    private static MultiverseCore MVCoreInstance;
    private static ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        instance = this;
        new CommandHandler(this);
        game = new Game();
        MVCoreInstance = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        protocolManager = ProtocolLibrary.getProtocolManager();
//        protocolManager.addPacketListener(new PacketAdapter(this, PacketType.Play.Server.ENTITY_METADATA) {
//            @Override
//            public void onPacketSending(PacketEvent e) {
//                if (Bukkit.getPlayer("CerealKiller7319").getEntityId() != e.getPacket().getIntegers().read(0)) {
//                    return;
//                }
//                List<WrappedWatchableObject> watchableObjectList = e.getPacket().getWatchableCollectionModifier().read(0);
//                for (WrappedWatchableObject metadata : watchableObjectList) {
//                    if (metadata.getIndex() == 0) {
//                        byte b = (byte) metadata.getValue();
//                        Bukkit.broadcastMessage("value: " + b);
//                        b |= 0b01000000; // Adding glow effect
//                        Bukkit.broadcastMessage("new value: " + b);
//                        metadata.setValue(b);
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onDisable() {
    }

    public static Assassination getInstance() {
        return instance;
    }

    public static Game getGame() {
        return game;
    }

    public static MultiverseCore getMV() {
        return MVCoreInstance;
    }

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
