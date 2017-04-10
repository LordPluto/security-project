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
	
	
	
	
}
