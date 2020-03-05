package be.nickoos.BungeeIRC.IRC.Event;

import be.nickoos.BungeeIRC.BungeeIRC;

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
    }
}
