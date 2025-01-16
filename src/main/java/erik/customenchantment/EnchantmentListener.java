package erik.customenchantment;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.entity.EntityArmorChangeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import erik.customenchantment.enchantments.ArmorEquipmentEnchant;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentListener implements Listener {

    private final Map<Long, Map<Integer, ArmorEquipmentEnchant>> trackedArmorEnchants = new HashMap<>();

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        trackPlayerArmor(player);
    }

    @EventHandler
    public void onArmorChange(EntityArmorChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        trackPlayerArmor(player);
    }

    private void trackPlayerArmor(Player player) {
        long playerId = player.getId();
        Map<Integer, ArmorEquipmentEnchant> currentTracked = new HashMap<>();
        Item[] armorInventory = player.getInventory().getArmorContents();

        for (int slot = 0; slot < armorInventory.length; slot++) {
            Item armorPiece = armorInventory[slot];
            if (armorPiece.hasEnchantments()) {
                for (Enchantment enchantment : armorPiece.getEnchantments()) {
                    System.out.println("Found enchantment: " + enchantment.getClass().getName());
                    if (enchantment instanceof ArmorEquipmentEnchant) {
                        System.out.println("Custom enchantment detected: " + ((ArmorEquipmentEnchant) enchantment).getName());
                    } else {
                        System.out.println("Unknown enchantment detected: " + enchantment.getClass().getName());
                    }
                }

            }
        }

        Map<Integer, ArmorEquipmentEnchant> previouslyTracked = trackedArmorEnchants.getOrDefault(playerId, new HashMap<>());
        previouslyTracked.keySet().stream()
                .filter(slot -> !currentTracked.containsKey(slot))
                .forEach(slot -> previouslyTracked.get(slot).onRemove(player, 0));

        trackedArmorEnchants.put(playerId, currentTracked);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        long playerId = player.getId();

        Map<Integer, ArmorEquipmentEnchant> trackedEnchants = trackedArmorEnchants.remove(playerId);
        if (trackedEnchants != null) {
            trackedEnchants.values().forEach(enchant -> enchant.onRemove(player, 0));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Item item = Item.get(Item.DIAMOND_BOOTS);
        item.addEnchantment(Enchantment.getEnchantment(Main.SPEED));
        player.getInventory().setItemInHand(item);
    }
}
