package de.master.ttt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.master.ttt.gamestates.LobbyState;
import de.master.ttt.main.Main;
import de.master.ttt.util.ConfigLocationUtil;
import de.master.ttt.voting.Map;

public class SetupCommand implements CommandExecutor{

	private Main plugin;
	
	public SetupCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			if(player.hasPermission("ttt.setup")) {
				if(args.length == 0) {
					player.sendMessage(Main.prefix+ "§7§l┌───────────────────────────┐");
					player.sendMessage(Main.prefix+ "§b/setup lobby §8→ §7Setze die Lobby.");
					player.sendMessage(Main.prefix+ "§b/setup create <Name> <Erbauer> §8→ §7Erstelle eine neue Map.");
					player.sendMessage(Main.prefix+ "§b/setup set <Name> <Spawn> §8→ §7Setze Spawns");
					player.sendMessage(Main.prefix+"§7                                     (Spieler- und Specspawns).");
					player.sendMessage(Main.prefix+ "§7§l└───────────────────────────┘");
				} else {
					if(args[0].equalsIgnoreCase("lobby")) {
						if(args.length == 1) {
							
							//Command: /setup lobby
							new ConfigLocationUtil(plugin, player.getLocation(), "Lobby").saveLocation();
							player.sendMessage(Main.prefix+ "§aDie Lobby wurde neu gesetzt.");
							
						} else
							player.sendMessage(Main.prefix+"§cBitte benutze §6/setup lobby§c!");
					} else if(args[0].equalsIgnoreCase("create")) {
						if(args.length == 3) {
							Map map = new Map(plugin, args[1]);
							if(!map.exists()) {
								
								//Command: /setup create NAME Arbor
								map.create(args[2]);
								player.sendMessage(Main.prefix+"§aDie Map §6" +map.getName()+" §avom Erbauer §6"+map.getBuilder()+" §awurde erstellt.");
								
							} else
								player.sendMessage(Main.prefix+"§cEine Arena unter diesem Namen existiert bereits!");
						} else
							player.sendMessage(Main.prefix+"§cBitte benutze §6/setup create <NAME> <ERBAUER>§c!");
					} else if(args[0].equalsIgnoreCase("set")) {
						if(args.length == 3) {
							Map map = new Map(plugin, args[1]);
							if(map.exists()) {
								//Command: /setup set NAME Number/Speck
								try {
									int spawnNumber = Integer.parseInt(args[2]);
									if(spawnNumber > 0 && spawnNumber <= LobbyState.MAX_PLAYERS) {
										
										map.setSpawnLocation(spawnNumber, player.getLocation());
										player.sendMessage(Main.prefix+"§aDu hast die Spawn-Location §6" +spawnNumber+ " §afür die Map §6" +map.getName()+ " §agesetzt.");
										
									} else
										player.sendMessage(Main.prefix+"§cBitte gib eine Zahl §6zwischen 1 und " +LobbyState.MAX_PLAYERS+" §can.");
								} catch(NumberFormatException e) {
									if(args[2].equalsIgnoreCase("spectator")) {
										map.setSpectatorLocation(player.getLocation());
										player.sendMessage(Main.prefix+ "§aDu hast den Spectator-Spawn für die Map §6" +map.getName()+ " §agesetzt.");
									} else
										player.sendMessage(Main.prefix+"§cBitte benutze &6/setup set <NAME> <1-" +LobbyState.MAX_PLAYERS+" // Spectator>§c!");
								}
							} else
								player.sendMessage(Main.prefix+"§cDiese Map existiert nicht.");
						} else
							player.sendMessage(Main.prefix+"§cBitte benutze &6/setup set <NAME> <1-" +LobbyState.MAX_PLAYERS+" // Spectator>§c!");
					} else
						player.sendMessage(Main.prefix+ "§cBitte benutze §6/setup <Lobby/Set>§c!");
				}	
			} else
				player.sendMessage(Main.NO_PERMISSION);
		}
		return false;
	}
	
}
