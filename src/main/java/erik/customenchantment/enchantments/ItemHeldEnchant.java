package erik.customenchantment.enchantments;

import cn.nukkit.Player;
import cn.nukkit.item.enchantment.EnchantmentType;

public interface ItemHeldEnchant {

    public abstract void onHeld(Player player, int level);

    public abstract void onUnHeld(Player player, int level);
}
