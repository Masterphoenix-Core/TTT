package de.master.ttt.main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.master.ttt.commands.SetupCommand;
import de.master.ttt.commands.StartCommand;
import de.master.ttt.countdowns.BuildCommand;
import de.master.ttt.gamestates.GameState;
import de.master.ttt.gamestates.GameStateManager;
import de.master.ttt.listener.GameProgressListener;
import de.master.ttt.listener.GameProtectionListener;
import de.master.ttt.listener.PlayerLobbyConnectionListener;
import de.master.ttt.listener.VotingListener;
import de.master.ttt.role.RoleManager;
import de.master.ttt.voting.Map;
import de.master.ttt.voting.Voting;

public class Main extends JavaPlugin{
	
	public static final String prefix = "§7[§cTTT§7] §r",
										NO_PERMISSION = prefix+ "§cDazu hast du keine Rechte!";
	
	private GameStateManager gameStateManager;
	private ArrayList<Player> players;
	private ArrayList<Map> maps;
	private Voting voting;
	private RoleManager roleManager;
	private GameProtectionListener gameProtectionListener;

	public void onEnable() {
		System.out.println("[TTT] Das Plugin wurde gestartet!");
		players = new ArrayList<>();
		
		gameStateManager = new GameStateManager(this);
		gameStateManager.setGameState(GameState.LOBBY_STATE);
		
		roleManager = new RoleManager(this);
		
		this.loadEvents();
		this.loadCommands();
		initVoting();
	}
	
	private void initVoting() {
		maps = new ArrayList<>();
		for(String current : getConfig().getConfigurationSection("Arenas").getKeys(false)) {
			Map map = new Map(this, current);
			if(map.playable()) maps.add(map); else Bukkit.getConsoleSender().sendMessage(Main.prefix+ "§4" +map.getName()+ "§c ist noch nicht fertig eingerichtet!");
		}
		if(maps.size() >= Voting.MAP_AMOUNT)
		voting = new Voting(this, maps);
		else {
			Bukkit.getConsoleSender().sendMessage("§cFür das Voting müssen mindestens §4" +Voting.MAP_AMOUNT+ " §cMaps eingerichtet sein.");
			voting = null;
		}
	}
	public void loadCommands() {
		this.getCommand("setup").setExecutor(new SetupCommand(this));
		this.getCommand("start").setExecutor(new StartCommand(this));
		this.getCommand("build").setExecutor(new BuildCommand(this));
	}
	public void loadEvents() {
		gameProtectionListener = new GameProtectionListener(this);
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new PlayerLobbyConnectionListener(this), this);
		pm.registerEvents(new VotingListener(this), this);
		pm.registerEvents(new GameProgressListener(this), this);
		pm.registerEvents(new GameProtectionListener(this), this);
	}
	
	public void onDisable() {
		System.out.println("[TTT] Das Plugin wurde gestoppt!");
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public GameStateManager getGameStateManager() {
		return gameStateManager;
	}
	public Voting getVoting() {
		return voting;
	}
	public ArrayList<Map> getMaps() {
		return maps;
	}
	public RoleManager getRoleManager() {
		return roleManager;
	}
	public GameProtectionListener getGameProtectionListener() {
		return gameProtectionListener;
	}
}
