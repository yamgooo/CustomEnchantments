package erik.customenchantment;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.event.entity.EntityArmorChangeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import erik.customenchantment.enchantments.ArmorEquipmentEnchant;
import erik.customenchantment.enchantments.ItemHeldEnchant;
import erik.customenchantment.enchantments.CustomEnchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class EnchantmentListener implements Listener {

    private final Map<Long, Map<Integer, CustomEnchantment>> trackedArmorEnchants;
    private final Map<Long, Map<Integer, CustomEnchantment>> lastHeldEnchants;

    private EnchantmentRegistry enchantmentRegistry;

    public EnchantmentListener() {
        this.trackedArmorEnchants = new WeakHashMap<>();
        this.lastHeldEnchants = new WeakHashMap<>();

        this.enchantmentRegistry = EnchantmentRegistry.getInstance();
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        checkArmorInventory(player.getInventory().getArmorContents(), player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        long playerId = player.getId();

        Map<Integer, CustomEnchantment> heldEnchants = lastHeldEnchants.get(playerId);
        if (heldEnchants != null) {
            validateHeldEnchant(player, heldEnchants);
        }

        Map<Integer, CustomEnchantment> armorEnchants = trackedArmorEnchants.get(playerId);
        if (armorEnchants != null) {
            for (Enchantment enchant : armorEnchants.values()) {
                Optional<CustomEnchantment> optionalEnchantment = enchantmentRegistry.getEnchantment(enchant.getId());
                if (!optionalEnchantment.isPresent()) continue;
                CustomEnchantment customEnchantment = optionalEnchantment.get();
                if (!(customEnchantment instanceof ArmorEquipmentEnchant)) continue;
                ArmorEquipmentEnchant armorEnchant = (ArmorEquipmentEnchant) customEnchantment;
                armorEnchant.onRemove(player, enchant.getLevel());
            }
        }

        trackedArmorEnchants.remove(playerId);
        lastHeldEnchants.remove(playerId);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItem();
        long playerId = player.getId();

        if (!item.hasEnchantments()) {
            Map<Integer, CustomEnchantment> lastEnchants = lastHeldEnchants.get(playerId);
            if (lastEnchants != null) {
                validateHeldEnchant(player, lastEnchants);
                lastHeldEnchants.remove(playerId);
            }
            return;
        }

        Map<Integer, CustomEnchantment> tracking = new HashMap<>();
        for (Enchantment enchant : item.getEnchantments()) {
            Optional<CustomEnchantment> optionalEnchantment = enchantmentRegistry.getEnchantment(enchant.getId());
            if (!optionalEnchantment.isPresent()) continue;
            CustomEnchantment customEnchantment = optionalEnchantment.get();
            if (!(customEnchantment instanceof ItemHeldEnchant)) continue;
            tracking.put(enchant.getId(), customEnchantment);
        }

        Map<Integer, CustomEnchantment> lastEnchants = lastHeldEnchants.get(playerId);
        if (lastEnchants != null) {
            for (Enchantment enchant : lastEnchants.values()) {
                if (enchant instanceof ItemHeldEnchant) {
                    ((ItemHeldEnchant) enchant).onUnHeld(player, enchant.getLevel());
                }
            }
        }

        lastHeldEnchants.put(playerId, tracking);
        for (Enchantment enchant : tracking.values()) {
            if (enchant instanceof ItemHeldEnchant) {
                ((ItemHeldEnchant) enchant).onHeld(player, enchant.getLevel());
            }
        }
    }

    private void validateHeldEnchant(Player player, Map<Integer, CustomEnchantment> lastEnchants) {
        for (Enchantment enchant : lastEnchants.values()) {
            Optional<CustomEnchantment> optionalEnchantment = enchantmentRegistry.getEnchantment(enchant.getId());
            if (!optionalEnchantment.isPresent()) continue;
            CustomEnchantment customEnchantment = optionalEnchantment.get();
            if (!(customEnchantment instanceof ItemHeldEnchant)) continue;
            ItemHeldEnchant heldEnchant = (ItemHeldEnchant) customEnchantment;
            heldEnchant.onUnHeld(player, enchant.getLevel());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArmorChange(EntityArmorChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Item oldItem = event.getOldItem();
        Item newItem = event.getNewItem();
        int slot = event.getSlot();

        if (oldItem != null && oldItem.hasEnchantments()) {
            for (Enchantment enchant : oldItem.getEnchantments()) {
                Optional<CustomEnchantment> optionalEnchantment = enchantmentRegistry.getEnchantment(enchant.getId());
                if (!optionalEnchantment.isPresent()) continue;
                CustomEnchantment customEnchantment = optionalEnchantment.get();
                if (!(customEnchantment instanceof ArmorEquipmentEnchant)) continue;
                ArmorEquipmentEnchant armorEnchant = (ArmorEquipmentEnchant) customEnchantment;
                armorEnchant.onRemove(player, enchant.getLevel());
                removeTrackedEnchant(player.getId(), enchant.getId());
            }
        }

        if (newItem != null && newItem.hasEnchantments()) {
            for (Enchantment enchant : newItem.getEnchantments()) {
                Optional<CustomEnchantment> optionalEnchantment = enchantmentRegistry.getEnchantment(enchant.getId());
                if (!optionalEnchantment.isPresent()) continue;
                CustomEnchantment customEnchantment = optionalEnchantment.get();
                if (!(customEnchantment instanceof ArmorEquipmentEnchant)) continue;
                ArmorEquipmentEnchant armorEnchant = (ArmorEquipmentEnchant) customEnchantment;
                armorEnchant.onEquip(player, enchant.getLevel());
                addTrackedEnchant(player.getId(), enchant.getId(), armorEnchant);

            }
        }
    }

    private void checkArmorInventory(Item[] inventory, Player player) {
        Map<Integer, CustomEnchantment> tracking = new HashMap<>();

        for (Item item : inventory) {
            if (!item.hasEnchantments()) continue;

            for (Enchantment enchant : item.getEnchantments()) {
                Optional<CustomEnchantment> optionalEnchantment = enchantmentRegistry.getEnchantment(enchant.getId());
                if (!optionalEnchantment.isPresent()) continue;
                CustomEnchantment customEnchantment = optionalEnchantment.get();
                if (!(customEnchantment instanceof ArmorEquipmentEnchant)) continue;
                ArmorEquipmentEnchant armorEnchant = (ArmorEquipmentEnchant) customEnchantment;
                tracking.put(enchant.getId(), armorEnchant);
                armorEnchant.onEquip(player, enchant.getLevel());

            }
        }

        trackedArmorEnchants.put(player.getId(), tracking);
    }

    private void addTrackedEnchant(long playerId, int enchantId, CustomEnchantment enchant) {
        trackedArmorEnchants.computeIfAbsent(playerId, k -> new HashMap<>())
                .put(enchantId, enchant);
    }

    private void removeTrackedEnchant(long playerId, int enchantId) {
        Map<Integer, CustomEnchantment> playerEnchants = trackedArmorEnchants.get(playerId);
        if (playerEnchants != null) {
            playerEnchants.remove(enchantId);
            if (playerEnchants.isEmpty()) {
                trackedArmorEnchants.remove(playerId);
            }
        }
    }
}