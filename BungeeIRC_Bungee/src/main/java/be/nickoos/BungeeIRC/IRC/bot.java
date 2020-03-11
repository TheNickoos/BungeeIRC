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

    public void New_client(String nick) {
        plugin.getLogger().info("New client for irc : " +nick);
        long ut2 = System.currentTimeMillis() / 1000L;
        String ip = "127.0.0.1";
        out.println(":" + plugin.getConfig().getString("link") + " NICK " + nick + " 1 " + ut2 + " " + nick + " " + ip + " " + plugin.getConfig().getString("link") + " :" + plugin.getConfig().getString("description"));
        out.println(":" + nick + " JOIN "+ plugin.getConfig().getString("channel"));
    }
    public void Drop_client(String nick) {
        plugin.getLogger().info("Lost client from irc : " +nick);
        out.println(":" + nick + " QUIT");
    }

    public void Message_Client(String nick, String message) {
        plugin.getLogger().info("Nouveau message pour irc : " +nick);
        out.println(":" + nick + " PRIVMSG #test :" +message);
    }
    public bot(BungeeIRC plugin) {
        this.plugin = plugin;

    }

    public void connect_irc() {
        try {
            plugin.getLogger().info("Trying connection...");

            socket = new Socket(plugin.getConfig().getString("server"), plugin.getConfig().getInt("port"));
            out =new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            in =new BufferedReader( new InputStreamReader(socket.getInputStream()));
            String line = null;
            out.println("PASS "+ plugin.getConfig().getString("pass"));
            out.println("SERVER "+ plugin.getConfig().getString("link") +" 1 :" + plugin.getConfig().getString("description"));
            long ut2 = System.currentTimeMillis() / 1000L;
            out.println(":"+ plugin.getConfig().getString("link") +" NICK " + plugin.getConfig().getString("botnick") +" 1 " + ut2 + " bot "+ plugin.getConfig().getString("link") +" "+ plugin.getConfig().getString("link") +" :"+ plugin.getConfig().getString("description"));
            out.println(":"+ plugin.getConfig().getString("botnick")+" MODE " + plugin.getConfig().getString("botnick")+" +oOSqsw");
            out.println(":"+ plugin.getConfig().getString("botnick")+" JOIN " + plugin.getConfig().getString("channel"));
            out.println(":"+ plugin.getConfig().getString("link") +" MODE " + plugin.getConfig().getString("channel") +" +oa "+plugin.getConfig().getString("botnick")+" "+plugin.getConfig().getString("botnick"));

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
