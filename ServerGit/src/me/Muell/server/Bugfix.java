
package me.Muell.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.TimingsCommand;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.spigotmc.CustomTimingsHandler;

import net.minecraft.server.v1_7_R4.PacketPlayOutEntityVelocity;
import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.JsonObject;

public class Bugfix implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

	if ((event.getMessage().startsWith("/timings paste")) && (event.getPlayer().hasPermission("timings.patch"))) {
	    handle(event.getPlayer());
	    event.setCancelled(true);
	}
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGH)
    public void DamageNerf(EntityDamageByEntityEvent e) {

	if (!(e.getEntity() instanceof Player))
	    return;

	if (!(e.getDamager() instanceof Player))
	    return;

	Player p = (Player) e.getDamager();

	if (((LivingEntity) e.getEntity()).getNoDamageTicks() > ((LivingEntity) e.getDamager()).getMaximumNoDamageTicks() / 2D && p.getLastDamageCause() instanceof EntityDamageByEntityEvent
		&& p.getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK) {

	    e.setDamage(0D);
	    return;
	}

	if ((p.getFallDistance() > 0.0F) && (!p.isOnGround()) && (!p.hasPotionEffect(PotionEffectType.BLINDNESS))) {
	    e.setDamage(e.getDamage() / 1.5 + 2D);
	    if (p.getItemInHand().getType() == Material.AIR)
		e.setDamage(e.getDamage() - 2);
	}

	// if (p.getItemInHand().getType().name().contains("SWORD"))
	// e.setDamage(e.getDamage() - 2);

    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onVel(PlayerVelocityEvent e) {

	EntityDamageEvent cause = e.getPlayer().getLastDamageCause();
	Player p = e.getPlayer();

	if (cause != null && cause instanceof EntityDamageByEntityEvent && cause.getCause() == DamageCause.ENTITY_ATTACK) {

	    if (p.isOnGround()) {

		if (e.getVelocity().getY() < 0.3) {
		    Vector knockback = e.getVelocity().multiply(3.2D).add(new Vector(0, -0.459, 0));
		    if ((Math.abs(knockback.getX()) + Math.abs(knockback.getZ())) > 1.7)
			return;
		    if (knockback.getY() < 0.34)
			knockback.setY(0.36);

		    e.setCancelled(true);

		    PacketPlayOutEntityVelocity packet = new PacketPlayOutEntityVelocity(p.getEntityId(), knockback.getX(), knockback.getY(), knockback.getZ());
		    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);

		}
	    }
	}

    }

    private void handle(CommandSender sender) {

	if (!Main.getPlugin().getServer().getPluginManager().useTimings()) {
	    sender.sendMessage("Please enable timings by setting \"settings.plugin-profiling\" to true in bukkit.yml");
	    return;
	}
	if (!Main.getPlugin().getServer().getPluginManager().useTimings()) {
	    sender.sendMessage("Please enable timings by typing /timings on");
	    return;
	}
	long sampleTime = System.nanoTime() - TimingsCommand.timingStart;
	int index = 0;
	File timingFolder = new File("timings");
	timingFolder.mkdirs();
	File timings = new File(timingFolder, "timings.txt");
	ByteArrayOutputStream bout = new ByteArrayOutputStream();
	while (timings.exists()) {
	    timings = new File(timingFolder, "timings" + ++index + ".txt");
	}
	PrintStream fileTimings = new PrintStream(bout);

	CustomTimingsHandler.printTimings(fileTimings);
	fileTimings.println("Sample time " + sampleTime + " (" + sampleTime / 1.0E9D + "s)");

	fileTimings.println("<spigotConfig>");
	fileTimings.println(Bukkit.spigot().getConfig().saveToString());
	fileTimings.println("</spigotConfig>");

	new PasteThread(sender, bout).start();
    }

    private static class PasteThread extends Thread {

	private final CommandSender sender;

	private final ByteArrayOutputStream bout;

	PasteThread(CommandSender sender, ByteArrayOutputStream bout) {
	    this.sender = sender;
	    this.bout = bout;
	}

	public synchronized void start() {

	    if ((sender instanceof RemoteConsoleCommandSender)) {
		run();
	    } else {
		super.start();
	    }
	}

	public void run() {

	    try {
		HttpURLConnection con = (HttpURLConnection) new URL("https://timings.spigotmc.org/paste").openConnection();
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setInstanceFollowRedirects(false);

		OutputStream out = con.getOutputStream();
		Throwable localThrowable3 = null;
		try {
		    out.write(bout.toByteArray());
		} catch (Throwable localThrowable1) {
		    localThrowable3 = localThrowable1;
		    try {
			throw localThrowable1;
		    } catch (Throwable e) {
			e.printStackTrace();
		    }
		} finally {
		    if (out != null)
			if (localThrowable3 != null)
			    try {
				out.close();
			    } catch (Throwable localThrowable2) {
				localThrowable3.addSuppressed(localThrowable2);
			    }
			else
			    out.close();
		}
		JsonObject location = (JsonObject) new Gson().fromJson(new InputStreamReader(con.getInputStream()), JsonObject.class);
		con.getInputStream().close();

		String pasteID = location.get("key").getAsString();
		sender.sendMessage(ChatColor.GREEN + "Timings results can be viewed at https://www.spigotmc.org/go/timings?url=" + pasteID);
	    } catch (IOException ex) {
		sender.sendMessage(ChatColor.RED + "Error pasting timings, check your console for more information");
		Bukkit.getServer().getLogger().log(Level.WARNING, "Could not paste timings", ex);
	    }
	}

    }

}
