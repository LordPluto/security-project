package Server;

import Models.GameModel;
import Proxies.CasinoViewProxy;

public class GameSessionManager {

	static GameModel currentOpenSession;
	
	public static synchronized GameModel joinGame(int sessionID,String username, double funds, String password, CasinoViewProxy proxy) {
	
		//Public Game
		if (sessionID == 0) {
			if (currentOpenSession == null || !currentOpenSession.hasSpaceAvailable() ){
				currentOpenSession = new GameModel();
			} 
			return currentOpenSession;
			
		}
		
		//Private Game
		//TODO: Private game validate pw and shtuff
		
		return null;
	}
	
}
