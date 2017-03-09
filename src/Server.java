import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The main executable for the server. Starts accepting authentication requests.
 * @author Elliot Allen
 *
 */
public class Server {
	
	public static void main(String[] args) throws IOException {
		String host = "localhost"; //TODO: Input 
		int port = 5009; //TODO: Port
		
		ServerSocket serversocket = new ServerSocket();
		serversocket.bind(new InetSocketAddress(host, port));

		
		
		CasinoSessionManager manager = new CasinoSessionManager();

		for (;;) {	
				Socket socket = serversocket.accept();
				//socket.setTcpNoDelay(true);
				CasinoViewProxy proxy = new CasinoViewProxy (socket);
				proxy.setViewListener ((ViewListener)manager);
		}
		
	}
	
		

	
}
