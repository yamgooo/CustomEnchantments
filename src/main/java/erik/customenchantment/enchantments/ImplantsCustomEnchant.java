package erik.customenchantment.enchantments;

import cn.nukkit.Player;
import cn.nukkit.PlayerFood;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.item.food.Food;

public class ImplantsCustomEnchant extends CustomEnchantment implements MovementEnchant {

    private static final double BASE_PROBABILITY_MULTIPLIER = 0.05;
    private static final double FOOD_ADDITION_MULTIPLIER = 0.5;
    private static final double SATURATION_ADDITION_MULTIPLIER = 0.5;

    public ImplantsCustomEnchant(int id, String name, String description, Rarity rarity, EnchantmentType type) {
        super(id, name, description, rarity, type);
    }

    @Override
    public void onMove(PlayerMoveEvent event, int level, Item item) {
        Player player = event.getPlayer();
        PlayerFood hungerManager = player.getFoodData();

        if (hungerManager.getLevel() >= hungerManager.getMaxLevel()) {
            return;
        }

        double chance = (level / (double) this.getMaxLevel()) * 100 * BASE_PROBABILITY_MULTIPLIER;
        if (Math.random() * 100 > chance) {
            return;
        }

        int maxFoodLevel = hungerManager.getMaxLevel();
        int foodToAdd = (int) Math.ceil(
                (level / (double) this.getMaxLevel()) * (maxFoodLevel * 0.1) * FOOD_ADDITION_MULTIPLIER
        );
        foodToAdd = Math.min(foodToAdd, maxFoodLevel - hungerManager.getLevel());

        float saturationToAdd = (float) (
                (level / (double) this.getMaxLevel()) * (maxFoodLevel * 0.05) * SATURATION_ADDITION_MULTIPLIER
        );
        saturationToAdd = Math.min(saturationToAdd, maxFoodLevel - hungerManager.getLevel());

        hungerManager.addFoodLevel(foodToAdd, saturationToAdd);
    }




    @Override
    public int getMaxLevel() {
        return 3;
    }
}
