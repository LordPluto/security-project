
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class CasinoMainMenuView implements ModelListener {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;

    static JFrame frame;
    static CasinoModelClone modelClone;
    static ViewListener listener;
	static JButton joinPublicGame = new JButton("Join Public Game");
	static JButton joinPrivateGame = new JButton("Join Private Game");
	static JButton newGame = new JButton("Host Game");
	static JButton account = new JButton("Acct. Details");
	static JButton quit = new JButton("Quit");
	
	static JLabel name = new JLabel();
	static JLabel balance  = new JLabel();

    public static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        //JButton button;
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		if (shouldFill) {
		//natural height, maximum width
		c.fill = GridBagConstraints.HORIZONTAL;
		}
		//Name
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		name.setText("Welcome: " + modelClone.getLoggedInUser());
		pane.add(name, c);
		
		//Available Balance
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		setBalance();
		pane.add(balance, c);
		c.gridwidth = 1;
		
		
		//Join Public Button
		c.gridx = 0;
		c.gridy = 1;
		pane.add(joinPublicGame, c);
		
		//Join Private Button
		c.gridx = 1;
		c.gridy = 1;
		pane.add(joinPrivateGame, c);
		
		//New Game
		c.insets = new Insets(10,0,10,0);  //padding
		c.gridx = 2;
		c.gridy = 1;
		pane.add(newGame, c);
		
		//Account Details
		c.insets = new Insets(10,0,10,0);  //padding
		c.gridx = 3;
		c.gridy = 1;
		pane.add(account, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = .5;  
		c.anchor = GridBagConstraints.PAGE_END; //bottom of space
		c.insets = new Insets(20,20,20,20);  //padding
		c.gridx = 1;       //aligned with button 2
		c.gridy = 2;       //third row
		c.gridwidth = 2;
		pane.add(quit, c);
		
		actionlobbyList();
	}

    
	public static void actionlobbyList(){
		joinPublicGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
					//lobbyList.main(null);
			}
		});
	}
	
	private static void setBalance() {
		if (modelClone != null) {
			String text = "Available balance: $" + modelClone.getAvailableFunds();
			balance.setText(text);
		}
	}

	public static void setViewListener(ViewListener theListener) {
		listener = theListener;
	}
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI(CasinoModelClone clone) {
    	
    	modelClone = clone;
    	//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Create and set up the window.
                frame = new JFrame("Main Menu");
                frame.setSize(500, 300);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //Set up the content pane.
                addComponentsToPane(frame.getContentPane());

                //Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });
    
    }


	@Override
	public void setAvailableFunds(int funds) throws IOException {
		setBalance();
		balance.repaint();
	}


	//Does Nothing
	@Override
	public void loginFailed() throws IOException {
		return;
	}


	@Override
	public void loginSuccessfulForAccount(String name) throws IOException {
		return;
		
	}
}