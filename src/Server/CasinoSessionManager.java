package Server;
import java.io.IOException;
import java.util.Base64;

import com.sun.swing.internal.plaf.synth.resources.synth;

import Database.DatabaseAccountRepository;
import Database.IAccountRepository;
import Listeners.CasinoViewListener;
import Models.CasinoModel;
import Proxies.CasinoViewProxy;

/**
 * Handles the login authentication of new clients and creates new CasinoModels for them.
 * Only implements the login(..) method.
 * @author ElliotAllen
 *
 */

public class CasinoSessionManager implements CasinoViewListener {

	@Override
	public synchronized void quit() throws IOException {
		System.err.println("Session Manager should not recieve QUIT messages.");
	}

	@Override
	public synchronized void login(CasinoViewProxy proxy, String username, String password) throws IOException {
		//TODO: Authentication
		IAccountRepository repo = DatabaseAccountRepository.getInstance();
		Boolean passedAuth = Security.verifyPassword(password, Base64.getDecoder().decode(repo.getPasswordHash(username)));
		if (passedAuth) {
			//New Model and sh*t
			System.out.printf("LOG: %s passed authentication.\n", username);
			CasinoModel model = new CasinoModel(username);
			model.login(proxy, username, "");
		} else {
			proxy.loginFailed();
		}
		
		
	}

	@Override
	public synchronized void joinGame(int sessionID, double fundsToBring, String sessionPass) throws IOException {
		//Handed by a CasinoModel post authentication.
		System.err.println("Session Manager should not recieve JOIN messages.");
		return;
	}


	
}
