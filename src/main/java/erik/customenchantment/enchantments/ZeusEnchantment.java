package erik.customenchantment.enchantments;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;

import java.util.Random;

public class ZeusEnchantment extends CustomEnchantment implements MeleeEnchantment {

    public ZeusEnchantment(int id, String name, String description, Rarity rarity, EnchantmentType type) {
        super(id, name, description, rarity, type);
    }

    @Override
    public void onPostAttack(Entity attacker, Entity victim, int enchantmentLevel, float finalDamage) {
        if (!(attacker instanceof EntityHuman)) return;
        if (!(victim instanceof Player)) return;

        Random random = new Random();
        if (random.nextDouble() > (0.25 * enchantmentLevel)) return;

        Player player = (Player) victim;
        summonLightning(player.getPosition());

        double newHealth = player.getHealth() - 0.8;
        player.setHealth((float) Math.max(newHealth, 0));
    }

    private void summonLightning(Position position) {
        EntityLightning lighting = new EntityLightning(position.getChunk(), Entity.getDefaultNBT(position));
        lighting.spawnToAll();
        position.getLevel().addSound(position, Sound.ITEM_TRIDENT_THUNDER);
    }
}

