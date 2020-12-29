package de.master.ttt.countdowns;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.master.ttt.gamestates.LobbyState;
import de.master.ttt.listener.GameProtectionListener;
import de.master.ttt.main.Main;

public class BuildCommand implements CommandExecutor{

	private Main plugin;
	
	public BuildCommand(Main plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String arg2, String[] args) {

		if(s instanceof Player) {
			Player p = (Player) s;
			if(p.hasPermission("ttt.build")) {
				if(args.length == 0) {
						if((plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) {
						GameProtectionListener gameProtectionListener = plugin.getGameProtectionListener();
						if(!gameProtectionListener.getBuildModePlayers().contains(p.getName())) {
							gameProtectionListener.getBuildModePlayers().add(p.getName());
							p.sendMessage(Main.prefix+"§7Du kannst nun §6bauen§7.");
						} else {
							gameProtectionListener.getBuildModePlayers().remove(p.getName());
							p.sendMessage(Main.prefix+"§7Du kannst nun §cnicht mehr bauen§7.");
						}
					} else
						p.sendMessage(Main.prefix+ "§cDu kannst nur im §6Lobby-State §cbauen§7.");
				} else
					p.sendMessage(Main.prefix+ "§cBitte benutze §6/build§c!");
			} else
				p.sendMessage(Main.NO_PERMISSION);
		}
		return false;
	}

}
