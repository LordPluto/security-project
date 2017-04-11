package Views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import Listeners.*;
import Models.*;

public class TestGameView implements GameModelListener {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    
    static final int NAME = 0;
    static final int BALANCE = 1;
    static final int PREDICTION = 2;
    static final int BET = 3;

    static JFrame frame;
    static GameModelClone modelClone;
    static GameViewListener listener;
	static JLabel[][] display = new JLabel[4][4];
	
	static JLabel name = new JLabel();
	static JLabel balance  = new JLabel();
	static JButton bet = new JButton("BET");
	static JLabel last = new JLabel();

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
		
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++) {
				c.gridx = i;
				c.gridy = j;
				display[i][j] = new JLabel();
				pane.add(display[i][j],c);
			}
		}
//		c.gridx = 0;
//		c.gridy = 0;
//		name.setText("Welcome: " + modelClone.getName(0));
//		pane.add(name, c);
//		
//		//Available Balance
//		c.gridx = 1;
//		c.gridy = 0;
//		c.gridwidth = 2;
//		setBalance();
//		pane.add(balance, c);
//		c.gridwidth = 1;
//		
//		
//		//Join Public Button
//		c.gridx = 0;
//		c.gridy = 1;
//		pane.add(joinPublicGame, c);
//		
//		//Join Private Button
//		c.gridx = 1;
//		c.gridy = 1;
//		pane.add(joinPrivateGame, c);
//		
//		//New Game
//		c.insets = new Insets(10,0,10,0);  //padding
//		c.gridx = 2;
//		c.gridy = 1;
//		pane.add(newGame, c);
//		
//		//Account Details
//		c.insets = new Insets(10,0,10,0);  //padding
//		c.gridx = 3;
//		c.gridy = 1;
//		pane.add(account, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = .5;  
		c.anchor = GridBagConstraints.PAGE_END; //bottom of space
		c.insets = new Insets(20,20,20,20);  //padding
		c.gridx = 0;       //aligned with button 2
		c.gridy = 5;       //third row
		c.gridwidth = 2;
		pane.add(bet, c);
		c.gridx = 3;       //aligned with button 2
		c.gridy = 5;       //third row
		pane.add(last, c);
		actionlobbyList();
	}

    
	public static void actionlobbyList(){
		bet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
					try {
						double f = getBet();
						if (f <= 0) return;
						int p = getPrediction();
						listener.bet(null, p, f);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "An error occurred: Unable to bet.");
					}
				
			}
				
		});
	}
	
	private static double getBet() {
		String s = (String)JOptionPane.showInputDialog(
                frame,
                "How much do you want to bet.",
                "Bet",
                JOptionPane.PLAIN_MESSAGE);
		if (s != null) {
			
			try {
				double f = Double.parseDouble(s);
				f = Math.floor(f * 100) / 100; //Truncate to 2 decimal places
				if (modelClone.getFunds(0) < f) {
					JOptionPane.showMessageDialog(null, "Insuffient Funds.");
				} else {
					return f;
				}
			} catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid amount of funds entered.");
			}
		}
		return 0;
	
	}
	
	private static int getPrediction() {
		String s = (String)JOptionPane.showInputDialog(
                frame,
                "What do you want to bet on",
                "Bet",
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[] { "Heads", "Tails" },
                null);
		if (s != null) {
			if (s.equals("Heads")) return 1;
			if (s.equals("Tails")) return 2;
		}
		return 0;
	
	}
	
	private static void setBalance() {
		if (modelClone != null) {
			String text = "Available balance: $" + modelClone.getFunds(0);
			balance.setText(text);
		}
	}

	public static void setViewListener(GameViewListener theListener) {
		listener = theListener;
	}
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI(GameModelClone clone) {
    	
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
	public void playerUpdate(int seat, String username, double funds)
			throws IOException {
		System.out.println("VIEW got PLAYER " + username+ "for" + " for "+ seat+". MODEL HAS " + modelClone.getName(seat));
		
		display[seat][NAME].setText(modelClone.getName(seat));
		display[seat][NAME].repaint();
		display[seat][BALANCE].setText(modelClone.getFunds(seat)+"");
		display[seat][BALANCE].repaint();
	}


	@Override
	public void updateBalance(int seat, double amount) throws IOException {
		System.out.println("VIEW got BALANCE " + amount+ " for "+ seat+". MODEL HAS " + modelClone.getFunds(seat));
		
		display[seat][BALANCE].setText(modelClone.getFunds(seat)+"");
		display[seat][BALANCE].repaint();
		
	}


	@Override
	public void updateBet(int seat, int prediction, double amount)
			throws IOException {
		System.out.println("VIEW got BET " + seat + " " + prediction + " " + amount);
		
		display[seat][PREDICTION].setText(modelClone.getPredictionString(seat));
		display[seat][PREDICTION].repaint();
		display[seat][BET].setText(amount+"");
		display[seat][BET].repaint();
		
	}


	@Override
	public void turnUpdate(int outcome, int time) throws IOException {
		last.setText("Last: " + (outcome== 1 ? "Heads": "Tails"));
		for (int i = 0; i < 4; i++){
				display[i][PREDICTION].setText("");
				display[i][PREDICTION].repaint();
				display[i][BET].setText("");
				display[i][BET].repaint();
		}
		
	}

}