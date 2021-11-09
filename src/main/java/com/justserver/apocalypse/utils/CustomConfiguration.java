package com.justserver.apocalypse.utils;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;

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
                InputStream inputStream = plugin.getResource(name);
                String data = readFromInputStream(inputStream);
                PrintWriter writer = new PrintWriter(config, StandardCharsets.UTF_8);
                writer.print(data);
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.config = YamlConfiguration.loadConfiguration(config);
        } catch (IllegalArgumentException e){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload confirm");
            return;
        }

    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
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
