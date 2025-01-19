package erik.customenchantment;

import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.TextFormat;
import erik.customenchantment.commands.CustomEnchantmentCommand;
import erik.customenchantment.enchantments.*;

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
                    new ItemHeldEffectsEnchant(
                            EnchantmentConstants.OXYGENATE,
                            "oxygenate",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.RARE,
                            EnchantmentType.BREAKABLE,
                            Effect.getEffect(Effect.WATER_BREATHING)
                    ),
                    new GluttonyEnchant(
                            EnchantmentConstants.GLUTTONY,
                            "gluttony",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.RARE,
                            EnchantmentType.BREAKABLE
                    ),
                    new ChopperEnchant(
                            EnchantmentConstants.CHOPPER,
                            "chopper",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.VERY_RARE,
                            EnchantmentType.BREAKABLE
                    ),
                    new ZeusEnchantment(
                            EnchantmentConstants.ZEUS,
                            "zeus",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.UNCOMMON,
                            EnchantmentType.BREAKABLE
                    ),
                    new ExperienceEnchant(
                            EnchantmentConstants.EXPERIENCE,
                            "experience",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.RARE,
                            EnchantmentType.BREAKABLE
                    ),
                    new EffectsOnOpponentDeathEnchant(
                            EnchantmentConstants.RECOVER,
                            "recover",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.VERY_RARE,
                            EnchantmentType.BREAKABLE,
                            Effect.getEffect(Effect.REGENERATION).setAmplifier(1).setDuration(100),
                            Effect.getEffect(Effect.ABSORPTION).setDuration(100)
                    ),
                    new AutoSmeltEnchant(
                            EnchantmentConstants.AUTO_SMELT,
                            "autosmelt",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.RARE,
                            EnchantmentType.BREAKABLE
                    ),
                    new HellforgedCustomEnchant(
                            EnchantmentConstants.HELLFORGED,
                            "hellforged",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.VERY_RARE,
                            EnchantmentType.BREAKABLE
                    ),
                    new ImplantsCustomEnchant(
                            EnchantmentConstants.IMPLANTS,
                            "implants",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.VERY_RARE,
                            EnchantmentType.ARMOR
                    ),
                    new ArmorEffectEquipmentEnchant(
                            EnchantmentConstants.INFRARED,
                            "infrared",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.UNCOMMON,
                            EnchantmentType.ARMOR,
                            Effect.getEffect(Effect.NIGHT_VISION)),
                    new ArmorEffectEquipmentEnchant(
                            EnchantmentConstants.MERMAID,
                            "mermaid",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.UNCOMMON,
                            EnchantmentType.ARMOR,
                            new Effect[] {
                                    Effect.getEffect(Effect.WATER_BREATHING)
                            }),
                    new ArmorEffectEquipmentEnchant(
                            EnchantmentConstants.INVISIBILITY,
                            "invisibility",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.VERY_RARE,
                            EnchantmentType.ARMOR,
                            Effect.getEffect(Effect.INVISIBILITY)),
                    new ArmorEffectEquipmentEnchant(
                            EnchantmentConstants.SPEED,
                            "speed",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.UNCOMMON,
                            EnchantmentType.ARMOR,
                            Effect.getEffect(Effect.SPEED)),
                    new ArmorEffectEquipmentEnchant(
                            EnchantmentConstants.FIRE_RESISTANCE,
                            "fire_resistance",
                            "Custom Armor Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.RARE,
                            EnchantmentType.ARMOR,
                            Effect.getEffect(Effect.FIRE_RESISTANCE)),
                    new ItemHeldEffectsEnchant(
                            EnchantmentConstants.STRENGTH,
                            "strength",
                            "Custom Item Held Enchantment",
                            cn.nukkit.item.enchantment.Enchantment.Rarity.VERY_RARE,
                            EnchantmentType.ALL,
                            Effect.getEffect(Effect.STRENGTH))
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
