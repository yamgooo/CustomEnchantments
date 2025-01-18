package erik.customenchantment.enchantments;

import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.item.Item;

public interface MovementEnchant {
    public abstract void onMove(PlayerMoveEvent event, int level, Item item);
}
