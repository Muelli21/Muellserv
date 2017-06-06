package me.Muell.server.bot;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Muell.server.Main;
import me.Muell.server.listener.SpawnListener;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.Items;
import me.Muell.server.types.PlayerData;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityZombie;
import net.minecraft.server.v1_7_R4.GenericAttributes;
import net.minecraft.server.v1_7_R4.PacketPlayOutAnimation;
import net.minecraft.server.v1_7_R4.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R4.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R4.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R4.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_7_R4.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_7_R4.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R4.World;
import net.minecraft.server.v1_7_R4.WorldServer;

public class CustomZombie extends EntityZombie {

    // http://pastebin.com/EfwAEmuG

    Plugin plugin = Main.getPlugin();
    Server server = Bukkit.getServer();

    @SuppressWarnings("rawtypes")
    public CustomZombie(final World world, final Player p, double speedMultiplier, double lives, boolean hardcore) {
	super(world);

	final Location loc = p.getLocation();
	final PlayerData pd = Main.getPlayerData(p);

	final net.minecraft.server.v1_7_R4.Entity entity = ((CraftEntity) p).getHandle();

	String name = "";

	if (hardcore) {

	    LivingEntity livingzombie = (LivingEntity) getBukkitEntity();

	    net.minecraft.server.v1_7_R4.ItemStack nmssword = CraftItemStack
		    .asNMSCopy(Items.createenchanteditem(Material.DIAMOND_SWORD, "Sword", null, Enchantment.KNOCKBACK, 0, Enchantment.DAMAGE_ALL, 1));
	    setEquipment(0, nmssword);

	    ItemStack helmet = Items.createitem(Material.DIAMOND_HELMET, "Helmet", null);
	    ItemStack chest = Items.createitem(Material.DIAMOND_CHESTPLATE, "Chest", null);
	    ItemStack leggings = Items.createitem(Material.DIAMOND_LEGGINGS, "Leggings", null);
	    ItemStack boots = Items.createitem(Material.DIAMOND_BOOTS, "Boots", null);

	    livingzombie.getEquipment().setHelmet(helmet);
	    livingzombie.getEquipment().setChestplate(chest);
	    livingzombie.getEquipment().setLeggings(leggings);
	    livingzombie.getEquipment().setBoots(boots);

	    livingzombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 4));
	    livingzombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));

	    name = "Schmockyyy";

	} else {

	    net.minecraft.server.v1_7_R4.ItemStack nmssword = CraftItemStack.asNMSCopy(Items.createenchanteditem(Material.STONE_SWORD, "Sword", null, Enchantment.KNOCKBACK, 0));
	    setEquipment(0, nmssword);

	    name = p.getName();
	}

	setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

	final WorldServer world1 = ((CraftWorld) p.getWorld()).getHandle();
	world1.addEntity(this);

	getAttributeInstance(GenericAttributes.maxHealth).setValue(lives);

	// setSprinting(true);

	getAttributeInstance(GenericAttributes.d).setValue(0.4D * speedMultiplier);
	getAttributeInstance(GenericAttributes.b).setValue(100.0D);
	getAttributeInstance(GenericAttributes.c).setValue(1D); // 1.7 // 0.9
	getAttributeInstance(GenericAttributes.e).setValue(0D);

	setHealth(getMaxHealth());
	setCustomName(p.getName());
	setCustomNameVisible(true);
	setOnFire(0);

	List goalB = (List) getPrivateField("b", PathfinderGoalSelector.class, goalSelector);
	goalB.clear();
	List goalC = (List) getPrivateField("c", PathfinderGoalSelector.class, goalSelector);
	goalC.clear();
	List targetB = (List) getPrivateField("b", PathfinderGoalSelector.class, targetSelector);
	targetB.clear();
	List targetC = (List) getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
	targetC.clear();

	this.goalSelector.a(0, new PathfinderGoalFloat(this));
	this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1D, true)); // 1
	this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 20.0F));
	this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
	this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
	this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));

	PlayerDisguise dis = new PlayerDisguise(name);
	dis.setReplaceSounds(true);
	DisguiseAPI.disguiseEntity(getBukkitEntity(), dis);

	setTarget(entity);

	final CustomZombie b = this;

	new BukkitRunnable() {

	    int jump = 1;
	    int damage = 0;
	    boolean start = false;

	    @Override
	    public void run() {

		damage++;

		if (!p.isOnline()) {

		    world.removeEntity(b);
		    cancel();
		    return;
		}

		if (p.isDead()) {

		    p.sendMessage(ChatColor.WHITE + "The bot had " + getHealth() / 2 + ChatColor.RED + " hearths" + ChatColor.WHITE + " left");
		    world.removeEntity(b);

		    cancel();
		    return;
		}

		if (!b.isAlive()) {

		    if (!p.isDead()) {

			Firework firework = b.getBukkitEntity().getWorld().spawn(p.getLocation(), Firework.class);

			FireworkEffect effect = FireworkEffect.builder().withColor(Color.RED).flicker(true).trail(true).withFade(Color.ORANGE).with(FireworkEffect.Type.BALL).build();

			FireworkMeta meta = firework.getFireworkMeta();
			meta.addEffect(effect);
			meta.setPower(0);

			firework.setFireworkMeta(meta);

			p.sendMessage(ChatColor.GOLD + "You killed the Bot! You will be teleported to hub in 5 seconds!");

			new BukkitRunnable() {

			    @Override
			    public void run() {
				SpawnListener sl = new SpawnListener();
				sl.spawnMethod(p);

			    }
			}.runTaskLater(Main.getPlugin(), 5 * 20L);

			cancel();

			return;
		    }

		}

		if (pd.getGamemode() != Gamemode.ONEVSONE) {

		    p.sendMessage(ChatColor.WHITE + "The bot had " + getHealth() / 2 + ChatColor.RED + " hearths" + ChatColor.WHITE + " left");
		    world.removeEntity(b);

		    cancel();
		    return;

		}

		if (p.getLocation().distanceSquared(b.getBukkitEntity().getLocation()) < 150) {

		    PacketPlayOutAnimation animation = new PacketPlayOutAnimation(b, 0);
		    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(animation);

		}

		if (p.getLocation().distanceSquared(b.getBukkitEntity().getLocation()) < 81 && !start) {

		    start = true;
		}

		if (!start) {

		    getBukkitEntity().setVelocity(new Vector(0, 0, 0));
		}

		int range = (int) (Math.random() * (6 - 4) + 4);

		if (getBukkitEntity().isOnGround()) {

		    if (p.getLocation().distanceSquared(getBukkitEntity().getLocation()) <= range) {
			hit();
			jump--;
		    }

		} else {
		    if (p.getLocation().distanceSquared(getBukkitEntity().getLocation()) <= range - 0.3) {
			hit();
			jump--;
		    }
		}
		if (jump <= 0 && getBukkitEntity().isOnGround() && p.getLocation().distanceSquared(getBukkitEntity().getLocation()) < 7) {

		    jump();
		    jump = 30;
		}

	    }

	    private void hit() {

		if (damage > 15) {
		    p.damage(7D, getBukkitEntity());
		    damage = 0;
		}

	    }
	}.runTaskTimer(Main.getPlugin(), 0, 1L);

    }

    public void jump() {

	bj();
    }

    public static Object getPrivateField(String fieldName, @SuppressWarnings("rawtypes") Class clazz, Object object) {
	Field field;
	Object o = null;
	try {
	    field = clazz.getDeclaredField(fieldName);
	    field.setAccessible(true);
	    o = field.get(object);
	} catch (NoSuchFieldException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}
	return o;
    }
}