package be.nickoos.BungeeIRC;
import be.nickoos.BungeeIRC.IRC.bot;

import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public class BungeeIRC extends Plugin implements Listener {

    private Configuration configuration;

    public Configuration getConfig() {
        return configuration;
    }
    public void reloadConfig(String paramString) throws IOException {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(loadResource("config.yml"));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Exception while reading config", e);
        }
    }

    public void loadConfig() {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(loadResource("config.yml"));
            getLogger().info("Configuration loaded !");
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Exception while reading config", e);
        }
    }

    public File loadResource(String resource) {
        File folder = getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = getResourceAsStream(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Exception while writing default config", e);
        }
        return resourceFile;
    }

    @Override
    public void onEnable() {
        getLogger().info("We're gonna take over the world of IRC !");
        getProxy().registerChannel( "bungeeirc:core" );
        loadConfig();
        new bot(this).connect_irc();
        getProxy().getPluginManager().registerListener(this, new PlayerListener(this));
        getProxy().getPluginManager().registerListener(this, new PluginMessageReceiver(this));

    }

    @Override
    public void onDisable()
    {
        configuration = null;
        getLogger().info( "Plugin disabled!" );
    }
}
