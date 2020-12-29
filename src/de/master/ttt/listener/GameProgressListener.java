package de.master.ttt.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.master.ttt.gamestates.IngameState;
import de.master.ttt.main.Main;
import de.master.ttt.role.Role;
import de.master.ttt.role.RoleManager;

public class GameProgressListener implements Listener{

	private Main plugin;
	private RoleManager roleManager;
	
	public GameProgressListener(Main plugin) {
		this.plugin = plugin;
		this.roleManager = plugin.getRoleManager();
	}
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState)) return;
		if(!(e.getDamager() instanceof Player)) return;
		if(!(e.getEntity() instanceof Player)) return;
		Player damager = (Player) e.getDamager(), victim = (Player) e.getEntity();
		Role damagerRole = roleManager.getPlayerRole(damager), victimRole = roleManager.getPlayerRole(victim);
		
		if((damagerRole == Role.INNOCENT || damagerRole == Role.DETECTIVE) && victimRole == Role.DETECTIVE);
			damager.sendMessage(Main.prefix+ "§cAchtung! Du hast einen Detective angegriffen!");
		if(damagerRole == Role.TRAITOR && victimRole == Role.TRAITOR) {
			e.setDamage(1);
			damager.sendMessage(Main.prefix+ "§cDu hast einen anderen Traitor angegriffen!");
			}
		}
	@EventHandler public void onDeath(PlayerDeathEvent e) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState)) return;
		IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
		Player victim = e.getEntity();
		if(victim.getKiller() != null) {
		Player killer = victim.getKiller();
		Role killerRole = roleManager.getPlayerRole(killer), victimRole = roleManager.getPlayerRole(victim);
		
		switch(killerRole) {
		case TRAITOR :
			if(victimRole == Role.TRAITOR) {
				killer.sendMessage(Main.prefix+"§cDu hast einen Traitor getötet!");
			} else {
				killer.sendMessage(Main.prefix+"§bDu hast einen " +victimRole.getChatColor() + victimRole.getName()+ " §bgetötet.");
			}
			break;
		case INNOCENT : case DETECTIVE :
			if(victimRole == Role.TRAITOR) {
				killer.sendMessage(Main.prefix+"§aDu hast einen §cTraitor §agetötet!");
			} else if(victimRole == Role.INNOCENT) {
				killer.sendMessage(Main.prefix+"§cDu hast einen §aInnocent §cgetötet!");
			} else if(victimRole == Role.DETECTIVE) {
				killer.sendMessage(Main.prefix+"§cDu hast einen §bDetective §cgetötet!");
			}
			break;
			
			default:
				break;
		}
		victim.sendMessage(Main.prefix+"§7Du wurdest von einem " +killerRole.getChatColor() + killerRole.getName()+ " §l" +killer.getName()+ " §7getötet!");
			
		if(victimRole == Role.TRAITOR) plugin.getRoleManager().getTraitorPlayers().remove(victim.getName());	
		plugin.getPlayers().remove(victim);
		
		ingameState.checkGameEnding();
		
		} else {
			victim.sendMessage(Main.prefix+ "§cDu bist gestorben.");
				
			if(plugin.getRoleManager().getPlayerRole(victim) == Role.TRAITOR) plugin.getRoleManager().getTraitorPlayers().remove(victim.getName());	
			plugin.getPlayers().remove(victim);
			
			ingameState.checkGameEnding();
		}
		if(e.getEntity() instanceof Player)
		e.setDeathMessage(Main.prefix+ "§7Der Spieler §6" +e.getEntity()+ " §7ist gestorben.");
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState)) return;
		Player p = e.getPlayer();
		if(plugin.getPlayers().contains(p)) {
			plugin.getPlayers().remove(p);
			IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
			e.setQuitMessage(Main.prefix+ "§7Der Spieler §6" +p.getName()+ " §7hat das Spiel verlassen.");
			
			ingameState.checkGameEnding();
		}
	}
}
