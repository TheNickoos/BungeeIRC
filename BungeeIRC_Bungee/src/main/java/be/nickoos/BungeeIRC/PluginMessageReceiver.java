package be.nickoos.BungeeIRC;

import be.nickoos.BungeeIRC.IRC.bot;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageReceiver implements Listener {
    private BungeeIRC plugin;

    public PluginMessageReceiver(BungeeIRC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(PluginMessageEvent event) {

        // We only read on channel "bungeeirc:core"
        if (!event.getTag().equalsIgnoreCase("bungeeirc:core")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput( event.getData() );
        String subChannel = in.readUTF();
        String message = in.readUTF();

        // This happen when the subchannel = message
        if (subChannel.equalsIgnoreCase("message")) {

            // If the sender of the message is a player
            if (event.getReceiver() instanceof ProxiedPlayer) {
                ProxiedPlayer receiver = (ProxiedPlayer) event.getReceiver();
                new bot(plugin).Message_Client(receiver.toString(), message);
            }

            // Need testing
            // If the sender of the message is the server
            if (event.getReceiver() instanceof Server) {
                Server receiver = (Server) event.getReceiver();
                // do things
            }
        }
    }
}
