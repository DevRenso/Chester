package dev.deqrenso.chester;

import dev.deqrenso.chester.utils.StringUtils;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigFile extends YamlConfiguration {

    @Getter
    private final File file;

    public ConfigFile(String name) throws RuntimeException {
        this.file = new File(Chester.getInstance().getDataFolder()+"/data", name);
        if(!file.exists()){
            try {
                file.createNewFile();
            }
            catch(IOException ex){
            }
        }
        try {
            this.load(this.file);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public void reload() {
        try {
            this.load(this.file);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationSection getSection(String name) {
        return super.getConfigurationSection(name);
    }

    @Override
    public int getInt(String path) {
        return super.getInt(path, 0);
    }

    @Override
    public double getDouble(String path) {
        return super.getDouble(path, 0.0);
    }

    @Override
    public boolean getBoolean(String path) {
        return super.getBoolean(path, false);
    }

    @Override
    public String getString(String path) {
        return StringUtils.CC(super.getString(path, ""));
    }

    @Override
    public List<String> getStringList(String path) {
        return super.getStringList(path).stream().map(StringUtils::CC).collect(Collectors.toList());
    }
}