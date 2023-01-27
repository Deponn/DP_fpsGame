package dp_fpsgame.dp_fpsgame;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;


public final class DP_fpsGame extends JavaPlugin implements Listener {

    @Override
    public void onLoad(){

    }
    @Override
    public void onEnable() {
        getLogger().info("aaaa");
        getServer().getPluginManager().registerEvents(this, this);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Snowball) {
            // ダメージ量の設定
            getLogger().info("aaaa");
            e.setDamage(5.0);
        }
    }

    @EventHandler
    public void onThrowSnowball(ProjectileLaunchEvent e){
        if(e.getEntity() instanceof Snowball){
            Vector velocity = e.getEntity().getVelocity();
            velocity.multiply(2);
            e.getEntity().setVelocity(velocity);
        }
    }
    @EventHandler
    public void onUseItem(PlayerInteractEvent e){
        if(Objects.equals(e.getItem(), new ItemStack(Material.DIAMOND))) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,120,1));
            e.getPlayer().damage(4);
        }
        if(Objects.equals(e.getItem(), new ItemStack(Material.IRON_INGOT))) {
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            new BukkitRunnable(){
                @Override
                public void run(){
                    e.getPlayer().setGameMode(GameMode.ADVENTURE);
                }
            }.runTaskLater(this,40);
        }
        
    }

}
