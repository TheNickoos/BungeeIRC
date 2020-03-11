package be.nickoos.BungeeIRC;

import be.nickoos.BungeeIRC.IRC.bot;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {
    private BungeeIRC plugin;

    public PlayerListener(BungeeIRC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        // This is the event when a new player join the game
        String IP = event.getPlayer().getAddress().getAddress().getHostAddress();
        String Username = event.getPlayer().getName();
        new bot(plugin).New_client(Username, IP);
    }
    @EventHandler
    public void onLeave(PlayerDisconnectEvent event) {
        // This is the event when a player leave the game
        String Username = event.getPlayer().getName();
        new bot(plugin).Drop_client(Username);
    }

}
