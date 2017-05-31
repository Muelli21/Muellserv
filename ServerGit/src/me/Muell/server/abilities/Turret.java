package me.Muell.server.abilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.Muell.server.Main;
import me.Muell.server.types.Hologram;
import me.Muell.server.types.PlayerData;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.DroppedItemWatcher;

public class Turret implements Listener {

    public static HashMap<Minecart, TurretObject> turrets = new HashMap<>();

    class TurretObject {

	private Player p;
	private Minecart cart;
	private int health;
	private int missiles;
	private int bullets;
	private boolean selfshooting = false;
	private boolean healthupdate = false;
	private boolean missilesupdate = false;
	private boolean bulletsupdate = false;
	private boolean update = false;
	private BukkitTask t;
	private Block fence;
	private BlockState state;
	private Hologram hologram;

	Location loc;

	public TurretObject(Player p) {

	    this.p = p;
	    this.loc = p.getLocation().add(0, 1.5, 0);
	    this.health = 20;
	    this.missiles = 20;
	    this.bullets = 200;
	    set(p);

	}

	public int getBullets() {
	    return bullets;
	}

	public void setBullets(int bullets) {
	    this.bullets = bullets;
	}

	public int getMissiles() {
	    return missiles;
	}

	public void setMissiles(int missiles) {
	    this.missiles = missiles;
	}

	public int getHealth() {
	    return health;
	}

	public void setHealth(int health) {
	    this.health = health;
	}

	public Minecart getCart() {
	    return cart;
	}

	public void setCart(Minecart cart) {
	    this.cart = cart;
	}

	public Player getP() {
	    return p;
	}

	public void setP(Player p) {
	    this.p = p;
	}

	public boolean isSelfshooting() {
	    return selfshooting;
	}

	public void setSelfshooting(boolean selfshooting) {
	    this.selfshooting = selfshooting;
	}

	public boolean isHealthupdate() {
	    return healthupdate;
	}

	public void setHealthupdate(boolean healthupdate) {
	    this.healthupdate = healthupdate;
	}

	public boolean isMissilesupdate() {
	    return missilesupdate;
	}

	public void setMissilesupdate(boolean missilesupdate) {
	    this.missilesupdate = missilesupdate;
	}

	public boolean isBulletsupdate() {
	    return bulletsupdate;
	}

	public void setBulletsupdate(boolean bulletsupdate) {
	    this.bulletsupdate = bulletsupdate;
	}

	public boolean isUpdate() {
	    return update;
	}

	public void setUpdate(boolean update) {
	    this.update = update;
	}

	public BukkitTask getT() {
	    return t;
	}

	public void setT(BukkitTask t) {
	    this.t = t;
	}

	public Block getFence() {
	    return fence;
	}

	public void setFence(Block fence) {
	    this.fence = fence;
	}

	public Hologram getHologram() {
	    return hologram;
	}

	public void setHologram(Hologram hologram) {
	    this.hologram = hologram;
	}

	public void destroy() {

	    getHologram().destroy();
	    fence.setType(state.getType());
	    t.cancel();
	    cart.remove();
	    p.sendMessage(ChatColor.DARK_PURPLE + "Your turret has been removed!");
	    turrets.remove(cart);
	}

	public void shootBullet(Vector handle) {

	    if (getBullets() == 0)
		return;

	    handle.add(new Vector(0, 0.2, 0));
	    Vector normalhandle = handle.normalize().multiply(1.4);
	    Arrow arrow = cart.getWorld().spawnArrow(loc.clone().add(normalhandle), handle, 1, 1);
	    arrow.setMetadata("Bullet", new FixedMetadataValue(Main.getPlugin(), false));
	    MiscDisguise dis = new MiscDisguise(DisguiseType.DROPPED_ITEM);
	    DroppedItemWatcher watcher = (DroppedItemWatcher) dis.getWatcher();
	    watcher.setItemStack(new ItemStack(Material.ENDER_PEARL));
	    DisguiseAPI.disguiseEntity(arrow, dis);
	    setBullets(getBullets() - 1);
	    update = true;
	}

	public void shootMissile(Vector handle) {

	    if (getMissiles() == 0)
		return;

	    handle.add(new Vector(0, 0.2, 0));
	    Vector normalhandle = handle.normalize().multiply(1.4);
	    Arrow arrow = cart.getWorld().spawnArrow(loc.clone().add(normalhandle), handle, 1, 1);
	    arrow.setMetadata("Missile", new FixedMetadataValue(Main.getPlugin(), false));
	    setMissiles(getMissiles() - 1);
	    update = true;

	}

	public void set(Player p) {

	    PlayerData pd = Main.getPlayerData(p);
	    Location cartloc = new Location(p.getWorld(), loc.clone().getBlockX() + 0.5, loc.clone().getBlockY() + 0.55, loc.clone().getBlockZ() + 0.5);
	    Block block = cartloc.clone().subtract(0, 1, 0).getBlock();
	    this.state = block.getState();
	    block.setType(Material.FENCE);

	    Minecart cart = (Minecart) p.getWorld().spawnEntity(cartloc, EntityType.MINECART);
	    cart.setPassenger(p);
	    cart.setMetadata(p.getName().toString(), new FixedMetadataValue(Main.getPlugin(), false));
	    cart.setVelocity(new Vector(0, 0, 0));

	    ArrayList<String> info = new ArrayList<>();
	    info.add(ChatColor.RED + "Health:" + getHealth());
	    info.add(ChatColor.GREEN + "Missiles:" + getMissiles());
	    info.add(ChatColor.DARK_GREEN + "Bullets:" + getBullets());

	    Hologram hologram = new Hologram(cartloc.clone().add(0, 2.3, 0));
	    hologram.set(info);
	    hologram.show();

	    this.hologram = hologram;
	    this.cart = cart;
	    this.fence = block;
	    turrets.put(cart, this);

	    BukkitTask t = new BukkitRunnable() {

		int time = 0;

		@Override
		public void run() {

		    time++;

		    if (p.isDead() || !pd.isIngame()) {
			destroy();
			cancel();
			return;
		    }

		    if ((getMissiles() == 0 && getBullets() == 0) || getHealth() == 0) {
			destroy();
			cancel();
			return;
		    }

		    if (update) {

			ArrayList<String> info = new ArrayList<>();
			info.add(ChatColor.RED + "Health:" + getHealth());
			info.add(ChatColor.GREEN + "Missiles:" + getMissiles());
			info.add(ChatColor.DARK_GREEN + "Bullets:" + getBullets());

			getHologram().update(info);
			update = false;
		    }

		    Location tploc = new Location(p.getWorld(), loc.clone().getBlockX() + 0.5, loc.clone().getBlockY() + 0.55, loc.clone().getBlockZ() + 0.5);

		    Vector totploc = new Vector(tploc.getX() - cart.getLocation().getX(), tploc.getY() - cart.getLocation().getY(), tploc.getZ() - cart.getLocation().getZ());

		    cart.setVelocity(totploc);
		    cart.setDerailedVelocityMod(totploc);
		    cart.setFlyingVelocityMod(totploc);

		    if (cart.getPassenger() != null) {
			setSelfshooting(false);
			return;
		    }

		    if (isSelfshooting()) {

			if (time > 10) {

			    for (Entity entity : cart.getNearbyEntities(20, 20, 20)) {

				if (!(entity instanceof LivingEntity))
				    continue;
				if (entity == p)
				    continue;

				Location eloc = entity.getLocation();
				eloc.add(0, 0.5, 0);
				Vector handle = new Vector(eloc.getX() - loc.getX(), eloc.getY() - loc.getY(), eloc.getZ() - loc.getZ());
				handle.add(new Vector(0, 1, 0));
				shootBullet(handle);
				break;
			    }

			    time = 0;
			}
		    }

		}
	    }.runTaskTimer(Main.getPlugin(), 0, 1);
	    setT(t);
	}
    }

    public void creatTurret(Player p) {
	new TurretObject(p);
    }

    @EventHandler
    public void onTurretEnter(VehicleEnterEvent e) {

	if (!(e.getVehicle() instanceof Minecart))
	    return;

	Minecart cart = (Minecart) e.getVehicle();

	if (!(e.getEntered() instanceof Player))
	    return;

	Player p = (Player) e.getEntered();
	TurretObject turretobject = turrets.get(cart);

	if (turretobject == null)
	    return;

	if (cart.hasMetadata(p.getName())) {
	    turretobject.setSelfshooting(false);
	    p.sendMessage(ChatColor.LIGHT_PURPLE + "You took control over you turret!");
	    return;
	}

	if (!cart.hasMetadata(p.getName())) {
	    e.setCancelled(true);
	    p.sendMessage(ChatColor.RED + "You can not use this Turret!");
	    return;
	}

    }

    @EventHandler
    public void onTurretDestroy(VehicleDestroyEvent e) {

	if (!(e.getVehicle() instanceof Minecart))
	    return;

	Minecart cart = (Minecart) e.getVehicle();
	TurretObject turretobject = turrets.get(cart);

	if (turretobject == null)
	    return;

	e.setCancelled(true);
	turretobject.setHealth(turretobject.getHealth() - 1);
	turretobject.setUpdate(true);
    }

    @EventHandler
    public void onTurretLeave(VehicleExitEvent e) {

	if (!(e.getVehicle() instanceof Minecart) || !(e.getExited() instanceof Player))
	    return;

	Minecart cart = (Minecart) e.getVehicle();
	TurretObject turretobject = turrets.get(cart);

	Player p = (Player) e.getExited();

	if (turretobject == null)
	    return;

	turretobject.setSelfshooting(true);
	p.sendMessage(ChatColor.LIGHT_PURPLE + "Your Turret is now in the selfshooting mode!");
    }

    @EventHandler
    public void onTurretMove(VehicleMoveEvent e) {

	if (!(e.getVehicle() instanceof Minecart))
	    return;

	Minecart cart = (Minecart) e.getVehicle();
	TurretObject turretobject = turrets.get(cart);

	if (turretobject == null)
	    return;
	cart.setFallDistance(0);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {

	Projectile pro = e.getEntity();

	if (pro.hasMetadata("Missile")) {
	    pro.getWorld().createExplosion(pro.getLocation(), 0);
	    pro.remove();
	}

	if (pro.hasMetadata("Bullet")) {
	    pro.remove();
	}

    }

    @EventHandler
    public void onShoot(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();

	if (!item.getType().equals(Material.EMERALD) || !pd.getKit().contains(Ability.AUTOMATON_TURRET))
	    return;

	if (!p.isInsideVehicle() || !(p.getVehicle() instanceof Minecart)) {
	    Turret turret = new Turret();
	    turret.creatTurret(p);
	    return;
	}

	e.setCancelled(true);
	Minecart cart = (Minecart) p.getVehicle();
	TurretObject turretObject = turrets.get(cart);

	if (turretObject == null)
	    return;

	Vector handle = p.getEyeLocation().getDirection();

	if (e.getAction().toString().contains("RIGHT"))
	    turretObject.shootBullet(handle);

	if (e.getAction().toString().contains("LEFT"))
	    turretObject.shootMissile(handle);
    }

}