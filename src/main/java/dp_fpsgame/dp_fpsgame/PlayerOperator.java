package dp_fpsgame.dp_fpsgame;

import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.Const;
import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.PlayMode;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Objects;


public class PlayerOperator {
    private final DP_fpsGame parent;
    private final PluginOperator op;
    private final String playerName;
    private PlayMode playMode;
    private boolean isInvincible;
    private final String teamName;

    public PlayerOperator(Player player,DP_fpsGame parent,PluginOperator op){
        this.op = op;
        this.parent = parent;
        this.playerName = player.getName();
        setPlayerMode(PlayMode.None);
        this.isInvincible = false;
        this.teamName = Objects.requireNonNull(getJoiningTeam()).getName();
    }

    public void setPlayerMode(PlayMode mode){
        playMode = mode;
        if(PlayMode.isGameMode(mode)){
            setInGameInventory();
        }else {
            setNonGameInventory();
        }
    }

    private void setInGameInventory(){
        Player player = Bukkit.getPlayer(playerName);
        Inventory inventory = Objects.requireNonNull(player).getInventory();
        InventoryMaker.setGameItems(inventory,playMode);
    }
    private void setNonGameInventory() {
        Player player = Bukkit.getPlayer(playerName);
        Inventory inventory = Objects.requireNonNull(player).getInventory();
        InventoryMaker.addBook(inventory);
    }

    public boolean isInvincible(){
        return isInvincible;
    }

    public void reload(){
        Player player = Bukkit.getPlayer(playerName);
        if(PlayMode.isGameMode(playMode)) {
            Inventory inventory = Objects.requireNonNull(player).getInventory();
            InventoryMaker.addSnowBall(inventory);
        }
    }

    private Team getJoiningTeam() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = Objects.requireNonNull(scoreboardManager).getMainScoreboard();
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            if (team.hasEntry(playerName)) {
                return team;
            }
        }
        return null;
    }
    public String getJoiningTeamName(){
        return teamName;
    }
    public static boolean isJoiningTeam(Player player) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = Objects.requireNonNull(scoreboardManager).getMainScoreboard();
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            if (team.hasEntry(player.getName())) {
                return true;
            }
        }
        return false;
    }

    public void useAbility1() {
        Player player = Bukkit.getPlayer(playerName);
        Inventory inventory = Objects.requireNonNull(player).getInventory();
        ItemStack item = inventory.getItem(Const.progressSlot1);
        if (item != null && item.getAmount() == Const.maxNum) {
            int period = 10;
            if(playMode == PlayMode.Skirmisher){
                period = runFast();
            }else if(playMode == PlayMode.Controller){
                period = SlowPotion();
            }else if(playMode == PlayMode.Duelist){
                period = selfHeal();
            }else if(playMode == PlayMode.Supporter){
                period = BuffPotion();
            }
            inventory.setItem(Const.progressSlot1, new ItemStack(Const.Ability1_Progress_Item, 1));
            ProgressTask task1 = new ProgressTask(inventory, Const.progressSlot1, parent);
            task1.runTaskTimer(parent, 1, period);
        }

    }
    public void useAbility2(){
        Player player = Bukkit.getPlayer(playerName);
        Inventory inventory = Objects.requireNonNull(player).getInventory();
        ItemStack item = inventory.getItem(Const.progressSlot2);
        if (item != null && item.getAmount() == Const.maxNum) {
            int period = 10;
            if(playMode == PlayMode.Skirmisher){
                period = jumpHigher();
            }else if(playMode == PlayMode.Controller){
                period = BlindPotion();
            }else if(playMode == PlayMode.Duelist){
                period = runAway();
            }else if(playMode == PlayMode.Supporter){
                period = scan();
            }
            inventory.setItem(Const.progressSlot2, new ItemStack(Const.Ability2_Progress_Item, 1));
            ProgressTask task1 = new ProgressTask(inventory, Const.progressSlot2, parent);
            task1.runTaskTimer(parent, 1, period);
        }
    }

    private int runFast(){
        Objects.requireNonNull(Bukkit.getPlayer(playerName)).addPotionEffect(new PotionEffect(PotionEffectType.SPEED,120,1));
        return 5;
    }
    private int jumpHigher(){
        Objects.requireNonNull(Bukkit.getPlayer(playerName)).addPotionEffect(new PotionEffect(PotionEffectType.JUMP,40,5));
        return 5;
    }

    private int SlowPotion(){
        ItemStack potion = new ItemStack(Material.LINGERING_POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        Objects.requireNonNull(meta).addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 40, 4), true);
        potion.setItemMeta(meta);
        ThrownPotion thrownPotion = Objects.requireNonNull(Bukkit.getPlayer(playerName)).launchProjectile(ThrownPotion.class);
        thrownPotion.setItem(potion);
        return 10;
    }

    private int BlindPotion(){
        ItemStack potion = new ItemStack(Material.LINGERING_POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        Objects.requireNonNull(meta).addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 4), true);
        potion.setItemMeta(meta);
        ThrownPotion thrownPotion = Objects.requireNonNull(Bukkit.getPlayer(playerName)).launchProjectile(ThrownPotion.class);
        thrownPotion.setItem(potion);
        return 10;
    }

    private int selfHeal(){
        Objects.requireNonNull(Bukkit.getPlayer(playerName)).addPotionEffect(new PotionEffect(PotionEffectType.HEAL,20,2));
        return 10;
    }
    private int runAway(){
        Player player = Objects.requireNonNull(Bukkit.getPlayer(playerName));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,80,1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,80,1));
        new ParticleTask(player,17,Particle.EXPLOSION_NORMAL).runTaskTimer(parent,0,5);
        isInvincible = true;
        return 30;
    }

    private int BuffPotion(){
        ItemStack potion = new ItemStack(Material.LINGERING_POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        Objects.requireNonNull(meta).addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 1), true);
        potion.setItemMeta(meta);
        ThrownPotion thrownPotion = Objects.requireNonNull(Bukkit.getPlayer(playerName)).launchProjectile(ThrownPotion.class);
        thrownPotion.setItem(potion);
        return 20;
    }
    private int scan(){
        List<World> worlds = Bukkit.getWorlds();
        for(World world : worlds){
            List<Player> players = world.getPlayers();
            for(Player player: players){
                if(player.getLocation().distance(Objects.requireNonNull(Bukkit.getPlayer(playerName)).getLocation()) < 10.0) {
                    String myTeam = getJoiningTeamName();
                    String targetTeam = op.getPlayer(player).getJoiningTeamName();
                    if (!Objects.equals(myTeam, targetTeam)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 1));
                    }
                }
            }
        }
        return 20;
    }



}

//高速化,ジャンプ、　毒爆弾,盲目爆弾、回復,透明になって逃げる、スキャン,バフポーション
        /*
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

         */

//高速化,回復、浮遊,盲目爆弾、ジャンプ,スペク化、ワープ,スキャン、全体回復,壁生成

//モク　透明になって逃げる

//高速化,回復、浮遊,盲目爆弾、ジャンプ,透明になって逃げる、モク,スキャン、全体回復,ワープ

//高速化,回復、　毒爆弾,盲目爆弾、ジャンプ,透明になって逃げる、スキャン,バフポーション