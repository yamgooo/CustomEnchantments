package erik.customenchantment.enchantments;

import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemAxeDiamond;
import cn.nukkit.item.ItemChestplateDiamond;
import cn.nukkit.item.ItemDurable;
import cn.nukkit.item.enchantment.EnchantmentType;
import erik.customenchantment.Enchantment;

public class HellforgedCustomEnchant extends CustomEnchantment implements MovementEnchant {

    public HellforgedCustomEnchant(int id, String name, String description, Rarity rarity, EnchantmentType type) {
        super(id, name, description, rarity, type);
    }

    @Override
    public void onMove(PlayerMoveEvent event, int level, Item item) {
        if (!(item instanceof ItemDurable)) {
            return;
        }

        if (item.getDamage() <= 0) {
            return;
        }

        int chance = (this.getMaxLevel() - level) * 20;
        if ((int) (Math.random() * chance) != 0) {
            return;
        }

        item.setDamage(Math.max(0, item.getDamage() - 1));
    }
}
