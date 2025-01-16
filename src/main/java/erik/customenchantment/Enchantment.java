package erik.customenchantment;

import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.TextFormat;
import erik.customenchantment.commands.CustomEnchantmentCommand;
import erik.customenchantment.enchantments.ArmorEffectEquipmentEnchant;
import erik.customenchantment.enchantments.ItemHeldEffectsEnchant;

public class Enchantment extends PluginBase
{
    public static Enchantment instance;

    private EnchantmentRegistry enchantmentRegistry;

    @Override
    public void onLoad() {
        instance = this;
        enchantmentRegistry = EnchantmentRegistry.getInstance();

        registerEnchantments();
    }

    private void registerEnchantments() {
        try {
            enchantmentRegistry.registerEnchantments(
                    new ArmorEffectEquipmentEnchant(
                            EnchantmentConstants.SPEED,
                            "speed",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.UNCOMMON,
                            EnchantmentType.ARMOR,
                            new Effect[] {
                                    Effect.getEffect(Effect.SPEED)
                            }),
                    new ArmorEffectEquipmentEnchant(
                            EnchantmentConstants.FIRE_RESISTANCE,
                            "fire_resistance",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.RARE,
                            EnchantmentType.ARMOR,
                            new Effect[] {
                                    Effect.getEffect(Effect.FIRE_RESISTANCE)
                            }),
                    new ItemHeldEffectsEnchant(
                            EnchantmentConstants.STRENGTH,
                            "strength",
                            "Custom Item Held Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.VERY_RARE,
                            EnchantmentType.ALL,
                            new Effect[] {
                                    Effect.getEffect(Effect.STRENGTH)
                            })
            );

            getLogger().info(TextFormat.GREEN + "Successfully registered all enchantments!");
        } catch (IllegalStateException e) {
            getLogger().error(TextFormat.RED + "Failed to register enchantments: " + e.getMessage());
        }
    }



    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new EnchantmentListener(), this);
        this.getServer().getCommandMap().register("customenchantment", new CustomEnchantmentCommand());
        this.getLogger().info(TextFormat.GREEN + "Enabled Custom Enchantments!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabling Custom Enchant");
    }
}
