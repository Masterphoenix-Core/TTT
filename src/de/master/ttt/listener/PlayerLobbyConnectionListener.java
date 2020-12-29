package de.master.ttt.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import de.master.ttt.countdowns.LobbyCountdown;
import de.master.ttt.gamestates.LobbyState;
import de.master.ttt.main.Main;
import de.master.ttt.util.ConfigLocationUtil;
import de.master.ttt.util.ItemBuilder;
import de.master.ttt.voting.Voting;

public class PlayerLobbyConnectionListener implements Listener{

	public static final String VOTING_ITEM_NAME = "§6§lVoting-Menü";
	
	private Main plugin;
	private ItemStack voteItem;
	
	public PlayerLobbyConnectionListener(Main plugin) {
		this.plugin = plugin;
		voteItem = new ItemBuilder(Material.NETHER_STAR).setDisplayName(PlayerLobbyConnectionListener.VOTING_ITEM_NAME).build();
	}
	
	@EventHandler
	public void handlePlayerJoin(PlayerJoinEvent e) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) return;
		Player p = e.getPlayer();
		plugin.getPlayers().add(p);
		e.setJoinMessage(Main.prefix+ "§a"+p.getDisplayName()+ " §7ist dem Spiel beigetreten. [§e" +plugin.getPlayers().size()+ "§7/§e" +LobbyState.MAX_PLAYERS+ "§7]");
		
		p.getInventory().clear();
		p.getInventory().setChestplate(null);
		p.getInventory().setHelmet(null);
		p.getInventory().setItem(4, voteItem);
		
		ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "Lobby");
		if(locationUtil.loadLocation() != null) {
			p.teleport(locationUtil.loadLocation());
		} else
			Bukkit.getConsoleSender().sendMessage(Main.prefix+ "§cDie Lobby-Location wurde noch nicht gesetzt!");
		
		LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
		LobbyCountdown countdown = lobbyState.getCountdown();
		if(plugin.getPlayers().size() >= LobbyState.MIN_PLAYERS) {
			if(!countdown.isRunning()) {
				countdown.stopIdle();
				countdown.start();
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) return;
		Player p = e.getPlayer();
		plugin.getPlayers().remove(p);
		e.setQuitMessage(Main.prefix+ "§c"+p.getDisplayName()+ " §7hat das Spiel verlassen. [§e" +plugin.getPlayers().size()+ "§7/§e" +LobbyState.MAX_PLAYERS+ "§7]");
		
		LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
		LobbyCountdown countdown = lobbyState.getCountdown();
		if(plugin.getPlayers().size() < LobbyState.MIN_PLAYERS) {
			if(countdown.isRunning()) {
				countdown.stop();
				countdown.startIdle();
			}
		} else
		if(Voting.getPlayerVotes().containsKey(p.getName())) {
			Voting.getVotingMaps()[Voting.getPlayerVotes().get(p.getName())].removeVote();
			Voting.initVotingInv();
		}
	}
}
