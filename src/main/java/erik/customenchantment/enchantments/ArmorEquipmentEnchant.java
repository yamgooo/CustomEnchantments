package erik.customenchantment.enchantments;

import cn.nukkit.Player;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public abstract class ArmorEquipmentEnchant extends CustomEnchantment {

    public ArmorEquipmentEnchant(int id, String name, String description, Enchantment.Rarity rarity, EnchantmentType type) {
        super(id, name, description, rarity, type);
    }

    public abstract void onEquip(Player player, int level);

    public abstract void onRemove(Player player, int level);
}
