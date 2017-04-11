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
	CasinoViewProxy self = this;
	Socket socket;
	DataOutputStream out;
	DataInputStream in;
	ReaderThread networkReader;

	public CasinoViewProxy(Socket socket) throws IOException {
		this.socket = socket;
		System.out.printf("New connection to %s %s\n",socket.getInetAddress().toString(), socket.getPort());
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(socket.getInputStream());
	}

	@Override
	public synchronized void setAvailableFunds(double fundsAvailable) throws IOException {
		System.out.println("SERVER SET FUNDS: "+fundsAvailable);
		out.writeByte('M');
		out.writeDouble(fundsAvailable);
		out.flush();
		
	}

	@Override
	public synchronized void loginFailed() throws IOException {
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
	
	public synchronized void setGameViewListener(GameViewListener listener) {
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
	public synchronized void joinGameFailed(String reason) throws IOException {
		out.writeByte('E');
		out.writeInt(ErrorCode.JOIN_GAME_FAILED);
		out.flush();
	}
	
	@Override
	public synchronized void playerUpdate(int seat, String username, double funds) throws IOException {
		System.out.printf("Server send PLAYER %s to %s\n",username, socket.getPort());
		out.writeByte('P');
		out.writeInt(seat);
		out.writeUTF(username);
		out.writeDouble(funds);
		out.flush();
	}
	
	@Override
	public synchronized void joinGameSuccess(CasinoModelProxy session) throws IOException {
		out.writeByte('G');
		out.flush();
	}
	
	@Override
	public void updateBalance(int seat, double amount) throws IOException {
		System.out.printf("Server send BAL %d to %s\n",seat, socket.getPort());
		out.writeByte('B');
		out.writeInt(seat);
		out.writeDouble(amount);
		out.flush();
	}

	@Override
	public void updateBet(int seat, int prediction, double amount)
			throws IOException {
		System.out.printf("Server send BET %d to %s\n",seat, socket.getPort());
		out.writeByte('b');
		out.writeInt(seat);
		out.writeInt(prediction);
		out.writeDouble(amount);
		out.flush();
		
	}

	@Override
	public void turnUpdate(int outcome, int time) throws IOException {
		System.out.printf("Server send TURN %d to %s\n",outcome, socket.getPort());
		out.writeByte('T');
		out.writeInt(outcome);
		out.writeInt(time);
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
							casinoListener.joinGame(sessionID, funds, password, self);
							break;
						case 'E':
							//Do we really need to quit here? the finally should get it
							//Don't want to double call
							break;
						case 'B':
							int outcome = in.readInt();
							funds = in.readDouble();
							if (gameListener != null) gameListener.bet(self,outcome,funds);
							break;
						default:
							System.err.println ("Message not recognized. " + b);
							break;
						}
					}
				}
			catch (IOException exc){}
			finally {
				try {
					System.out.printf("Closed connection to %s %s\n",socket.getInetAddress().toString(), socket.getPort());
					casinoListener.quit();
					if (gameListener != null) gameListener.quitGame(self);
					socket.close();
				} catch (IOException exc) {}
			}
		}
	}


	


	


	


	

}
