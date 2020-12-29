package de.master.ttt.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.master.ttt.main.Main;
import de.master.ttt.voting.Voting;

public class VotingListener implements Listener {
	
	public static final int MAP_AMOUNT = 2;
	public static final String VOTING_INV_TITLE = "Voting-Inventory";
	
	public VotingListener(Main plugin) {
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		if(item.getItemMeta() == null) return;
		if(item.getItemMeta().getDisplayName().equals(PlayerLobbyConnectionListener.VOTING_ITEM_NAME)) {

			p.openInventory(Voting.getVotingInv());
		} 
	}
	@EventHandler
	public void onVote(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
			if(!e.getInventory().getTitle().equals(Voting.VOTING_INV_TITLE)) return;
			e.setCancelled(true);
			for(int i = 0; i < Voting.getVotingInvOrder().length; i++) {
				if(Voting.getVotingInvOrder()[i] == e.getSlot()) {
					Voting.vote(p, i);
					return;
				}
			}
	}
}
