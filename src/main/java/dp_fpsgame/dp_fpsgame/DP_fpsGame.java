package dp_fpsgame.dp_fpsgame;

import dp_fpsgame.dp_fpsgame.Command.CmdName;
import dp_fpsgame.dp_fpsgame.Command.CommandSuggest;
import dp_fpsgame.dp_fpsgame.Command.ParserChangeProperties;
import dp_fpsgame.dp_fpsgame.Command.ParserSetPlayMode;
import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.Const;
import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.MyProperties;
import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.PlayMode;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;


public final class DP_fpsGame extends JavaPlugin implements Listener {

    private boolean isEnabledPlugin;
    private PluginOperator op;
    @Override
    public void onEnable() {
        isEnabledPlugin = false;
        getLogger().info("雪合戦プラグインが有効になりました。");
        getServer().getPluginManager().registerEvents(this, this);
        for(CmdName cmdName : CmdName.values()){
            Objects.requireNonNull(this.getCommand(cmdName.getCmd())).setTabCompleter(new CommandSuggest());
        }
        op = new PluginOperator(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("雪合戦プラグインが無効になりました。");
    }
    public boolean isEnabledPlugin(){
        return isEnabledPlugin;
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
                    sender.sendMessage("初期化しました。");
                }
                op.initialize();
                return true;
            }else if(cmd.getName().equalsIgnoreCase(CmdName.SetPlayMode.getCmd())){
                if ((sender instanceof Player)) {
                    ParserSetPlayMode parser = ParserSetPlayMode.Parse(sender, args);
                    if (!parser.isSuccess()) {
                        // パース失敗
                        return true;
                    }
                    Player player = (Player) sender;
                    if(op.isExist()) {
                        op.setPlayer(player);
                        op.getPlayer(player).setPlayerMode(parser.playMode);
                        sender.sendMessage(ChatColor.GREEN + parser.playMode.getExp());
                    }
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
    public void onHit(EntityDamageByEntityEvent e) {
        if (isEnabledPlugin) {
            if (e.getDamager() instanceof Snowball) {
                String entityName = e.getEntity().getName();
                Player player = Bukkit.getPlayer(entityName);
                if (player != null) {
                    if (op.isExist()) {
                        if (op.isExistPlayer(player)) {
                            Player shooter = (Player) Objects.requireNonNull(((Snowball) e.getDamager()).getShooter());
                            if (!Objects.equals(op.getPlayer(shooter).getJoiningTeamName(), op.getPlayer(player).getJoiningTeamName())) {
                                if (!op.getPlayer(player).isInvincible()) {
                                    // ダメージ量の設定
                                    e.setDamage(op.getProp().Damage);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onThrowEntity(ProjectileLaunchEvent e){
        if(isEnabledPlugin) {
            if (e.getEntity() instanceof Snowball) {
                Vector velocity = e.getEntity().getVelocity();
                velocity.multiply(Const.Velocity);
                e.getEntity().setVelocity(velocity);
            } else if (e.getEntity() instanceof ThrownPotion) {
                Vector velocity = e.getEntity().getVelocity();
                velocity.multiply(Const.Velocity);
                e.getEntity().setVelocity(velocity);
            }
        }
    }
    @EventHandler
    public void onClickInventory(InventoryClickEvent e){
        if(isEnabledPlugin) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (isEnabledPlugin) {
            Player player = e.getPlayer();
            try {
                Bukkit.getLogger().info(Objects.requireNonNull(Bukkit.getPlayer(e.getPlayer().getName())).getName());
            }catch (Exception error){
                Bukkit.getLogger().info(error.getMessage());
            }
            if (op.isExist()) {
                if (op.isExistPlayer(player)) {
                    new BukkitRunnable(){
                        @Override
                        public void run(){
                            op.getPlayer(player).setPlayerMode(PlayMode.None);
                        }
                    }.runTaskLater(this,2);

                }
            }
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (isEnabledPlugin) {
            Action action = e.getAction();
            Player player = e.getPlayer();
            Block block = e.getClickedBlock();
            ItemStack item = e.getItem();
            if (op.isExist() && op.isExistPlayer(player)) {
                if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                    try {
                        Material blockType = Objects.requireNonNull(block).getType();
                        if (blockType.equals(Material.SNOW_BLOCK) || blockType.equals(Material.SNOW)) {
                            op.getPlayer(player).reload();
                        }
                    } catch (Exception exception) {
                        getLogger().info(exception.getMessage());
                    }
                } else {
                    if(item != null) {
                        if (item.getType() == new ItemStack(Const.Ability1_Item).getType()) {
                            op.getPlayer(player).useAbility1();
                        } else if (item.getType() == new ItemStack(Const.Ability2_Item).getType()) {
                            op.getPlayer(player).useAbility2();
                        }
                    }
                }
            }
        }
    }

}

