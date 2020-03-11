package be.nickoos;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeIRC extends JavaPlugin implements PluginMessageListener {

    private static BungeeIRC plugin;
    PluginManager pm;

    public static BungeeIRC getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        this.pm = Bukkit.getPluginManager();
        this.pm.registerEvents(new ListenerChat(), this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "bungeeirc:core");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "bungeeirc:core", this);

        getLogger().info("onEnable is called!");
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("bungeeirc:core")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("SomeSubChannel")) {
            // Use the code sample in the 'Response' sections below to read
            // the data.
        }
    }


}
