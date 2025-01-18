package erik.customenchantment.enchantments;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.block.BlockWood;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.scheduler.ServerScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChopperEnchant extends CustomEnchantment implements BlockBreakEnchant {

    public ChopperEnchant(int id, String name, String description, Rarity rarity, EnchantmentType type) {
        super(id, name, description, rarity, type);
    }

    @Override
    public void onBreak(BlockBreakEvent event, int level) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (block instanceof BlockWood) {
            breakTree(player, block);
        }
    }

    private void breakTree(Player player, Block block) {
        throw new UnsupportedOperationException();
    }

    private void scheduleBreakTree(Player player, Block block) {
        /*ServerScheduler scheduler = Server.getInstance().getScheduler();
        scheduler.scheduleDelayedTask(new PluginTask() {
            @Override
            public void onRun(int i) {
                breakTree(player, block);
            }
        }, 1);*/
    }
}
