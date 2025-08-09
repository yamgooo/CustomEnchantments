## CustomEnchantments for Nukkit

Powerful, production-ready custom enchantments for Nukkit (MCPE). This plugin adds a curated set of combat, utility, and QoL enchants that integrate cleanly with vanilla mechanics and the Nukkit API.

— Author: yamgooo

### Features
- Well-structured enchantment registry with ID-based lookup
- Passive armor effects, held-item effects, movement-based effects, melee triggers, and block-break hooks
- Smelting and XP logic for ores, tree-feller style chopping, lightning strikes, hunger management, and more
- Command to apply enchants by name or ID (supports enchanted books)

### Requirements
- Java 14+
- Nukkit MOT-SNAPSHOT (API 1.0.13)

### Installation
1. Download or build `CustomEnchant.jar`.
2. Drop the jar into your server `plugins/` directory.
3. Start the server. You should see “Enabled Custom Enchantments!” in console.

### Build from source
This project uses Maven.

```bash
mvn -U -DskipTests package
```

The built artifact will be at `target/CustomEnchant-1.0-SNAPSHOT.jar`.

### Commands and permissions
- Command: `/customenchantment`
  - Usage (by ID): `/customenchantment <player> <enchantmentId> [level]`
  - Usage (by name): `/customenchantment <player> <enchantmentName> [level]`
- Permission: `nukkit.command.enchant` (OP by default on most servers)

Examples:
```text
/customenchantment Steve speed 2
/customenchantment Steve 248 1       # AutoSmelt level 1 by ID
/customenchantment Steve implants 3   # Implants level 3 (max)
```

### Enchantments
Below is the full list of registered enchantments. All names are case-insensitive in the command. Levels affect potency where applicable.

| ID  | Name            | Category         | What it does |
|-----|-----------------|------------------|--------------|
| 254 | oxygenate       | Held item        | Grants Water Breathing while holding the item. |
| 253 | gluttony        | Melee (on hit)   | Small chance per level to restore a bit of hunger on successful hit. |
| 252 | chopper         | Block break      | Tree feller: breaks all connected wood when you start breaking a log. |
| 251 | zeus            | Melee (on hit)   | Chance per level to summon lightning on the victim and deal extra damage. Players only. |
| 250 | experience      | Block break      | Extra XP from ores. Multiplies drop XP by (level + 1). |
| 249 | recover         | Melee (on kill)  | On kill, grants short Regeneration and Absorption. |
| 248 | autosmelt       | Block break      | Ores drop their smelted form and appropriate XP, no furnace needed. |
| 247 | hellforged      | Movement         | While moving, occasionally repairs 1 durability on the equipped/durable item. |
| 246 | implants        | Movement (armor) | Gradually restores hunger and saturation as you move. Max level 3. |
| 245 | infrared        | Armor passive    | Grants Night Vision while equipped. |
| 244 | mermaid         | Armor passive    | Grants Water Breathing while equipped. |
| 243 | invisibility    | Armor passive    | Grants Invisibility while equipped. |
| 240 | speed           | Armor passive    | Grants Speed while equipped. |
| 241 | fire_resistance | Armor passive    | Grants Fire Resistance while equipped. |
| 242 | strength        | Held item        | Grants Strength while holding the item. |

Notes:
- Some potion effects scale with level (amplifier = level − 1).
- XP and probability-based effects scale with level where implemented.

### Compatibility
- Nukkit API: 1.0.13 (MOT-SNAPSHOT)
- Designed for Bedrock/MCPE servers running Nukkit. Other forks may require adjustments.

### Configuration
No external configuration is required. All behavior is code-driven and safe by default.

### Development overview
- Main entry: `erik.customenchantment.Enchantment`
- Event router: `erik.customenchantment.EnchantmentListener`
- Registry: `erik.customenchantment.EnchantmentRegistry`
- Base class: `erik.customenchantment.enchantments.CustomEnchantment`

### Contributing
PRs and issues are welcome. Please keep changes focused and include testing notes.

### License
All rights reserved unless a license is added. If you intend to fork and publish, please add an explicit license file.


