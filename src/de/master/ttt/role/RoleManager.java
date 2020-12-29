package de.master.ttt.role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import de.master.ttt.main.Main;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;

public class RoleManager {

	private Main plugin;
	private HashMap<String, Role> playerRoles;
	private ArrayList<Player> players;
	private ArrayList<String> traitorPlayers;
	
	private int traitors, detectives, innocents;
	
	public RoleManager(Main plugin) {
		this.plugin = plugin;
		playerRoles = new HashMap<>();
		players = plugin.getPlayers();
		traitorPlayers = new ArrayList<>();
	}
	public void calcRoles() {
		int playerSize = players.size();
			traitors = (int) Math.round(Math.log(playerSize) * 1.32);
			detectives = (int) Math.round(Math.log(playerSize) * 0.75);
			innocents = playerSize - traitors - detectives;
			
			Collections.shuffle(players);
		
			int counter = 0;
		for(int i = counter; i < traitors; i++) {
			playerRoles.put(players.get(i).getName(), Role.TRAITOR);
			traitorPlayers.add(players.get(i).getName());
		}
		counter += traitors;
		
		for(int i = counter; i < detectives + counter; i++) 
			playerRoles.put(players.get(i).getName(), Role.DETECTIVE);
		counter += detectives;
		
		for(int i = counter; i < innocents + counter; i++)
			playerRoles.put(players.get(i).getName(), Role.INNOCENT);
		
		for(Player current : players) {
			switch(getPlayerRole(current)) {
			case TRAITOR:
				for(Player others : players)
					setFakeArmor(others, current.getEntityId(), (getPlayerRole(others) != Role.TRAITOR) ? Color.GREEN : Color.RED);
				break;
			case DETECTIVE:
				setArmor(current, Color.BLUE);
				break;
			case INNOCENT:
				setArmor(current, Color.GREEN);
				break;
				
			default:
				break;
			}
		}
	}
	
	public void setArmor(Player p, Color color)  {
		p.getInventory().setChestplate(LeatherChestp(color));
	}
	public void setFakeArmor(Player p, int entityID, Color color) {
		ItemStack armor = LeatherChestp(color);
		final int CHESTPLATE_SLOT = 3;
		PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(entityID, CHESTPLATE_SLOT, CraftItemStack.asNMSCopy(armor));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
	
	private ItemStack LeatherChestp(Color color) {
		ItemStack is = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta lam = (LeatherArmorMeta) is.getItemMeta();
		lam.setColor(color);
		is.setItemMeta(lam);
		return is;
	}
	
	public Role getPlayerRole(Player p) {
		return playerRoles.get(p.getName());
	}
	public ArrayList<String> getTraitorPlayers() {
		return traitorPlayers;
	}
}
