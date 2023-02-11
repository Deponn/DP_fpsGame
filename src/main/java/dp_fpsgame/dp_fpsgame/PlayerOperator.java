package dp_fpsgame.dp_fpsgame;

import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.PlayMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class PlayerOperator {

    private final String playerName;
    private PlayMode playMode;

    public PlayerOperator(Player player){
        this.playerName = player.getName();
        this.playMode = PlayMode.None;
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
        inventory.clear();
        InventoryMaker.setGameItems(inventory);
    }
    private void setNonGameInventory(){
        Player player = Bukkit.getPlayer(playerName);
        Inventory inventory = Objects.requireNonNull(player).getInventory();
        inventory.clear();
        InventoryMaker.addBook(inventory);
    }



}
