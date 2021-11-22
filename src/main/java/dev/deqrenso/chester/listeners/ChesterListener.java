package dev.deqrenso.chester.listeners;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.deqrenso.chester.Chester;
import dev.deqrenso.chester.ConfigFile;
import dev.deqrenso.chester.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChesterListener implements Listener {

    public ChesterListener(){
        Bukkit.getPluginManager().registerEvents(this, Chester.getInstance());
    }
    public Map<UUID, String> uuids = Maps.newHashMap();
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Chester.getInstance().getChests().forEach(m -> {
            if(e.getPlayer().getItemInHand().getItemMeta().hasLore() && e.getPlayer().getItemInHand().getItemMeta().getLore().contains(StringUtils.CC(Chester.getInstance().getConfig().getString("VERIFY-LORE")+m))){
                e.setCancelled(true);
                return;
            }
        });
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(!Chester.getInstance().getChests().isEmpty()){
            for(String uuid : Chester.getInstance().getChests()){
                if(e.getPlayer().getItemInHand().getType() != Material.AIR &&
                        e.getPlayer().getItemInHand().getType() == Material.CHEST &&
                        e.getPlayer().getItemInHand().getItemMeta().hasLore() &&
                        !e.getPlayer().getItemInHand().getItemMeta().getLore().isEmpty() &&
                        e.getPlayer().getItemInHand().getItemMeta().getLore().contains(StringUtils.CC(Chester.getInstance().getConfig().getString("VERIFY-LORE")+uuid))){

                    if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) return;
                    Inventory inv = Bukkit.createInventory(null,9*Chester.getInstance().getConfig().getInt("CHEST-HOTBARS"), StringUtils.CC("&aChester Inventory"));
                    ConfigFile invFile = new ConfigFile(uuid+".yml");
                    List<ItemStack> invContents = (List<ItemStack>)invFile.get("CHEST-ITEMS");
                    ItemStack[] contents = invContents.toArray(new ItemStack[0]);
                    if(!invContents.isEmpty() && contents.length != 0){
                        inv.setContents(contents);
                    }
                    uuids.put(e.getPlayer().getUniqueId(), uuid);
                    p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1.5f, 0.5f);
                    p.openInventory(inv);
                    return;
                }
            }
        }

    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e){
        if(!Chester.getInstance().getConfig().getStringList("CHESTS-UUID").isEmpty()){
            for(String uuid : Chester.getInstance().getChests()){
                if(uuids.containsKey(e.getPlayer().getUniqueId()) && uuids.get(e.getPlayer().getUniqueId()).equalsIgnoreCase(uuid) && e.getInventory().getTitle().equalsIgnoreCase(StringUtils.CC("&aChester Inventory"))){
                    ConfigFile invFile = new ConfigFile(uuid + ".yml");
                    invFile.set("CHEST-ITEMS", null);
                    invFile.set("CHEST-ITEMS", e.getInventory().getContents());
                    invFile.save();
                    uuids.remove(e.getPlayer().getUniqueId());
                    ((Player)e.getPlayer()).playSound(e.getPlayer().getLocation(), Sound.CHEST_CLOSE, 1.5f, 0.5f);
                    return;
                }
            }
        }


    }
}
