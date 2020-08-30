package example.tutorial1;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public final class Tutorial1 extends JavaPlugin implements Listener {

    public List<UUID> players;
    @Override
    public void onEnable() {

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "AFK Kick : Enable");
        getServer().getPluginManager().registerEvents(this, this);
        this.players = new ArrayList<UUID>();

    }

    @Override
    public void onDisable() {

    }

    public Inventory invenCreate(int n){
        Inventory inventory = Bukkit.createInventory(null,9,ChatColor.RED + "AFK Check");
        ItemStack Red = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack Green = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta rmeta = Red.getItemMeta();
        rmeta.setDisplayName(ChatColor.RED + "✘");
        ItemMeta gmeta = Green.getItemMeta();
        gmeta.setDisplayName(ChatColor.GREEN + "✔");
        Red.setItemMeta(rmeta);
        Green.setItemMeta(gmeta);
        for(int i = 0; i <= 8;i++){
            if(i==n){
                inventory.setItem(n,Green);
            }else {
                inventory.setItem(i,Red);
            }

        }
        return inventory;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!players.contains(player.getUniqueId())) {
            Random rng = new Random();
            int n = rng.nextInt(9);
            player.openInventory(invenCreate(n));
            player.sendMessage("not have player" + players.contains(player.getUniqueId()));
            players.add(player.getUniqueId());
            player.sendMessage("not have player" + players.contains(player.getUniqueId()));
            player.sendMessage("UUID : " + players);
        }
    }
    @EventHandler
    public void onInvopen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        player.sendMessage("เปิดคลัง");

        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "AFK Check")) {
            if (players.contains(player.getUniqueId())) {
                BukkitRunnable rtask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "AFK Check")) {
                            if (players.contains(player.getUniqueId())) {

                                player.sendMessage("คุณโดนเตะตัดขา");
                                player.kickPlayer("เตะออกเซิฟ");
                                players.remove(player.getUniqueId());
                                player.closeInventory();


                            }
                        }
                    }
                };
                rtask.runTaskLater(this, 100);
                player.sendMessage("Runnable = " + rtask.getTaskId());
            } else {

            }
        }
    }

    @EventHandler
    public void onClickinven(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) {
            return;
        }
        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "AFK Check")) {
            if (!(event.getClickedInventory().getHolder() instanceof Player)) {
                if (event.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
                    players.remove(player.getUniqueId());
                    event.setCancelled(true);
                    player.closeInventory();
                }
                else if(event.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE)){

                    event.setCancelled(true);
                    player.closeInventory();

                }
            }
            else {

                event.setCancelled(true);

            }
            player.updateInventory();
        }


    }

    @EventHandler
    public void onCloseinven(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        player.sendMessage(ChatColor.AQUA + "haveplayer : " + players);
        player.sendMessage("UUID : " + player.getUniqueId());
        player.sendMessage("name = " + event.getView().getTitle());
        if(event.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "AFK Check")){
            if(players.contains(player.getUniqueId())){
                if(event.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "AFK Check")) {
                    player.sendMessage(ChatColor.AQUA + "Anti AFK2");
                    BukkitRunnable sss = new BukkitRunnable() {
                        @Override
                        public void run() {
                                    player.openInventory(event.getInventory());
                        }
                    };
                    sss.runTaskLater(this,10);
                }
            }
            else if (!(players.contains(player.getUniqueId()))){

                player.sendMessage(ChatColor.AQUA + "Anti AFK" + ChatColor.WHITE + " : " + ChatColor.GREEN + "Success");
            }
            else {
                player.sendMessage("Check else 2");
            }
        }
        else {
            player.sendMessage("Check else 1");
        }
    }




}
