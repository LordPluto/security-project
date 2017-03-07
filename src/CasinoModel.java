import java.io.IOException;

/**
 * Keeps track of the casino model (login/main menu items)
 * @author ElliotAllen
 *
 */
public class CasinoModel implements ViewListener {
	
	ModelListener listener;
	
	public void setModelListener(ModelListener listener) {
		this.listener = listener;
	}

	@Override
	public void quit() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void login(CasinoViewProxy proxy, String username, String password)
			throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	

}
