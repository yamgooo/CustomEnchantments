package erik.customenchantment.enchantments;

import cn.nukkit.Player;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.potion.Effect;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ArmorEffectEquipmentEnchant extends ArmorEquipmentEnchant {
    private final Effect[] givenEffects;
    private final Map<Long, Map<Effect, Integer>> trackedPlayers = new ConcurrentHashMap<>();

    public ArmorEffectEquipmentEnchant(int id, String name, String description, Rarity rarity, EnchantmentType type, Effect[] givenEffects) {
        super(id, name, description, rarity, type);
        this.givenEffects = new Effect[givenEffects.length];
        for (int i = 0; i < givenEffects.length; i++) {
            this.givenEffects[i] = givenEffects[i].clone();
            this.givenEffects[i].setDuration(Integer.MAX_VALUE);
        }
    }

    @Override
    public void onEquip(Player player, int level) {
        if (player == null) return;

        long playerId = player.getId();
        Map<Effect, Integer> playerEffects = trackedPlayers.computeIfAbsent(playerId, k -> new HashMap<>());

        for (Effect baseEffect : givenEffects) {
            Effect effect = baseEffect.clone();
            effect.setAmplifier(level - 1); // TODO: check if is required the forced amplifier
            player.addEffect(effect);
            playerEffects.put(effect, System.identityHashCode(effect));
        }
    }

    @Override
    public void onRemove(Player player, int level) {
        if (player == null) return;

        long playerId = player.getId();
        Map<Effect, Integer> playerEffects = trackedPlayers.get(playerId);
        if (playerEffects == null) return;

        playerEffects.forEach((effect, hashCode) -> {
            int effectId = effect.getId();
            if (player.hasEffect(effectId)) {
                Effect activeEffect = player.getEffect(effectId);
                if (System.identityHashCode(activeEffect) == hashCode) {
                    player.removeEffect(effectId);
                }
            }
        });

        trackedPlayers.remove(playerId);
    }
}