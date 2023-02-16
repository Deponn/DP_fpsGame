package dp_fpsgame.dp_fpsgame;

import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.Const;
import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.PlayMode;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
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
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Objects;


public class PlayerOperator {
    private final DP_fpsGame parent;
    private final PluginOperator op;
    private final String playerName;
    private PlayMode playMode;
    private boolean isInvincible;
    private final String teamName;
    private boolean snowBallFlag = true;

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
            Objects.requireNonNull(Bukkit.getPlayer(playerName)).sendMessage(ChatColor.GREEN + mode.getExp());
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

    public void lunchSnowBall(){
        Player player = Bukkit.getPlayer(playerName);
        if(player != null) {
            if(snowBallFlag) {
                Snowball snowball = player.launchProjectile(Snowball.class);
                Vector velocity = snowball.getVelocity();
                velocity.multiply(Const.Velocity);
                snowball.setVelocity(velocity);
                InventoryMaker.useSnowBall(player.getInventory());
                playSound(player, Sound.ENTITY_SNOWBALL_THROW);
                snowBallFlag = false;
                new BukkitRunnable(){
                    @Override
                    public void run(){
                        snowBallFlag = true;
                    }
                }.runTaskLater(parent,5);
            }
        }
    }
    public void useAbility1() {
        Player player = Bukkit.getPlayer(playerName);
        Inventory inventory = Objects.requireNonNull(player).getInventory();
        ItemStack item = inventory.getItem(Const.progressSlot1);
        if (item != null && item.getAmount() >= Const.maxNum) {
            int period = 10;
            if(playMode == PlayMode.Skirmisher){
                period = runFast();
            }else if(playMode == PlayMode.Controller){
                period = LevitationPotion();
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
        if (item != null && item.getAmount() >= Const.maxNum) {
            int period = 10;
            if(playMode == PlayMode.Skirmisher){
                period = jumpHigher();
            }else if(playMode == PlayMode.Controller){
                period = PoisonPotion();
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
        Player player = Bukkit.getPlayer(playerName);
        if(player != null) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1));
            playSound(player,Sound.ENTITY_ARROW_SHOOT);
        }
        return 5;
    }
    private int jumpHigher(){
        Player player = Bukkit.getPlayer(playerName);
        if(player != null) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 5));
            playSound(player,Sound.ENTITY_ARROW_SHOOT);
        }
        return 5;
    }

    private int LevitationPotion(){
        Player player = Bukkit.getPlayer(playerName);
        if(player != null) {
            ItemStack potion = new ItemStack(Material.LINGERING_POTION);
            PotionMeta meta = (PotionMeta) potion.getItemMeta();
            if (meta != null) {
                meta.addCustomEffect(new PotionEffect(PotionEffectType.LEVITATION, 40, 5), true);
                meta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 80, 5), true);
            }
            potion.setItemMeta(meta);
            ThrownPotion thrownPotion = player.launchProjectile(ThrownPotion.class);
            thrownPotion.setItem(potion);
            playSound(player,Sound.ENTITY_LINGERING_POTION_THROW);
        }
        return 10;
    }

    private int PoisonPotion(){
        Player player = Bukkit.getPlayer(playerName);
        if(player != null) {
            ItemStack potion = new ItemStack(Material.LINGERING_POTION);
            PotionMeta meta = (PotionMeta) potion.getItemMeta();
            if (meta != null) {
                meta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1), true);
                meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 40, 2), true);
                meta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 40, 2), true);
            }
            potion.setItemMeta(meta);
            ThrownPotion thrownPotion = player.launchProjectile(ThrownPotion.class);
            thrownPotion.setItem(potion);
            Vector velocity = thrownPotion.getVelocity();
            velocity.multiply(Const.Velocity);
            thrownPotion.setVelocity(velocity);
            playSound(player,Sound.ENTITY_LINGERING_POTION_THROW);
        }
        return 25;
    }

    private int selfHeal(){
        Player player = Bukkit.getPlayer(playerName);
        if(player != null) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 1));
            playSound(player,Sound.ENTITY_PLAYER_LEVELUP);
        }
        return 10;
    }
    private int runAway(){
        Player player = Bukkit.getPlayer(playerName);
        if(player != null) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 80, 1));
            new ParticleTask(player, 17, Particle.EXPLOSION_NORMAL).runTaskTimer(parent, 0, 5);
            isInvincible = true;
            playSound(player,Sound.ENTITY_ARROW_SHOOT);
            new BukkitRunnable(){
                @Override
                public void run(){
                    isInvincible = false;
                }
            }.runTaskLater(parent,80);
        }
        return 25;
    }

    private int BuffPotion(){
        Player player = Bukkit.getPlayer(playerName);
        if(player != null) {
            ItemStack potion = new ItemStack(Material.LINGERING_POTION);
            PotionMeta meta = (PotionMeta) potion.getItemMeta();
            Objects.requireNonNull(meta).addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 1), true);
            potion.setItemMeta(meta);
            ThrownPotion thrownPotion = player.launchProjectile(ThrownPotion.class);
            thrownPotion.setItem(potion);
            playSound(player, Sound.ENTITY_LINGERING_POTION_THROW);
        }
        return 15;
    }
    private int scan(){
        Player player = Bukkit.getPlayer(playerName);
        if(player != null) {
            List<World> worlds = Bukkit.getWorlds();
            for (World world : worlds) {
                List<Player> players = world.getPlayers();
                for (Player targetPlayer : players) {
                    if (targetPlayer.getLocation().distance(player.getLocation()) < 30.0) {
                        String myTeam = getJoiningTeamName();
                        String targetTeam = op.getPlayer(targetPlayer).getJoiningTeamName();
                        if (!Objects.equals(myTeam, targetTeam)) {
                            targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 1));
                            playSound(targetPlayer,Sound.ENTITY_IRON_GOLEM_DAMAGE);
                        }
                    }
                }
            }
            playSound(player,Sound.ENTITY_ARROW_SHOOT);
        }
        return 20;
    }

    private void playSound(Player player,Sound sound){
        player.getWorld().playSound(player.getLocation(),sound,1,1);
    }

}