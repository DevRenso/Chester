package dev.deqrenso.chester;

import dev.deqrenso.chester.commands.ChesterCommand;
import dev.deqrenso.chester.listeners.ChesterListener;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class Chester extends JavaPlugin {

    @Getter
    public static Chester instance;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        File dir = new File(this.getDataFolder()+"/data");
        if (!dir.exists()){
            dir.mkdirs();
        }
        loadChests(dir);
        new ChesterCommand();
        new ChesterListener();
    }

    @Override
    public void onDisable() {
        instance = null;
    }
    private static void loadChests(File dir){
        List<String> chests = Chester.getInstance().getConfig().getStringList("CHESTS-UUID");
        chests.forEach(m ->{
            File f = new File(dir, m+".yml");
            if(!f.exists()){
                try {
                    f.createNewFile();
                }
                catch(IOException ex){
                }
            }
            new ConfigFile(m+".yml");
        });
    }
    public List<String> getChests(){
        return getConfig().getStringList("CHESTS-UUID");
    }

}
