import java.io.IOException;

/** 
 * 
 * @author ElliotAllen
 *
 */
public class CasinoModelClone implements ModelListener {
	
	ModelListener modelListener;
	String loggedInUser;
	int availableFunds;
	
	public String getLoggedInUser() {
		return loggedInUser;
	}


	public int getAvailableFunds() {
		return availableFunds;
	}


	public void setModelListener(ModelListener view) {
		this.modelListener = view;
	}


	@Override
	public void setAvailableFunds(int funds) throws IOException {
		availableFunds = funds;
		modelListener.setAvailableFunds(funds);
	}

	@Override
	public void loginFailed() throws IOException {
		modelListener.loginFailed();
	}

	@Override
	public void loginSuccessfulForAccount(String name) throws IOException {
		this.loggedInUser = name;
		modelListener.loginSuccessfulForAccount(name);
		
	}

	
	
}
