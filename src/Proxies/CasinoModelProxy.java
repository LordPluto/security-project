package Proxies;
import java.io.*;
import java.net.Socket;



import Listeners.*;



public class CasinoModelProxy implements CasinoViewListener, GameViewListener {
	
	private CasinoModelListener casinoListener;
	private GameModelListener gameListener;
	private CasinoModelProxy self = this;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private ReaderThread networkReader;
	
	public CasinoModelProxy(Socket socket) {
		this.socket = socket;
		try {
			this.out = new DataOutputStream(socket.getOutputStream());
			this.in = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setCasinoModelListener(CasinoModelListener listener) {
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
	
	public void setGameModelListener(GameModelListener listener) {
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
	public void quit() throws IOException {
		System.out.println("CLIENT sent QUIT");
		out.writeByte('Q');
		out.flush();
	}

	@Override
	public void login(CasinoViewProxy proxy, String username, String password) throws IOException {
		System.out.println("CLIENT sent LOGIN");
		out.writeByte('L');
		out.writeUTF(username);
		out.writeUTF(password);
		out.flush();
	}
	
	@Override
	public void joinGame(int sessionID, double fundsToBring, String sessionPassword, GameModelListener l) throws IOException {
		System.out.println("CLIENT sent JOIN "+ sessionID + " with $" + fundsToBring);
		setGameModelListener(l);
		out.writeByte('J');
		out.writeInt(sessionID);
		out.writeDouble(fundsToBring);
		out.writeUTF(sessionPassword);
		out.flush();
	}
	
	@Override
	public void quitGame(CasinoViewProxy client) throws IOException {
		out.writeByte('E');
		out.flush();
	}
	
	private class ReaderThread
	extends Thread
	{
		public void run() {
			try {
				System.out.println("Client reader started.");
				for (;;) {
					String name;
					double funds;
					byte b = in.readByte();
					switch (b) {
						case 'L':
							name = in.readUTF();
							System.out.println("Client got LOGIN SUCCESS");
							casinoListener.loginSuccessfulForAccount(name);
							break;
						case 'E':
							int error = in.readInt();
							switch(error) {
								case ErrorCode.LOGIN_FAILED:
									casinoListener.loginFailed();
									break;
								case ErrorCode.JOIN_GAME_FAILED:
									String reason = in.readUTF();
									casinoListener.joinGameFailed(reason);
									break;
							}
							break;
						case 'M':
							funds = in.readDouble();
							System.out.println("Client got FUNDS: " + funds);
							casinoListener.setAvailableFunds(funds);
							break;
						case 'G': //Join game success
							System.out.println("Client got JOIN SUCCESS");
							casinoListener.joinGameSuccess(self);
							break;
						case 'P':
							int seat = in.readInt();
							name = in.readUTF();
							funds = in.readDouble();
							System.out.printf("Client got PLAYER in %d, %s\n", seat, name);
							if (gameListener != null)
								gameListener.playerUpdate(seat, name, funds);
							break;
						default:
							System.err.println ("Message not recognized." + b);
							break;
						}
					}
				}
			catch (IOException exc){exc.printStackTrace();}
			finally {
				try {
					socket.close();
				} catch (IOException exc) {}
			}
		}
	}

	

	

}


