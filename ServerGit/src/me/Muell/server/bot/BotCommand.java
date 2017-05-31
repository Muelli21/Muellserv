package me.Muell.server.bot;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.PlayerData;
import net.minecraft.server.v1_7_R4.World;

public class BotCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	Player p = (Player) sender;
	PlayerData pd = Main.getPlayerData(p);

	if (!(p.hasPermission("administration"))) { return true; }

	World world = ((CraftWorld) Bukkit.getWorld("world")).getHandle();
	new CustomZombie(world, p, 1, 400);

	pd.setIngame(true);
	pd.setGamemode(Gamemode.ONEVSONE);
	p.sendMessage(Main.pre + "Your testbot has been created!");

	return false;
    }

}
