package erik.customenchantment.enchantments;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.block.BlockBreakEvent;

public interface BlockBreakEnchant {

    public abstract void onBreak(BlockBreakEvent ev, int level);

}
