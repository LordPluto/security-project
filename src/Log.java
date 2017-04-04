import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Log extends JFrame implements ModelListener {
	
	private ViewListener listener;
	private ModelListener model;
	public void setViewListener(ViewListener listener) {
		this.listener = listener;
	}

	
	JButton blogin = new JButton("Login");
	JPanel panel = new JPanel();
	JTextField txuser = new JTextField(15);
	JPasswordField pass = new JPasswordField(15);
	
	Log(ModelListener modelClone){
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
	public void setAvailableFunds(int funds) throws IOException {
		// TODO Auto-generated method stub
		
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
		newframe regFace =new newframe();
		regFace.setVisible(true);
		dispose();
		
	}
}
