package be.nickoos.BungeeIRC.IRC.Event;

import be.nickoos.BungeeIRC.BungeeIRC;
import be.nickoos.BungeeIRC.IRC.bot;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.PrintWriter;

public class Event {
    BungeeIRC plugin;
    public Event (BungeeIRC plugin, String line, PrintWriter out) {
        String temp[] = line.trim().split(" ");
        switch (temp[0]) {

            // This is the last line sended by the IRC server when the link is done.
            case "NETINFO":
                plugin.getLogger().info("Connected to server !");
                // Its useless, but I prefer send to the server we have finished the sync.
                out.println("EOS");
                break;

            // Ping Pong
            case "PING":
                out.println("PONG :" + temp[1]);
                break;
        }
        switch (temp[1]) {
            // When a player is kicked from IRC, he will be also kicked from the game.
            case "KICK":
                String nick[] = temp[0].split(":");
                String temp2[] = line.trim().split(" ");
                String kicker = nick[1]; // The guy who kicked the bad guy
                String chan = temp2[2]; // The channel when it happen
                String victim = temp2[3]; // This is the kicked

                ProxiedPlayer Player = ProxyServer.getInstance().getPlayer(victim);
                Player.disconnect(new TextComponent("kicked"));
        }
    }
}
