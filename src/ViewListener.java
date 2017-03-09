import java.io.IOException;

/**
 * ViewListener interface receives and passes along messages from the model
 * @author Group 5
 *
 */
public interface ViewListener {

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
	
	
}
