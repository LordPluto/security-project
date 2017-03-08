import java.io.IOException;
import java.util.Base64;


public class CasinoSessionManager implements ViewListener {

	@Override
	public void quit() throws IOException {}

	@Override
	public synchronized void login(CasinoViewProxy proxy, String username, String password) throws IOException {
		//TODO: Authentication
		IAccountRepository repo = FileAccountRepository.getInstance();
		//Boolean passedAuth = Security.verifyPassword(password, Base64.getDecoder().decode(repo.getPasswordHash(username)));
		Boolean passedAuth = true;//test
		if (passedAuth) {
			//New Model and sh*t
			System.out.printf("LOG: %s passed authentication.\n", username);
			CasinoModel model = new CasinoModel(username);
			model.login(proxy, username, password);
		} else {
			proxy.loginFailed();
		}
		
		
	}


	
}
