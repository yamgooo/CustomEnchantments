package erik.customenchantment.enchantments;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.potion.Effect;

public class EffectsOnOpponentDeathEnchant extends CustomEnchantment implements MeleeEnchantment  {

    private final Effect[] givenEffects;

    public EffectsOnOpponentDeathEnchant(int id, String name, String description, Rarity rarity, EnchantmentType type, Effect... givenEffects) {
        super(id, name, description, rarity, type);
        this.givenEffects = givenEffects;
    }

    @Override
    public void onPostAttack(Entity attacker, Entity victim, int enchantmentLevel, float finalDamage) {
        if (victim instanceof EntityLiving) {
            EntityLiving livingVictim = (EntityLiving) victim;
            if (livingVictim.getHealth() > finalDamage) {
                return;
            }
        }

        if (!attacker.isAlive()) {
            return;
        }

        for (Effect effect : givenEffects) {
            Effect clonedEffect = effect.clone();
            attacker.addEffect(clonedEffect);
        }
    }
}
