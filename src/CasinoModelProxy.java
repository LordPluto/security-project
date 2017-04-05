import java.io.*;
import java.net.Socket;



public class CasinoModelProxy implements CasinoViewListener {
	
	private CasinoModelListener listener;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	
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
	
	public void setModelListener(CasinoModelListener view) {
		if (this.listener == null) {
			this.listener = view;
			new ReaderThread() .start();
		}
		else {
			this.listener = view;
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
	
	private class ReaderThread
	extends Thread
	{
		public void run() {
			try {
				System.out.println("Client reader started.");
				for (;;) {
					String name;
					int funds;
					byte b = in.readByte();
					switch (b) {
						case 'L':
							name = in.readUTF();
							System.out.println("Client got LOGIN SUCCESS");
							listener.loginSuccessfulForAccount(name);
							break;
						case 'F':
							System.out.println("Client got LOGIN FAIL");
							listener.loginFailed();
							break;
						case 'M':
							funds = in.readInt();
							System.out.println("Client got FUNDS: " + funds);
							listener.setAvailableFunds(funds);
							break;
						default:
							System.err.println ("Message not recognized.");
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


