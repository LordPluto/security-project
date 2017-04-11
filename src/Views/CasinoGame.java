package Views;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Listeners.CasinoViewListener;
//import Models.CasinoGameClone;

public class CasinoGame implements CasinoGameModel{

    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;

    static JFrame frame;
//    static CasinoGameClone modelClone;
//  static CasinoGameClone modelClone;
  static CasinoViewListener listener;
	static JButton bet = new JButton("Bet");
	static JButton quit = new JButton("Quit");
	
	static JLabel usrInfo  = new JLabel();
	static JLabel usrBet  = new JLabel();
	
	static JTextField BET = new JTextField(15);
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
		usrInfo  = new JLabel("User 0 "+"Balance: $" + 50);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10,10,0,0);
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(usrInfo, c);
		//User Info 1
		usrBet  = new JLabel("Pick: Heads/Tails " +"$" + 10);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,10,0,0);
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(usrBet, c);
		
		//User Entered Bet 1
		//setBalance here for opponent user
		usrInfo  = new JLabel("User 1 "+"Balance: $" + 51);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10,0,0,10);
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 0;
		pane.add(usrInfo, c);
		//User Info 1
		usrBet  = new JLabel("Pick: Heads/Tails " +"$" + 11);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,0,10);
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 1;
		pane.add(usrBet, c);

		//BOOLEAN Representing heads/tails
		JLabel coin  = new JLabel("if boolean");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,0,10);
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 2;
		pane.add(coin, c);
		
		//User Entered Bet 2
		//setBalance here for opponent user
		usrInfo  = new JLabel("User 2 "+"Balance: $" + 52);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,10,0,0);
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(usrInfo, c);
		//User Info 2
		usrBet  = new JLabel("Pick: Heads/Tails " +"$" + 12);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 4;
		pane.add(usrBet, c);
		
		//User Entered Bet 3
		//setBalance here for opponent user
		usrInfo  = new JLabel("User 1 "+"Balance: $" + 53);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,0,10);
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 3;
		pane.add(usrInfo, c);
		//User Info 3
		usrBet  = new JLabel("Pick: Heads/Tails " +"$" + 13);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 4;
		pane.add(usrBet, c);
		
		
		//Logged In User Info
		//setBalance here for MYUSERNAME here
		usrInfo  = new JLabel("MYUSERNAME "+"Balance: 		$" + 100);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10,10,10,10);
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 6;
		pane.add(usrInfo, c);
		
		
		//Choose One: label
		usrInfo  = new JLabel("Choose One: ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,100,0,0);
		//c.anchor = GridBagConstraints.WEST;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 8;
		pane.add(usrInfo, c);
		
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
		
		BET.setText("");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,0,10);
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 9;
		pane.add(BET, c);

		
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
		
		
	}
  
  /**
   * Creates a JList
   * @return A JList with two strings; Heads and Tails, and its JScrollPane
   */
  private static JScrollPane fliplist(){
      DefaultListModel<String> listModel = new DefaultListModel<String>();  
      JList<String> list = new JList<String>(listModel);

      

      listModel.addElement("Heads");
      listModel.addElement("Tails");
      
      // Set the item width
      int cellWidth = 10;
      list.setFixedCellWidth(cellWidth);

      // Set the item height
      int cellHeight = 12;
      list.setFixedCellHeight(cellHeight);
      
      JScrollPane pane = new JScrollPane(list);
      return pane;
  }

    

	
	private static void setBalance() {
		if (modelClone != null) {
			String text = "Available balance: $" + modelClone.getAvailableFunds();
			balance.setText(text);
		}
	}

	public static void setViewListener(CasinoViewListener theListener) {
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
                frame = new JFrame("Game Menu");
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
