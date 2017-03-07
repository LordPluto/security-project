import java.net.Socket;


public class CasinoViewProxy implements ModelListener {
	
	ViewListener listener;
	Socket socket;
	

	public CasinoViewProxy(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void setAvailableFunds(int funds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loginFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loginSuccessfulForAccount(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub
		
	}

	public void setViewListener(ViewListener listener) {
		this.listener = listener;
	}


}
