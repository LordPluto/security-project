package Models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import Listeners.*;
import Proxies.CasinoViewProxy;
import Server.Server;


public class GameModel implements GameViewListener {
	
	
	
	private static final int DEFAULT_CAPACITY = 5;
	private static final int TURN_LENGTH = 10;
	
	private int playerCapacity = DEFAULT_CAPACITY;
	
	private int numPlayers = 0;
	private String passwordHash;
	private Random rand = new Random();
	private final ScheduledExecutorService scheduler =
		     Executors.newScheduledThreadPool(1);
	ScheduledFuture<?> turnHandle;
	
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
	
	final Runnable turn = new Runnable() {
	       public void run() {
	    	   Boolean outcomeBool = rand.nextBoolean();
	    	   int outcomeInt = 0;
	    	   if (outcomeBool) outcomeInt = 1; else outcomeInt = 2;
	    	   for (Player player : players) {
	    		   if (player == null) return;
	    		   Boolean isWinner = player.prediction == outcomeInt;
	    		   try {
	    			   player.listener.turnUpdate(outcomeInt, TURN_LENGTH);
		    		   if (isWinner) {
		    			   player.addFunds(2* player.currentBet);
		    			   System.out.println(player.username + " won " + 2*player.currentBet);
		    		   }
		    		   //savePlayerFunds();
		    		   player.currentBet = 0;
	    			   player.prediction = 0;
	    			   notifyAllBalances();
	    		   } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
	    		   }
	    	   }
	       }
  };
	
	public GameModel() {
		startGame();
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

		startGame();
	}
	
	private class Player {
		int seat;
		String username;
		double totalFunds; //Total balance for account for accurate Updates
		double startingVisibleFunds; //Amount user game to the table with
		double netFunds; // +/- what player has won/lost.
		GameModelListener listener;
		public int prediction = 0;
		double currentBet;
		
		public double getFunds() {
			return startingVisibleFunds + netFunds;
		}

		public double getTotalFundsToSave() {
			return totalFunds + netFunds;
		}

		//Add or remove funds
		public void addFunds(double funds) {
			this.netFunds += funds;
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
			this.totalFunds = Server.AccountRepositoryInstance.getFundsAvailable(username);
			this.startingVisibleFunds = fund;
			this.netFunds = 0;
			this.listener = listener;
		}
		
		public Boolean setBet(double funds) {
			if (getFunds() - funds >= 0 && currentBet == 0) {
				addFunds(-funds);
				currentBet = funds;
				return true;
			}
			return false;
			
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

	@Override
	public synchronized void bet(GameModelListener client, int outcome, double amount) throws IOException {
		// TODO Auto-generated method stub
		if (outcome == 0 || !(outcome == 1 || outcome == 2)) {
			System.err.println("Game got invalid bet outcome.");
			return;
		}
		amount = Math.floor(amount * 100) /100; //Truncate
		Player p = players[proxyToPlayerMap.get(client)];
		if (p.setBet(amount)){
			p.prediction = outcome;
		} else {
			System.err.println("Game got invalid bet.");
			return;
		}
		 
		
		for (Player player : players) {
			if (player != null){
				player.listener.updateBet(p.seat, outcome, amount);
				player.listener.updateBalance(p.seat, p.getFunds());
			}
				
		}
	}
	
	private synchronized void startGame() {
		turnHandle =  scheduler.scheduleAtFixedRate(turn, 10, TURN_LENGTH, TimeUnit.SECONDS);
	}
	
	
	private synchronized void savePlayerFunds() {
		for (Player player : players) {
			Server.AccountRepositoryInstance.modifyFundsAvailable(player.username, player.getTotalFundsToSave());
		}
	}
	
	private synchronized void notifyAllBalances() throws IOException {
		for (Player toPlayer : players) {
			if (toPlayer != null){
				for (Player ofPlayer : players) {
					if (ofPlayer != null){
						toPlayer.listener.updateBalance(ofPlayer.getSeat(), ofPlayer.getFunds());
					}
				}
			}
				
		}
	}
 	
	
	

}


