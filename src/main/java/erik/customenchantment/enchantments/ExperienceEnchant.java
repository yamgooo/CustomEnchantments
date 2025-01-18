package erik.customenchantment.enchantments;

import cn.nukkit.block.BlockID;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.item.enchantment.EnchantmentType;

public class ExperienceEnchant extends CustomEnchantment implements BlockBreakEnchant {

    private static final int[] ORES = {
            BlockID.GOLD_ORE,
            BlockID.IRON_ORE,
            BlockID.COAL_ORE,
            BlockID.LAPIS_ORE,
            BlockID.DIAMOND_ORE,
            BlockID.REDSTONE_ORE,
            BlockID.EMERALD_ORE,
            BlockID.QUARTZ_ORE,
            BlockID.DEEPSLATE_IRON_ORE,
            BlockID.DEEPSLATE_COAL_ORE,
            BlockID.DEEPSLATE_LAPIS_ORE,
            BlockID.DEEPSLATE_DIAMOND_ORE,
            BlockID.DEEPSLATE_REDSTONE_ORE,
            BlockID.DEEPSLATE_EMERALD_ORE
    };

    public ExperienceEnchant(int id, String name, String description, Rarity rarity, EnchantmentType type) {
        super(id, name, description, rarity, type);
    }

    @Override
    public void onBreak(BlockBreakEvent event, int level) {
        int blockId = event.getBlock().getId();

        if (!isOre(blockId)) {
            int currentXp = event.getDropExp();
            int multiplier = level + 1;
            event.setDropExp(currentXp * multiplier);
        }
    }

    private boolean isOre(int blockId) {
        for (int ore : ORES) {
            if (ore == blockId) {
                return true;
            }
        }
        return false;
    }
}
