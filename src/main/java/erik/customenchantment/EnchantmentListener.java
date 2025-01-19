package erik.customenchantment;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.event.entity.EntityArmorChangeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.ItemAxeNetherite;
import cn.nukkit.item.ItemTool;
import erik.customenchantment.enchantments.*;

import javax.tools.Tool;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class EnchantmentListener implements Listener {

    private static class TrackedEnchant {
        final CustomEnchantment enchantment;
        final int slot;

        TrackedEnchant(CustomEnchantment enchantment, int slot) {
            this.enchantment = enchantment;
            this.slot = slot;
        }
    }

    private final Map<Long, Map<Integer, TrackedEnchant>> trackedArmorEnchants;
    private final Map<Long, Map<Integer, CustomEnchantment>> lastHeldEnchants;
    private final EnchantmentRegistry enchantmentRegistry;

    public EnchantmentListener() {
        this.trackedArmorEnchants = new WeakHashMap<>();
        this.lastHeldEnchants = new WeakHashMap<>();
        this.enchantmentRegistry = EnchantmentRegistry.getInstance();
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        checkArmorInventory(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        long playerId = player.getId();

        // Clean up held enchants
        Map<Integer, CustomEnchantment> heldEnchants = lastHeldEnchants.get(playerId);
        if (heldEnchants != null) {
            for (CustomEnchantment enchant : heldEnchants.values()) {
                if (enchant instanceof ItemHeldEnchant) {
                    ((ItemHeldEnchant) enchant).onUnHeld(player, enchant.getLevel());
                }
            }
        }

        // Clean up armor enchants
        Map<Integer, TrackedEnchant> armorEnchants = trackedArmorEnchants.get(playerId);
        if (armorEnchants != null) {
            for (TrackedEnchant tracked : armorEnchants.values()) {
                if (tracked.enchantment instanceof ArmorEquipmentEnchant) {
                    ((ArmorEquipmentEnchant) tracked.enchantment).onRemove(player, tracked.enchantment.getLevel());
                }
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

        Map<Integer, CustomEnchantment> lastEnchants = lastHeldEnchants.get(playerId);
        if (lastEnchants != null) {
            for (CustomEnchantment enchant : lastEnchants.values()) {
                if (enchant instanceof ItemHeldEnchant) {
                    ((ItemHeldEnchant) enchant).onUnHeld(player, enchant.getLevel());
                }
            }
        }

        if (!item.hasEnchantments()) {
            lastHeldEnchants.remove(playerId);
            return;
        }

        Map<Integer, CustomEnchantment> tracking = new HashMap<>();
        for (CustomEnchantment enchant : enchantmentRegistry.getEnchantments(item)) {
            if (!(enchant instanceof ItemHeldEnchant)) continue;
            tracking.put(enchant.getId(), enchant);
            ((ItemHeldEnchant) enchant).onHeld(player, enchant.getLevel());
        }

        if (!tracking.isEmpty()) {
            lastHeldEnchants.put(playerId, tracking);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArmorChange(EntityArmorChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        var inv = player.getInventory();
        Item oldItem = event.getOldItem();
        Item newItem = event.getNewItem();
        var invSize = inv.getSize();
        var slotSize = event.getSlot();
        int slot = slotSize - invSize;

        if (oldItem != null && oldItem.hasEnchantments()) {
            for (CustomEnchantment enchant : enchantmentRegistry.getEnchantments(oldItem)) {
                removeTrackedEnchant(player.getId(), enchant.getId());
                if (enchant instanceof ArmorEquipmentEnchant) {
                    ((ArmorEquipmentEnchant) enchant).onRemove(player, enchant.getLevel());
                }
            }
        }

        if (newItem != null && newItem.hasEnchantments()) {
            for (CustomEnchantment enchant : enchantmentRegistry.getEnchantments(newItem)) {
                addTrackedEnchant(player.getId(), enchant.getId(), enchant, slot);
                if (enchant instanceof ArmorEquipmentEnchant) {
                    ((ArmorEquipmentEnchant) enchant).onEquip(player, enchant.getLevel());
                }
            }
        }
    }

    private void checkArmorInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        Map<Integer, TrackedEnchant> tracking = new HashMap<>();

        for (int slot = 0; slot < 4; slot++) {
            Item item = inventory.getArmorItem(slot);
            if (!item.hasEnchantments()) continue;

            for (CustomEnchantment enchant : enchantmentRegistry.getEnchantments(item)) {
                tracking.put(enchant.getId(), new TrackedEnchant(enchant, slot));
                if (enchant instanceof ArmorEquipmentEnchant) {
                    ((ArmorEquipmentEnchant) enchant).onEquip(player, enchant.getLevel());
                }
            }
        }

        if (!tracking.isEmpty()) {
            trackedArmorEnchants.put(player.getId(), tracking);
        }
    }

    private void addTrackedEnchant(long playerId, int enchantId, CustomEnchantment enchant, int slot) {
        trackedArmorEnchants.computeIfAbsent(playerId, k -> new HashMap<>())
                .put(enchantId, new TrackedEnchant(enchant, slot));
    }

    private void removeTrackedEnchant(long playerId, int enchantId) {
        Map<Integer, TrackedEnchant> playerEnchants = trackedArmorEnchants.get(playerId);
        if (playerEnchants != null) {
            playerEnchants.remove(enchantId);
            if (playerEnchants.isEmpty()) {
                trackedArmorEnchants.remove(playerId);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        double distance = event.getFrom().distance(event.getTo());
        if (distance < 0.001) return;

        long playerId = player.getId();

        Map<Integer, TrackedEnchant> armorEnchants = trackedArmorEnchants.get(playerId);
        if (armorEnchants != null && !armorEnchants.isEmpty()) {
            PlayerInventory inventory = player.getInventory();

            for (TrackedEnchant tracked : armorEnchants.values()) {
                if (!(tracked.enchantment instanceof MovementEnchant)) continue;

                Item currentItem = inventory.getArmorItem(tracked.slot);
                if (!currentItem.hasEnchantment(tracked.enchantment.getId())) continue;

                ((MovementEnchant) tracked.enchantment).onMove(event, tracked.enchantment.getLevel(), currentItem);
            }
        }

        // TODO: Handle held item movement enchants only in items with HOLDEABLE tag
        /*Item heldItem = player.getInventory().getItemInHand();
        if (heldItem.hasEnchantments()) {
            for (CustomEnchantment enchant : enchantmentRegistry.getEnchantments(heldItem)) {
                if (!(enchant instanceof MovementEnchant)) continue;
                ((MovementEnchant) enchant).onMove(event, enchant.getLevel(), );
            }
        }*/
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Item heldItem = damager.getInventory().getItemInHand();

        if (!heldItem.hasEnchantments() || !(heldItem instanceof Tool)) {
            return;
        }


        for (CustomEnchantment enchant : enchantmentRegistry.getEnchantments(heldItem)) {
            if (!(enchant instanceof MeleeEnchantment)) continue;

            ((MeleeEnchantment) enchant).onPostAttack(
                    damager,
                    event.getEntity(),
                    enchant.getLevel(),
                    event.getFinalDamage()
            );
        }
    }

    @EventHandler
    public void handleDeath(PlayerDeathEvent event) {
        Player entity = event.getEntity();
        EntityDamageEvent lastDamage = entity.getLastDamageCause();

        if (!(lastDamage instanceof EntityDamageByEntityEvent)) {
            return;
        }

        Entity damager = ((EntityDamageByEntityEvent) lastDamage).getDamager();
        if (!(damager instanceof Player)) {
            return;
        }

        Player player = (Player) damager;
        PlayerInventory inventory = player.getInventory();

        for (Item item : inventory.getArmorContents()) {
            if (!item.hasEnchantments()) {
                continue;
            }

            for (CustomEnchantment enchant : enchantmentRegistry.getEnchantments(item)) {
                if (!(enchant instanceof MeleeEnchantment)) {
                    continue;
                }

                ((MeleeEnchantment) enchant).onPostAttack(
                        player,
                        entity,
                        enchant.getLevel(),
                        0
                );
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Item item = event.getItem();

        if (!item.hasEnchantments() || !(item instanceof ItemTool)) {
            return;
        }

        for (CustomEnchantment enchant : enchantmentRegistry.getEnchantments(item)) {
            if (!(enchant instanceof BlockBreakEnchant)) {
                continue;
            }
            ((BlockBreakEnchant) enchant).onBreak(event, enchant.getLevel());
        }
    }
}