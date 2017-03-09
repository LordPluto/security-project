package Old;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class newframe extends JFrame {

public static void main(String[] args) {
	newframe frameTabel = new newframe();
}

JLabel mainMenu = new JLabel("Main Menu");
JPanel panel = new JPanel();

newframe(){
	super("Main Menu");
	setSize(600,400);
	setLocation(500,280);
	panel.setLayout (null); 
	
	mainMenu.setBounds(250,40,150,80);
	
	panel.add(mainMenu);
	
	getContentPane().add(panel);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setVisible(true);
}

}
