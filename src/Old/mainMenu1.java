package Old;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class mainMenu1 implements ActionListener {

	public static void main(String args[]) {
	    new mainMenu1();
	}
	
	JButton bplay = new JButton("Play");
	JPanel panel = new JPanel();
	JTextField txuser = new JTextField(15);
	JPasswordField pass = new JPasswordField(15);
	
	mainMenu1() {
	    JFrame f = new JFrame("Menu Demo");
	    f.setSize(500, 300);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    bplay.setBounds(110,100,80,20);
		panel.setLayout (null); 
		
		
		txuser.setBounds(70,30,150,20);
		pass.setBounds(70,65,150,20);
		bplay.setBounds(110,100,80,20);
		
		panel.add(bplay);
		panel.add(txuser);
		panel.add(pass);
		
		f.add(panel);
	    
	    JMenuBar jmb = new JMenuBar();
	    JMenu jmFile = new JMenu("File");
	    JMenuItem jmiOpen = new JMenuItem("Open");
	    JMenuItem jmiClose = new JMenuItem("Close");
	    JMenuItem jmiSave = new JMenuItem("Save");
	    JMenuItem jmiExit = new JMenuItem("Exit");
	    jmFile.add(jmiOpen);
	    jmFile.add(jmiClose);
	    jmFile.add(jmiSave);
	    jmFile.addSeparator();
	    jmFile.add(jmiExit);
	    jmb.add(jmFile);
	
	    JMenu jmOptions = new JMenu("Options");
	    JMenu a = new JMenu("A");
	    JMenuItem b = new JMenuItem("B");
	    JMenuItem c = new JMenuItem("C");
	    JMenuItem d = new JMenuItem("D");
	    a.add(b);
	    a.add(c);
	    a.add(d);
	    jmOptions.add(a);
	
	    JMenu e = new JMenu("E");
	    e.add(new JMenuItem("F"));
	    e.add(new JMenuItem("G"));
	    jmOptions.add(e);
	
	    jmb.add(jmOptions);
	
	    JMenu jmHelp = new JMenu("Help");
	    JMenuItem jmiAbout = new JMenuItem("About");
	    jmHelp.add(jmiAbout);
	    jmb.add(jmHelp);
	
	    jmiOpen.addActionListener(this);
	    jmiClose.addActionListener(this);
	    jmiSave.addActionListener(this);
	    jmiExit.addActionListener(this);
	    b.addActionListener(this);
	    c.addActionListener(this);
	    d.addActionListener(this);
	    jmiAbout.addActionListener(this);
	
	    f.setJMenuBar(jmb);
	    f.setVisible(true);
	}
	public void actionPerformed(ActionEvent ae) {
	    String comStr = ae.getActionCommand();
	    System.out.println(comStr + " Selected");
	}

}