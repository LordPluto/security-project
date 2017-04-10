package Models;

import java.io.IOException;
import java.util.HashMap;

import Listeners.*;
import Proxies.CasinoViewProxy;


public class GameModel implements GameViewListener {
	
	
	
	private static final int DEFAULT_CAPACITY = 4;

	private int playerCapacity = DEFAULT_CAPACITY;
	private int numPlayers = 0;
	private String passwordHash;
	
	private Player[] players = new Player[playerCapacity];
	private HashMap<GameModelListener, Integer> proxyToPlayerMap = new HashMap<GameModelListener, Integer>();
	
	public synchronized Boolean hasSpaceAvailable() {
		return (playerCapacity - numPlayers) > 0; 
	}
	
	public synchronized void addPlayer(String username, double funds, GameModelListener modelListener) {
		if (hasSpaceAvailable()) {
			int seat = getNextAvailableSeat();
			Player p = new Player(seat, username, funds, modelListener);
			players[seat] = p;
			proxyToPlayerMap.put(modelListener, seat);
			numPlayers++;
			try {
				notifyAllAddedPlayer(p);
				notifyPlayerOfAllOtherPlayers(p);
			} catch (IOException e) {
				closeGame();
			}
		}
	}
	
	public GameModel() {
		
	}
	
	/**
	 * Returns the first seat that has a null player
	 * -1 if all seats are full
	 * @return
	 */
	private int getNextAvailableSeat() {
		for (int i = 0; i < playerCapacity; i++) {
			if (players[i] == null) return i;
		}
		return -1;
	}
	
	private synchronized void notifyAllAddedPlayer(Player newPlayer) throws IOException {
		for (Player otherPlayer : players) {
			if (otherPlayer != null) {
				otherPlayer.getListener().playerUpdate(newPlayer.getSeat(), newPlayer.getUsername(), newPlayer.getFunds());
			}
		}
	}
	
	private synchronized void notifyPlayerOfAllOtherPlayers(Player newPlayer) throws IOException {
		for (Player otherPlayer : players) {
			if (otherPlayer != null) {
				newPlayer.getListener().playerUpdate(otherPlayer.getSeat(), otherPlayer.getUsername(), otherPlayer.getFunds());
			}
		}
	}
	
	private synchronized void notifyPlayerRemoved(int seat) throws IOException {
		for (Player otherPlayer : players) {
			if (otherPlayer != null) {
				otherPlayer.getListener().playerUpdate(seat, "", 0);
			}
		}
	}
	
	/**
	 * Closes the game current session due to an error
	 */
	private synchronized void closeGame() {
		//TODO: Notify clients of error
		//TODO: Clear all proxies of game Listeners
	}
	
	public GameModel(String passwordHash) {
		
	}
	
	private class Player {
		int seat;
		String username;
		double funds;
		GameModelListener listener;
		
		public double getFunds() {
			return funds;
		}

		public void setFunds(double funds) {
			this.funds = funds;
		}

		public int getSeat() {
			return seat;
		}

		public String getUsername() {
			return username;
		}

		public GameModelListener getListener() {
			return listener;
		}

		public Player(int seat, String username, double fund, GameModelListener listener) {
			this.seat = seat;
			this.username = username;
			this.funds = fund;
			this.listener = listener;
		}
		
		
	}

	/**
	 * A Player has quit
	 * @throws IOException 
	 */
	@Override
	public synchronized void quitGame(CasinoViewProxy clientProxy) throws IOException {
		clientProxy.setGameViewListener(null);
		int seat = proxyToPlayerMap.get(clientProxy);
		players[seat] = null;
		numPlayers--;
		notifyPlayerRemoved(seat);
		proxyToPlayerMap.remove(clientProxy);
	}

}


