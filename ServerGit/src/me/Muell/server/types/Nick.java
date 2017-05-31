
package me.Muell.server.types;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

public class Nick {

    public static HashSet<Player> nickedPlayers = new HashSet<>();
    public static HashMap<Player, Nick> nicks = new HashMap<>();

    private String realname;
    private String nickname;

    public void nick(Player p, String nickname) {

	if (nickedPlayers.contains(p)) {

	    p.sendMessage("You are already nicked!");
	    return;
	}

	PlayerData pd = Main.getPlayerData(p);

	this.realname = p.getName();
	this.nickname = nickname;
	nickedPlayers.add(p);
	nicks.put(p, this);

	PlayerDisguise dis = new PlayerDisguise(nickname);
	dis.setKeepDisguiseOnPlayerDeath(true);
	dis.setViewSelfDisguise(true);
	DisguiseAPI.disguiseEntity(p, dis);

	if (Bukkit.getPlayer(nickname) == null)
	    p.setPlayerListName(nickname);

	p.setDisplayName(nickname);

	pd.setNick(nickname);
	p.sendMessage(ChatColor.DARK_GREEN + "You are now nicked!");
    }

    public void nickoff(Player p) {

	if (nickedPlayers == null) {
	    p.sendMessage("You have never been nicked!");
	    return;
	}

	if (nicks == null) {
	    p.sendMessage("You have never been nicked!");
	    return;
	}

	if (!nickedPlayers.contains(p)) {
	    p.sendMessage("You have never been nicked!");
	    return;
	}

	PlayerData pd = Main.getPlayerData(p);

	PlayerDisguise dis = new PlayerDisguise(realname);
	dis.setKeepDisguiseOnPlayerDeath(true);
	dis.setViewSelfDisguise(true);
	DisguiseAPI.disguiseEntity(p, dis);

	nicks.remove(p);
	nickedPlayers.remove(p);
	pd.setNick(null);

	p.setPlayerListName(realname);
	p.setDisplayName(realname);

	p.sendMessage(ChatColor.DARK_GREEN + "You are not nicked anymore!");
    }

    public String getNickname() {

	return nickname;
    }

    public void setNickname(String nickname) {

	this.nickname = nickname;
    }

    public String getRealname() {

	return realname;
    }

    public void setRealname(String realname) {

	this.realname = realname;
    }

}
