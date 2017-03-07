/**
 * The main executable for the client. 
 * Establishes a secure connection socket to server.
 * Establishes main view and model clones.
 * @author Elliot
 *
 */
public class Client {

	
	public static void main(String args[]) {
		//TODO: Create View, Model & Proxy
		
		CasinoModelClone model = new CasinoModelClone();
		CasinoView view = new CasinoView(model);
		
		model.setModelListener(view);
		
		model.setAvailableFunds(1000);
	}
}
