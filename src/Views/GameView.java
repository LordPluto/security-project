package Views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import Listeners.*;
import Models.*;

public class GameView implements GameModelListener {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    
    static final int NAME_AND_BAL = 0;
    static final int PICK_AND_BET = 1;

    static JFrame frame;
    static GameModelClone modelClone;
    static GameViewListener listener;
	static JLabel[][] display = new JLabel[5][5];
	
	static JButton bet = new JButton("Bet");
	static JButton quit = new JButton("Quit");
	static JLabel coin  = new JLabel("Result:");
	
	static JLabel usrInfo  = new JLabel();
	static JLabel usrBet  = new JLabel();
	
	static JTextField BETtf = new JTextField(15);
	static JList<String> list;
	//dropdown for heads/tails
	
	//public static void main(String[] args){
	//	createAndShowGUI(clone);
	//}

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
		
		JLabel usrInfo  = new JLabel();
		JLabel usrBet  = new JLabel();
		
		//User Entered Bet 0
		//setBalance here for opponent user *************
		display[1][NAME_AND_BAL]  = new JLabel();//"User 0 "+"Balance: $" + 50
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10,10,0,0);
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(display[1][NAME_AND_BAL], c);
		//User Info 1
		//
		display[1][PICK_AND_BET]  = new JLabel(); //"Pick: Heads/Tails " +"$" + 10
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,10,0,0);
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(display[1][PICK_AND_BET], c);
		
		//User Entered Bet 1
		//setBalance here for opponent user
		display[2][NAME_AND_BAL]  = new JLabel();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10,0,0,10);
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 0;
		pane.add(display[2][NAME_AND_BAL], c);
		//User Info 1
		display[2][PICK_AND_BET]  = new JLabel();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,0,10);
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 1;
		pane.add(display[2][PICK_AND_BET], c);

		//BOOLEAN Representing heads/tails
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,0,10);
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 2;
		pane.add(coin, c);
		
		//User Entered Bet 2
		//setBalance here for opponent user
		display[3][NAME_AND_BAL]  = new JLabel();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,10,0,0);
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(display[3][NAME_AND_BAL], c);
		//User Info 2
		display[3][PICK_AND_BET]  = new JLabel();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 4;
		pane.add(display[3][PICK_AND_BET], c);
		
		//User Entered Bet 3
		//setBalance here for opponent user
		display[4][NAME_AND_BAL]  = new JLabel();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,0,10);
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 3;
		pane.add(display[4][NAME_AND_BAL], c);
		//User Info 3
		display[4][PICK_AND_BET]  = new JLabel();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 4;
		pane.add(display[4][PICK_AND_BET], c);
		
		
		//Logged In User Info
		//setBalance here for MYUSERNAME here
		display[0][NAME_AND_BAL]  = new JLabel();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10,10,10,10);
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 6;
		pane.add(display[0][NAME_AND_BAL], c);
		
		
		//Choose One: label
		display[0][PICK_AND_BET]  = new JLabel("Choose One: ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,100,0,0);
		//c.anchor = GridBagConstraints.WEST;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 8;
		pane.add(display[0][PICK_AND_BET], c);
		
		//List showing heads and tails option
      c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0,0,0,0);
      c.weightx = 0.5;
      c.weighty = 0.5;
		c.gridx = 1;
		c.gridy = 8;
      pane.add(fliplist(),c);
      
		
		//Bet: label
		usrInfo  = new JLabel("Bet: ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,100,0,0);
		//c.anchor = GridBagConstraints.EAST;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 9;
		pane.add(usrInfo, c);
		
		BETtf.setText("");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,0,10);
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 9;
		pane.add(BETtf, c);

		
		//Bet Button
		c.insets = new Insets(10,10,10,10);  //padding
		c.gridx = 2;
		c.gridy = 9;
		pane.add(bet, c);
		
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = .5;  
		c.anchor = GridBagConstraints.PAGE_END; //bottom of space
		c.insets = new Insets(10,10,10,10);  //padding
		c.gridx = 2;       //aligned with button 2
		c.gridy = 10;       //third row
		c.gridwidth = 1;
		pane.add(quit, c);
		
		actionlobbyList();
	}
  
  /**
   * Creates a JList
   * @return A JList with two strings; Heads and Tails, and its JScrollPane
   */
  private static JScrollPane fliplist(){
      DefaultListModel<String> listModel = new DefaultListModel<String>();  
      list = new JList<String>(listModel);

      

      listModel.addElement("Heads");
      listModel.addElement("Tails");
      
      // Set the item width
      int cellWidth = 10;
      list.setFixedCellWidth(cellWidth);

      // Set the item height
      int cellHeight = 12;
      list.setFixedCellHeight(cellHeight);

      return new JScrollPane(list);
  }

    
	public static void actionlobbyList(){
		bet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
					try {
						double f = getBet();
						if (f <= 0) return;
						int p = getPrediction();
						listener.bet(null, p, f);
						bet.setEnabled(false);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "An error occurred: Unable to bet.");
					}
				
			}
				
		});
	}
	
	private static double getBet() {
		String s = BETtf.getText();
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
		String s = list.getSelectedValue();
		if (s != null) {
			if (s.equals("Heads")) return 1;
			if (s.equals("Tails")) return 2;
		}
		return 0;
	
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
		
		display[seat][NAME_AND_BAL].setText(modelClone.getName(seat)+" Balance: $" + modelClone.getFunds(seat));
		display[seat][NAME_AND_BAL].repaint();
	}


	@Override
	public void updateBalance(int seat, double amount) throws IOException {
		System.out.println("VIEW got BALANCE " + amount+ " for "+ seat+". MODEL HAS " + modelClone.getFunds(seat));
		
		display[seat][NAME_AND_BAL].setText(modelClone.getName(seat)+" Balance: $" + modelClone.getFunds(seat));
		display[seat][NAME_AND_BAL].repaint();
		
	}


	@Override
	public void updateBet(int seat, int prediction, double amount)
			throws IOException {
		System.out.println("VIEW got BET " + seat + " " + prediction + " " + amount);
		
		display[seat][PICK_AND_BET].setText(modelClone.getPredictionString(seat) + " for $" + amount);
		display[seat][PICK_AND_BET].repaint();
		
	}


	@Override
	public void turnUpdate(int outcome, int time) throws IOException {
		bet.setEnabled(true);
		coin.setText("Result: " + (outcome== 1 ? "Heads": "Tails"));
		coin.repaint();
		for (int i = 1; i < 5; i++){
				display[i][PICK_AND_BET].setText("");
				display[i][PICK_AND_BET].repaint();
		}
		display[0][PICK_AND_BET].setText("Choose One:");
		display[0][PICK_AND_BET].repaint();
		
	}

}