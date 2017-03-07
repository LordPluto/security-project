/** 
 * 
 * @author ElliotAllen
 *
 */
public class CasinoModelClone implements ModelListener {
	
	ModelListener modelListener;
	String loggedInUser;
	int availableFunds;
	
	public void setModelListener(ModelListener view) {
		this.modelListener = view;
	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAvailableFunds(int funds) {
		availableFunds = funds;
		modelListener.setAvailableFunds(funds);
	}

	@Override
	public void loginFailed() {
		modelListener.loginFailed();
		
	}

	@Override
	public void loginSuccessfulForAccount(String name) {
		this.loggedInUser = name;
		modelListener.loginSuccessfulForAccount(name);
		
	}

	
	
}
