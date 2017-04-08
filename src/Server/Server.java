package Server;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocketFactory;

import Listeners.CasinoViewListener;
import Proxies.CasinoViewProxy;

/**
 * The main executable for the server. Starts accepting authentication requests.
 * @author Elliot Allen
 *
 */
public class Server {
	
	public static void main(String[] args) throws IOException {
		System.setProperty("javax.net.ssl.keyStore", "casinostore");
		System.setProperty("javax.net.ssl.keyStorePassword", "casinostore");
		
		String host = "localhost"; //TODO: Input 
		int port = 5009; //TODO: Port
		
		//ServerSocket serversocket = new ServerSocket();
		ServerSocket serversocket = SSLServerSocketFactory.getDefault().createServerSocket();
		serversocket.bind(new InetSocketAddress(host, port));

		
		CasinoSessionManager manager = new CasinoSessionManager();

		for (;;) {	
				Socket socket = serversocket.accept();
				CasinoViewProxy proxy = new CasinoViewProxy (socket);
				proxy.setViewListener ((CasinoViewListener)manager);
		}
		
	}
	
		

	
}
