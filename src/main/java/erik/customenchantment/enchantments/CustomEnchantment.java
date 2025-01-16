package erik.customenchantment.enchantments;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.utils.Identifier;
import cn.nukkit.utils.TextFormat;

import javax.annotation.Nullable;
import java.util.Map;

public class CustomEnchantment  extends Enchantment {
    public final String description;
    public final String name;
    public final int id;

    public CustomEnchantment(int id, String name, String description, Enchantment.Rarity rarity, EnchantmentType type) {
        super(id, name, rarity, type);
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getLoreLine(int level) {
        Map<Integer, String> romanNumerals = Map.of(
                1, "I", 2, "II", 3, "III", 4, "IV", 5, "V",
                6, "VI", 7, "VII", 8, "VIII", 9, "IX", 10, "X"
        );

        Map<Integer, String> rarityColors = Map.of(
                Rarity.COMMON.ordinal(), TextFormat.YELLOW.toString(),
                Rarity.UNCOMMON.ordinal(), TextFormat.MINECOIN_GOLD.toString(),
                Rarity.RARE.ordinal(), TextFormat.RED.toString(),
                Rarity.VERY_RARE.ordinal(), TextFormat.DARK_RED.toString()
        );

        String rarityColor = rarityColors.getOrDefault(getRarity().ordinal(), TextFormat.WHITE.toString());
        String romanLevel = romanNumerals.getOrDefault(level, String.valueOf(level));

        return rarityColor + getName() + " " + romanLevel;
    }

    @Nullable
    @Override
    public Identifier getIdentifier()  { return new Identifier("enchantments", name.toLowerCase()); }
}
