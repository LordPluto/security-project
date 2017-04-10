package Models;

import java.io.IOException;

import Listeners.*;
import Views.*;
import Proxies.CasinoModelProxy;

/** 
 * 
 * @author ElliotAllen
 *
 */
public class CasinoModelClone implements CasinoModelListener {
	
	CasinoModelListener modelListener;
	String loggedInUser;
	double availableFunds;
	GameModelClone gameClone;
	
	public  String getLoggedInUser() {
		return loggedInUser;
	}


	public  double getAvailableFunds() {
		return availableFunds;
	}


	public synchronized void setModelListener(CasinoModelListener view) {
		this.modelListener = view;
	}


	@Override
	public  void setAvailableFunds(double funds) throws IOException {
		availableFunds = funds;
		modelListener.setAvailableFunds(funds);
	}

	@Override
	public  void loginFailed() throws IOException {
		modelListener.loginFailed();
	}

	@Override
	public  void loginSuccessfulForAccount(String name) throws IOException {
		this.loggedInUser = name;
		modelListener.loginSuccessfulForAccount(name);
		
	}


	@Override
	public  void joinGameFailed(String reason) throws IOException {
		modelListener.joinGameFailed(reason);
	}


	@Override
	public  void joinGameSuccess(CasinoModelProxy gameSession) throws IOException {
		TestGameView.createAndShowGUI(gameClone);
		TestGameView.setViewListener(gameSession);
		modelListener.joinGameSuccess(null);
		
	}
	
	public GameModelListener initGame() {
		gameClone = new GameModelClone(4, loggedInUser);
		gameClone.setGameModelListener(new TestGameView());
		return gameClone;
	}

	
	
}
