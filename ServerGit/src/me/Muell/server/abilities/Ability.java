
package me.Muell.server.abilities;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import me.Muell.server.types.Items;

public enum Ability {

    ADVANCEDPRISONER(
	    "Advanced Prisoner",
	    12,
	    50000L,
	    new ItemStack[] { Items.createitem(Material.LAVA_BUCKET, "Advanced Prisoner", null) },
	    new ItemStack(Items.createitem(Material.LAVA_BUCKET, "Advanced Prisoner", null))),

    ANCHOR(
	    "Anchor",
	    15,
	    0L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.ANVIL, "Anchor", null))),

    ANGEL(
	    "Angel",
	    5,
	    0L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.POTION, "Angel", null))),

    ANTI_ZEN(
	    "Anti-Zen",
	    10,
	    30000L,
	    new ItemStack[] { Items.createitem(Material.EYE_OF_ENDER, "Anti-Zen", null) },
	    new ItemStack(Items.createitem(Material.EYE_OF_ENDER, "Anti-Zen", null))),

    AUTOMATON_TURRET(
	    "Automaton Turret",
	    15,
	    60000L,
	    new ItemStack[] { Items.createitem(Material.EMERALD, "Automaton Turret", null) },
	    new ItemStack(Items.createitem(Material.FENCE, "Automaton Turret", null))),

    BLACK_BEAM(
	    "Blackbeam",
	    10,
	    30000L,
	    new ItemStack[] { Items.createitem(Material.COAL_BLOCK, "Blackbeam", null) },
	    new ItemStack(Items.createitem(Material.COAL_BLOCK, "Blackbeam", null))),

    BLUE_BEAM(
	    "Bluebeam",
	    10,
	    40000L,
	    new ItemStack[] { Items.createitem(Material.LAPIS_BLOCK, "Bluebeam", null) },
	    new ItemStack(Items.createitem(Material.LAPIS_BLOCK, "Bluebeam", null))),

    BLOCK_MASTER(
	    "Blockmaster",
	    7,
	    40000L,
	    new ItemStack[] { Items.createitem(Material.WOOD_SPADE, "Blockmaster", null) },
	    new ItemStack(Items.createitem(Material.WOOD_SPADE, "Blockmaster", null))),

    BURST_MINE(
	    "Burst-Mine",
	    3,
	    15000L,
	    new ItemStack[] { Items.createitem(Material.CARPET, "Burst-Mine", null) },
	    new ItemStack(Items.createitem(Material.CARPET, "Burst-Mine", null))),

    CAMEL(
	    "Camel",
	    4,
	    0L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.SAND, "Camel", null))),

    CATACLYSM_BEAM(
	    "Cataclysm Beam",
	    15,
	    60000L,
	    new ItemStack[] { Items.createitem(Material.FERMENTED_SPIDER_EYE, "Cataclysm Beam", null) },
	    new ItemStack(Items.createitem(Material.FERMENTED_SPIDER_EYE, "Cataclysm Beam", null))),

    CLONE(
	    "Clone",
	    12,
	    40000L,
	    new ItemStack[] { Items.createitem(Material.SKULL_ITEM, "Clone", null) },
	    new ItemStack(Items.createitem(Material.SKULL_ITEM, "Clone", null))),

    COWBOY(
	    "Cowboy",
	    12,
	    40000L,
	    new ItemStack[] { Items.createitem(Material.LEASH, "Cowboy", null) },
	    new ItemStack(Items.createitem(Material.LEASH, "Cowboy", null))),

    CRAFT_AURA(
	    "Craft-Aura",
	    6,
	    2000L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.MUSHROOM_SOUP, "Craft-Aura", null))),

    DEVASTOR(
	    "Devastor",
	    7,
	    0L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.GRASS, "Devastor", null))),

    DOLPHIN(
	    "Dolphin",
	    10,
	    0L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.WATER, "Dolphin", null))),

    DOMBO(
	    "Dombo",
	    9,
	    0L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.IRON_SWORD, "Dombo", null))),

    DIVER(
	    "Diver",
	    5,
	    25000L,
	    new ItemStack[] { Items.createitem(Material.GHAST_TEAR, "Diver", null) },
	    new ItemStack(Items.createitem(Material.GHAST_TEAR, "Diver", null))),

    GRAPPLER(
	    "Grappler",
	    15,
	    0L,
	    new ItemStack[] { Items.createitem(Material.LEASH, "Grappler", null) },
	    new ItemStack(Items.createitem(Material.LEASH, "Grappler", null))),

    GREEN_BEAM(
	    "Greenbeam",
	    10,
	    30000L,
	    new ItemStack[] { Items.createitem(Material.EMERALD_BLOCK, "Greenbeam", null) },
	    new ItemStack(Items.createitem(Material.EMERALD_BLOCK, "Greenbeam", null))),

    HADES(
	    "Hades",
	    12,
	    40000L,
	    new ItemStack[] { Items.createitem(Material.BONE, "Hades", null) },
	    new ItemStack(Items.createitem(Material.BONE, "Hades", null))),

    ICEPRISONER(
	    "Ice-Prisoner",
	    11,
	    40000L,
	    new ItemStack[] { Items.createitem(Material.ICE, "Ice-Prisoner", null) },
	    new ItemStack(Items.createitem(Material.ICE, "Ice-Prisoner", null))),

    JEDI(
	    "Jedi",
	    12,
	    40000L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.LEVER, "Jedi", null))),

    JESUS(
	    "Jesus",
	    25,
	    0L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.BONE, "Jesus", null))),

    KAMEHAMEHA(
	    "Kamehameha",
	    12,
	    50000L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.SOUL_SAND, "Kamehameha", null))),

    KANGAROO(
	    "Kangaroo",
	    10,
	    0L,
	    new ItemStack[] { Items.createitem(Material.FIREWORK, "Kangaroo", null) },
	    new ItemStack(Items.createitem(Material.FIREWORK, "Kangaroo", null))),

    MARIO_CART(
	    "Mario-Cart",
	    10,
	    60000L,
	    new ItemStack[] { Items.createitem(Material.MINECART, "Mario-Cart", null) },
	    new ItemStack(Items.createitem(Material.MINECART, "Mario-Cart", null))),

    MONKEY(
	    "Monkey",
	    7,
	    0L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.VINE, "Monkey", null))),

    NAPALM(
	    "Napalm",
	    12,
	    50000L,
	    new ItemStack[] { Items.createitem(Material.FIRE, "Napalm", null) },
	    new ItemStack(Items.createitem(Material.FIRE, "Napalm", null))),

    PARACHUTE(
	    "Parachute",
	    11,
	    30000L,
	    new ItemStack[] { Items.createitem(Material.STRING, "Parachute", null) },
	    new ItemStack(Items.createitem(Material.STRING, "Parachute", null))),

    PHANTOM(
	    "Phantom",
	    11,
	    50000L,
	    new ItemStack[] { Items.createitem(Material.FEATHER, "Phantom", null) },
	    new ItemStack(Items.createitem(Material.FEATHER, "Phantom", null))),

    PILLAR(
	    "Pillar",
	    8,
	    40000L,
	    new ItemStack[] { Items.createitem(Material.CLAY_BALL, "Pillar", null) },
	    new ItemStack(Items.createitem(Material.CLAY_BALL, "Pillar", null))),

    PRISONER(
	    "Prisoner",
	    8,
	    30000L,
	    new ItemStack[] { Items.createitem(Material.GLASS, "Prisoner", null) },
	    new ItemStack(Items.createitem(Material.GLASS, "Prisoner", null))),

    RED_BEAM(
	    "Redbeam",
	    10,
	    40000L,
	    new ItemStack[] { Items.createitem(Material.REDSTONE_BLOCK, "Redbeam", null) },
	    new ItemStack(Items.createitem(Material.REDSTONE_BLOCK, "Redbeam", null))),

    RHINO(
	    "Rhino",
	    7,
	    25000L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.ANVIL, "Rhino", null))),

    SENTRYGUN(
	    "Sentry-Gun",
	    11,
	    30000L,
	    new ItemStack[] { Items.createitem(Material.ARROW, "Sentry-Gun", null) },
	    new ItemStack(Items.createitem(Material.ARROW, "Sentry-Gun", null))),

    SONIC(
	    "Sonic",
	    11,
	    30000L,
	    new ItemStack[] { Items.createitem(Material.GLASS_BOTTLE, "Sonic", null) },
	    new ItemStack(Items.createBoots(Color.BLUE, "Sonic", null))),

    SUPER_SAIYAJIN(
	    "Super-Saiyajin",
	    12,
	    30000L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.BLAZE_POWDER, "Super-Saiyajin", null))),

    SPACEMAN(
	    "Spaceman",
	    10,
	    40000L,
	    new ItemStack[] { Items.createitem(Material.BEACON, "Spaceman", null) },
	    new ItemStack(Items.createitem(Material.BEACON, "Spaceman", null))),

    TARZAN(
	    "Tarzan",
	    10,
	    15000L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.LONG_GRASS, "Tarzan", null))),

    WITCH(
	    "Witch",
	    10,
	    20000L,
	    new ItemStack[] { Items.createitem(Material.STICK, "Witch", null) },
	    new ItemStack(Items.createPotion(PotionType.INSTANT_DAMAGE, 2, "Witch", true))),

    ZEN(
	    "Zen",
	    10,
	    30000L,
	    new ItemStack[] { Items.createitem(Material.MAGMA_CREAM, "Zen", null) },
	    new ItemStack(Items.createitem(Material.MAGMA_CREAM, "Zen", null))),

    TEST(
	    "Test",
	    0,
	    0L,
	    new ItemStack[] { Items.createitem(Material.STICK, "Test", null) },
	    new ItemStack(Items.createitem(Material.STICK, "Test", null))),

    NONE(
	    "None",
	    0,
	    0L,
	    new ItemStack[] {},
	    new ItemStack(Items.createitem(Material.GRASS, "None", null)));

    String name;

    Long cooldown;

    ItemStack[] items;

    ItemStack invitem;

    int kitpoints;

    private Ability(String name, int kitpoints, Long cooldown, ItemStack[] items, ItemStack invitem) {

	this.name = name;
	this.cooldown = cooldown;
	this.items = items;
	this.invitem = invitem;
	this.kitpoints = kitpoints;

    }

    public String getName() {

	return this.name;

    }

    public ItemStack[] getItems() {

	return this.items;

    }

    public ItemStack getInvItem() {

	return this.invitem;

    }

    public Long getCooldown() {

	return this.cooldown;

    }

    public int getKitpoints() {

	return this.kitpoints;
    }

}
