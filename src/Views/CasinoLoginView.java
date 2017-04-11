package Views;
import javax.swing.*;

import Listeners.CasinoModelListener;
import Listeners.CasinoViewListener;
import Listeners.GameViewListener;
import Models.CasinoModelClone;
import Proxies.CasinoModelProxy;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class CasinoLoginView extends JFrame implements CasinoModelListener {
	
	private int loginAttemptsRemaining;
	private CasinoViewListener listener;
	private CasinoModelClone model;
	private Timer waitTimer;
	public void setViewListener(CasinoViewListener listener) {
		this.listener = listener;
	}

	
	JButton blogin = new JButton("Login");
	JButton bCreateAccount = new JButton("Create Account");
	JPanel panel = new JPanel();
	JTextField txuser = new JTextField(15);
	JPasswordField pass = new JPasswordField(15);
	
	public CasinoLoginView(CasinoModelClone modelClone){
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
		
		loginAttemptsRemaining = 3;
		waitTimer = new Timer();
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
		txuser.setText("");
		pass.setText("");
		
		--loginAttemptsRemaining;
		if(loginAttemptsRemaining <= 0) {
			JOptionPane.showMessageDialog(null,"Wrong Password / Username\nToo many login attempts - timed out for five minutes.");
			
			//Need to time out the user for five minutes.
			blogin.setEnabled(false);
			final TimerTask r = new TimerTask() {
				@Override
				public void run() {
					loginAttemptsRemaining = 3;
					blogin.setEnabled(true);
				}
			};
			//Five minute timeout -
			// 5 minutes = 300 seconds = 300000 milliseconds
			waitTimer.schedule(r, 300000);
		}
		else {
			JOptionPane.showMessageDialog(null,"Wrong Password / Username");
		}
		
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

	@Override
	public void joinGameFailed(String reason) throws IOException {
		//Does nothing in login View
	}

	@Override
	public void joinGameSuccess(CasinoModelProxy session) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
