package de.master.ttt.listener;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import de.master.ttt.gamestates.IngameState;
import de.master.ttt.gamestates.LobbyState;
import de.master.ttt.main.Main;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;

public class GameProtectionListener implements Listener {

	private Main plugin;
	private ArrayList<String> buildModePlayers;
	
	public GameProtectionListener(Main plugin) {
		this.plugin = plugin;
		buildModePlayers = new ArrayList<>();
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(buildModePlayers.contains(e.getPlayer().getName())) return;
			e.setCancelled(true);
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e)  {
		if(buildModePlayers.contains(e.getPlayer().getName())) return;
		e.setCancelled(true);
	}
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
			if(buildModePlayers.contains(e.getPlayer().getName())) {
				e.setCancelled(false);
				return;
			}
				e.setCancelled(true);
				return;
			}
		
		Material material = e.getItemDrop().getItemStack().getType();
		if(material == Material.LEATHER_CHESTPLATE || material == Material.STICK || ((material == Material.BOW) && (e.getItemDrop().getItemStack().getItemMeta() != null)))
			e.setCancelled(true);
	}
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		if(e.getCurrentItem().getType() == Material.LEATHER_CHESTPLATE)
			e.setCancelled(true);
	}
	@EventHandler
	public void changeFood(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void entitySpawn(CreatureSpawnEvent e) {
		if(e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL)
			e.setCancelled(true);
	}
	@EventHandler
	public void onBed(PlayerBedEnterEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void onBowShot(EntityShootBowEvent e) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState)) {
			e.setCancelled(true);
			return;
		}
		IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
		if(ingameState.inGrace()) e.setCancelled(true);
	}
	@EventHandler
	public void onEntDamage(EntityDamageByEntityEvent e) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState)) {
			e.setCancelled(true);
			return;
		}
		IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
		if(ingameState.inGrace()) e.setCancelled(true);
	}
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
		((CraftPlayer) p).getHandle().playerConnection.a(packet);
		
		p.getInventory().setChestplate(null);
		p.getInventory().setHelmet(null);
	}
	@EventHandler
	public ArrayList<String> getBuildModePlayers() {
		return buildModePlayers;
	}
}
