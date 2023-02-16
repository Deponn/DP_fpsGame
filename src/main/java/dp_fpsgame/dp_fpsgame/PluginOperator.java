package dp_fpsgame.dp_fpsgame;

import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.MyProperties;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginOperator {
    private final DP_fpsGame parent;
    private boolean isExist;
    private Map<String,PlayerOperator> players;
    private MyProperties prop;

    public PluginOperator(DP_fpsGame parent){
        this.isExist = false;
        this.parent = parent;
    }

    public void initialize(){
        isExist = true;
        players = new HashMap<>();
        List<World> worlds = Bukkit.getWorlds();
        for (World world : worlds){
            List<Player> playerList = world.getPlayers();
            for (Player player : playerList){
                if(PlayerOperator.isJoiningTeam(player)) {
                    setPlayer(player);
                }
            }
        }
        prop = new MyProperties();
    }

    public MyProperties getProp() {
        if (isExist) {
            return prop;
        }else {
            return null;
        }
    }

    public boolean isExist(){
        return isExist;
    }

    public PlayerOperator getPlayer(Player player) {
        if(isExist) {
            if(players.containsKey(player.getName())) {
                return players.get(player.getName());
            }
        }
        return null;
    }

    public boolean isExistPlayer(Player player){
        return players.containsKey(player.getName());
    }

    public void setPlayer(Player player){
        if(isExist){
            if(!players.containsKey(player.getName())) {
                players.put(player.getName(), new PlayerOperator(player,parent,this));
            }
        }
    }
}
