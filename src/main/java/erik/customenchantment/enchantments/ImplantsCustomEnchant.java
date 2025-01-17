package erik.customenchantment.enchantments;

import cn.nukkit.Player;
import cn.nukkit.PlayerFood;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.item.food.Food;

public class ImplantsCustomEnchant extends CustomEnchantment implements MovementEnchant {
    public ImplantsCustomEnchant(int id, String name, String description, Rarity rarity, EnchantmentType type) {
        super(id, name, description, rarity, type);
    }

    @Override
    public void onMove(PlayerMoveEvent event, int level) {
        Player player = event.getPlayer();
        PlayerFood hungerManager = player.getFoodData();

        if (hungerManager.getLevel() >= hungerManager.getMaxLevel()) {
            return;
        }

        if (Math.random() * 100 > ((this.getMaxLevel() - level) * 10)) {
            return;
        }

        int foodToAdd = (int) ((level / (double) this.getMaxLevel()) * 2);

        float saturationToAdd = (float) ((level / (double) this.getMaxLevel()) * 1.5);

        hungerManager.addFoodLevel(foodToAdd, saturationToAdd);
    }


    @Override
    public int getMaxLevel() {
        return 3;
    }
}
