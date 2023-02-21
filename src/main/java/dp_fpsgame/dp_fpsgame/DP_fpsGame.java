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
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;


public final class DP_fpsGame extends JavaPlugin implements Listener {

    private boolean isEnabledPlugin;
    private PluginOperator op;

    @Override
    public void onEnable() {
        isEnabledPlugin = false;
        getLogger().info("雪合戦プラグインが有効になりました。");
        getServer().getPluginManager().registerEvents(this, this);
        for (CmdName cmdName : CmdName.values()) {
            Objects.requireNonNull(this.getCommand(cmdName.getCmd())).setTabCompleter(new CommandSuggest());
        }
        op = new PluginOperator(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("雪合戦プラグインが無効になりました。");
    }

    public boolean isEnabledPlugin() {
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
        } else if (cmd.getName().equalsIgnoreCase(CmdName.DisableFPS.getCmd())) {
            isEnabledPlugin = false;
            if ((sender instanceof Player)) {
                sender.sendMessage("プラグイン無効化");
            }
            return true;
        }
        if (isEnabledPlugin) {
            if (cmd.getName().equalsIgnoreCase(CmdName.InitializeGame.getCmd())) {
                if ((sender instanceof Player)) {
                    sender.sendMessage("初期化しました。");
                }
                op.initialize();
                return true;
            } else if (cmd.getName().equalsIgnoreCase(CmdName.SetPlayMode.getCmd())) {
                ParserSetPlayMode parser = ParserSetPlayMode.Parse(sender, args);
                if (!parser.isSuccess()) {
                    // パース失敗
                    return true;
                }
                if ((sender instanceof Player)) {
                    Player player = (Player) sender;
                    if (op.isExist()) {
                        op.setPlayer(player);
                        op.getPlayer(player).setPlayerMode(parser.playMode);
                    }
                } else if (sender instanceof BlockCommandSender) {
                    try {
                        BlockCommandSender commandBlock = (BlockCommandSender) sender;
                        List<World> worlds = Bukkit.getWorlds();
                        for (World world : worlds) {
                            List<Player> players = world.getPlayers();
                            for (Player targetPlayer : players) {
                                if (targetPlayer.getLocation().distance(commandBlock.getBlock().getLocation()) < 3.0) {
                                    if (op.isExist()) {
                                        op.setPlayer(targetPlayer);
                                        op.getPlayer(targetPlayer).setPlayerMode(parser.playMode);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        getLogger().info(e.getMessage());
                    }
                } else {
                    sender.sendMessage(ChatColor.DARK_GRAY + "このコマンドはコマブロとプレイヤーのみが使えます。");
                }
                return true;
            } else if (cmd.getName().equalsIgnoreCase(CmdName.SetProperties.getCmd())) {
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
                        Player shooter = (Player) Objects.requireNonNull(((Snowball) e.getDamager()).getShooter());
                        if (op.isExistPlayer(player) && op.isExistPlayer(shooter)) {
                            if (!Objects.equals(op.getPlayer(shooter).getJoiningTeamName(), op.getPlayer(player).getJoiningTeamName())) {
                                if (!op.getPlayer(player).isInvincible()) {
                                    // ダメージ量の設定
                                    e.setDamage(op.getProp().Damage);
                                    shooter.getWorld().playSound(shooter.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,0.15f,1);
                                }
                            }
                        }
                    }
                }else {
                    e.setDamage(20.0);
                }
            }
        }
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent e) {
        if (isEnabledPlugin) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
        event.getPlayer().updateInventory();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (isEnabledPlugin) {
            Player player = e.getPlayer();
            if (op.isExist()) {
                if (op.isExistPlayer(player)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            op.getPlayer(player).setPlayerMode(PlayMode.None);
                        }
                    }.runTaskLater(this, 2);

                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (isEnabledPlugin) {
            boolean flag = false;
            Player player = e.getPlayer();
            Block block = e.getClickedBlock();
            ItemStack item = e.getItem();
            if (e.getHand() == EquipmentSlot.OFF_HAND) {
                e.setCancelled(true);
            }else if (op.isExist() && op.isExistPlayer(player)) {
                if (item != null) {
                    if (item.getType() == new ItemStack(Const.Ability1_Item).getType()) {
                        op.getPlayer(player).useAbility1();
                        flag = true;
                    } else if (item.getType() == new ItemStack(Const.Ability2_Item).getType()) {
                        op.getPlayer(player).useAbility2();
                        flag = true;
                    }
                }
                if (block != null) {
                    if(!flag) {
                        Material blockType = block.getType();
                        if (blockType.equals(Material.SNOW_BLOCK) || blockType.equals(Material.SNOW)) {
                            op.getPlayer(player).reload();
                            flag = true;
                        }
                    }
                }
                if (item != null) {
                    if(!flag) {
                        if (item.getType() == new ItemStack(Const.SNOWBALL).getType()) {
                            op.getPlayer(player).lunchSnowBall();
                        }
                    }
                }
            }
        }
    }
}

/*
opじゃないと選択できない
死ぬと職業リセット
弾めっちゃ早く打てる
ジャンプ効果が切れるとダメージ食らっちゃう
ジョブの名前
クールタイムの説明
企画の見栄え的なものを意識したい
リロードできない場所があったら面白い
鎧も脱げちゃう


・攻城戦の舞台設定→企画枠採用のさきちゃんやﾀﾜｹさんの意見を聞いたほうがいいかも
[23:24]
・ジョブの名前？→舞台設定と合わせて、分かりやすいものにしたほうがいいかも？
[23:24]
・攻城戦鯖での仕様→ぽてちさんとかに確認
[23:25]
・舞台設定の如何によっては建築勢へのヘルプが必要かも
[23:25]
・KUNさんのとっさの要望に応えられるような余白？
[23:26]
例）チームを増やす、死んだらスペク、分数
[23:27]
例）チケット制を想定していたが、大将戦やビーコン戦になる可能性？
[23:28]
これら含めて、長くやってきた参加勢に聞いたほうがいい

⚠咳が止まらない — 昨日 23:30
完成近くになったら参加勢にテストプレイを頼む
[23:37]
・死んだらジョブがリセットされる仕様が果たしてKUNさんに合うのかどうか（煩わしく思われないか）


⚠咳が止まらない — 昨日 23:59
・雪（弾薬）を多く配給して、その代わりに弾薬の補給場所を限定的にするという選択肢



虚空中打てちゃう
回復強すぎ
壁高くしよう
耐性長くしたい
浮遊のジャンプなくす
リスキルなくしたい
Fキーで連射できちゃう

 */



