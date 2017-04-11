//package Views;
//import java.awt.Dimension;
//import java.io.IOException;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//
//import Listeners.CasinoModelListener;
//import Listeners.CasinoViewListener;
//
///**
// * Main view right now. Name subject to change. (This is mostly just for testing will probably integrate with Davids GUI)
// * Holds JFrame info
// *
// */
//public class CasinoView implements CasinoModelListener{
//	
//	private JFrame frame;
//	CasinoModelListener modelClone;
//	JTextField name;
//	JTextField funds;
//	JPanel panel;
//	CasinoViewListener listener;
//	
//	public CasinoView (CasinoModelListener model) {
//		modelClone = model;
//		frame = new JFrame("Casino Royal");
//		frame.setVisible(true);
//		Dimension dim = new Dimension (200, 200);
//		frame.setMinimumSize (dim);
//		frame.setPreferredSize (dim);
//		name = new JTextField();
//		funds = new JTextField();
//		frame.getContentPane().add(name);
//		frame.getContentPane().add(funds);
//	}
//	
//	/**
//	 * Set what object is listening to this view. (The proxy)
//	 * @param listener
//	 */
//	public void setViewListener(CasinoViewListener listener) {
//		this.listener = listener;
//	}
//	
//	/**
//	 * Test function for logging in over the network. 
//	 * TODO: Rework in new UI with events
//	 * @throws IOException 
//	 */
//	public void doLogin() throws IOException {
//		listener.login(null, "testUser", "testPass");
//	}
//
//	@Override
//	public void setAvailableFunds(double funds) {
//		this.funds.setText(funds + "");
//		frame.repaint();
//		
//	}
//
//	@Override
//	public void loginFailed() {
//		this.name.setText("Login Failed");
//		frame.repaint();
//		
//	}
//
//	@Override
//	public void loginSuccessfulForAccount(String name) {
//		// TODO Auto-generated method stub
//		this.name.setText(name);
//		frame.repaint();
//		
//	}
//
//}
