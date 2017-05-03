package Database;
/**
 * Performs CRUD operations on a storage medium
 * 
 * @author ElliotAllen
 *
 */
public interface IAccountRepository {
	
	//Mostly just prototyping/test methods
	//TODO: Make this database repository protocol not crap
	

	/**
	 * Reads the password hash from whatever storage medium we are using.
	 * @param username
	 */
	public String getPasswordHash(String username);
	
	/**
	 * Write a new account to the db
	 * @param username
	 * @param hash
	 */
	public void addAccount(String username, String hash, int balance);
	
	/**
	 * Get the available balance for the user.
	 * @param username
	 * @return
	 */
	public double getFundsAvailable(String username);
	
	/**
	 * Add or subtract from the user's balance
	 * @param username
	 * @param amount
	 */
	public void modifyFundsAvailable(String username, double amount);
	
	/**
	 * Set the available balance for the user.
	 * @param username
	 * @param balance
	 */
	public void setFundsAvailable(String username, double balance);
	
	/**
	 * Set the password to access the user's funds.
	 * @param username
	 * @param hash
	 */
	public void setFundsPassword(String username, String hash);
	
	/**
	 * Gets the password to access the user's funds.
	 * @param username
	 * @return
	 */
	public String getFundsPassword(String username);
}
