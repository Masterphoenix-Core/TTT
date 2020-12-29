package de.master.ttt.voting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.master.ttt.main.Main;
import de.master.ttt.util.ItemBuilder;

public class Voting {

	public static final int MAP_AMOUNT = 2;
	public static final String VOTING_INV_TITLE = "§4Voting";

	private ArrayList<Map> maps;
	private static Map[] votingMaps;
	private static int[] votingInvOrder = new int[] {3, 5};
	private static HashMap<String, Integer> playerVotes;
	private static Inventory votingInv;
	
	public Voting(Main plugin, ArrayList<Map> maps) {
		this.maps = maps;
		votingMaps = new Map[Voting.MAP_AMOUNT];
		playerVotes = new HashMap<>();
		
		chooseRandomMaps();
		initVotingInv();
	}
	
	private void chooseRandomMaps() {
		for(int i = 0; i < votingMaps.length; i++) {
			Collections.shuffle(maps);
			votingMaps[i] = maps.remove(0);
		}
	}
	
	public static void initVotingInv() {
		votingInv = Bukkit.createInventory(null, 9*1, Voting.VOTING_INV_TITLE);
		for(int i = 0; i < votingMaps.length; i++) {
			Map currentMap = votingMaps[i];
			votingInv.setItem(votingInvOrder[i], new ItemBuilder(Material.PAPER).setDisplayName("§6" +currentMap.getName()).setLore(" ", "§7Erbauer: §a" +currentMap.getBuilder()).build());
		}
	}
	
	public Map getWinnerMap() {
		Map winnerMap = votingMaps[0];
		for(int i = 1; i < votingMaps.length; i++) {
			if(votingMaps[i].getVotes() >= winnerMap.getVotes()) winnerMap = votingMaps[i];
		}
		return winnerMap;
	}
	public static void vote(Player p, int votingMap) {
		if(!playerVotes.containsKey(p.getName())) {
			votingMaps[votingMap].addVote();
			p.closeInventory();
			p.sendMessage(Main.prefix+"§aDu hast für die Map §6" +votingMaps[votingMap].getName() + " §aabgestimmt.");
			playerVotes.put(p.getName(), votingMap);
			initVotingInv();
		} else {
			votingMaps[votingMap].removeVote();
			p.closeInventory();
			if(playerVotes.containsKey(p.getName()))
			p.sendMessage(Main.prefix+"§cDu hast deinen Vote für die Map §6" +votingMaps[votingMap].getName() + " §czurückgezogen!");
			playerVotes.remove(p.getName(), votingMap);
			initVotingInv();
		}
	}
	public static HashMap<String, Integer> getPlayerVotes() {
		return playerVotes;
	}
	public ArrayList<Map> getMaps() {
		return maps;
	}
	public static Map[] getVotingMaps() {
		return votingMaps;
	}
	public static Inventory getVotingInv() {
		return votingInv;
	}
	public static int[] getVotingInvOrder() {
		return votingInvOrder;
	}
}
