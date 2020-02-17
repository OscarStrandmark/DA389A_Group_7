package gui;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Frame that shows rules of the game, as of now needs rules
 * to be isnerted, this currently holds empty pictures.
 * @author AevanDino, Oscar Strandmark
 *
 */
public class Rules extends JPanel {
    
	/**
	 * Tabs to show in ui, easy to add more tabs if needed
	 * You will have to use images as input in this implementation
	 * feel free to change if you want to do it any other way.
	 */
	public Rules() {
		new GridLayout(1, 1);
		JTabbedPane tabbedPane = new JTabbedPane();

		JComponent pnl1 = createPaneWithImage("images/placeholder.png");
		tabbedPane.addTab("Intro", pnl1);

		JComponent pnl2 = createPaneWithImage("images/placeholder.png");
		tabbedPane.addTab("Kartbitar", pnl2);

		JComponent pnl3 = createPaneWithImage("images/placeholder.png");
		tabbedPane.addTab("Skjuta", pnl3);

		JComponent pnl4 = createPaneWithImage("images/placeholder.png");
		tabbedPane.addTab("Hoppa", pnl4);
		
		JComponent pnl5 = createPaneWithImage("images/placeholder.png");
		tabbedPane.addTab("Fly", pnl5);
		add(tabbedPane); 		
	}

	protected JComponent createPaneWithImage(String imgPath) {
		JPanel pnl = new JPanel(false);
		// Raden nedan skalar om bilden till 450, 600
		ImageIcon imgTab = new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(450, 600, Image.SCALE_SMOOTH));
		JLabel lblContent = new JLabel(imgTab);
		lblContent.setHorizontalAlignment(JLabel.CENTER);
		pnl.setLayout(new GridLayout(1, 1));
		pnl.add(lblContent);
		return pnl;
	}

	/**
	 * Create the GUI and show it. Method creates panel and frame
	 * to show the user.
	 */
	private static void createAndShowFrame() {
		JFrame frame = new JFrame("Rules");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(new Rules(), BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * This method is called every time
	 * the user presses "View Rules" button.
	 */
	public void showRules() {
		createAndShowFrame();
	}

}