/**
 * ModelListener interface receives and passes along messages from the model
 * So far this is only for the login/landing pages.
 * @author Elliot Allen - epa4566
 *
 */
public interface ModelListener {
	
	
	/**
	 * Report the amount of funds that the logged in has.
	 * PREQUISITE: Account is logged in.
	 * @param funds - funds available.
	 */
	public void setAvailableFunds(int funds);
	
	/** 
	 * The server reports that the login attempt failed.
	 */
	public void loginFailed();
	
	/**
	 * Reports the the user that their login attempt was successful.
	 * Passes the name of the logged in user back.
	 * @param name
	 */
	public void loginSuccessfulForAccount(String name);
	

	//TODO: Create Protocol
	
	/**
	 * Report that the session has ended and to quit.
	 */
	public void quit();
}
