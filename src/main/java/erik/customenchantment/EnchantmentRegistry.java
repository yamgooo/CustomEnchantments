package erik.customenchantment;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Identifier;
import erik.customenchantment.enchantments.CustomEnchantment;

import java.util.*;

public class EnchantmentRegistry {
    private static EnchantmentRegistry instance;
    private final Map<Integer, CustomEnchantment> enchantments;

    private EnchantmentRegistry() {
        this.enchantments = new HashMap<>();
    }

    public static EnchantmentRegistry getInstance() {
        if (instance == null) instance = new EnchantmentRegistry();
        return instance;
    }

    public void registerEnchantments(CustomEnchantment... enchantments)
    {
        for (CustomEnchantment enchantment : enchantments) {
            registerEnchantment(enchantment);
        }
    }

    public void registerEnchantment(CustomEnchantment enchantment) throws IllegalStateException {
        int id = enchantment.getId();
        if (enchantments.containsKey(id)) throw new IllegalStateException("Enchantment with ID " + id + " is already registered!");

        try {
            Enchantment.register(enchantment, true);
            enchantments.put(id, enchantment);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to register enchantment: " + e.getMessage(), e);
        }
    }

    public Optional<CustomEnchantment> getEnchantment(int id) {
        return Optional.ofNullable(enchantments.get(id));
    }

    public Map<Integer, CustomEnchantment> getAllEnchantments() {
        return new HashMap<>(enchantments);
    }

    public CustomEnchantment[] getEnchantments(Item item) {
        if (!item.hasEnchantments()) {
            return new CustomEnchantment[] {};
        } else {
            List<CustomEnchantment> enchantments = new ArrayList<>();
            ListTag<CompoundTag> ench = item.getNamedTag().getList("ench", CompoundTag.class);

            for(CompoundTag entry : ench.getAll()) {
                Optional<CustomEnchantment> e = this.getEnchantment(entry.getShort("id"));
                if (e.isEmpty()) continue;
                var enchantment = e.get();
                enchantment.setLevel(entry.getShort("lvl"));
                 enchantments.add(enchantment);

            }

            return enchantments.toArray(new CustomEnchantment[0]);
        }
    }
}