package be.nickoos.BungeeIRC;

import be.nickoos.BungeeIRC.IRC.bot;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.logging.Level;

public class PluginMessageReceiver implements Listener {
    private BungeeIRC plugin;

    public PluginMessageReceiver(BungeeIRC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(PluginMessageEvent event)
    {


        if ( !event.getTag().equalsIgnoreCase("bungeeirc:core")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput( event.getData() );
        String subChannel = in.readUTF();
        String message = in.readUTF();
        if ( subChannel.equalsIgnoreCase( "message" ) )
        {

            if ( event.getReceiver() instanceof ProxiedPlayer) {
                ProxiedPlayer receiver = (ProxiedPlayer) event.getReceiver();
                new bot(plugin).Message_Client(receiver.toString(), message);

            }
            if ( event.getReceiver() instanceof Server)
            {
                Server receiver = (Server) event.getReceiver();
                // do things
            }
        }
    }
}
