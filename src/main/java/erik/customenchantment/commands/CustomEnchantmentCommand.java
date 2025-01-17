package erik.customenchantment.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.TextFormat;
import erik.customenchantment.EnchantmentRegistry;
import erik.customenchantment.enchantments.CustomEnchantment;

import java.util.*;

public class CustomEnchantmentCommand extends Command {

    EnchantmentRegistry enchantmentRegistry;

    public CustomEnchantmentCommand() {
        super("customenchantment",
                "Commands for custom enchantments",
                "/customenchantment [player] [enchantment] [level]");

        this.enchantmentRegistry = EnchantmentRegistry.getInstance();

        var enchantments = this.enchantmentRegistry.getAllEnchantments();
        var enchantNames = enchantments.values().stream().map(Enchantment::getName).toArray(String[]::new);

        CommandEnum enchant = new CommandEnum("enchantment", enchantNames);
        this.setPermission("nukkit.command.enchant");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("player", CommandParamType.TARGET),
                CommandParameter.newType("enchantmentId", CommandParamType.INT),
                CommandParameter.newType("level", true, CommandParamType.INT)
        });
        this.commandParameters.put("byName", new CommandParameter[]{
                CommandParameter.newType("player", CommandParamType.TARGET),
                CommandParameter.newEnum("enchantmentName", enchant),
                CommandParameter.newType("level", true, CommandParamType.INT)
        });
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        var list = result.getValue();
        List<Entity> entities = list.getResult(0);
        if (entities.isEmpty()) {
            log.addNoTargetMatch().output();
            return 0;
        }
        CustomEnchantment enchantment;
        int enchantLevel = 1;
        switch (result.getKey()) {
            case "default" -> {
                int enchant = list.getResult(1);
                Optional<CustomEnchantment> optionalEnchantment = this.enchantmentRegistry.getEnchantment(enchant);
                if (optionalEnchantment.isEmpty()) {
                    log.addError("commands.enchant.notFound", String.valueOf(enchant)).output();
                    return 0;
                }
                enchantment = optionalEnchantment.get();
            }
            case "byName" -> {
                String enchantmentName = list.getResult(1);
                Optional<CustomEnchantment> optionalEnchantment = this.enchantmentRegistry.getAllEnchantments().values().stream()
                        .filter(e -> e.getName().equalsIgnoreCase(enchantmentName))
                        .findFirst();
                if (optionalEnchantment.isEmpty()) {
                    log.addError("commands.enchant.notFound", enchantmentName).output();
                    return 0;
                }
                enchantment = optionalEnchantment.get();
            }
            default -> {
                return 0;
            }
        }
        if (list.hasResult(2)) {
            enchantLevel = list.getResult(2);
            if (enchantLevel < 1) {
                log.addNumTooSmall(2, 1).output();
                return 0;
            }
        }
        int success = 0;
        for (Entity entity : entities) {
            Player player = (Player) entity;
            enchantment.setLevel(enchantLevel);
            Item item = player.getInventory().getItemInHand();
            if (item.getId() == 0) {
                log.addError("commands.enchant.noItem").output();
                continue;
            }
            if (item.getId() != ItemID.BOOK) {
                item = enchantment.apply(item);

                var itemLore = new ArrayList<>(Arrays.asList(item.getLore()));
                var lore = TextFormat.RESET + enchantment.getLoreLine(enchantLevel);
                itemLore.add(lore);

                item.setLore(itemLore.toArray(new String[0]));

                player.getInventory().setItemInHand(item);
            } else {
                Item enchanted = Item.get(ItemID.ENCHANTED_BOOK, 0, 1, item.getCompoundTag());
                enchanted = enchantment.apply(enchanted);

                var itemLore = new ArrayList<>(Arrays.asList(enchanted.getLore()));
                var lore = TextFormat.RESET + enchantment.getLoreLine(enchantLevel);
                itemLore.add(lore);

                enchanted.setLore(itemLore.toArray(new String[0]));

                Item clone = item.clone();
                clone.count--;
                PlayerInventory inventory = player.getInventory();
                inventory.setItemInHand(clone);
                player.giveItem(enchanted);
            }

            log.addSuccess("commands.enchant.success", enchantment.getName()).output(true);
            success++;
        }
        return success;
    }
}
