import java.io.IOException;

/**
 * Keeps track of the casino model (login/main menu items)
 * @author ElliotAllen
 *
 */
public class CasinoModel implements CasinoViewListener {
	
	String username;
	int fundsAvailable;
	
	CasinoModelListener listener;
	
	public CasinoModel(String username) {
		this.username = username;
		fundsAvailable = FileAccountRepository.getInstance().getFundsAvailable(username);
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
