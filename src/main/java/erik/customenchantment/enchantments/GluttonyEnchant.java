package erik.customenchantment.enchantments;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.potion.Effect;

public class GluttonyEnchant extends CustomEnchantment implements MeleeEnchantment  {


    public GluttonyEnchant(int id, String name, String description, Rarity rarity, EnchantmentType type) {
        super(id, name, description, rarity, type);
    }

    @Override
    public void onPostAttack(Entity attacker, Entity victim, int enchantmentLevel, float finalDamage) {
        if (!(attacker instanceof Player)) return;

        Player player = (Player) attacker;

        double chance = 0.10 * enchantmentLevel;
        if (Math.random() > chance) return;

        int currentFood = player.getFoodData().getLevel();
        int maxFood = player.getFoodData().getMaxLevel();

        if (currentFood >= maxFood) return;

        player.getFoodData().addFoodLevel(1, 0.1f);
    }
}
