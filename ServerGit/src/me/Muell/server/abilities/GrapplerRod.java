package me.Muell.server.abilities;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftSnowball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_7_R4.EntityFishingHook;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EntitySnowball;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;

public class GrapplerRod extends EntityFishingHook {

    public static HashMap<Player, GrapplerRod> Grapplerrods = new HashMap<>();
    public static HashMap<Snowball, GrapplerRod> rods = new HashMap<>();

    private Entity hookedEntity;
    private Player owner;
    private Snowball ball;
    private boolean hooked;
    private EntitySnowball controller;

    public Player getOwner() {
	return owner;
    }

    public void setOwner(Player owner) {
	this.owner = owner;
    }

    public Entity getHooked() {
	return hookedEntity;
    }

    public void setHooked(Entity hookedEntity) {
	this.hookedEntity = hookedEntity;
    }

    public Snowball getBall() {
	return ball;
    }

    public void setBall(Snowball ball) {
	this.ball = ball;
    }

    public boolean isHooked() {
	return hooked;
    }

    public void setHooked(boolean hooked) {
	this.hooked = hooked;
    }

    public GrapplerRod(Player p) {

	super(((CraftWorld) p.getWorld()).getHandle(), ((CraftPlayer) p).getHandle());

	GrapplerRod former = Grapplerrods.get(p);

	if (former != null)
	    former.destroy();

	this.owner = p;
	Grapplerrods.put(p, this);
	((CraftWorld) owner.getEyeLocation().getWorld()).getHandle().addEntity(this);
    }

    @Override
    public void h() {

	if (getHooked() == null) {

	    getBukkitEntity().teleport(ball);

	} else {

	    getBukkitEntity().teleport(hookedEntity);

	}
    }

    @SuppressWarnings("deprecation")
    public void Throw() {

	Snowball ball = owner.launchProjectile(Snowball.class);
	ball.setVelocity(owner.getEyeLocation().getDirection().normalize().multiply(1.9));
	this.ball = ball;
	this.controller = ((CraftSnowball) this.ball).getHandle();
	rods.put(ball, this);

	PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(controller.getId());

	for (Player ps : Bukkit.getOnlinePlayers()) {
	    EntityPlayer nmsPlayer = ((CraftPlayer) ps).getHandle();
	    nmsPlayer.playerConnection.sendPacket(destroy);
	}

	setHooked(false);
	setHooked(null);
    }

    public void hook() {

	if (!hooked)
	    return;

	double distance = getBukkitEntity().getLocation().distance(owner.getLocation());
	double d = distance * 2;
	double handle_x = (0.8D + 0.07D * d) * (getBukkitEntity().getLocation().getX() - owner.getLocation().getX())
		/ d;
	double handle_y = (0.8D + 0.01D * d) * (getBukkitEntity().getLocation().getY() - owner.getLocation().getY())
		/ d;
	double handle_z = (0.8D + 0.07D * d) * (getBukkitEntity().getLocation().getZ() - owner.getLocation().getZ())
		/ d;

	Vector handle = new Vector(handle_x, handle_y, handle_z);

	owner.setVelocity(handle);
	owner.setFallDistance(0);
	owner.getWorld().playSound(owner.getLocation(), Sound.STEP_GRAVEL, 1.0F, 1.0F);
    }

    public void die() {};

    public void destroy() {

	super.die();
	ball.remove();
	hookedEntity = null;
	Grapplerrods.remove(owner);
	rods.remove(ball);
	owner = null;
    }
}
