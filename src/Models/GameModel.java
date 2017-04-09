package Models;

import java.util.HashMap;

import Listeners.*;


public class GameModel implements GameViewListener {
	
	
	
	private static final int DEFFAULT_CAPACITY = 4;

	private int playerCapacity;
	private int numPlayers = 0;
	private String passwordHash;
	
	private GameModelListener[] playerViews;
	private double[] funds;
	
	public synchronized Boolean hasSpaceAvailable() {
		return (playerCapacity - numPlayers) > 0; 
	}
	
	public synchronized void addPlayer(String username, double funds, GameModelListener modelListener) {
		if (hasSpaceAvailable()){
			playerViews[numPlayers] = modelListener;
			this.funds[numPlayers] = funds;
			numPlayers++;
			
		}
	}
	
	public GameModel() {
		playerViews = new GameModelListener[DEFFAULT_CAPACITY];
		funds = new double[DEFFAULT_CAPACITY];
	}
	
	public GameModel(String passwordHash) {
		
	}
	
	private class Player {
		int seat;
		String username;
		double funds;
		GameModelListener listener;
	}

}


