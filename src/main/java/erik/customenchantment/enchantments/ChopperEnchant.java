package erik.customenchantment.enchantments;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockWood;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.PluginTask;

import java.util.HashSet;
import java.util.Set;

public class ChopperEnchant extends CustomEnchantment implements BlockBreakEnchant {

    public ChopperEnchant(int id, String name, String description, Rarity rarity, EnchantmentType type) {
        super(id, name, description, rarity, type);
    }

    @Override
    public void onBreak(BlockBreakEvent event, int level) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (block instanceof BlockWood) {
            event.setCancelled(true);
            scheduleBreakTree(player, block);
        }
    }

    private void breakTree(Player player, Block initialBlock) {
        Position position = initialBlock.abs();
        Set<Vector3> checkedBlocks = new HashSet<>();
        Set<Vector3> woodBlocks = new HashSet<>();

        findConnectedWood(position, checkedBlocks, woodBlocks, player.getLevel());

        for (Vector3 pos : woodBlocks) {
            Block woodBlock = player.getLevel().getBlock(pos);
            if (woodBlock instanceof BlockWood && (player.isCreative() || player.isSurvival())) {
                player.getLevel().setBlock(pos, Block.get(Block.AIR));
                player.getLevel().addParticle(new DestroyBlockParticle(pos, woodBlock));
                player.getLevel().dropItem(pos, woodBlock.toItem());
            }
        }
    }

    private void findConnectedWood(Position pos, Set<Vector3> checkedBlocks, Set<Vector3> woodBlocks, Level level) {
        if (checkedBlocks.contains(pos)) {
            return;
        }

        checkedBlocks.add(pos);
        Block block = level.getBlock(pos);

        if (block instanceof BlockWood) {
            woodBlocks.add(pos.clone());  // Clonamos la posición para evitar referencias compartidas

            Vector3[] directions = {
                    new Vector3(0, 1, 0),  // Arriba
                    new Vector3(0, -1, 0), // Abajo
                    new Vector3(1, 0, 0),  // Este
                    new Vector3(-1, 0, 0), // Oeste
                    new Vector3(0, 0, 1),  // Sur
                    new Vector3(0, 0, -1)  // Norte
            };

            for (Vector3 dir : directions) {
                Vector3 newPos = pos.add(dir);
                findConnectedWood(new Position(newPos.x, newPos.y, newPos.z, level), checkedBlocks, woodBlocks, level);
            }
        }
    }

    private void scheduleBreakTree(Player player, Block block) {
        Server.getInstance().getScheduler().scheduleDelayedTask(new PluginTask<>(player.getServer().getPluginManager().getPlugin("CustomEnchantment")) {
            @Override
            public void onRun(int currentTick) {
                breakTree(player, block);
            }
        }, 1);
    }
}