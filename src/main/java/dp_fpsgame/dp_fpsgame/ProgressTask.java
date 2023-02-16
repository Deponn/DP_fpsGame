package dp_fpsgame.dp_fpsgame;

import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.Const;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


public class ProgressTask extends BukkitRunnable {

    private final Inventory inv;
    private final int slot;
    private final DP_fpsGame parent;

    public ProgressTask(Inventory inventory, int slot, DP_fpsGame parent) {
        this.inv = inventory;
        this.slot = slot;
        this.parent = parent;
    }

    @Override
    public void run() {
        if (parent.isEnabledPlugin()) {
            ItemStack item = inv.getItem(slot);
            if (item != null) {
                int itemNum = item.getAmount();
                itemNum += 3;
                if (itemNum < Const.maxNum) {
                    item.setAmount(itemNum);
                } else if (itemNum == Const.maxNum) {
                    item.setAmount(itemNum);
                    cancel();
                } else {
                    cancel();
                }
            } else {
                cancel();
            }
        } else {
            cancel();
        }
    }
}
