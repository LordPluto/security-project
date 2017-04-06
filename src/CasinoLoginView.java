import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class CasinoLoginView extends JFrame implements CasinoModelListener {
	
	private CasinoViewListener listener;
	private CasinoModelClone model;
	public void setViewListener(CasinoViewListener listener) {
		this.listener = listener;
	}

	
	JButton blogin = new JButton("Login");
	JButton bCreateAccount = new JButton("Create Account");
	JPanel panel = new JPanel();
	JTextField txuser = new JTextField(15);
	JPasswordField pass = new JPasswordField(15);
	
	CasinoLoginView(CasinoModelClone modelClone){
		super("Login Autentification");
		model = modelClone;
		setSize(300,200);
		setLocation(500,280);
		panel.setLayout (null); 
		
		
		txuser.setBounds(70,30,150,20);
		pass.setBounds(70,65,150,20);
		blogin.setBounds(110,100,80,20);
		
		panel.add(blogin);
		panel.add(txuser);
		panel.add(pass);
		
		getContentPane().add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		actionlogin();
	}
	
	public void actionlogin(){
		blogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String username = txuser.getText();
				String password = pass.getText(); //TODO: Change to getPassword; Clear after use.
				
				try {
					listener.login(null, username, password);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			
			}
		});
	}

	@Override
	public void setAvailableFunds(double funds) throws IOException {
		return;
	}

	@Override
	public void loginFailed() throws IOException {
		JOptionPane.showMessageDialog(null,"Wrong Password / Username");
		txuser.setText("");
		pass.setText("");
		txuser.requestFocus();
		
	}

	@Override
	public void loginSuccessfulForAccount(String name) throws IOException {
		CasinoMainMenuView.createAndShowGUI(model);
		CasinoMainMenuView.setViewListener(listener);
		//This is SUCH a hack but the main menu is static stuff right now
		//TODO: Make this suck less
		model.setModelListener(new CasinoMainMenuView());  

		dispose();
		
	}
}
