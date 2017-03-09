import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The main executable for the client. 
 * Establishes a secure connection socket to server.
 * Establishes main view and model clones.
 * @author Elliot
 *
 */
public class Client {

	
	public static void main(String args[]) throws IOException {
		//TODO: Create View, Model & Proxy
		
		String hostname = "localhost";
		int hostPort = 5009;
		
		CasinoModelClone model = new CasinoModelClone();
		//CasinoView view = new CasinoView(model);
		Log view = new Log(model);
		
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress("localhost", hostPort));
		//socket.setTcpNoDelay(true);
		CasinoModelProxy proxy = new CasinoModelProxy(socket);
		proxy.setModelListener(model);
		view.setViewListener(proxy);
		model.setModelListener(view);
		
		
		
		//socket.close();
	}
}
