package Models;

import java.io.IOException;

import Database.DatabaseAccountRepository;
import Listeners.CasinoModelListener;
import Listeners.CasinoViewListener;
import Proxies.CasinoViewProxy;

/**
 * Keeps track of the casino model (login/main menu items)
 * @author ElliotAllen
 *
 */
public class CasinoModel implements CasinoViewListener {
	
	String username;
	double fundsAvailable;
	
	CasinoModelListener listener;
	
	public CasinoModel(String username) {
		this.username = username;
		fundsAvailable = DatabaseAccountRepository.getInstance().getFundsAvailable(username);
	}
	
	public void setModelListener(CasinoModelListener listener) {
		this.listener = listener;
	}

	@Override
	public void quit() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void login(CasinoViewProxy proxy, String username, String password)
			throws IOException {
		
			setModelListener(proxy);
			proxy.loginSuccessfulForAccount(username);
			proxy.setAvailableFunds(fundsAvailable);
		
	}
	
	

}
