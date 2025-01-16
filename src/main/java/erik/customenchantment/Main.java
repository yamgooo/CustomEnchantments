package erik.customenchantment;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Identifier;
import cn.nukkit.utils.TextFormat;
import erik.customenchantment.enchantments.ArmorEffectEquipmentEnchant;
import erik.customenchantment.enchantments.ArmorEquipmentEnchant;
import erik.customenchantment.enchantments.SpeedEnchantment;

public class Main extends PluginBase
{

    public static Main INSTANCE;
    public static int SPEED = 134;
    public static final Identifier SPEED_ID = new Identifier("nukkit", "speed");


    @Override
    public void onLoad() {
        INSTANCE = this;
        try {
            Enchantment.register(new ArmorEffectEquipmentEnchant(
                    Main.SPEED,
                    "speed",
                    "Custom Armor Enchantment",
                    Enchantment.Rarity.UNCOMMON,
                    EnchantmentType.ARMOR,
                    new Effect[] {
                            Effect.getEffect(Effect.RESISTANCE)
                    }), true);

        }
        catch (Exception e) {
            this.getLogger().info(TextFormat.RED + "Failed to register speed enchantment! Error: " + e.getMessage());
        }
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new EnchantmentListener(), this);
        this.getLogger().info(TextFormat.GREEN + "Enabled Custom Enchantments!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabling Custom Enchant");
    }
}
