package de.ksquared.test.system.keyboard;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Control extends JFrame implements ActionListener {
	private static final long serialVersionUID = 2741423796025675295L;

	JPanel myPanel2 = new JPanel();
	JTextArea myText = new JTextArea();

	public void AddToFrame(String labelNew , boolean punch) {	
		myText.setBounds(10, 10, 220, 30);			
		myPanel2.add(myText);
		add(myPanel2).setBounds(0, 0, 220, 30);
		setSize(400, 400);
		setBackground(Color.BLACK);
		setTitle(Constants.MENU_HEADER_NAME_AND_POP_UP_NAME);
		setLocationRelativeTo(null);
		
		if(punch)
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
		myText.setText(labelNew );		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
