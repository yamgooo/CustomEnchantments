package erik.customenchantment.enchantments;

import cn.nukkit.entity.Entity;

public interface MeleeEnchantment {

    public abstract void onPostAttack(Entity attacker, Entity victim, int enchantmentLevel, float finalDamage);

}
