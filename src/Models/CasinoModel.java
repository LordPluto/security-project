package Models;

import java.io.IOException;

import Database.DatabaseAccountRepository;
import Listeners.CasinoModelListener;
import Listeners.CasinoViewListener;
import Listeners.GameModelListener;
import Proxies.CasinoViewProxy;
import Server.CasinoSessionManager;
import Server.GameSessionManager;

/**
 * Keeps track of the casino model (login/main menu items)
 * Also sort of acts as the controller I guess
 * @author ElliotAllen
 *
 */
public class CasinoModel implements CasinoViewListener {
	
	public static final int IN_MENU = 0;
	public static final int IN_GAME = 1;
	
	String username;
	double fundsAvailable;
	int gameStatus;
	
	CasinoModelListener listener;
	
	public CasinoModel(String username) {
		this.username = username;
		gameStatus = IN_MENU;
		fundsAvailable = DatabaseAccountRepository.getInstance().getFundsAvailable(username);
	}
	
	public void setModelListener(CasinoModelListener listener) {
		this.listener = listener;
	}

	@Override
	public void quit() throws IOException {
		CasinoSessionManager.removeSession(username);		
	}

	@Override
	public void login(CasinoViewProxy proxy, String username, String password)
			throws IOException {
		
			setModelListener(proxy);
			proxy.setCasinoViewListener(this);
			proxy.loginSuccessfulForAccount(username);
			proxy.setAvailableFunds(fundsAvailable);
		
	}

	@Override
	public void joinGame(int sessionID, double fundsToBring, String sessionPassword, GameModelListener l) throws IOException {
		if (fundsToBring <= fundsAvailable) {
			GameModel gameSession = GameSessionManager.joinGame(
					sessionID,
					username,
					fundsToBring,
					sessionPassword,
					(CasinoViewProxy)listener);
			if (gameSession != null) {
				listener.joinGameSuccess(null);
				((CasinoViewProxy)l).setGameViewListener(gameSession);
				gameSession.addPlayer(username, fundsToBring, ((CasinoViewProxy)listener));
			}
		} else {
			listener.joinGameFailed("Funds requested not available.");
		}
		
	}
	
	

}
