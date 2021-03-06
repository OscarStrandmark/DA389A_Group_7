package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import client.GameClient;
import client.Sound;

/**
 * Class that creates the menu window for the user. 
 * The user can choose to click on three buttons
 * Host: Creates a game server that multiple users can then connect to
 * Join: Connect to an existing game server
 * Quit: Exit application
 * @author Julian Hultgren, Lukas Persson, Erik Johansson, Simon Börjesson
 * Version 2.0
 *
 */

public class MenuFrame implements ActionListener{
	private	IconPanel iconPanel;
	private	JButton host = new JButton("Host");
	private	JButton join = new JButton("Join");
	private	JButton quit = new JButton("Quit");
	private JButton help = new JButton("Help & Rules");
	private GridBagLayout layout = new GridBagLayout();	
	private	JPanel panel = new JPanel(new GridLayout(5,1,10,10));
	JFrame frame = new JFrame("Main Menu");
	
	/**
	 * Constructor who receives an ImageIcon object.
	 * The constructor also builds the menu box
	 * @param	ImageIcon	icon
	 */
	
	public MenuFrame(ImageIcon icon) {
		
		iconPanel = new IconPanel(icon);
		iconPanel.setLayout(layout);
		panel.setPreferredSize(new Dimension(250,150));
		panel.setOpaque(false);
		panel.add(host);
		panel.add(join);
		panel.add(help);
		panel.add(quit);
		panel.add(new BackgroundMusicControlPanel());
	
		iconPanel.add(panel,new GridBagConstraints());
		iconPanel.setPreferredSize(new Dimension(800,600));
		
		host.addActionListener(this);
		join.addActionListener(this);
		help.addActionListener(this);
		quit.addActionListener(this);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(iconPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}	
	
	/**
	 * Method is called for when a user clicks on any of the buttons. 
	 * Host: Creates a game server that multiple users can then join. 
	 * Join: Connecting to an existing game server. 
	 * Quit: Closes the application
	 */
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == host) {
			System.out.println("Någon har klickat på 'Host'"); //Kommentar för White box-testning - Julian Hultgren
			GameClient gc = new GameClient();
			ClientFrame gf = new ClientFrame(gc);
			ServerFrame sf = new ServerFrame();
			frame.dispose();
		}
		if(e.getSource() == join) {
			System.out.println("Någon har klickat på 'Join'"); //Kommentar för White box-testning - Julian Hultgren
			GameClient gc = new GameClient();
			ClientFrame gf = new ClientFrame(gc);
			frame.dispose();
		}
		if(e.getSource() == quit) {
			System.out.println("Någon har klickat på 'Quit'"); //Kommentar för White box-testning - Julian Hultgren
			System.exit(0);
		}
		if(e.getSource() == help) {
			new Rules().showRules();
		}
	}	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new MenuFrame(new ImageIcon("images/bg.jpg")));
	}
}
