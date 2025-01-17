package erik.customenchantment.enchantments;

import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.item.Item;

public interface MovementEnchant {
    void onMove(PlayerMoveEvent event, int level);
}
