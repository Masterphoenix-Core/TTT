package de.master.ttt.gamestates;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import de.master.ttt.countdowns.RoleCountdown;
import de.master.ttt.main.Main;
import de.master.ttt.role.Role;
import de.master.ttt.voting.Map;

public class IngameState extends GameState{

	private Main plugin;
	private Map map;
	private ArrayList<Player> players;
	private RoleCountdown roleCountdown;
	private Role winningRole;
	private boolean Grace;
	
	public IngameState(Main plugin) {
		this.plugin = plugin;
		roleCountdown = new RoleCountdown(plugin);
	}
	@Override
	public void start() {
		Grace = true;
		
		Collections.shuffle(plugin.getPlayers());
		players = plugin.getPlayers();
		
		map = plugin.getVoting().getWinnerMap();
		map.load();
		for(int i = 0; i< players.size(); i++)
			players.get(i).teleport(map.getSpawnLocations()[i]);
		
		for(Player current : players)  {
			current.getInventory().clear();
			current.setFoodLevel(20);
			current.setHealth(20);
			current.setGameMode(GameMode.SURVIVAL);
			plugin.getGameProtectionListener().getBuildModePlayers().remove(current.getName());
		}
		roleCountdown.start();
	}
	
	public void checkGameEnding() {
		if(plugin.getRoleManager().getTraitorPlayers().size() <= 0) {
			winningRole = Role.INNOCENT;
			plugin.getGameStateManager().setGameState(ENDING_STATE);
			
		} else if(plugin.getRoleManager().getTraitorPlayers().size() == plugin.getPlayers().size()) {
			winningRole = Role.TRAITOR;
			plugin.getGameStateManager().setGameState(ENDING_STATE);
			
		}
	}

	@Override
	public void stop() {
		Bukkit.broadcastMessage(Main.prefix+ "§7Das Spiel ist vorbei!");
		Bukkit.broadcastMessage(Main.prefix+ "§6Sieger: " +winningRole.getChatColor()+ winningRole.getName());
		
	}
	public void setGrace(boolean grace) {
		Grace = grace;
	}
	public boolean inGrace() {
		return Grace;
	}
}
