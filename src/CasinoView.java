import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Main view right now. Name subject to change.
 * Holds JFrame info
 *
 */
public class CasinoView implements ModelListener{
	
	private JFrame frame;
	ModelListener modelClone;
	JTextField name;
	JTextField funds;
	JPanel panel;
	
	public CasinoView (ModelListener model) {
		modelClone = model;
		frame = new JFrame("Casino Royal");
		frame.setVisible(true);
		name = new JTextField();
		funds = new JTextField();
		frame.getContentPane().add(name);
		frame.getContentPane().add(funds);
	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAvailableFunds(int funds) {
		this.funds.setText(funds + "");
		frame.repaint();
		
	}

	@Override
	public void loginFailed() {
		this.name.setText("Login Failed");
		frame.repaint();
		
	}

	@Override
	public void loginSuccessfulForAccount(String name) {
		// TODO Auto-generated method stub
		this.name.setText(name);
		frame.repaint();
		
	}

}
