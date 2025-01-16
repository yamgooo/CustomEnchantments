package erik.customenchantment;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.Identifier;
import erik.customenchantment.enchantments.CustomEnchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
}