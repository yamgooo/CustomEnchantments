package erik.customenchantment.enchantments;

import cn.nukkit.block.BlockID;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.enchantment.EnchantmentType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AutoSmeltEnchant extends CustomEnchantment implements BlockBreakEnchant {

    // Maps for smelted drops and XP amounts
    private static final Map<Integer, Item[]> SMELT_DROPS = new HashMap<>();
    private static final Map<Integer, int[]> XP_RANGES = new HashMap<>();
    private static final Random random = new Random();

    static {
        // Regular ores
        SMELT_DROPS.put(BlockID.IRON_ORE, new Item[]{
                Item.get(ItemID.IRON_INGOT)
        });
        SMELT_DROPS.put(BlockID.GOLD_ORE, new Item[]{
                Item.get(ItemID.GOLD_INGOT)
        });
        /*SMELT_DROPS.put(BlockID.COPPER_ORE, new Item[]{
                Item.get(ItemID.COPPER_INGOT)
        });*/
        SMELT_DROPS.put(BlockID.DIAMOND_ORE, new Item[]{
                Item.get(ItemID.DIAMOND)
        });
        SMELT_DROPS.put(BlockID.EMERALD_ORE, new Item[]{
                Item.get(ItemID.EMERALD)
        });
        SMELT_DROPS.put(BlockID.LAPIS_ORE, new Item[]{
                Item.get(ItemID.DYE, 4)
        });
        SMELT_DROPS.put(BlockID.REDSTONE_ORE, new Item[]{
                Item.get(ItemID.REDSTONE)
        });
        SMELT_DROPS.put(BlockID.COAL_ORE, new Item[]{
                Item.get(ItemID.COAL)
        });

        // Deep/Raw ores
        SMELT_DROPS.put(BlockID.DEEPSLATE_IRON_ORE, new Item[]{
                Item.get(ItemID.IRON_INGOT)
        });
        SMELT_DROPS.put(BlockID.DEEPSLATE_GOLD_ORE, new Item[]{
                Item.get(ItemID.GOLD_INGOT)
        });
        /*SMELT_DROPS.put(BlockID.DEEPSLATE_COPPER_ORE, new Item[]{
                Item.get(ItemID.KNOWN)
        });*/
        SMELT_DROPS.put(BlockID.DEEPSLATE_DIAMOND_ORE, new Item[]{
                Item.get(ItemID.DIAMOND)
        });
        SMELT_DROPS.put(BlockID.DEEPSLATE_EMERALD_ORE, new Item[]{
                Item.get(ItemID.EMERALD)
        });
        SMELT_DROPS.put(BlockID.DEEPSLATE_LAPIS_ORE, new Item[]{
                Item.get(ItemID.DYE, 4)
        });
        SMELT_DROPS.put(BlockID.DEEPSLATE_REDSTONE_ORE, new Item[]{
                Item.get(ItemID.REDSTONE)
        });
        SMELT_DROPS.put(BlockID.DEEPSLATE_COAL_ORE, new Item[]{
                Item.get(ItemID.COAL)
        });

        // Nether ores
        SMELT_DROPS.put(BlockID.NETHER_GOLD_ORE, new Item[]{
                Item.get(ItemID.GOLD_INGOT)
        });
        SMELT_DROPS.put(BlockID.QUARTZ_ORE, new Item[]{
                Item.get(ItemID.NETHER_QUARTZ)
        });
        SMELT_DROPS.put(BlockID.ANCIENT_DEBRIS, new Item[]{
                Item.get(ItemID.NETHERITE_SCRAP)
        });

        XP_RANGES.put(BlockID.IRON_ORE, new int[]{0, 3});
        XP_RANGES.put(BlockID.DEEPSLATE_IRON_ORE, new int[]{0, 3});
        XP_RANGES.put(BlockID.GOLD_ORE, new int[]{2, 4});
        XP_RANGES.put(BlockID.DEEPSLATE_GOLD_ORE, new int[]{2, 4});
        XP_RANGES.put(BlockID.COPPER_ORE, new int[]{0, 2});
        XP_RANGES.put(BlockID.DEEPSLATE_COPPER_ORE, new int[]{0, 2});
        XP_RANGES.put(BlockID.DIAMOND_ORE, new int[]{3, 7});
        XP_RANGES.put(BlockID.DEEPSLATE_DIAMOND_ORE, new int[]{3, 7});
        XP_RANGES.put(BlockID.EMERALD_ORE, new int[]{3, 7});
        XP_RANGES.put(BlockID.DEEPSLATE_EMERALD_ORE, new int[]{3, 7});
        XP_RANGES.put(BlockID.LAPIS_ORE, new int[]{2, 5});
        XP_RANGES.put(BlockID.DEEPSLATE_LAPIS_ORE, new int[]{2, 5});
        XP_RANGES.put(BlockID.REDSTONE_ORE, new int[]{1, 5});
        XP_RANGES.put(BlockID.DEEPSLATE_REDSTONE_ORE, new int[]{1, 5});
        XP_RANGES.put(BlockID.COAL_ORE, new int[]{0, 2});
        XP_RANGES.put(BlockID.DEEPSLATE_COAL_ORE, new int[]{0, 2});
        XP_RANGES.put(BlockID.NETHER_GOLD_ORE, new int[]{2, 4});
        XP_RANGES.put(BlockID.QUARTZ_ORE, new int[]{2, 5});
        XP_RANGES.put(BlockID.ANCIENT_DEBRIS, new int[]{2, 5});
    }

    public AutoSmeltEnchant(int id, String name, String description, Rarity rarity, EnchantmentType type) {
        super(id, name, description, rarity, type);
    }

    @Override
    public void onBreak(BlockBreakEvent event, int level) {
        int blockId = event.getBlock().getId();

        Item[] smeltedDrops = SMELT_DROPS.get(blockId);
        if (smeltedDrops != null) {
            event.setDrops(smeltedDrops);
        }

        int[] xpRange = XP_RANGES.get(blockId);
        if (xpRange != null) {
            int xpAmount = random.nextInt(xpRange[1] - xpRange[0] + 1) + xpRange[0];
            event.setDropExp(xpAmount);
        }
    }
}
