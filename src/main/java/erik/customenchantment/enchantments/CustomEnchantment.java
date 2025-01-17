package erik.customenchantment.enchantments;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Identifier;
import cn.nukkit.utils.TextFormat;

import javax.annotation.Nullable;
import java.util.Map;

public class CustomEnchantment  extends Enchantment {
    public final String description;
    public final String name;
    public final int id;
    public int level;

    @Override
    public Enchantment setLevel(int level) {
        this.level = level;
        return this;
    }

    @Override
    public int getLevel() {
        return level;
    }

    public CustomEnchantment(int id, String name, String description, Enchantment.Rarity rarity, EnchantmentType type) {
        super(id, name, rarity, type);
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLoreLine(int level) {
        Map<Integer, String> romanNumerals = Map.of(
                1, "I", 2, "II", 3, "III", 4, "IV", 5, "V",
                6, "VI", 7, "VII", 8, "VIII", 9, "IX", 10, "X"
        );

        Map<Integer, TextFormat> rarityColors = Map.of(
                Rarity.COMMON.ordinal(), TextFormat.BLUE,
                Rarity.UNCOMMON.ordinal(), TextFormat.MINECOIN_GOLD,
                Rarity.RARE.ordinal(), TextFormat.RED,
                Rarity.VERY_RARE.ordinal(), TextFormat.DARK_RED
        );

        TextFormat rarityColor = rarityColors.getOrDefault(getRarity().ordinal(), TextFormat.WHITE);
        String formattedName = formatName();
        String romanLevel = romanNumerals.getOrDefault(level, String.valueOf(level));

        return rarityColor + formattedName + " " + romanLevel;
    }

    private String formatName() {
        String[] parts = name.split("_");

        StringBuilder formattedName = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) continue;
            formattedName.append(part.substring(0, 1).toUpperCase())
                    .append(part.substring(1).toLowerCase())
                    .append(" ");
        }

        return formattedName.toString().trim();
    }


    @Nullable
    @Override
    public Identifier getIdentifier()  { return new Identifier("enchantments", name.toLowerCase()); }

    public Item apply(Item item) {
        CompoundTag tag = item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag();

        ListTag<CompoundTag> enchTag;
        if (tag.contains("ench")) {
            enchTag = tag.getList("ench", CompoundTag.class);
        } else {
            enchTag = new ListTag<>("ench");
        }

        CompoundTag enchantmentTag = new CompoundTag();
        enchantmentTag.putShort("id", (short) this.getId());
        enchantmentTag.putShort("lvl", (short) this.getLevel());

        enchTag.add(enchantmentTag);

        tag.putList(enchTag);
        item.setNamedTag(tag);

        return item;
    }

}
