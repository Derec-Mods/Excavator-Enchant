execute if score init wide_mining.enchantment matches 1 unless score @s wide_mining.enchantment matches 1 run return fail
scoreboard players set @s wide_mining.enchantment 0

tag @s[scores={wide_mining.stone_mined=1..}] add block_mined
tag @s[scores={wide_mining.deepslate_mined=1..}] add block_mined
tag @s[scores={wide_mining.dripstone_block_mined=1..}] add block_mined
tag @s[scores={wide_mining.sandstone_mined=1..}] add block_mined
tag @s[scores={wide_mining.red_sandstone_mined=1..}] add block_mined
tag @s[scores={wide_mining.smooth_sandstone_mined=1..}] add block_mined
tag @s[scores={wide_mining.granite_mined=1..}] add block_mined
tag @s[scores={wide_mining.diorite_mined=1..}] add block_mined
tag @s[scores={wide_mining.andesite_mined=1..}] add block_mined
tag @s[scores={wide_mining.tuff_mined=1..}] add block_mined
tag @s[scores={wide_mining.basalt_mined=1..}] add block_mined
tag @s[scores={wide_mining.smooth_basalt_mined=1..}] add block_mined
tag @s[scores={wide_mining.calcite_mined=1..}] add block_mined
tag @s[scores={wide_mining.crimson_nylium_mined=1..}] add block_mined
tag @s[scores={wide_mining.warped_nylium_mined=1..}] add block_mined
tag @s[scores={wide_mining.netherrack_mined=1..}] add block_mined
tag @s[scores={wide_mining.magma_block_mined=1..}] add block_mined
tag @s[scores={wide_mining.blackstone_mined=1..}] add block_mined
tag @s[scores={wide_mining.end_stone_mined=1..}] add block_mined

execute if entity @s[tag=block_mined] unless predicate wide_mining:is_sneaking at @e[type=item,limit=1,sort=nearest,nbt={Age:0s}] run function wide_mining:block_destroyed
tag @s remove block_mined

scoreboard players set @s wide_mining.stone_mined 0
scoreboard players set @s wide_mining.deepslate_mined 0
scoreboard players set @s wide_mining.dripstone_block_mined 0
scoreboard players set @s wide_mining.sandstone_mined 0
scoreboard players set @s wide_mining.red_sandstone_mined 0
scoreboard players set @s wide_mining.smooth_sandstone_mined 0
scoreboard players set @s wide_mining.granite_mined 0
scoreboard players set @s wide_mining.diorite_mined 0
scoreboard players set @s wide_mining.andesite_mined 0
scoreboard players set @s wide_mining.tuff_mined 0
scoreboard players set @s wide_mining.basalt_mined 0
scoreboard players set @s wide_mining.smooth_basalt_mined 0
scoreboard players set @s wide_mining.calcite_mined 0
scoreboard players set @s wide_mining.crimson_nylium_mined 0
scoreboard players set @s wide_mining.warped_nylium_mined 0
scoreboard players set @s wide_mining.netherrack_mined 0
scoreboard players set @s wide_mining.magma_block_mined 0
scoreboard players set @s wide_mining.blackstone_mined 0
scoreboard players set @s wide_mining.end_stone_mined 0
