package dp_fpsgame.dp_fpsgame;

import dp_fpsgame.dp_fpsgame.Command.CmdName;
import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.Const;
import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.MyProperties;
import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.PlayMode;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class InventoryMaker {

    public static void addBook(Inventory inv){
        clear(inv);
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        Objects.requireNonNull(bookMeta).setTitle("モード選択");
        bookMeta.setAuthor("Deponn");
        BaseComponent[] page = new ComponentBuilder(Const.Exp).create();
        bookMeta.spigot().addPage(page);
        for(PlayMode mode :  PlayMode.values()) {
            if(PlayMode.isGameMode(mode)) {
                page = new ComponentBuilder(mode.getExp())
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/Dp" + CmdName.SetPlayMode + " " + mode.getString()))
                        .create();
                bookMeta.spigot().addPage(page);
            }
        }
        book.setItemMeta(bookMeta);
        inv.setItem(0,book);
    }

    public static void setGameItems(Inventory inv, PlayMode mode,int snowBallNum){
        clear(inv);
        ItemStack food = new ItemStack(Const.FOOD,Const.foodNum);
        ItemStack snowBall = new ItemStack(Const.SNOWBALL,snowBallNum);
        ItemStack ability1Item = new ItemStack(Const.Ability1_Item,1);
        ItemStack ability2Item = new ItemStack(Const.Ability2_Item,1);
        ItemStack ability1ProgressItem = new ItemStack(Const.Ability1_Progress_Item,Const.maxNum);
        ItemStack ability2ProgressItem = new ItemStack(Const.Ability2_Progress_Item,Const.maxNum);

        ItemMeta meta1 = ability1Item.getItemMeta();
        Objects.requireNonNull(meta1).setDisplayName(mode.getAb1Exp());
        ability1Item.setItemMeta(meta1);

        ItemMeta meta2 = ability2Item.getItemMeta();
        Objects.requireNonNull(meta2).setDisplayName(mode.getAb2Exp());
        ability2Item.setItemMeta(meta2);

        inv.setItem(Const.foodSlot,food);
        inv.setItem(Const.snowBallSlot,snowBall);
        inv.setItem(Const.abilitySlot1,ability1Item);
        inv.setItem(Const.abilitySlot2,ability2Item);
        inv.setItem(Const.progressSlot1,ability1ProgressItem);
        inv.setItem(Const.progressSlot2,ability2ProgressItem);
    }
    public static void addSnowBall(Inventory inv, int snowBallNum){
        ItemStack snowBall = new ItemStack(Const.SNOWBALL,snowBallNum);
        inv.setItem(Const.snowBallSlot,snowBall);
    }

    public static void useSnowBall(Inventory inv){
        int snowBallNum = Objects.requireNonNull(inv.getItem(Const.snowBallSlot)).getAmount();
        ItemStack snowBall = new ItemStack(Const.SNOWBALL,snowBallNum - 1);
        inv.setItem(Const.snowBallSlot,snowBall);
    }

    public static void clear(Inventory inventory){
        for (int i = 0; i < 36; i++) {
            inventory.clear(i);
        }
    }
}
