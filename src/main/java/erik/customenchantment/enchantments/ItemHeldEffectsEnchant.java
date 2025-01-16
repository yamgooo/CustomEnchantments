package erik.customenchantment.enchantments;

import cn.nukkit.Player;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.potion.Effect;

import java.util.HashMap;
import java.util.Map;

public class ItemHeldEffectsEnchant extends CustomEnchantment implements ItemHeldEnchant {
    private final Effect[] givenEffects;
    private final Map<Long, Map<Integer, Effect>> trackedPlayers = new HashMap<>();

    public ItemHeldEffectsEnchant(int id, String name, String description, Enchantment.Rarity rarity, EnchantmentType type, Effect[] givenEffects) {
        super(id, name, description, rarity, type);
        this.givenEffects = givenEffects;

        for (Effect effect : this.givenEffects) {
            effect.setDuration(Integer.MAX_VALUE);
        }
    }

    @Override
    public void onHeld(Player player, int level) {
        long playerId = player.getId();
        trackedPlayers.putIfAbsent(playerId, new HashMap<>());

        for (Effect effectInstance : givenEffects) {
            Effect clonedEffect = effectInstance.clone();
            clonedEffect.setAmplifier(level);
            player.addEffect(clonedEffect);
            trackedPlayers.get(playerId).put(System.identityHashCode(clonedEffect), clonedEffect);
        }
    }

    @Override
    public void onUnHeld(Player player, int level) {
        long playerId = player.getId();
        Map<Integer, Effect> playerEffects = trackedPlayers.getOrDefault(playerId, new HashMap<>());

        for (Map.Entry<Integer, Effect> entry : playerEffects.entrySet()) {
            Effect effect = entry.getValue();

            Effect activeEffect = player.getEffect(effect.getId());
            if (activeEffect != null && System.identityHashCode(activeEffect) == entry.getKey()) {
                player.removeEffect(effect.getId());
                playerEffects.remove(entry.getKey());
            }
        }

        if (playerEffects.isEmpty()) {
            trackedPlayers.remove(playerId);
        }
    }
}
