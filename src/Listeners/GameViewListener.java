package Listeners;

import java.io.IOException;

import Proxies.*;

public interface GameViewListener {

	/** 
	 * The client has quit the game.
	 * @throws IOException 
	 */
	public void quitGame(CasinoViewProxy client) throws IOException;
}
