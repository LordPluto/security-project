package Listeners;
import java.io.IOException;

import Proxies.CasinoViewProxy;

/**
 * ViewListener interface receives and passes along messages from the model
 * @author Group 5
 *
 */
public interface CasinoViewListener {

	//TODO: Create Protocol
	/**
	 * Report that the client terminated the program.
	 * @throws IOException 
	 */
	public void quit() throws IOException;
	
	/**
	 * Requests to login to the server
	 * @param proxy - Proxy to respond with. Client should pass null. Used by the session manager.
	 * @param username
	 * @param password
	 * @throws IOException 
	 */
	public void login(CasinoViewProxy proxy, String username, String password) throws IOException;
	
	/**
	 * Joins an open public game.
	 * @param fundsToBring Amount of funds to bring from available funds, usable in game.
	 * @throws IOException 
	 */
	public void joinGame(int sessionID, double fundsToBring, String sessionPassword, GameModelListener respondTo) throws IOException;
}
