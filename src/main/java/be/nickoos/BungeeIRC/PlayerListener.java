package be.nickoos.BungeeIRC;

import be.nickoos.BungeeIRC.IRC.bot;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {
    private BungeeIRC plugin;

    public PlayerListener(BungeeIRC plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onLogin(PostLoginEvent event) {
        plugin.getLogger().info("New client for Bungee");
        String IP = event.getPlayer().getAddress().getAddress().getHostAddress();
        String Username = event.getPlayer().getName();
        new bot(plugin).New_client(Username);
    }
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {

    }

}
