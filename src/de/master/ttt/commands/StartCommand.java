package de.master.ttt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.master.ttt.gamestates.LobbyState;
import de.master.ttt.main.Main;

public class StartCommand implements CommandExecutor{

	private static final int START_SECONDS = 10;
	
	private Main plugin;
	
	public StartCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission("ttt.start")) {
				if(args.length == 0) {
					if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
						LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
						if(lobbyState.getCountdown().isRunning() && lobbyState.getCountdown().getSeconds() > START_SECONDS) {
							lobbyState.getCountdown().setSeconds(START_SECONDS);
							p.sendMessage(Main.prefix+"§aDas Spiel wird gestartet.");
						} else
							p.sendMessage(Main.prefix+"§cDas Spiel ist bereits gestartet.");
					} else
						p.sendMessage(Main.prefix+"§cDas Spiel ist bereits gestartet.");
				} else
					p.sendMessage(Main.prefix+"§cBitte benutze §6/start§c!");
			} else
				p.sendMessage(Main.NO_PERMISSION);
		}
		return false;
	}

}
