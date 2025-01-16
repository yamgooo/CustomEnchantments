package erik.customenchantment.enchantments;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.potion.Effect;

public class SpeedEnchantment  extends ArmorEffectEquipmentEnchant {

    public SpeedEnchantment(int id, String name, String description, Enchantment.Rarity rarity, EnchantmentType type, Effect[] givenEffects) {
        super(id, name, description, rarity, type, givenEffects);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

}
