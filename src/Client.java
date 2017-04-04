import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

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
		
		System.setProperty("javax.net.ssl.trustStore", "casinostore");
		System.setProperty("javax.net.ssl.trustStorePassword", "casinostore");
		
		String hostname = "localhost";
		int hostPort = 5009;
		
		CasinoModelClone model = new CasinoModelClone();
		//CasinoView view = new CasinoView(model);
		Log view = new Log(model);
		
		SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket("localhost", hostPort);

		CasinoModelProxy proxy = new CasinoModelProxy(socket);
		proxy.setModelListener(model);
		view.setViewListener(proxy);
		model.setModelListener(view);
		
		
		
		//socket.close();
	}
}
