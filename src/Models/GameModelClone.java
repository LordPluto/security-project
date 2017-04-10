package Models;

import java.io.IOException;
import java.util.HashMap;

import Listeners.GameModelListener;

public class GameModelClone implements GameModelListener {
	
	private Player[] players;
	HashMap<Integer, Integer> uiSeatToGameSeatMap = new HashMap<Integer, Integer>();
	private GameModelListener listener;
	private String localPlayerName;
	private int numOpponents;
	
	private int mySeat = -1;
	
	public void setGameModelListener(GameModelListener theListener) {
		this.listener = theListener;
	}
	
	public GameModelClone(int capacity, String playerName) {
		players = new Player[capacity];
		
		localPlayerName = playerName;
	}
	
	public void setModelListener(GameModelListener view) {
		listener = view;
	}
	
	
	//External Methods (Used by view)
	public String getName(int seatIndex) {
		if (!uiSeatToGameSeatMap.containsKey(seatIndex)) return "";
		Player p = players[uiSeatToGameSeatMap.get(seatIndex)];
		if (p == null) return "";
		return p.getUsername();
	}
	
	public double getFunds(int seatIndex) {
		if (!uiSeatToGameSeatMap.containsKey(seatIndex)) return 0;
		Player p = players[uiSeatToGameSeatMap.get(seatIndex)];
		if (p == null) return 0;
		return p.getFunds();
	}

	
	
	
	
	//Interface methods

	@Override
	public void playerUpdate(int seat, String username, double funds)
			throws IOException {
		if (username == "") {
			if (username.equals(localPlayerName)) {
				System.err.println("Client should not receive removal of themselves.");
			}
			Player p = players[seat];
			if (p != null && uiSeatToGameSeatMap.containsKey(p.UISeat)) {
				uiSeatToGameSeatMap.remove(p.UISeat);
				players[seat] = null;
				listener.playerUpdate(seat, username, funds);
				numOpponents--;
				return;
			}
		}
		Player p = new Player(seat, username, funds);
		players[seat] = p;
		if (username.equals(localPlayerName)) {
			uiSeatToGameSeatMap.put(0, seat);
			p.UISeat = 0;
		} else {
			uiSeatToGameSeatMap.put(1+numOpponents, seat);
			p.UISeat = 1+numOpponents;
			numOpponents++;
			
		}
		listener.playerUpdate(seat,username,funds);
		
	}

	
	private class Player {
		int seat;
		public int UISeat;
		String username;
		double funds;
		double currentBet;
		Boolean currentChoice;
		
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

		public Player(int seat, String username, double fund) {
			this.seat = seat;
			this.username = username;
			this.funds = fund;
		}
	}
}
