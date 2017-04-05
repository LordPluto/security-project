import java.io.*;
import java.net.Socket;

/**
 * Reads data from view off the network. 
 * Sends data from the model over the network.
 * @author ElliotAllen
 *
 */
public class CasinoViewProxy implements ModelListener {
	
	ViewListener listener;
	Socket socket;
	DataOutputStream out;
	DataInputStream in;

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
		out.writeByte('F');
		out.flush();
		
	}

	@Override
	public void loginSuccessfulForAccount(String name) throws IOException {
		System.out.println("SERVER SENT LOGIN SUCCESS");
		out.writeByte('L');
		out.writeUTF(name);
		out.flush();
	}

	public void setViewListener(ViewListener listener) {
		if (this.listener == null) {
			this.listener = listener;
			new ReaderThread() .start();
		}
		else {
			this.listener = listener;
		}
	}
	

	private class ReaderThread
	extends Thread
	{
		public void run() {
			try {
				for (;;) {
					String name, password;
					byte b = in.readByte();
					switch (b) {
						case 'L':
							name = in.readUTF();
							password = in.readUTF();
							System.out.printf("SERVER GOT LOGIN: %s, %s\n", name, password);
							listener.login(CasinoViewProxy.this, name, password);
							break;
						case 'Q':
							System.out.println("Server got QUIT");
							listener.quit();
							break;
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
