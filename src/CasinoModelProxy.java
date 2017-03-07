import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class CasinoModelProxy implements ViewListener {
	
	private ModelListener listener;
	private Socket socket;
	private DataOutputStream out;
	
	public CasinoModelProxy(Socket socket) {
		this.socket = socket;
		try {
			this.out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setViewListener(ModelListener view) {
		this.listener = view;
	}

	@Override
	public void quit() throws IOException {
		out.writeByte('Q');
		out.flush();
	}

	@Override
	public void login(CasinoViewProxy proxy, String username, String password) throws IOException {
		out.writeByte('L');
		out.writeUTF(username);
		out.writeUTF(password);
		out.flush();
	}

}
