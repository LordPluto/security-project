
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CasinoMainMenuView {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;

	static JButton joinGame = new JButton("Join Game");
	static JButton newGame = new JButton("Host Game");
	static JButton account = new JButton("Acct. Details");
	static JButton quit = new JButton("Quit");

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
	
		if (shouldWeightX) {
			c.weightx = 0.5;
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(joinGame, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.insets = new Insets(10,10,10,10);  //padding
		c.gridx = 1;
		c.gridy = 1;
		pane.add(newGame, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.insets = new Insets(10,10,10,10);  //padding
		c.gridx = 2;
		c.gridy = 1;
		pane.add(account, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = .5;  
		c.anchor = GridBagConstraints.PAGE_END; //bottom of space
		c.insets = new Insets(20,20,20,20);  //padding
		c.gridx = 1;       //aligned with button 2
		c.gridy = 2;       //third row
		pane.add(quit, c);
		
		actionlobbyList();
	}

    
	public static void actionlobbyList(){
		joinGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
					//lobbyList.main(null);
			}
		});
	}
    
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
    	//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Create and set up the window.
                JFrame frame = new JFrame("Main Menu");
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
}