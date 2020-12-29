package de.master.ttt.countdowns;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.master.ttt.gamestates.IngameState;
import de.master.ttt.main.Main;
import de.master.ttt.role.Role;

public class RoleCountdown extends Countdown{

	private Main plugin;
	private int seconds = 15;
	
	public RoleCountdown(Main plugin) {
		this.plugin = plugin;
	}
	@Override
	public void start() {
		taskID2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				for (Player all : Bukkit.getOnlinePlayers()) {
					all.setExp(0);
					all.setLevel(seconds);
				}
				switch(seconds) {
				case 15 :
					Bukkit.broadcastMessage(Main.prefix+"§7Die Rollen werden in §6" +seconds+" §7Sekunden bekannt gegeben.");
					break;
				case 10 : case 5 : case 4 : case  3 : case 2 :
					Bukkit.broadcastMessage(Main.prefix+ "§7Noch §6" +seconds+ " §7Sekunden bis zu Rollenbekanntgabe.");
					break;
				case 1:
					Bukkit.broadcastMessage(Main.prefix+ "§7Noch §6" +seconds+ " §7Sekunde bis zu Rollenbekanntgabe.");
					break;
					
				case 0:
					stop(); 
					IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
					ingameState.setGrace(false);
					
					Bukkit.broadcastMessage(Main.prefix+ "§aDie Rollen wurden verteilt!");
					plugin.getRoleManager().calcRoles();
					
					ArrayList<String> traitorPlayers = plugin.getRoleManager().getTraitorPlayers();
					for(Player current : plugin.getPlayers()) {
						Role playerRole = plugin.getRoleManager().getPlayerRole(current);
						current.sendMessage(Main.prefix+ "§7Du bist: §l" +playerRole.getChatColor() +playerRole.name());
						current.setDisplayName(playerRole.getChatColor()+ current.getName());
						
						if(playerRole == Role.TRAITOR) 
							current.sendMessage(Main.prefix+"§7Die Traitor sind: §4§l" +String.join(", ", traitorPlayers));
					}
					break;
					
					default:
						break;
				}
				
				seconds --;
			}
		},0, 20);
		
	}

	@Override
	public void stop() {
		Bukkit.getScheduler().cancelTask(taskID2);
		
	}

}
