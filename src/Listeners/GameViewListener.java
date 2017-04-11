package Listeners;

import java.io.IOException;

import Proxies.*;

public interface GameViewListener {

	/** 
	 * The client has quit the game.
	 * @throws IOException 
	 */
	public void quitGame(CasinoViewProxy client) throws IOException;
	
	
	/**
	 * Client reports their bet.
	 * @param client - The client proxy that sent the message
	 * @param outcome - Int value indicating their prediction 0 - no bet, 1 - heads, 2 - tails
	 * @param amount - Amount of money they bet.
	 * @throws IOException 
	 */
	public void bet(GameModelListener client, int outcome, double amount) throws IOException;
	
	
}
