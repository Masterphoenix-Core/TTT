package de.master.ttt.voting;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import de.master.ttt.gamestates.LobbyState;
import de.master.ttt.main.Main;
import de.master.ttt.util.ConfigLocationUtil;

public class Map {

	private Main plugin;
	private String name;
	private String builder;
	private Location[] spawnLocations = new Location[LobbyState.MAX_PLAYERS];
	private Location spectatorLocation;
	
	private int votes;
	
	public Map(Main plugin, String name) {
		this.plugin = plugin;
		this.name = name.toUpperCase();
		
		if(exists())
			builder = plugin.getConfig().getString("Arenas." +name+ ".Builder");
	}
	public void create(String builder) {
		this.builder = builder;
		plugin.getConfig().set("Arenas." +name+ ".Builder", builder);
		plugin.saveConfig();
	}
	public void load() {
		for(int i = 0; i < spawnLocations.length; i++)
			spawnLocations[i] = new ConfigLocationUtil(plugin, "Arenas." +name+ "." +(i + 1)).loadLocation();
		spectatorLocation = new ConfigLocationUtil(plugin,"Arenas." +name+ ".Spectator").loadLocation();
	}
	public boolean exists() {
		return(plugin.getConfig().getString("Arenas." +name+ ".Builder") != null);
	}
	public boolean playable() {
		ConfigurationSection configSection = plugin.getConfig().getConfigurationSection("Arenas." +name);
		if(!configSection.contains("Spectator")) return false;
		if(!configSection.contains("Builder")) return false;
		for(int i = 1; i < LobbyState.MAX_PLAYERS + 1; i++){
			if(!configSection.contains(Integer.toString(i))) return false;
		}
		return true;
	}
	public void setSpawnLocation(int spawnNumber, Location location) {
		spawnLocations[spawnNumber - 1] = location;
		new ConfigLocationUtil(plugin, location, "Arenas." +name+ "." +spawnNumber).saveLocation();
	}
	public void setSpectatorLocation(Location location) {
		spectatorLocation = location;
		new ConfigLocationUtil(plugin, location, "Arenas." +name+ ".Spectator").saveLocation();
	}
	public void addVote() {
		votes ++;
	}
	public void removeVote() {
		votes --;
	}
	public String getName() {
		return name;
	}
	public String getBuilder() {
		return builder;
	}
	public Location[] getSpawnLocations() {
		return spawnLocations;
	}
	public Location getSpectatorLocation() {
		return spectatorLocation;
	}
	public int getVotes() {
		return votes;
	}
}
