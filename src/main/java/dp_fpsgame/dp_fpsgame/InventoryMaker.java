package dp_fpsgame.dp_fpsgame;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Objects;

public class InventoryMaker {

    public static void addBook(Inventory inv){
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        Objects.requireNonNull(bookMeta).setTitle("test");
        bookMeta.setAuthor("test");
        BaseComponent[] page = new ComponentBuilder("bbb")
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/bbb"))
                .create();
        bookMeta.spigot().addPage(page);
        book.setItemMeta(bookMeta);
        inv.addItem(book);
    }

    public static void setGameItems(Inventory inv){

    }
}
