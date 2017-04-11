package Listeners;

import java.io.IOException;

public interface GameModelListener {

	/**
	 * Informs the game client of a player in a current seat.
	 * @param seat
	 * @param username
	 * @param funds
	 * @throws IOException 
	 */
	public void playerUpdate(int seat, String username, double funds) throws IOException;
	
	/**
	 * Updates the balance of a player
	 * @param seat - The seat of the player
	 * @param amount - The total amount in their 'stack'
	 */
	public void updateBalance(int seat, double amount) throws IOException;
	
	/**
	 * The total amount the player is visibly betting
	 * @param seat
	 * @param amount
	 */
	public void updateBet(int seat, int prediction, double amount) throws IOException;
	
	/**
	 * Tells the client a turn change has occured. 
	 * @param outcome - The outcome of the previous round.
	 * @param time - Time until next turn.
	 */
	public void turnUpdate(int outcome, int time) throws IOException;
	
}
