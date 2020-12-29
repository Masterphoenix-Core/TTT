package de.master.ttt.gamestates;

import org.bukkit.Bukkit;

import de.master.ttt.countdowns.LobbyCountdown;
import de.master.ttt.main.Main;

public class LobbyState extends GameState{

	public static final int MIN_PLAYERS = 1,
								MAX_PLAYERS = 2;
	
	private LobbyCountdown countdown;
	
	public LobbyState(GameStateManager gameStateManager) {
		countdown = new LobbyCountdown(gameStateManager);
	}
	@Override
	public void start() {
		countdown.startIdle();
		
	}

	@Override
	public void stop() {
		Bukkit.broadcastMessage(Main.prefix+"§cAlle Spieler werden teleportiert!");
		
	}

	public LobbyCountdown getCountdown() {
		return countdown;
	}
}
