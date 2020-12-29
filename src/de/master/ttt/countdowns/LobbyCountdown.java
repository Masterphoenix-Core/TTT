package de.master.ttt.countdowns;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.master.ttt.gamestates.GameState;
import de.master.ttt.gamestates.GameStateManager;
import de.master.ttt.gamestates.LobbyState;
import de.master.ttt.main.Main;
import de.master.ttt.voting.Map;
import de.master.ttt.voting.Voting;

public class LobbyCountdown extends Countdown{

	public static final int COUNTDOWN_TIME = 60, IDLE_TIME = 15;
	
	private GameStateManager gameStateManager;
	
	private int seconds;
	private int idleID;
	private boolean isIdle;
	private boolean isRunning;

	public LobbyCountdown(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		seconds = COUNTDOWN_TIME;
	}
	@Override
	public void start() {
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				for (Player all : Bukkit.getOnlinePlayers()) {
					all.setExp(1);
					all.setLevel(getSeconds());
				}
				isRunning = true;
				switch(seconds) {
				case 60: case 30 : case 10 : case 5 : case 4 : case 3 : case 2 : 
					Bukkit.broadcastMessage(Main.prefix+ "§7Das Spiel startet in §a" +seconds+ " §7Sekunden.");
					
					if(seconds == 5) {
						Voting voting = gameStateManager.getPlugin().getVoting();
						Map winningMap;
						if(voting != null)
							winningMap = voting.getWinnerMap();
						else {
							ArrayList<Map> maps = gameStateManager.getPlugin().getMaps();
							Collections.shuffle(maps);
							winningMap = maps.get(1);
						}
						Bukkit.broadcastMessage(Main.prefix+ "§7Sieger des Votings §6" +winningMap.getName() + "§7!");
					}
					break;
				case 1:
					Bukkit.broadcastMessage(Main.prefix+ "§7Das Spiel startet in §a" +seconds+ " §7Sekunde.");
					break;
				case 0:
					gameStateManager.setGameState(GameState.INGAME_STATE);
					stop();
					break;	
				default:
					break;
				} 
				seconds --;
			}
		}, 0, 20);
	}

	@Override
	public void stop() {
		if(isRunning) { 
			Bukkit.getScheduler().cancelTask(taskID);
			isRunning = false;
			seconds = COUNTDOWN_TIME;
		}
	}

	public void startIdle() {
		isIdle = true;
		idleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				Bukkit.broadcastMessage(Main.prefix+"§7Bis zum Spielstart fehlen noch §6" + (LobbyState.MIN_PLAYERS - gameStateManager.getPlugin().getPlayers().size()) + " Spieler§7.");
				
			}
		}, 0, 20 * IDLE_TIME);
	}
	
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	public void stopIdle() {
		if(isIdle) {
			Bukkit.getScheduler().cancelTask(idleID);
		}
	}
	public boolean isRunning() {
		return isRunning;
	}
}
