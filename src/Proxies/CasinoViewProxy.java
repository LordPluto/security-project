package Proxies;
import java.io.*;
import java.net.Socket;

import Listeners.*;

/**
 * Reads data from view off the network. 
 * Sends data from the model over the network.
 * @author ElliotAllen
 *
 */
public class CasinoViewProxy implements CasinoModelListener, GameModelListener{
	
	CasinoViewListener casinoListener;
	GameViewListener gameListener;
	Socket socket;
	DataOutputStream out;
	DataInputStream in;
	ReaderThread networkReader;

	public CasinoViewProxy(Socket socket) throws IOException {
		this.socket = socket;
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(socket.getInputStream());
	}

	@Override
	public void setAvailableFunds(double fundsAvailable) throws IOException {
		System.out.println("SERVER SET FUNDS: "+fundsAvailable);
		out.writeByte('M');
		out.writeDouble(fundsAvailable);
		out.flush();
		
	}

	@Override
	public void loginFailed() throws IOException {
		System.out.println("SERVER SENT LOGIN FAILED");
		out.writeByte('E');
		out.writeInt(ErrorCode.LOGIN_FAILED);
		out.flush();
		
	}

	@Override
	public void loginSuccessfulForAccount(String name) throws IOException {
		System.out.println("SERVER SENT LOGIN SUCCESS");
		out.writeByte('L');
		out.writeUTF(name);
		out.flush();
	}

	public void setCasinoViewListener(CasinoViewListener listener) {
		if (this.casinoListener == null) {
			this.casinoListener = listener;
			if (networkReader == null) {
				networkReader = new ReaderThread();
				networkReader.start();
			}
		}
		else {
			this.casinoListener = listener;
		}
	}
	
	public void setGameViewListener(GameViewListener listener) {
		if (this.gameListener == null) {
			this.gameListener = listener;
			if (networkReader == null) {
				networkReader = new ReaderThread();
				networkReader.start();
			}
		}
		else {
			this.gameListener = listener;
		}
	}
	
	@Override
	public void joinGameFailed(String reason) throws IOException {
		out.writeByte('E');
		out.writeInt(ErrorCode.JOIN_GAME_FAILED);
		out.flush();
	}
	

	private class ReaderThread
	extends Thread
	{
		public void run() {
			try {
				for (;;) {
					String name, password;
					int sessionID;
					double funds;
					byte b = in.readByte();
					switch (b) {
						case 'L':
							name = in.readUTF();
							password = in.readUTF();
							System.out.printf("SERVER GOT LOGIN: %s, %s\n", name, password);
							casinoListener.login(CasinoViewProxy.this, name, password);
							break;
						case 'Q':
							System.out.println("Server got QUIT");
							casinoListener.quit();
							break;
						case 'J':
							sessionID = in.readInt();
							funds = in.readDouble();
							password = in.readUTF();
							casinoListener.joinGame(sessionID, funds, password);
						default:
							System.err.println ("Message not recognized.");
							break;
						}
					}
				}
			catch (IOException exc){}
			finally {
				try {
					socket.close();
				} catch (IOException exc) {}
			}
		}
	}


	

}
