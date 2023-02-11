package dp_fpsgame.dp_fpsgame;

import dp_fpsgame.dp_fpsgame.Command.CmdName;
import dp_fpsgame.dp_fpsgame.Command.CommandSuggest;
import dp_fpsgame.dp_fpsgame.Command.ParserChangeProperties;
import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.MyProperties;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;


public final class DP_fpsGame extends JavaPlugin implements Listener {

    private boolean isEnabledPlugin;
    @Override
    public void onEnable() {
        isEnabledPlugin = false;
        getLogger().info("雪合戦プラグインが有効になりました。");
        getServer().getPluginManager().registerEvents(this, this);
        for(CmdName cmdName : CmdName.values()){
            Objects.requireNonNull(this.getCommand(cmdName.getCmd())).setTabCompleter(new CommandSuggest());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("雪合戦プラグインが無効になりました。");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        //コマンドの有効化無効化処理
        if (cmd.getName().equalsIgnoreCase(CmdName.EnableFPS.getCmd())) {
            isEnabledPlugin = true;
            if ((sender instanceof Player)) {
                sender.sendMessage("プラグイン有効化");
            }
            return true;
        }else if(cmd.getName().equalsIgnoreCase(CmdName.DisableFPS.getCmd())) {
            isEnabledPlugin = false;
            if ((sender instanceof Player)) {
                sender.sendMessage("プラグイン無効化");
            }
            return true;
        }
        if(isEnabledPlugin) {
            if (cmd.getName().equalsIgnoreCase(CmdName.ResetGame.getCmd())) {
                if ((sender instanceof Player)) {

                } else {

                }

                return true;
            }else if(cmd.getName().equalsIgnoreCase(CmdName.SetPlayMode.getCmd())){
                if ((sender instanceof Player)) {

                } else {
                    sender.sendMessage(ChatColor.DARK_GRAY + "このコマンドはプレイヤーのみが使えます。");
                }
                return true;
            }else if(cmd.getName().equalsIgnoreCase(CmdName.SetProperties.getCmd())){
                if ((sender instanceof Player)) {
                    ParserChangeProperties parser = ParserChangeProperties.parse(sender, args);
                    if (!parser.isSuccess()) {
                        // パース失敗
                        return true;
                    }
                    sender.sendMessage(parser.PropertiesName + "を" + parser.Value + "に設定しました。初期化すると反映されます");
                    MyProperties.PropertiesChange(parser.PropertiesName, parser.Value);
                } else {
                    sender.sendMessage(ChatColor.DARK_GRAY + "このコマンドはプレイヤーのみが使えます。");
                }
                return true;
            }
        }
        return true;
    }


    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Snowball) {


            String entityName =  e.getEntity().getName();
            getLogger().info(entityName);
            // ダメージ量の設定
            e.setDamage(5.0);
        }
    }

    @EventHandler
    public void onThrowEntity(ProjectileLaunchEvent e){
        if(e.getEntity() instanceof Snowball){
            Vector velocity = e.getEntity().getVelocity();
            velocity.multiply(2);
            e.getEntity().setVelocity(velocity);
        }else if(e.getEntity() instanceof ThrownPotion){
            Vector velocity = e.getEntity().getVelocity();
            velocity.multiply(2);
            e.getEntity().setVelocity(velocity);
        }
    }
    @EventHandler
    public void onClickInventory(InventoryClickEvent e){

    }

    public void onPlayerDead(PlayerDeathEvent e){

    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
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
        if(Objects.equals(e.getItem(), new ItemStack(Material.LAPIS_LAZULI))) {
            ItemStack potion = new ItemStack(Material.LINGERING_POTION);
            PotionMeta meta = (PotionMeta) potion.getItemMeta();
            Objects.requireNonNull(meta).addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 4), true);
            potion.setItemMeta(meta);
            ThrownPotion thrownPotion = e.getPlayer().launchProjectile(ThrownPotion.class);
            thrownPotion.setItem(potion);
        }
        if(Objects.equals(e.getItem(), new ItemStack(Material.REDSTONE))) {
            ItemStack enderPearl = new ItemStack(Material.ENDER_PEARL);
            EnderPearl thrownPearl = e.getPlayer().launchProjectile(EnderPearl.class);
            thrownPearl.setItem(enderPearl);
        }
        if(Objects.equals(e.getItem(), new ItemStack(Material.COAL))) {
            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);

            BookMeta bookMeta = (BookMeta) book.getItemMeta();
            Objects.requireNonNull(bookMeta).setTitle("test");
            bookMeta.setAuthor("test");
            BaseComponent[] page = new ComponentBuilder("bbb")
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/bbb"))
                    .create();
            bookMeta.spigot().addPage(page);

            book.setItemMeta(bookMeta);

            e.getPlayer().getInventory().addItem(book);
        }
    }


}

//高速化,回復、浮遊,盲目爆弾、ジャンプ,スペク化、ワープ,スキャン、全体回復,壁生成

//モク　透明になって逃げる

//高速化,回復、浮遊,盲目爆弾、ジャンプ,透明になって逃げる、モク,スキャン、全体回復,ワープ

//高速化,回復、　毒爆弾,盲目爆弾、ジャンプ,透明になって逃げる、スキャン,バフポーション