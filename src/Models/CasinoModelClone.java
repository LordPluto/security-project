package Models;

import java.io.IOException;

import Listeners.CasinoModelListener;

/** 
 * 
 * @author ElliotAllen
 *
 */
public class CasinoModelClone implements CasinoModelListener {
	
	CasinoModelListener modelListener;
	String loggedInUser;
	double availableFunds;
	
	public String getLoggedInUser() {
		return loggedInUser;
	}


	public double getAvailableFunds() {
		return availableFunds;
	}


	public void setModelListener(CasinoModelListener view) {
		this.modelListener = view;
	}


	@Override
	public void setAvailableFunds(double funds) throws IOException {
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
