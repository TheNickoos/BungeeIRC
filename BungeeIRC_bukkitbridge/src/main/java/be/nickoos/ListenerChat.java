package be.nickoos;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ListenerChat implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat1(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("message");
        out.writeUTF(message);
        BungeeIRC.getPlugin().getLogger().info("Message jeu->IRC");
        player.sendPluginMessage(BungeeIRC.getPlugin(), "bungeeirc:core", out.toByteArray());

    }
}
