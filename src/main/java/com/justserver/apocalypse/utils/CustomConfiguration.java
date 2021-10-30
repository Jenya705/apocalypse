package com.justserver.apocalypse.utils;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class CustomConfiguration {
    public final Apocalypse plugin;
    public String name;
    public YamlConfiguration config;

    public CustomConfiguration(Apocalypse plugin, String name){
        this.plugin = plugin;
        this.name = name;
        File config = new File(plugin.getDataFolder(), name);
        if(!config.exists()){
            try {
                config.getParentFile().mkdir();
                config.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(config);
    }

    public void save() {
        File config = new File(plugin.getDataFolder(), name);
        try {
            this.config.save(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        File config = new File(plugin.getDataFolder(), name);
        this.config = YamlConfiguration.loadConfiguration(config);
    }

    public static void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
