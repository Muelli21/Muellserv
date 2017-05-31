package me.Muell.server.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Muell.server.Main;
import me.Muell.server.abilities.Beam.playerEffect;
import me.Muell.server.abilities.Prison.TargetEffect;
import me.Muell.server.playstyle.PlayStyleInv;
import me.Muell.server.playstyle.Playstyle;
import me.Muell.server.types.BlockProcessor;
import me.Muell.server.types.Hologram;
import me.Muell.server.types.Items;
import me.Muell.server.types.PlayerData;
import me.Muell.server.types.Playerchecks;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;

public class AbilityListener implements Listener {

    private static HashSet<Player> kangacooldown = new HashSet<>();
    AbilityCooldown cooldown = new AbilityCooldown();

    @EventHandler
    public void onKanga(PlayerInteractEvent e) {

	Player p = (Player) e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	Vector handle = p.getEyeLocation().getDirection();

	ItemStack item = p.getItemInHand();
	if (e.getAction().toString().contains("CLICK")) {

	    if (!item.hasItemMeta())
		return;

	    if (!pd.getKit().contains(Ability.KANGAROO) || !item.getType().equals(Material.FIREWORK) || !item.getItemMeta().getDisplayName().equals(Ability.KANGAROO.getName()))
		return;

	    e.setCancelled(true);

	    if (!kangacooldown.contains(p)) {

		if (p.isSneaking()) {

		    handle.multiply(1.5F);
		    handle.setY(0.5F);
		    p.setVelocity(handle);

		}
		if (!p.isSneaking()) {

		    handle.multiply(0.001F);
		    handle.setY(1F);
		    p.setVelocity(handle);

		}

		p.setFallDistance(-10F);
		kangacooldown.add(p);
	    }
	}
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onKangaLand(PlayerMoveEvent e) {

	Player p = (Player) e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!pd.getKit().contains(Ability.KANGAROO))
	    return;

	if (kangacooldown.contains(p)) {
	    if (p.isOnGround())
		kangacooldown.remove(p);

	}

    }

    @EventHandler
    public void onAnchor(EntityDamageByEntityEvent e) {

	if ((e.getEntity() instanceof Player) && (e.getDamager() instanceof Player)) {

	    Player p = (Player) e.getEntity();
	    Player target = (Player) e.getDamager();
	    PlayerData pd = Main.getPlayerData(p);
	    PlayerData td = Main.getPlayerData(target);

	    if (!pd.isIngame() || !td.isIngame())
		return;

	    if (pd.getKit().contains(Ability.ANCHOR) || td.getKit().contains(Ability.ANCHOR)) {

		e.setCancelled(true);

		p.damage(e.getDamage());
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 0);
		target.playSound(target.getLocation(), Sound.ANVIL_LAND, 1, 0);
	    }
	} else if (e.getEntity() instanceof Player) {

	    Player p = (Player) e.getEntity();
	    PlayerData pd = Main.getPlayerData(p);

	    if (!pd.isIngame())
		return;

	    if (pd.getKit().contains(Ability.ANCHOR)) {

		e.setCancelled(true);

		p.damage(e.getDamage());
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 0);
	    }
	}

	else if (e.getDamager() instanceof Player) {

	    if (!(e.getEntity() instanceof LivingEntity))
		return;

	    Player p = (Player) e.getDamager();
	    PlayerData pd = Main.getPlayerData(p);

	    if (!pd.isIngame())
		return;

	    if (pd.getKit().contains(Ability.ANCHOR)) {

		e.setCancelled(true);

		((CraftLivingEntity) e.getEntity()).damage(e.getDamage());
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 0);
	    }
	}
    }

    @EventHandler
    public void onPillar(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();
	if (e.getAction().toString().contains("CLICK")) {

	    if (!pd.getKit().contains(Ability.PILLAR) || !item.getType().equals(Material.CLAY_BALL))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.PILLAR, true))
		return;

	    cooldown.useAbility(p, Ability.PILLAR);

	    HashSet<Block> blocks = new HashSet<>();

	    new BukkitRunnable() {

		int i = 0;
		Location loc = p.getLocation();

		@Override
		public void run() {

		    p.setFallDistance(-10);

		    if (p.getLocation().getBlock().getType().toString().contains("STEP")) {
			i++;
		    }

		    if (loc.clone().add(0, i + 2, 0).getBlock().getType() != Material.AIR) {
			p.sendMessage(ChatColor.DARK_PURPLE + "There is not enough space to create a pillar!");
			removeBlocks(blocks);
			cancel();
			return;
		    }

		    Location loc1 = loc.clone().add(0, i, 0);
		    p.teleport(loc1.clone().add(0, 1, 0));

		    Block block = loc1.getBlock();
		    BlockProcessor.inst().add(block, Material.CLAY, (byte) 1, true);
		    blocks.add(block);

		    if (i >= 8) {

			removeBlocks(blocks);
			cancel();
			return;
		    }

		    i++;
		}
	    }.runTaskTimer(Main.getPlugin(), 0, 5L);
	}
    }

    private void removeBlocks(HashSet<Block> blocks) {

	new BukkitRunnable() {

	    @Override
	    public void run() {
		for (Block b : blocks) {

		    BlockProcessor.inst().add(b, Material.AIR, (byte) 1, true);
		    b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.CLAY);
		}

		blocks.clear();
	    }
	}.runTaskLater(Main.getPlugin(), 5 * 20);
    }

    @EventHandler
    public void onBlueBeam(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();

	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();

	Vector handle = p.getEyeLocation().getDirection();

	if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().equals(Material.LAPIS_BLOCK) || !pd.getKit().contains(Ability.BLUE_BEAM) || !item.getItemMeta().getDisplayName().equals(Ability.BLUE_BEAM.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.BLUE_BEAM, true))
		return;

	    cooldown.useAbility(p, Ability.BLUE_BEAM);
	    handle.multiply(2F);

	    new Beam(p, Material.LAPIS_BLOCK, handle, true, new playerEffect() {

		public void hit(Entity target) {

		    ((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 10));
		}
	    });
	}
    }

    @EventHandler
    public void onRedBeam(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();

	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();

	Vector handle = p.getEyeLocation().getDirection();

	if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().equals(Material.REDSTONE_BLOCK) || !pd.getKit().contains(Ability.RED_BEAM) || !item.getItemMeta().getDisplayName().equals(Ability.RED_BEAM.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.RED_BEAM, true))
		return;
	    cooldown.useAbility(p, Ability.RED_BEAM);

	    handle.multiply(2F);

	    new Beam(p, Material.REDSTONE_BLOCK, handle, true, new playerEffect() {

		public void hit(Entity target) {

		    target.setFireTicks(100);
		}
	    });
	}
    }

    @EventHandler
    public void onBlackBeam(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();

	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();

	Vector handle = p.getEyeLocation().getDirection();

	if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().equals(Material.COAL_BLOCK) || !pd.getKit().contains(Ability.BLACK_BEAM) || !item.getItemMeta().getDisplayName().equals(Ability.BLACK_BEAM.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.BLACK_BEAM, true))
		return;

	    cooldown.useAbility(p, Ability.BLACK_BEAM);

	    handle.multiply(2F);

	    new Beam(p, Material.COAL_BLOCK, handle, true, new playerEffect() {

		public void hit(Entity target) {

		    ((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10 * 20, 5));
		}
	    });
	}
    }

    @EventHandler
    public void onGreenBeam(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();

	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();

	Vector handle = p.getEyeLocation().getDirection();

	if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().equals(Material.EMERALD_BLOCK) || !pd.getKit().contains(Ability.GREEN_BEAM) || !item.getItemMeta().getDisplayName().equals(Ability.GREEN_BEAM.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.GREEN_BEAM, true))
		return;

	    cooldown.useAbility(p, Ability.GREEN_BEAM);

	    handle.multiply(2F);

	    new Beam(p, Material.EMERALD_BLOCK, handle, true, new playerEffect() {

		public void hit(Entity target) {

		    ((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10 * 20, 3));
		}
	    });
	}
    }

    @EventHandler
    public void onSpaceMan(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();
	if (e.getAction().toString().contains("CLICK")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().equals(Material.BEACON) || !pd.getKit().contains(Ability.SPACEMAN) || !item.getItemMeta().getDisplayName().equals(Ability.SPACEMAN.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.SPACEMAN, true))
		return;

	    cooldown.useAbility(p, Ability.SPACEMAN);

	    if (p.isSneaking()) {

		Damageable pdmg = p;
		p.setFallDistance(-100);
		p.setVelocity(new Vector(0, 10, 0));

		if (pdmg.getHealth() <= 20)
		    pdmg.setHealth(1D);

		if (pdmg.getHealth() >= 20)
		    pdmg.setHealth(pdmg.getHealth() - 18D);

	    }

	    if (!p.isSneaking()) {

		for (final Entity entity : p.getNearbyEntities(2, 2, 2)) {

		    if (!(entity instanceof LivingEntity))
			continue;

		    Damageable entitydmg = (Damageable) entity;
		    entity.setFallDistance(-100);

		    if (entity instanceof Player)
			entity.setVelocity(new Vector(0, 10, 0));

		    if (!(entity instanceof Player))
			entity.setVelocity(new Vector(0, 3, 0));

		    if (entitydmg.getHealth() <= 20)
			entitydmg.setHealth(1D);

		    if (entitydmg.getHealth() >= 20)
			entitydmg.setHealth(entitydmg.getHealth() - 18);
		}
	    }
	}
    }

    @EventHandler
    public void onHades(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();
	final PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();
	if (e.getAction().toString().contains("CLICK")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().equals(Material.BONE) || !pd.getKit().contains(Ability.HADES) || !item.getItemMeta().getDisplayName().equals(Ability.HADES.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.HADES, true))
		return;

	    cooldown.useAbility(p, Ability.HADES);

	    p.sendMessage(ChatColor.LIGHT_PURPLE + "Your wolfs have been spawned!");

	    HashSet<Wolf> wolfs = new HashSet<Wolf>();

	    for (int i = 0; i <= 6; i++) {

		final Wolf w = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
		w.setTamed(true);
		w.setOwner(p);
		w.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
		w.setAdult();
		w.setMaxHealth(20D);
		w.setHealth(20D);
		w.setSitting(false);
		w.setCustomName(p.getName() + "'s wolf ");
		w.setCustomNameVisible(true);
		wolfs.add(w);
	    }

	    new BukkitRunnable() {

		int time = 0;

		@Override
		public void run() {

		    time++;

		    if (p.isDead() || !pd.isIngame()) {

			for (Wolf w : wolfs)
			    w.remove();

			cancel();
			return;
		    }

		    if (time > 10 * 20) {

			for (Wolf w : wolfs)
			    w.remove();

			cancel();
			return;
		    }

		}
	    }.runTaskTimer(Main.getPlugin(), 0L, 1L);
	}
    }

    @EventHandler
    public void onClone(PlayerInteractEntityEvent e) {

	final Player p = (Player) e.getPlayer();
	final Entity target = (Entity) e.getRightClicked();
	final PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();

	if (!(item.hasItemMeta()))
	    return;

	if (!item.getType().equals(Material.SKULL_ITEM) || !pd.getKit().contains(Ability.CLONE) || !item.getItemMeta().getDisplayName().equals(Ability.CLONE.getName()))
	    return;

	e.setCancelled(true);

	if (cooldown.onCooldown(p, Ability.CLONE, true))
	    return;

	cooldown.useAbility(p, Ability.CLONE);

	if (!(target instanceof Player)) {

	    Entity entity = target;
	    LivingEntity entityliving = (LivingEntity) entity;
	    createClone(p, entityliving);

	}

	if ((target instanceof Player)) {

	    Player entity = (Player) target;
	    LivingEntity entityliving = (LivingEntity) entity;
	    createClone(p, entityliving);

	}

	p.sendMessage(ChatColor.LIGHT_PURPLE + "Your clones have been created!");
    }

    void createClone(Player p, LivingEntity entity) {

	PlayerData pd = Main.getPlayerData(p);
	HashSet<Wolf> wolfs = new HashSet<Wolf>();
	Location loc = p.getEyeLocation().clone();

	for (int yaw = -180; yaw <= 180; yaw += 60) {

	    loc.setYaw(yaw);

	    Vector handle = loc.getDirection().multiply(2);
	    handle.setY(0);
	    loc.add(handle);

	    Wolf w = (Wolf) p.getWorld().spawnEntity(new Location(p.getWorld(), loc.getX(), loc.getY(), loc.getZ()), EntityType.WOLF);

	    w.setTamed(true);
	    w.setOwner(p);
	    w.setAdult();
	    w.setMaxHealth(20D);
	    w.setHealth(20D);
	    w.setSitting(false);
	    w.setCustomName(p.getName());
	    w.setCustomNameVisible(true);
	    w.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
	    w.setSitting(true);

	    wolfs.add(w);

	    PlayerDisguise dis = new PlayerDisguise(p.getName());
	    dis.setReplaceSounds(true);
	    DisguiseAPI.disguiseEntity(w, dis);
	}

	new BukkitRunnable() {

	    int time = 0;

	    @Override
	    public void run() {

		time++;

		if (p.isDead() || !pd.isIngame()) {

		    for (Wolf w : wolfs)
			w.remove();

		    cancel();
		    return;
		}

		if (time > 10 * 20) {

		    for (Wolf w : wolfs)
			w.remove();

		    cancel();
		    return;
		}

	    }
	}.runTaskTimer(Main.getPlugin(), 0L, 1L);
    }

    @EventHandler
    public void onPhantom(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();
	final PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();
	if (e.getAction().toString().contains("CLICK")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().equals(Material.FEATHER) || !pd.getKit().contains(Ability.PHANTOM) || !item.getItemMeta().getDisplayName().equals(Ability.PHANTOM.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.PHANTOM, true))
		return;

	    cooldown.useAbility(p, Ability.PHANTOM);

	    p.setAllowFlight(true);
	    p.setFlying(true);
	    p.teleport(p.getLocation().add(0, 1, 0));
	    p.sendMessage(ChatColor.LIGHT_PURPLE + "You are now able to fly for 5 seconds!");

	    final ItemStack[] armor = p.getInventory().getArmorContents();
	    p.getInventory().setArmorContents(null);

	    ItemStack Helmet = Items.createitem(Material.LEATHER_HELMET, "Helmet", null);
	    ItemStack Chest = Items.createitem(Material.LEATHER_CHESTPLATE, "Chest", null);
	    ItemStack Leggings = Items.createitem(Material.LEATHER_LEGGINGS, "Leggings", null);
	    ItemStack Boots = Items.createitem(Material.LEATHER_BOOTS, "Boots", null);
	    ItemStack[] phantom = new ItemStack[] { Boots, Leggings, Chest, Helmet };

	    p.getInventory().setArmorContents(phantom);

	    for (Entity entity : p.getNearbyEntities(10, 10, 10)) {

		if (entity instanceof Player) {
		    Player target = (Player) entity;
		    target.sendMessage(ChatColor.DARK_GRAY + "There is a Phantom flying around! ");
		    target.sendMessage(ChatColor.DARK_GRAY + "He is not hacking, its part of the ability! ");
		}
	    }

	    new BukkitRunnable() {

		int fly = 5;

		@Override
		public void run() {

		    if (fly > 0) {
			fly--;
			p.sendMessage(fly + " seconds of flight remaining!");
		    }

		    if (fly == 0) {
			p.sendMessage(ChatColor.DARK_PURPLE + "Your flight were off!");
			p.setAllowFlight(false);
			p.setFlying(false);
			p.getInventory().setArmorContents(armor);
			cancel();
			return;
		    }
		}
	    }.runTaskTimer(Main.getPlugin(), 0L, 20L);
	}
    }

    @EventHandler
    public void onDive(PlayerInteractEntityEvent e) {

	final Player p = (Player) e.getPlayer();
	final Entity target = (Entity) e.getRightClicked();
	final PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();

	if (!(item.hasItemMeta()))
	    return;

	if (!item.getType().equals(Material.GHAST_TEAR) || !pd.getKit().contains(Ability.DIVER) || !item.getItemMeta().getDisplayName().equals(Ability.DIVER.getName()))
	    return;

	e.setCancelled(true);

	if (cooldown.onCooldown(p, Ability.DIVER, true))
	    return;
	cooldown.useAbility(p, Ability.DIVER);

	new BukkitRunnable() {

	    int time = 0;

	    @Override
	    public void run() {

		time++;

		p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, Material.WOOL);

		if (time == 1) {

		    double x = target.getLocation().getX();
		    double y = target.getLocation().getY();
		    double z = target.getLocation().getZ();

		    double x1 = p.getLocation().getX();
		    double y1 = p.getLocation().getY();
		    double z1 = p.getLocation().getZ();

		    Vector handle = new Vector((x1) - (x), (y1) - (y), (z1) - (z));
		    handle.normalize();
		    handle.setY(1);
		    p.setVelocity(handle);
		}

		if (time == 10) {

		    double x = target.getLocation().getX();
		    double y = target.getLocation().getY();
		    double z = target.getLocation().getZ();

		    double x1 = p.getLocation().getX();
		    double y1 = p.getLocation().getY();
		    double z1 = p.getLocation().getZ();

		    Vector handle = new Vector((x1) - (x), (y1) - (y), (z1) - (z));
		    handle.multiply(-1);
		    p.setVelocity(handle);

		}

		if (time == 15) {

		    Damageable d = (Damageable) target;
		    d.damage(6D);
		    target.setVelocity(p.getVelocity().normalize().setY(0.36));
		    cancel();
		    return;
		}
	    }
	}.runTaskTimer(Main.getPlugin(), 1L, 1L);
    }

    @EventHandler
    public void onKamehameha(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();
	final PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();
	if (e.getAction().toString().contains("RIGHT_CLICK")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().toString().contains("SWORD") || !pd.getKit().contains(Ability.KAMEHAMEHA) || !item.getItemMeta().getDisplayName().equals(Ability.KAMEHAMEHA.getName()))
		return;

	    e.setCancelled(true);

	    if (!p.isSneaking())
		return;

	    if (cooldown.onCooldown(p, Ability.KAMEHAMEHA, true))
		return;

	    p.getWorld().playEffect(p.getEyeLocation(), Effect.STEP_SOUND, Material.SOUL_SAND);

	    new BukkitRunnable() {

		int time = 0;

		@Override
		public void run() {

		    time++;

		    if (!p.isSneaking()) {
			cancel();
			return;
		    }

		    if (time > 20) {

			if (!p.isSneaking()) {
			    p.sendMessage(Main.pre + ChatColor.DARK_PURPLE + "You have to Block to get more strength");
			    p.getWorld().playEffect(p.getEyeLocation(), Effect.STEP_SOUND, Material.SOUL_SAND);
			    cancel();
			    return;
			}
		    }

		    if (time == 60) {

			Vector handle = p.getEyeLocation().getDirection();

			cooldown.useAbility(p, Ability.KAMEHAMEHA);
			handle.multiply(2F);

			new Beam(p, Material.SOUL_SAND, handle, true, new playerEffect() {

			    public void hit(Entity target) {

				((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
				((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 1));
				((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 1));
				target.setFireTicks(40);

				target.setVelocity(new Vector(0, 1, 0));

				for (Entity entity : target.getNearbyEntities(20, 20, 20)) {

				    if (!(entity instanceof Player))
					continue;

				    Damageable d1 = (Damageable) entity;
				    d1.damage(4D, target);

				    Player p = (Player) entity;
				    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
				}
			    }
			});

			cancel();
			return;

		    }
		}
	    }.runTaskTimer(Main.getPlugin(), 0L, 1L);
	}
    }

    @EventHandler
    public void onCamel(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!pd.getKit().contains(Ability.CAMEL))
	    return;

	if (p.getLocation().clone().subtract(0, 1, 0).getBlock().getType().toString().contains("SAND")) {
	    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
	    return;

	}

	if (!(p.getLocation().clone().subtract(0, 1, 0).getBlock().getType().toString().contains("SAND") || (p.getLocation().clone().subtract(0, 1, 0).getBlock().getType() == Material.AIR))) {
	    p.removePotionEffect(PotionEffectType.SPEED);
	}

    }

    @EventHandler
    public void onSentryGun(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();
	final PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();
	if (e.getAction().toString().contains("CLICK")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().equals(Material.ARROW) || !pd.getKit().contains(Ability.SENTRYGUN) || !item.getItemMeta().getDisplayName().equals(Ability.SENTRYGUN.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.SENTRYGUN, true))
		return;

	    cooldown.useAbility(p, Ability.SENTRYGUN);

	    new BukkitRunnable() {
		int times = 20;

		@Override
		public void run() {

		    times--;

		    if (p.isDead() || !p.isOnline())
			cancel();

		    if (times < 0) {
			p.sendMessage(ChatColor.DARK_PURPLE + "This charge of your sentry gun is empty!");
			cancel();
		    }
		    p.launchProjectile(Arrow.class).teleport(p.getEyeLocation().add(0, 0.25, 0));
		}
	    }.runTaskTimer(Main.getPlugin(), 0, 2);
	}
    }

    @EventHandler
    public void onMonkey(PlayerMoveEvent e) {

	final Player p = (Player) e.getPlayer();
	final PlayerData pd = Main.getPlayerData(p);

	if (!pd.getKit().contains(Ability.MONKEY))
	    return;

	if (!p.isSneaking())
	    return;

	Location loc = p.getLocation();

	if (loc.getBlock().getRelative(BlockFace.NORTH).getType().isSolid() || loc.getBlock().getRelative(BlockFace.SOUTH).getType().isSolid()
		|| loc.getBlock().getRelative(BlockFace.WEST).getType().isSolid() || loc.getBlock().getRelative(BlockFace.EAST).getType().isSolid()) {

	    Vector handle = p.getLocation().getDirection().multiply(1);
	    p.setVelocity(handle);
	    p.setFallDistance(-10);
	    p.getWorld().playSound(p.getLocation(), Sound.STEP_GRAVEL, 100, 100);

	}
    }

    @EventHandler
    public void onNapalm(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();
	final PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();
	if (e.getAction().toString().contains("CLICK")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().equals(Material.FIRE) || !pd.getKit().contains(Ability.NAPALM) || !item.getItemMeta().getDisplayName().equals(Ability.NAPALM.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.NAPALM, true))
		return;

	    cooldown.useAbility(p, Ability.NAPALM);

	    Fireball i = p.launchProjectile(Fireball.class);
	    i.setVelocity(i.getVelocity().multiply(2));
	    i.setIsIncendiary(false);
	    i.setMetadata("napalm", new FixedMetadataValue(Main.getPlugin(), p.getName()));

	}

    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {

	if (e.getEntity() == null || e.getEntityType() != EntityType.FIREBALL)
	    return;

	if (!e.getEntity().hasMetadata("napalm"))
	    return;

	e.setCancelled(true);

	Location loc = e.getEntity().getLocation();

	int X = loc.getBlockX();
	int Y = loc.getBlockY();
	int Z = loc.getBlockZ();

	for (Entity entity : e.getEntity().getNearbyEntities(5, 2, 5)) {

	    if (entity instanceof Player) {
		Player target = (Player) entity;
		target.damage(8D);
	    }

	}

	for (int x = -5; x < 5; x++) {
	    for (int y = -2; y < 2; y++) {
		for (int z = -5; z < 5; z++) {

		    Block block = e.getEntity().getWorld().getBlockAt(X + x, Y + y, Z + z);

		    if (block.getType() == Material.AIR) {
			block.setType(Material.FIRE);

			new BukkitRunnable() {

			    @Override
			    public void run() {

				block.setType(Material.AIR);

			    }
			}.runTaskLater(Main.getPlugin(), 5 * 20);
		    }
		}
	    }
	}
    }

    @EventHandler
    public void onRhino(PlayerToggleSneakEvent e) {

	final Player p = (Player) e.getPlayer();
	final PlayerData pd = Main.getPlayerData(p);

	if (!pd.getKit().contains(Ability.RHINO))
	    return;

	if (cooldown.onCooldown(p, Ability.RHINO, true))
	    return;
	cooldown.useAbility(p, Ability.RHINO);

	for (Entity entity : p.getNearbyEntities(5, 5, 5)) {

	    Vector handle = p.getLocation().getDirection();
	    handle.multiply(6);
	    handle.setY(1);

	    entity.setVelocity(handle);
	    continue;

	}

    }

    @EventHandler
    public void onBurstMine(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();
	final PlayerData pd = Main.getPlayerData(p);

	if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

	return; }

	ItemStack item = p.getItemInHand();

	if (!(item.hasItemMeta()))
	    return;

	if (!item.getType().equals(Material.CARPET) || !pd.getKit().contains(Ability.BURST_MINE) || !item.getItemMeta().getDisplayName().equals(Ability.BURST_MINE.getName()))
	    return;

	e.setCancelled(true);

	if (cooldown.onCooldown(p, Ability.BURST_MINE, true))
	    return;
	cooldown.useAbility(p, Ability.BURST_MINE);

	Block block = e.getClickedBlock().getRelative(BlockFace.UP);
	block.setType(Material.CARPET);
	block.setMetadata("Burst-Mine", new FixedMetadataValue(Main.getPlugin(), false));

	new BukkitRunnable() {

	    @Override
	    public void run() {

		block.setType(Material.AIR);

	    }
	}.runTaskLater(Main.getPlugin(), 5 * 20);
    }

    @EventHandler
    public void onBurstMine(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!pd.isIngame())
	    return;

	Block block = p.getLocation().getBlock();

	if (block.getType() == Material.CARPET && block.hasMetadata("Burst-Mine")) {

	    Vector handle = new Vector(0, 1, 0);
	    p.setVelocity(handle);
	    p.getWorld().playEffect(p.getLocation(), Effect.EXPLOSION, 1);
	    p.setFireTicks(40);
	    p.damage(8D);
	    block.setType(Material.AIR);

	}

    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onAngel(PotionSplashEvent e) {

	if (!(e.getPotion().getShooter() instanceof Player))
	    return;

	Player p = (Player) e.getPotion().getShooter();
	PlayerData pd = Main.getPlayerData(p);

	if (!pd.getKit().contains(Ability.ANGEL))
	    return;

	if (pd.getStyle() != Playstyle.POT) {
	    p.sendMessage(ChatColor.RED + "You can only use this ability as a potter! ");
	    return;
	}

	Location loc = e.getPotion().getLocation();
	Block block = loc.getBlock();
	Material blockmaterial = block.getType();
	block.setType(Material.OBSIDIAN);

	BlockProcessor.inst().addFuture(block, blockmaterial, block.getData(), 5 * 20, false);

    }

    @EventHandler
    public void onCowboy(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();
	if (e.getAction().toString().contains("CLICK")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().equals(Material.LEASH) || !pd.getKit().contains(Ability.COWBOY) || !item.getItemMeta().getDisplayName().equals(Ability.COWBOY.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.COWBOY, true))
		return;

	    cooldown.useAbility(p, Ability.COWBOY);

	    final Set<Horse> horses = new HashSet<>();

	    Location spawn = p.getEyeLocation();
	    spawn.add(p.getEyeLocation().getDirection().multiply(3));

	    Horse horse1 = (Horse) p.getWorld().spawnEntity(spawn, EntityType.HORSE);
	    Horse horse2 = (Horse) p.getWorld().spawnEntity(spawn, EntityType.HORSE);
	    Horse horse3 = (Horse) p.getWorld().spawnEntity(spawn, EntityType.HORSE);
	    Horse horse4 = (Horse) p.getWorld().spawnEntity(spawn, EntityType.HORSE);

	    horses.add(horse1);
	    horses.add(horse2);
	    horses.add(horse3);
	    horses.add(horse4);

	    for (Horse horse : horses) {

		horse.setAdult();
		horse.setTamed(false);
		horse.setCustomName(p.getName() + "'s horse");
		horse.setCustomNameVisible(true);
		horse.setTarget(null);

		new BukkitRunnable() {

		    int time = 5 * 20;

		    @Override
		    public void run() {

			time--;

			if (time < 0 || p.isDead() || !pd.isIngame() || horse.isDead()) {

			    horse.remove();
			    cancel();
			    return;
			}

			for (Entity entity : horse.getNearbyEntities(0.5, 0.5, 0.5)) {

			    if (entity instanceof Horse) { return; }
			    if (!(entity instanceof Damageable)) { return; }

			    Damageable d = (Damageable) entity;
			    d.damage(8D, horse);
			}

			Vector handle = p.getLocation().getDirection();
			handle.normalize();
			Location loc = p.getEyeLocation();
			loc.add(handle);

		    }
		}.runTaskTimer(Main.getPlugin(), 0, 1);
	    }
	}
    }

    private static HashMap<Player, AtomicInteger> masteruses = new HashMap<>();

    @EventHandler
    public void onBlockMaster(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();
	final PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();

	if (!(item.hasItemMeta()))
	    return;

	if (!item.getType().equals(Material.WOOD_SPADE) || !pd.getKit().contains(Ability.BLOCK_MASTER) || !item.getItemMeta().getDisplayName().equals(Ability.BLOCK_MASTER.getName()))
	    return;

	e.setCancelled(true);

	if (cooldown.onCooldown(p, Ability.BLOCK_MASTER, true))
	    return;

	if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {

	    Location loc = p.getEyeLocation();
	    loc.add(p.getEyeLocation().getDirection().multiply(3));
	    Block block = p.getWorld().getBlockAt(loc);
	    setAndRemoveBlock(p, block);
	    return;
	}

	if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

	    if (!e.hasBlock())
		return;

	    Block b = e.getClickedBlock();
	    BlockFace face = e.getBlockFace();
	    Block block = b.getRelative(face);
	    setAndRemoveBlock(p, block);
	    return;

	}
    }

    @SuppressWarnings("deprecation")
    private void setAndRemoveBlock(Player p, Block block) {

	if (block.getType() != Material.AIR || p.getEyeLocation().getBlock() == block || p.getLocation().getBlock() == block)
	    return;

	Material material = block.getType();
	BlockProcessor.inst().add(block, Material.DIAMOND_BLOCK, block.getData(), true);
	BlockProcessor.inst().addFuture(block, material, block.getData(), 5 * 20, true);

	if (masteruses.get(p) != null) {
	    masteruses.get(p).incrementAndGet();

	    if (masteruses.get(p).get() > 15) {
		masteruses.put(p, new AtomicInteger(1));
		cooldown.useAbility(p, Ability.BLOCK_MASTER);
		return;

	    }
	}

	if (masteruses.get(p) == null) {
	    masteruses.put(p, new AtomicInteger(1));
	}
    }

    @EventHandler
    public void onJesus(EntityDamageEvent e) {

	if (!(e.getEntity() instanceof Player))
	    return;

	Player p = (Player) e.getEntity();
	PlayerData pd = Main.getPlayerData(p);

	if (!pd.getKit().contains(Ability.JESUS))
	    return;

	Damageable d = p;

	if (d.getHealth() - e.getDamage() <= 0) {

	    if (playerswhousedJesus.contains(p)) {
		p.sendMessage(ChatColor.DARK_PURPLE + "You have already used Jesus!");
		return;
	    }

	    d.setHealth(d.getMaxHealth());
	    e.setDamage(0D);

	    PlayStyleInv pinv = new PlayStyleInv();
	    pinv.playStyleInv(p);
	    p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20, 2));

	    playerswhousedJesus.add(p);

	    new BukkitRunnable() {

		double height = 0;
		double radius = 10;

		double px = p.getLocation().getX();
		double py = p.getLocation().getY();
		double pz = p.getLocation().getZ();

		@SuppressWarnings("deprecation")
		@Override
		public void run() {

		    if (!pd.isIngame()) {
			cancel();
			return;
		    }

		    if (radius > 0) {

			radius = radius - 0.1;
			height = height + 0.1;

			double y = height;
			double x = radius * Math.sin(y);
			double z = radius * Math.cos(y);

			Location loc = new Location(p.getWorld(), px + x, py + y, pz + z);
			Location loc2 = new Location(p.getWorld(), px - z, py + y, pz + x);
			Location loc3 = new Location(p.getWorld(), px - x, py + y, pz - z);
			Location loc4 = new Location(p.getWorld(), px + z, py + y, pz - x);

			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles("fireworksSpark", (float) (loc.getX()), (float) (loc.getY()), (float) (loc.getZ()), 0, 0, 0, 0, 1);
			PacketPlayOutWorldParticles packet2 = new PacketPlayOutWorldParticles("fireworksSpark", (float) (loc2.getX()), (float) (loc2.getY()), (float) (loc2.getZ()), 0, 0, 0, 0, 1);
			PacketPlayOutWorldParticles packet3 = new PacketPlayOutWorldParticles("fireworksSpark", (float) (loc3.getX()), (float) (loc3.getY()), (float) (loc3.getZ()), 0, 0, 0, 0, 1);
			PacketPlayOutWorldParticles packet4 = new PacketPlayOutWorldParticles("fireworksSpark", (float) (loc4.getX()), (float) (loc4.getY()), (float) (loc4.getZ()), 0, 0, 0, 0, 1);

			for (Player online : Bukkit.getOnlinePlayers()) {
			    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
			    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet2);
			    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet3);
			    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet4);
			}

			p.setVelocity(new Vector(0, 0.1, 0));

		    } else {

			cancel();
			Location loc = p.getLocation();

			for (int i = 0; i < 5; i++) {

			    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles("fireworksSpark", (float) (loc.getX()), (float) (loc.getY()), (float) (loc.getZ()), i, i, i, i, 20);

			    for (Player online : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);

			    }
			}
		    }
		}
	    }.runTaskTimer(Main.getPlugin(), 0, 1);
	}
    }

    public static ArrayList<Player> playerswhousedJesus = new ArrayList<>();

    @EventHandler
    public void onJesusDeath(PlayerDeathEvent e) {
	playerswhousedJesus.remove(e.getEntity());
    }

    @EventHandler
    public void onSUPER_SAIYAJIN(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();
	if (e.getAction().toString().contains("RIGHT")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().toString().contains("SWORD") || !pd.getKit().contains(Ability.SUPER_SAIYAJIN) || !p.isSneaking())
		return;

	    if (cooldown.onCooldown(p, Ability.SUPER_SAIYAJIN, true))
		return;

	    cooldown.useAbility(p, Ability.SUPER_SAIYAJIN);

	    int level = p.getLevel();
	    new BukkitRunnable() {

		int time = 0;

		@Override
		public void run() {

		    p.setLevel(time);

		    if (time < 50) {

			time++;

			if (p.isSneaking())
			    return;

			cancel();
			p.setLevel(level);
			return;

		    } else {

			if (p.isSneaking())
			    return;

			cancel();
			p.setLevel(level);
			onShoot(p);
			return;

		    }
		}
	    }.runTaskTimer(Main.getPlugin(), 0, 1);
	}
    }

    private void onShoot(Player p) {

	Location loc = p.getEyeLocation();
	Vector handle1 = p.getEyeLocation().getDirection();
	Vector handle = p.getEyeLocation().getDirection();
	p.setVelocity(handle1.multiply(-2).setY(0.36));
	handle.multiply(2);

	new BukkitRunnable() {

	    int time = 0;

	    @Override
	    public void run() {
		time++;

		if (time > 5 * 20) {
		    cancel();
		    return;
		}

		loc.add(handle);
		p.getWorld().strikeLightning(loc);

		for (Entity entity : p.getWorld().getEntities()) {

		    Location eloc = entity.getLocation();
		    Location ploc = p.getLocation();

		    if (entity.getLocation().distance(loc) < 2) {

			Vector entityKnockback = new Vector(eloc.getX() - ploc.getX(), eloc.getY() - ploc.getY(), eloc.getZ() - ploc.getZ()).normalize().setY(0.36);

			entity.setVelocity(entityKnockback);
		    }
		}
	    }
	}.runTaskTimer(Main.getPlugin(), 0, 1);
    }

    @EventHandler
    public void onMario_Cart(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();

	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();

	if (e.getAction().toString().contains("CLICK")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().equals(Material.MINECART) || !pd.getKit().contains(Ability.MARIO_CART) || !item.getItemMeta().getDisplayName().equals(Ability.MARIO_CART.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.MARIO_CART, true))
		return;

	    cooldown.useAbility(p, Ability.MARIO_CART);

	    Minecart cart = (Minecart) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.MINECART);
	    cart.setPassenger(p);
	    cart.setMetadata(p.getName(), new FixedMetadataValue(Main.getPlugin(), false));
	    cart.setMaxSpeed(cart.getMaxSpeed() + 0.6D);

	    new BukkitRunnable() {

		@Override
		public void run() {

		    cart.setPassenger(null);
		    cart.setMetadata(null, new FixedMetadataValue(Main.getPlugin(), false));
		    cart.eject();
		    cart.setDamage(40D);
		    cart.remove();
		    return;

		}
	    }.runTaskLater(Main.getPlugin(), 20 * 20);
	}
    }

    @EventHandler
    public void onMineCartEnter(VehicleEnterEvent e) {

	if (!(e.getVehicle() instanceof Minecart) || !(e.getVehicle().getPassenger() instanceof Player))
	    return;

	Player p = (Player) e.getVehicle().getPassenger();

	if (!e.getVehicle().hasMetadata(p.getName())) {
	    e.setCancelled(true);
	    p.sendMessage(ChatColor.DARK_PURPLE + "You can not use this cart!");
	}
    }

    @EventHandler
    public void onMinecart(VehicleMoveEvent e) {

	if (!(e.getVehicle() instanceof Minecart) || !(e.getVehicle().getPassenger() instanceof Player))
	    return;

	Player p = (Player) e.getVehicle().getPassenger();
	PlayerData pd = Main.getPlayerData(p);

	Minecart cart = (Minecart) e.getVehicle();

	if (pd.getKit().contains(Ability.MARIO_CART)) {

	    if (e.getVehicle().hasMetadata(p.getName())) {
		Location loc = p.getEyeLocation().clone();
		loc.setPitch(0);
		Vector handle = loc.getDirection();
		handle.multiply(15);

		handle.divide(cart.getDerailedVelocityMod());

		if (cart.isOnGround()) {

		    if (cart.getLocation().getBlock().getRelative(BlockFace.NORTH).getType().isSolid() || cart.getLocation().getBlock().getRelative(BlockFace.SOUTH).getType().isSolid()
			    || cart.getLocation().getBlock().getRelative(BlockFace.WEST).getType().isSolid() || cart.getLocation().getBlock().getRelative(BlockFace.EAST).getType().isSolid()) {

			cart.setVelocity(handle.setY(+2));
			return;
		    }
		    cart.setVelocity(handle.setY(0.5));
		} else

		if (!cart.isOnGround())
		    cart.setVelocity(handle.setY(-0.5));

	    }

	}

    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onDevastor(EntityDamageEvent e) {

	if (!(e.getEntity() instanceof Player))
	    return;

	if (e.getCause() != DamageCause.FALL)
	    return;

	Player p = (Player) e.getEntity();
	PlayerData pd = Main.getPlayerData(p);

	if (!pd.getKit().contains(Ability.DEVASTOR))
	    return;

	e.setCancelled(true);

	Location loc = p.getLocation();
	int X = loc.getBlockX();
	int Y = loc.getBlockY();
	int Z = loc.getBlockZ();

	HashSet<Block> blocks = new HashSet<>();

	int falldistance = (int) p.getFallDistance();

	if (falldistance < 40)
	    falldistance = falldistance / 2;

	if (falldistance > 40)
	    falldistance = 10;

	for (int x = -falldistance; x < falldistance; x++) {
	    for (int y = -2; y < 2; y++) {
		for (int z = -falldistance; z < falldistance; z++) {

		    Block block = p.getWorld().getBlockAt(X + x, Y + y, Z + z);
		    blocks.add(block);
		}
	    }
	}

	for (Block block : blocks) {

	    float x = (float) (Math.random() * ((1 - -1)));

	    FallingBlock fblock = p.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
	    fblock.setDropItem(false);
	    fblock.setVelocity(new Vector(0, x, 0));
	    fblock.setMetadata("Devastor", new FixedMetadataValue(Main.getPlugin(), false));
	}

    }

    @EventHandler
    public void onDevastorChange(EntityChangeBlockEvent e) {

	if (!(e.getEntity() instanceof FallingBlock))
	    return;

	FallingBlock fallingBlock = (FallingBlock) e.getEntity();

	if (fallingBlock.hasMetadata("Devastor")) {
	    fallingBlock.getWorld().playEffect(fallingBlock.getLocation(), Effect.STEP_SOUND, fallingBlock.getMaterial());

	    for (Entity entity : fallingBlock.getNearbyEntities(1, 1, 1)) {

		if (!(entity instanceof Damageable))
		    continue;

		if (entity instanceof Player) {

		    PlayerData pd = Main.getPlayerData((Player) entity);
		    if (pd.getKit().contains(Ability.DEVASTOR))
			continue;
		}

		((Damageable) entity).damage(4D);

	    }
	    e.setCancelled(true);
	}

    }

    // not really working well

    @EventHandler
    public void onCraftAura(CraftItemEvent e) {

	Player p = (Player) e.getWhoClicked();
	PlayerData pd = Main.getPlayerData(p);

	if (!pd.getKit().contains(Ability.CRAFT_AURA))
	    return;

	ItemStack item = e.getCurrentItem();

	if (item.getType() == Material.MUSHROOM_SOUP || item.getType() == Material.POTION) {

	    if (cooldown.onCooldown(p, Ability.CRAFT_AURA, false))
		return;

	    cooldown.useAbility(p, Ability.CRAFT_AURA);

	    p.getWorld().createExplosion(p.getLocation(), 0);
	    p.getWorld().createExplosion(p.getLocation(), 0);
	    p.getWorld().createExplosion(p.getLocation(), 0);

	    for (Entity entities : p.getNearbyEntities(4, 4, 4)) {

		if (!(entities instanceof Damageable))
		    return;

		Damageable d = (Damageable) entities;
		d.damage(6D, p);

	    }

	}

    }

    private static HashMap<Player, AtomicInteger> tarzanuses = new HashMap<>();

    @EventHandler
    public void onTarzan(PlayerToggleSneakEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!pd.getKit().contains(Ability.TARZAN) || !p.isSneaking())
	    return;

	if (cooldown.onCooldown(p, Ability.TARZAN, true))
	    return;

	Playerchecks checks = new Playerchecks();

	if (!checks.inAir(p))
	    return;

	Vector handle = p.getLocation().getDirection();
	handle.multiply(3);
	handle.setY(0.5);

	p.setVelocity(handle);

	if (tarzanuses.get(p) != null) {
	    tarzanuses.get(p).incrementAndGet();

	    if (tarzanuses.get(p).get() > 3) {
		tarzanuses.put(p, new AtomicInteger(1));
		cooldown.useAbility(p, Ability.TARZAN);
		return;

	    }
	}

	if (tarzanuses.get(p) == null) {
	    tarzanuses.put(p, new AtomicInteger(1));
	}

    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onZen(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();

	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();

	if (e.getAction().toString().contains("CLICK")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!pd.getKit().contains(Ability.ZEN) || !item.getItemMeta().getDisplayName().equals(Ability.ZEN.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.ZEN, true))
		return;

	    for (Player ps : Bukkit.getOnlinePlayers()) {

		if (p == ps)
		    continue;

		PlayerData pds = Main.getPlayerData(ps);

		if (!pds.isIngame())
		    continue;

		if (ps.getLocation().distance(p.getLocation()) < 50) {

		    p.teleport(ps);
		    p.sendMessage(ChatColor.GOLD + "Your Zen teleported you to an opponent!");
		    cooldown.useAbility(p, Ability.ZEN);
		    return;
		}

		p.sendMessage(ChatColor.YELLOW + "Your Zen is not strong enough to find an opponent!");
	    }
	}
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onAntiZen(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();

	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();

	if (e.getAction().toString().contains("CLICK")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!pd.getKit().contains(Ability.ANTI_ZEN) || !item.getItemMeta().getDisplayName().equals(Ability.ANTI_ZEN.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.ANTI_ZEN, true))
		return;

	    for (Player ps : Bukkit.getOnlinePlayers()) {

		if (p == ps)
		    continue;

		PlayerData pds = Main.getPlayerData(ps);

		if (!pds.isIngame())
		    continue;

		if (pds.getKit().contains(Ability.ZEN) && ps.getLocation().distance(p.getLocation()) < 50) {

		    ps.teleport(p);
		    p.sendMessage(ChatColor.DARK_PURPLE + "Your destructive Power teleported a zen fighter to you!");
		    cooldown.useAbility(p, Ability.ANTI_ZEN);
		    return;
		}

		p.sendMessage(ChatColor.DARK_PURPLE + "Could not find a zen fighter!");
	    }

	}
    }

    private static HashSet<Player> parachute = new HashSet<>();
    private static HashMap<Player, HashSet<Block>> blocks = new HashMap<>();

    @EventHandler
    public void onParachute(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();

	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();

	if (e.getAction().toString().contains("CLICK")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!pd.getKit().contains(Ability.PARACHUTE) || !item.getItemMeta().getDisplayName().equals(Ability.PARACHUTE.getName()))
		return;

	    e.setCancelled(true);

	    Playerchecks ps = new Playerchecks();

	    if (!ps.inAir(p))
		return;

	    if (cooldown.onCooldown(p, Ability.PARACHUTE, true))
		return;
	    cooldown.useAbility(p, Ability.PARACHUTE);

	    parachute.add(p);
	    p.sendMessage(ChatColor.LIGHT_PURPLE + "You opened your parachute!");

	}
    }

    @EventHandler
    public void onParachuteMove(PlayerMoveEvent e) {

	Player p = e.getPlayer();

	if (!parachute.contains(p))
	    return;

	Playerchecks ps = new Playerchecks();

	if (!ps.inAir(p)) {

	    parachute.remove(p);
	    p.setFallDistance(0);

	    if (blocks.get(p) != null) {

		for (Block b : blocks.get(p))
		    if (b.hasMetadata("parachute"))
			b.setType(Material.AIR);

		blocks.get(p).clear();
	    }
	    return;
	}

	Location to = e.getTo();
	to.setY(e.getFrom().getY() - 0.1);
	Vector handle = new Vector(to.getX() - e.getFrom().getX(), to.getY() - e.getFrom().getY(), to.getZ() - e.getFrom().getZ());

	p.setVelocity(handle);

	final float newZl = (float) (p.getLocation().getZ() + (-2 * Math.sin(Math.toRadians(p.getLocation().getYaw() + 90 * 1))));

	final float newXl = (float) (p.getLocation().getX() + (-2 * Math.cos(Math.toRadians(p.getLocation().getYaw() + 90 * 1))));

	final float newZr = (float) (p.getLocation().getZ() + (2 * Math.sin(Math.toRadians(p.getLocation().getYaw() + 90 * 1))));

	final float newXr = (float) (p.getLocation().getX() + (2 * Math.cos(Math.toRadians(p.getLocation().getYaw() + 90 * 1))));

	Location loc = p.getLocation().clone().add(0, 4, 0);

	Block block = loc.getBlock();
	Block block1 = p.getWorld().getBlockAt(loc.clone().add(newXl, 0, newZl));
	Block block2 = p.getWorld().getBlockAt(loc.clone().add(newXr, 0, newZr));

	if (blocks.get(p) != null) {

	    for (Block b : blocks.get(p))
		if (b.hasMetadata("parachute"))
		    b.setType(Material.AIR);

	    blocks.get(p).clear();
	} else {
	    blocks.put(p, new HashSet<>());
	}

	blocks.get(p).add(block);
	blocks.get(p).add(block1);
	blocks.get(p).add(block2);

	for (Block b : blocks.get(p)) {

	    b.setType(Material.WOOL);
	    b.setMetadata("parachute", new FixedMetadataValue(Main.getPlugin(), false));
	}

    }

    @EventHandler
    public void onGrapple(PlayerInteractEvent e) {

	final Player p = (Player) e.getPlayer();

	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();

	if (!(item.hasItemMeta()))
	    return;

	if (!pd.getKit().contains(Ability.GRAPPLER) || !item.getItemMeta().getDisplayName().equals(Ability.GRAPPLER.getName()))
	    return;

	e.setCancelled(true);

	if (e.getAction().toString().contains("LEFT")) {

	    GrapplerRod rod = new GrapplerRod(p);
	    rod.Throw();
	}

	if (e.getAction().toString().contains("RIGHT")) {

	    GrapplerRod rod = GrapplerRod.Grapplerrods.get(p);

	    if (rod == null)
		return;
	    rod.hook();
	}
    }

    @EventHandler
    private void onHook(EntityDamageByEntityEvent e) {

	if (!(e.getEntity() instanceof LivingEntity))
	    return;

	LivingEntity entity = (LivingEntity) e.getEntity();
	Entity damager = e.getDamager();

	if (!(damager instanceof Snowball))
	    return;

	GrapplerRod rod = GrapplerRod.rods.get(damager);

	if (rod == null)
	    return;

	rod.setHooked(true);
	rod.setHooked(entity);
	rod.getOwner().sendMessage("Your rod is now hooked at an entity!");
    }

    @EventHandler
    private void onHook(ProjectileHitEvent e) {

	Entity entity = e.getEntity();

	if (!(entity instanceof Snowball))
	    return;

	GrapplerRod rod = GrapplerRod.rods.get(entity);

	if (rod == null)
	    return;

	rod.setHooked(true);
	rod.setHooked(rod.getBukkitEntity());
	rod.getOwner().sendMessage("Your rod is now hooked!");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

	Player p = e.getEntity();

	GrapplerRod rod = GrapplerRod.Grapplerrods.get(p);

	if (rod == null)
	    return;

	rod.destroy();
    }

    @EventHandler
    public void onSonic(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	ItemStack item = e.getPlayer().getItemInHand();

	PlayerData pd = Main.getPlayerData(p);

	if (e.getAction().toString().contains("CLICK")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!pd.getKit().contains(Ability.SONIC) || !item.getItemMeta().getDisplayName().equals(Ability.SONIC.getName()))
		return;

	    e.setCancelled(true);

	    if (cooldown.onCooldown(p, Ability.SONIC, true))
		return;

	    cooldown.useAbility(p, Ability.SONIC);

	    Float walkspeed = p.getWalkSpeed();
	    ItemStack boots = p.getInventory().getBoots();
	    p.setWalkSpeed(walkspeed * 4);

	    p.getInventory().setBoots(Items.createBoots(Color.BLUE, "Sonic", null));

	    new BukkitRunnable() {

		int time = 10 * 20;

		@Override
		public void run() {

		    time--;

		    Vector handle = p.getLocation().getDirection();

		    if (p.isSprinting())
			p.getWorld().playEffect(p.getLocation().clone().add(0, 1, 0).subtract(handle), Effect.STEP_SOUND, Material.LAPIS_BLOCK);

		    if (time < 0) {
			p.setWalkSpeed(walkspeed);
			p.getInventory().setBoots(boots);
			cancel();
			return;
		    }
		}
	    }.runTaskTimer(Main.getPlugin(), 0, 1);

	}
    }

    @EventHandler
    public void onPrison(PlayerInteractEntityEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	Entity entity = e.getRightClicked();

	ItemStack item = e.getPlayer().getItemInHand();

	if (!(item.hasItemMeta()))
	    return;

	if (!pd.getKit().contains(Ability.PRISONER) || !item.getItemMeta().getDisplayName().equals(Ability.PRISONER.getName()))
	    return;

	e.setCancelled(true);

	if (cooldown.onCooldown(p, Ability.PRISONER, true))
	    return;

	cooldown.useAbility(p, Ability.PRISONER);
	new Prison(p, entity, Material.GLASS, Material.AIR, 10 * 20, null);

    }

    @EventHandler
    public void onIcePrisoner(PlayerInteractEntityEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	Entity entity = e.getRightClicked();

	ItemStack item = e.getPlayer().getItemInHand();

	if (!(item.hasItemMeta()))
	    return;

	if (!pd.getKit().contains(Ability.ICEPRISONER) || !item.getItemMeta().getDisplayName().equals(Ability.ICEPRISONER.getName()))
	    return;

	e.setCancelled(true);

	if (cooldown.onCooldown(p, Ability.ICEPRISONER, true))
	    return;

	cooldown.useAbility(p, Ability.ICEPRISONER);
	new Prison(p, entity, Material.ICE, Material.ICE, 10 * 20, new TargetEffect() {

	    public void effect(Entity target) {
		((CraftLivingEntity) target).damage(3D);
	    }
	});
    }

    @EventHandler
    public void onAdvancedPrisoner(PlayerInteractEntityEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	Entity entity = e.getRightClicked();

	ItemStack item = e.getPlayer().getItemInHand();

	if (!(item.hasItemMeta()))
	    return;

	if (!pd.getKit().contains(Ability.ADVANCEDPRISONER) || !item.getItemMeta().getDisplayName().equals(Ability.ADVANCEDPRISONER.getName()))
	    return;

	e.setCancelled(true);

	if (cooldown.onCooldown(p, Ability.ADVANCEDPRISONER, true))
	    return;

	cooldown.useAbility(p, Ability.ADVANCEDPRISONER);
	new Prison(p, entity, Material.GLASS, Material.LAVA, 10 * 20, new TargetEffect() {

	    public void effect(Entity target) {
		((CraftLivingEntity) target).damage(3D);
	    }
	});
    }

    @EventHandler
    public void onAdvancedPrisonerTp(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = e.getPlayer().getItemInHand();

	if (!(item.hasItemMeta()))
	    return;

	if (!pd.getKit().contains(Ability.ADVANCEDPRISONER) || !item.getItemMeta().getDisplayName().equals(Ability.ADVANCEDPRISONER.getName()))
	    return;

	e.setCancelled(true);

	Prison prison = Prison.prisons.get(p);

	if (prison == null)
	    return;

	if (prison.joinable) {
	    p.teleport(prison.getTarget());
	}
    }

    @EventHandler
    public void onCataclysm(PlayerInteractEvent e) {
	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	ItemStack item = e.getPlayer().getItemInHand();
	if (!(item.hasItemMeta()))
	    return;
	if (!pd.getKit().contains(Ability.CATACLYSM_BEAM) || !item.getItemMeta().getDisplayName().equals(Ability.CATACLYSM_BEAM.getName()))
	    return;
	e.setCancelled(true);
	if (cooldown.onCooldown(p, Ability.CATACLYSM_BEAM, true))
	    return;
	cooldown.useAbility(p, Ability.CATACLYSM_BEAM);

	Vector handle = p.getEyeLocation().getDirection();
	handle.multiply(2F);

	new Beam(p, Material.GLASS, handle, false, new playerEffect() {
	    public void hit(Entity target) {
		int random = (int) (Math.random() * 3 + 0);
		if (random == 1)
		    new Prison(p, target, Material.ICE, Material.ICE, 10 * 20, new TargetEffect() {
			public void effect(Entity target) {
			    ((CraftLivingEntity) target).damage(3D);
			}
		    });
		if (random == 2)
		    new Prison(p, target, Material.GLASS, Material.LAVA, 10 * 20, new TargetEffect() {
			public void effect(Entity target) {
			    ((CraftLivingEntity) target).damage(3D);
			}
		    });

		if (random == 3)
		    new Prison(p, target, Material.GLASS, Material.AIR, 10 * 20, null);
	    }
	});
    }

    @EventHandler
    public void onJedi(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();
	if (e.getAction().toString().contains("RIGHT")) {

	    if (!(item.hasItemMeta()))
		return;

	    if (!item.getType().toString().contains("SWORD") || !pd.getKit().contains(Ability.JEDI))
		return;

	    if (cooldown.onCooldown(p, Ability.JEDI, false))
		return;

	    cooldown.useAbility(p, Ability.JEDI);

	    new BukkitRunnable() {

		int time = 10 * 20;

		@Override
		public void run() {

		    time--;

		    if (time < 0) {

			cancel();
			return;
		    }

		    if (!p.isBlocking()) {

			cancel();
			return;
		    }

		    for (Entity entity : p.getNearbyEntities(4, 4, 4)) {

			Vector handle = entity.getLocation().clone().toVector().subtract(p.getLocation().toVector());
			handle.normalize();
			handle.multiply(0.5);
			entity.setVelocity(handle);
		    }
		}

	    }.runTaskTimer(Main.getPlugin(), 0, 1);
	}
    }

    @EventHandler
    public void onDolphin(PlayerToggleSneakEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!e.isSneaking())
	    return;

	if (!new Playerchecks().isInLiquid(p))
	    return;

	if (!pd.getKit().contains(Ability.DOLPHIN))
	    return;

	Vector handle = p.getLocation().getDirection();
	handle.multiply(3);
	handle.add(new Vector(0, 2, 0));

	p.setVelocity(handle);
	p.setFallDistance(-20);
    }

    private static HashMap<Player, AtomicInteger> dombohits = new HashMap<>();

    @EventHandler
    public void onDombo(EntityDamageByEntityEvent e) {

	if (e.getEntity() instanceof Player) {

	    Player target = (Player) e.getEntity();
	    PlayerData td = Main.getPlayerData(target);

	    if (!td.getKit().contains(Ability.DOMBO))
		return;

	    dombohits.put(target, new AtomicInteger(0));
	}

	if (!(e.getDamager() instanceof Player))
	    return;

	Player p = (Player) e.getDamager();
	PlayerData pd = Main.getPlayerData(p);

	if (!pd.getKit().contains(Ability.DOMBO))
	    return;

	if (dombohits.get(p) == null)
	    dombohits.put(p, new AtomicInteger(0));

	if (dombohits.get(p).incrementAndGet() >= 4) {
	    dombohits.put(p, new AtomicInteger(0));

	    if (p.hasPotionEffect(PotionEffectType.SPEED)) {
		for (PotionEffect effect : p.getActivePotionEffects()) {

		    if (effect.getType().equals(PotionEffectType.SPEED)) {

			int amplifier = effect.getAmplifier();
			amplifier++;

			p.removePotionEffect(PotionEffectType.SPEED);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, amplifier));
		    }
		}
	    } else
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 0));
	}
    }

    @EventHandler
    public void onWitch(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	ItemStack item = e.getPlayer().getItemInHand();

	if (!(item.hasItemMeta()))
	    return;

	if (!pd.getKit().contains(Ability.WITCH) || !item.getItemMeta().getDisplayName().equals(Ability.WITCH.getName()))
	    return;

	e.setCancelled(true);

	if (cooldown.onCooldown(p, Ability.WITCH, true))
	    return;

	cooldown.useAbility(p, Ability.WITCH);

	Location loc = p.getEyeLocation();

	for (int yaw = -180; yaw <= 180; yaw += 10) {

	    ItemStack potionitem = Items.createPotion(PotionType.INSTANT_DAMAGE, 2, "Witch", true);

	    ThrownPotion thrownPotion = p.launchProjectile(ThrownPotion.class);
	    thrownPotion.setItem(potionitem);

	    loc.setYaw(yaw);

	    Vector handle = loc.getDirection().multiply(2);
	    thrownPotion.setVelocity(handle);

	}
    }

    @EventHandler
    public void onWitchDamage(PotionSplashEvent e) {

	for (Entity entity : e.getAffectedEntities()) {

	    if (!(entity instanceof Player))
		continue;

	    Player target = (Player) entity;
	    PlayerData td = Main.getPlayerData(target);

	    if (td.getKit().contains(Ability.WITCH))
		e.setIntensity((LivingEntity) entity, 0);

	}

    }

    @EventHandler
    public void onTest(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	ItemStack item = e.getPlayer().getItemInHand();

	if (!(item.hasItemMeta()))
	    return;

	if (!pd.getKit().contains(Ability.TEST) || !item.getItemMeta().getDisplayName().equals(Ability.TEST.getName()))
	    return;

	e.setCancelled(true);

	if (cooldown.onCooldown(p, Ability.TEST, true))
	    return;

	cooldown.useAbility(p, Ability.TEST);

	ArrayList<String> info = new ArrayList<>();
	info.add(ChatColor.RED + "Test");

	final Hologram hologram = new Hologram(p.getEyeLocation());
	hologram.set(info);
	hologram.show();

	new BukkitRunnable() {

	    @Override
	    public void run() {

		hologram.updatePosition(p.getEyeLocation().clone().add(0, 1, 0));

	    }
	}.runTaskTimer(Main.getPlugin(), 0, 1);
    }
}
