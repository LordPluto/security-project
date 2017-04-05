import java.io.IOException;

/**
 * ModelListener interface receives and passes along messages from the model
 * So far this is only for the login/landing pages.
 * @author Elliot Allen - epa4566
 *
 */
public interface CasinoModelListener {
	
	//TODO: Refine protocol. Make sure its secure etc.
	
	
	/**
	 * Report the amount of funds that the logged in has.
	 * PREQUISITE: Account is logged in.
	 * @param funds - funds available.
	 * @throws IOException 
	 */
	public void setAvailableFunds(int funds) throws IOException;
	
	/** 
	 * The server reports that the login attempt failed.
	 * @throws IOException 
	 */
	public void loginFailed() throws IOException;
	
	/**
	 * Reports the the user that their login attempt was successful.
	 * Passes the name of the logged in user back.
	 * @param name
	 * @throws IOException 
	 */
	public void loginSuccessfulForAccount(String name) throws IOException;
	

	

}
