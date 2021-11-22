package dev.deqrenso.chester.commands;

import com.google.common.collect.Lists;
import dev.deqrenso.chester.Chester;
import dev.deqrenso.chester.ConfigFile;
import dev.deqrenso.chester.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ChesterCommand implements CommandExecutor {

    public ChesterCommand(){
        Chester.getInstance().getCommand("chester").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("chester.admin")) return false;
        if(args.length == 0){
            sender.sendMessage(StringUtils.CC("&cPlease give a one argument."));
            sender.sendMessage(StringUtils.CC("&7[&c*&7] &3&l/"+label+" give [ID]"));
            sender.sendMessage(StringUtils.CC("&7[&c*&7] &3&l/"+label+" remove [ID]"));
            sender.sendMessage(StringUtils.CC("&7[&c*&7] &3&l/"+label+" create"));
            sender.sendMessage(StringUtils.CC("&7[&c*&7] &3&l/"+label+" list"));
            return false;
        }
        if(args[0].equalsIgnoreCase("create")){
            UUID uuid = UUID.randomUUID();
            this.createChest(uuid);
            sender.sendMessage(StringUtils.CC("&aChest created. Unique ID: &7"+uuid));
            if(sender instanceof Player){
                Player p = (Player)sender;
                ItemStack chest = new ItemStack(Material.CHEST);
                ItemMeta chestMeta = chest.getItemMeta();
                chestMeta.setLore(Lists.newArrayList(StringUtils.CC(Chester.getInstance().getConfig().getString("VERIFY-LORE")+uuid)));
                chestMeta.setDisplayName(StringUtils.CC("&cExample name"));
                chest.setItemMeta(chestMeta);
                p.getInventory().addItem(chest);
            }
        }else if(args[0].equalsIgnoreCase("list")){
            for(int i =0; i<Chester.getInstance().getChests().size(); i++){
                sender.sendMessage(StringUtils.CC("&7Number: &a"+i+" &7Unique id: &a"+Chester.getInstance().getChests().get(i)));
            }
        }else if(args[0].equalsIgnoreCase("delete")){
            if(args.length < 2){
                sender.sendMessage(StringUtils.CC("&cPlease give a two argument."));
                return true;
            }
            if(Chester.getInstance().getChests().size() < Integer.valueOf(args[1])){
                sender.sendMessage(StringUtils.CC("&cArgument .'"+args[1]+"' is incorrect."));
                return true;
            }
            sender.sendMessage(StringUtils.CC("&aSuccesfully remove "+Chester.getInstance().getChests().get(Integer.valueOf(args[1]))+" UUID!"));
            Chester.getInstance().getChests().remove(Integer.valueOf(args[1]));
            Chester.getInstance().getConfig().set("CHESTS-UUID", Chester.getInstance().getChests());
            Chester.getInstance().saveConfig();
            Chester.getInstance().reloadConfig();
            new File(Chester.getInstance().getDataFolder()+"/data", Chester.getInstance().getChests().get(Integer.valueOf(args[1]))+".yml").delete();
        }else if(args[0].equalsIgnoreCase("give")){
            if(!(sender instanceof Player)) return false;
            if(args.length < 2){
                sender.sendMessage(StringUtils.CC("&cPlease give a two argument."));
                return true;
            }
            if(Chester.getInstance().getChests().size() < Integer.valueOf(args[1])){
                sender.sendMessage(StringUtils.CC("&cArgument .'"+args[1]+"' is incorrect."));
                return true;
            }
            sender.sendMessage(StringUtils.CC("&aSuccesfully give "+Chester.getInstance().getChests().get(Integer.valueOf(args[1]))+" UUID!"));
            ItemStack chest = new ItemStack(Material.CHEST);
            ItemMeta chestMeta = chest.getItemMeta();
            chestMeta.setLore(Lists.newArrayList(StringUtils.CC(Chester.getInstance().getConfig().getString("VERIFY-LORE")+Chester.getInstance().getChests().get(Integer.valueOf(args[1])))));
            chestMeta.setDisplayName(StringUtils.CC("&cExample name"));
            chest.setItemMeta(chestMeta);
            ((Player)sender).getInventory().addItem(chest);
            return true;
        }else{

        }
        return false;
    }

    private void createChest(UUID uuid) {
        if(!Chester.getInstance().getChests().contains(uuid.toString())){
            List chestList = Chester.getInstance().getConfig().getStringList("CHESTS-UUID");
            chestList.add(uuid.toString());
            Chester.getInstance().getConfig().set("CHESTS-UUID", chestList);
            Chester.getInstance().saveConfig();
            Chester.getInstance().reloadConfig();
            File f = new File(Chester.getInstance().getDataFolder()+"/data", uuid+".yml");
            if(!f.exists()){
                try {
                    f.createNewFile();
                    ConfigFile configFile = new ConfigFile(uuid.toString()+".yml");
                    configFile.set("CHEST-ITEMS", new ItemStack[]{});
                    configFile.save();
                }
                catch(IOException ex){
                }
            }

        }
    }
}
