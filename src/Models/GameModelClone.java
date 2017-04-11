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
	
	
	private int getNextAvailableUISeat() {
		for (int i = 1; i< 5; i++) {
			if (!uiSeatToGameSeatMap.containsKey(i)){
				return i;
			}
		}
		return -1;
	}
	
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

	public String getPredictionString(int seat) {
		if (!uiSeatToGameSeatMap.containsKey(seat)) return "";
		Player p = players[uiSeatToGameSeatMap.get(seat)];
		if (p == null) return "";
		return p.getPredictionString();
	}
	
	
	
	
	//Interface methods

	@Override
	public void playerUpdate(int seat, String username, double funds)
			throws IOException {
		if (username == "") { //Player removed
			if (username.equals(localPlayerName)) {
				System.err.println("Client should not receive removal of themselves.");
			}
			Player p = players[seat];
			if (p != null && uiSeatToGameSeatMap.containsKey(p.UISeat)) {
				uiSeatToGameSeatMap.remove(p.UISeat);
				players[seat] = null;
				listener.playerUpdate(p.UISeat, username, funds);
				numOpponents--;
				return;
			}
		}
		Player p = new Player(seat, username, funds);
		if (players[seat] != null){
			if (username.equals(players[seat].getUsername())) return;
			else System.err.println("Client Shouldn't overwrite player.");
		}
		
		players[seat] = p;
		if (username.equals(localPlayerName)) {
			uiSeatToGameSeatMap.put(0, seat);
			p.UISeat = 0;
		} else {
			int newUISeat = getNextAvailableUISeat();
			uiSeatToGameSeatMap.put(newUISeat, seat);
			p.UISeat = newUISeat;
			//TODO: Find next available the right way.
			numOpponents++;
			
		}
		listener.playerUpdate(p.UISeat,username,funds);
		
	}

	
	private class Player {
		int seat;
		public int UISeat;
		String username;
		double funds;
		double currentBet;
		int currentChoice = 0;
		
		public double getCurrentBet() {
			return currentBet;
		}

		public void setCurrentBet(double currentBet) {
			this.currentBet = currentBet;
		}

		public int getCurrentChoice() {
			return currentChoice;
		}

		public void setCurrentChoice(int currentChoice) {
			this.currentChoice = currentChoice;
		}
		
		public String getPredictionString() {
			switch (currentChoice) {
				case 1: 
					return "Heads";
				case 2:
					return "Tails";
				default:
					return "";
						
			}
		}
		
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


	@Override
	public void updateBalance(int seat, double amount) throws IOException {
		Player p = players[seat];
		p.setFunds(amount);
		listener.updateBalance(p.UISeat, amount);
	}

	@Override
	public void updateBet(int seat, int prediction, double amount)
			throws IOException {
		Player p = players[seat];
		p.setCurrentBet(amount);
		p.setCurrentChoice(prediction);
		listener.updateBet(p.UISeat, prediction, amount);
		
	}

	@Override
	public void turnUpdate(int outcome, int time) throws IOException {
		listener.turnUpdate(outcome, time);
		
	}
}
