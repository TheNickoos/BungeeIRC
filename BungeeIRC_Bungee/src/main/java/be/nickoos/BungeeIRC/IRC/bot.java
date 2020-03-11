package be.nickoos.BungeeIRC.IRC;

import be.nickoos.BungeeIRC.BungeeIRC;
import be.nickoos.BungeeIRC.IRC.Event.Event;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;

public class bot implements Runnable{
    BungeeIRC plugin;

    public static Socket socket = null;
    public static PrintWriter out;
    public static BufferedReader in = null;
    Thread thread = new Thread(this);

    public void New_client(String nick, String ip) {
        // This is called when a new player join the game, he will also join IRC
        long ut2 = System.currentTimeMillis() / 1000L;
        out.println(":" + plugin.getConfig().getString("link") + " NICK " + nick + " 1 " + ut2 + " " + nick + " " + ip + " " + plugin.getConfig().getString("link") + " :" + plugin.getConfig().getString("description"));
        out.println(":" + nick + " JOIN "+ plugin.getConfig().getString("channel"));
    }
    public void Drop_client(String nick) {
        // If the player leave the game, we also make him leave IRC
        out.println(":" + nick + " QUIT");
    }

    public void Message_Client(String nick, String message) {
        // If the player say something in game, he also talk in IRC
        out.println(":" + nick + " PRIVMSG "+ plugin.getConfig().getString("channel") + " :" +message);
    }
    public bot(BungeeIRC plugin) {
        this.plugin = plugin;

    }

    public void connect_irc() {
        try {
            plugin.getLogger().info("Trying connection...");

            // We connect our socket on IRC
            socket = new Socket(plugin.getConfig().getString("server"), plugin.getConfig().getInt("port"));
            out =new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            in =new BufferedReader( new InputStreamReader(socket.getInputStream()));
            String line = null;

            // Unreal IRCD 4.0 ask the password first then our link information
            out.println("PASS "+ plugin.getConfig().getString("pass"));
            out.println("SERVER "+ plugin.getConfig().getString("link") +" 1 :" + plugin.getConfig().getString("description"));

            // This is the way to make UNIXTIME in java
            long ut2 = System.currentTimeMillis() / 1000L;

            // We connect the main bot
            out.println(":"+ plugin.getConfig().getString("link") +" NICK " + plugin.getConfig().getString("botnick") +" 1 " + ut2 + " bot "+ plugin.getConfig().getString("link") +" "+ plugin.getConfig().getString("link") +" :"+ plugin.getConfig().getString("description"));

            // We make the main bot join the main channel and the service channel
            out.println(":"+ plugin.getConfig().getString("botnick")+" MODE " + plugin.getConfig().getString("botnick")+" +oOSqsw");
            out.println(":"+ plugin.getConfig().getString("botnick")+" JOIN " + plugin.getConfig().getString("channel"));
            out.println(":"+ plugin.getConfig().getString("link") +" MODE " + plugin.getConfig().getString("channel") +" +oa "+plugin.getConfig().getString("botnick")+" "+plugin.getConfig().getString("botnick"));
            out.println(":"+ plugin.getConfig().getString("botnick")+" JOIN " + plugin.getConfig().getString("servicechannel"));
            out.println(":"+ plugin.getConfig().getString("link") +" MODE " + plugin.getConfig().getString("servicechannel") +" +oa "+plugin.getConfig().getString("botnick")+" "+plugin.getConfig().getString("botnick"));

            // We put theses modes for no external messages and only ops can set the topic
            out.println(":"+ plugin.getConfig().getString("link") +" MODE " + plugin.getConfig().getString("servicechannel") +" +nt");

            // We put theses modes for no external messages, only ops can set the topic and secret channel
            out.println(":"+ plugin.getConfig().getString("link") +" MODE " + plugin.getConfig().getString("servicechannel") +" +nts");

            thread.start();
        }
        catch (UnknownHostException e) {
            plugin.getLogger().log(Level.SEVERE, "A problem has been occurred. Read the console log.");
            e.printStackTrace();
        }
        catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "A problem has been occurred. Read the console log.");
            e.printStackTrace();
        }
    }
    public void run(){
        String line = null;
        try{
            while (thread != null && (line = in.readLine()) != null) {
                if (plugin.getConfig().getBoolean("irc-debug")) { System.out.println("[BungeeIRC Debug] "+ line); }
                new Event(plugin ,line, out);
            }
        }
        catch(IOException ioe){
            plugin.getLogger().log(Level.SEVERE, "A problem has been occurred. Read the console log.");
            ioe.printStackTrace();
        }
    }

}
